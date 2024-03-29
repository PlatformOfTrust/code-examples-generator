(ns code-examples-generator.test-utils
  "Convenience methods for testing. Mainly file system related."
  (:require [clojure.java.io :as io])
  (:import [java.nio.file Files]
           [java.nio.file.attribute FileAttribute]))


(defn create-temp-file
  "Create temporary file with optional suffix `s`."
  ([]
   (create-temp-file ".tmp"))
  ([s]
   (java.io.File/createTempFile "Test" s)))
  
(defn create-temp-dir
  "Create temporary directory and return it's path."
  []
  (.toString (Files/createTempDirectory "Test" (into-array FileAttribute []))))

(defn set-read-only
  "Find file/directory at path and set it to read only. Return path if success."
  [s]
  (when (.setReadOnly (io/file s)) s))

(defn set-writable
  "Find file/directory at path and set it writable. Return path if success."
  [s]
  (when (.setWritable (io/file s) true) s))

(defn uuid "Generate uuid." [] (java.util.UUID/randomUUID))

(defn random-path
  "Return random string (that could be used for testing hypothetical file locations)."
  []
  (->> uuid .toString (str "./")))

(defn create-map
  "Create a map with a key for each item in sequence `seq` that 
   has an optional value f (`nil` by default)."
  ([seq]
   (create-map seq nil))
  ([seq f]
   (into {} (map (fn [kw _] {kw f}) seq (range)))))
