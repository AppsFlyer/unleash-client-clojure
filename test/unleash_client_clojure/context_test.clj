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
             (c/get-app-name context)))
      (is (= "env"
             (c/get-environment context)))
      (is (= "userId"
             (c/get-user-id context)))
      (is (= "42"
             (c/get-session-id context)))
      (is (= "1.2.2.1"
             (c/get-remote-address context)))
      (is (= "bar"
             (c/get-property context "foo")))
      (is (= "baz"
             (c/get-by-name context "bar"))))))
