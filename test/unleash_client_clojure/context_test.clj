(ns unleash-client-clojure.context-test
  (:require [clojure.test :refer [deftest is testing]]
            [unleash-client-clojure.context :as c]))

(deftest builder
  (testing "building unleash context sets correct config"
    (let [context (c/build
                    (c/app-name "app") 
                    (c/environment "env") 
                    (c/user-id "userId") 
                    (c/session-id "42") 
                    (c/remote-address "1.2.2.1") 
                    (c/add-property "foo" "bar") 
                    (c/add-property "bar" "baz"))] 
                   
      (is (= "app"
             (.get (.getAppName context))))
      (is (= "env"
             (.get (.getEnvironment context))))
      (is (= "userId"
             (.get (.getUserId context))))
      (is (= "42"
             (.get (.getSessionId context))))
      (is (= "1.2.2.1"
             (.get (.getRemoteAddress context))))
      (is (= "bar"
             (.get (.getByName context "foo"))))
      (is (= "baz"
             (.get (.getByName context "bar")))))))
