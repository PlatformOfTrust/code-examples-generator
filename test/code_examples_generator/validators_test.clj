(ns code-examples-generator.validators-test
  (:require
   [clojure.test :refer :all]
   [code-examples-generator.test-utils :as u]
   [code-examples-generator.validators :refer :all]))


(deftest test-valid-source?
  (testing "source must be an existing path"
    (is (false? (is-valid-source? (u/random-path))))
    (are [s] (true? (is-valid-source? s))
      (u/create-temp-file)
      (u/create-temp-dir))))

;; TODO should test that the destination is writable
;; when it does not exist? Does this work on windows?
;; There are many thing that can go wrong e.g. not
;; enough space etc. Maybe it's not worth testing.
(deftest test-is-valid-dest?
  (testing "destination must be a non-existing and writable directory"
    (are [s] (false? (is-valid-dest? s))
      (u/create-temp-file)
      (u/set-read-only (u/create-temp-dir)))
    (are [s] (true? (is-valid-dest? s))
      (u/random-path)
      (u/set-writable (u/create-temp-dir)))))

(deftest test-is-valid-host?
  (are [s] (true? (is-valid-host? s)) "ok" "api.oftrust.net") 
  (are [s] (false? (is-valid-host? s)) 1 :ok nil ""))

(deftest test-is-valid-scheme?
  (are [s] (true? (is-valid-scheme? s)) "https" "http") 
  (are [s] (false? (is-valid-scheme? s)) "" "somethingrandom" "HTTPS" "HTTP"))

(deftest test-is-valid-templates-dir?
  (is (false? (is-valid-templates-dir? (u/create-temp-file))))
  (is (true? (is-valid-templates-dir? (u/create-temp-dir)))))
