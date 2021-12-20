(ns unleash-client-clojure.unleash-test
  (:require [unleash-client-clojure.unleash :as u]
            [unleash-client-clojure.variant :as v]
            [unleash-client-clojure.fake]
            [clojure.test :refer [testing is deftest]])
  (:import [no.finn.unleash FakeUnleash Variant]
           [no.finn.unleash.variant Payload]))

(deftest is-enabled
  (let [fake-unleash (FakeUnleash.)]
    (testing "enabled returns true"
      (.enableAll fake-unleash)
      (is (true? (u/enabled? fake-unleash "foo"))))
    (testing "enabled returns false"
      (.resetAll fake-unleash)
      (is (false? (u/enabled? fake-unleash "foo"))))
    (testing "enabled returns true/false"
      (.enable fake-unleash (into-array String ["foo"]))
      (is (true? (u/enabled? fake-unleash "foo")))
      (.disable fake-unleash (into-array String ["foo"]))
      (is (false? (u/enabled? fake-unleash "foo"))))
    (testing "enabled returns false for unknwon"
      (is (false? (u/enabled? fake-unleash "bar"))))))

(deftest variants
  (let [fu (FakeUnleash.)]
    (testing "get-variant"
      (.enable fu (into-array String ["variant1"]))
      (let [payload (Payload. "string" "secret-string")]
        (.setVariant fu "variant1" (Variant. "red" payload true))
        (let [v (u/get-variant fu "variant1")]
          (is (true? (v/variant-enabled? v)))
          (is (= "red" (v/get-name v)))
          (is (= payload (v/payload v)))
          (is (= "string" (v/get-type v)))
          (is (= "secret-string" (v/get-value v))))
        (let [v (u/get-variant fu "not-there")]
          (is (false? (v/variant-enabled? v)))
          (is (= "disabled" (v/get-name v)))
          (is (nil? (v/payload v)))
          (is (nil? (v/get-type v)))
          (is (nil? (v/get-value v))))))))
