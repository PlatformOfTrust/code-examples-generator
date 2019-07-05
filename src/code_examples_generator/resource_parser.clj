(ns code-examples-generator.resource-parser
  "Methods for parsing HTTP requests from RAML map."
  (:require
   [clojure.string :refer [starts-with?]]
   [ring.util.codec :refer [form-encode]]))


;; TODO maybe report that certain values are nil? It will be easier to fix the docs.
;; TODO Reporting should be done on higher levels
;; TODO maybe not the best name for this method?
(defn coerce-examples->values
  "Parse node and return a map of key-value pairs where key is key and 
   value is taken from `:example`. Not recursive!"
  [m]
  (into {} (map (fn [[k {v :example}]] [k v]) m)))

(defn get-resources
  "Returns a sequence of vectors where each one consists of two values:
   a) resource name (identified as a `/`-prefixed string )
   b) resource node
   e.g. '([s m] [s m])"
  [m]
  (let [keys (->> m keys (filter #(and (string? %) (starts-with? % "/"))) set)]
    (filter (fn [[k _]] (contains? keys k)) m)))


;; TODO would it make sense to test them being valid ring requests?
(defn get-ring-request
  "Return `ring request` https://github.com/ring-clojure/ring/blob/master/SPEC."
  [{:keys [queryParameters body headers]} method host scheme uri]
  (let [r {:request-method method
           :server-name host
           :scheme scheme
           :uri uri
           :query-string (form-encode (coerce-examples->values queryParameters))
           :body (coerce-examples->values body)
           :headers (coerce-examples->values headers)}]
    (into {} (remove (fn [[_ v]] (when (not (keyword? v)) (empty? v))) r))))

(defn get-methods
  "Parse node and returns a sequence of maps which have the following keys:
   a) `:ring-request` - https://github.com/ring-clojure/ring/blob/master/SPEC
   b) `:desc` - resource description"
  [m {:keys [host scheme]} uri]
  (let [http-methods #{:get :patch :put :post :delete :options :head}]
    (->> m
         (map
          (fn [[k v]]
            (when (contains? http-methods k)
              (assoc {}
                     :ring-request (get-ring-request v k host scheme uri)
                     :desc (:description v)))))
         (remove nil?))))

;; TODO return with-meta so that hierarchical strucuture could be returned?
(defn get-requests
  "Traverse through provided RAML map `m` and return a collection of 
   HTTP requests with provided prefix `s`."
  ([m cli-args]
   (get-requests m cli-args ""))
  ([m cli-args s]
   (flatten
    (map
     (fn [[k v]]
       (let [uri (str s k)
             methods (get-methods v cli-args uri)
             requests (get-requests v cli-args uri)]
         [methods requests]))
     (get-resources m))))) 