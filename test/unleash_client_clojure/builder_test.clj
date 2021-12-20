(ns unleash-client-clojure.builder-test
  (:require [clojure.test :refer [deftest is testing]]
            [unleash-client-clojure.builder :as b]
            [unleash-client-clojure.context :as c]
            [unleash-client-clojure.subscriber :as subscriber]
            [clojure.string :as s])
  (:import [java.io File]
           [no.finn.unleash CustomHttpHeadersProvider UnleashContextProvider]
           [no.finn.unleash.strategy DefaultStrategy] 
           [no.finn.unleash.repository ToggleBootstrapProvider]
           [java.net Proxy]))

(deftest builder
  (testing "building unleash sets correct config"
    (let [app-name    "app"
          instance-id "some-instance"
          api         "http://127.0.0.1"
          backup-path (s/join File/separatorChar
                              [(System/getProperty "java.io.tmpdir") (str (rand-int 100) ".json")])
          context     (c/build (c/app-name "some-app"))
          fallback-strategy (DefaultStrategy.)
          subscriber  (subscriber/build {})
          config      (b/build
                        (b/app-name app-name)
                        (b/instance-id instance-id)
                        (b/unleash-api api)
                        (b/custom-http-header "header-name" "header-value")
                        (b/send-metrics-interval 42)
                        (b/fetch-toggles-interval 43)
                        (b/synchronous-fetch-on-initialisation true)
                        (b/enable-proxy-authentication-by-jvm-properties)
                        (b/backup-file backup-path)
                        (b/environment "staging")
                        (b/custom-http-header-provider
                          (reify CustomHttpHeadersProvider
                            (getCustomHeaders [_] {"foo" "bar"})))
                        (b/unleash-context-provider
                          (reify UnleashContextProvider
                            (getContext [_] context)))
                        (b/subscriber subscriber)
                        (b/fallback-strategy fallback-strategy)
                        (b/toggle-bootstrap-provider (reify ToggleBootstrapProvider
                                                       (read [_this] "{}")))
                        (b/proxy Proxy/NO_PROXY))]

      (is (= {"header-name" "header-value"}
             (.getCustomHttpHeaders config)))
      (is (= 42
             (.getSendMetricsInterval config)))
      (is (= 43
             (.getFetchTogglesInterval config)))
      (is (= true
             (.isSynchronousFetchOnInitialisation config)))
      (is (= true
             (.isProxyAuthenticationByJvmProperties config)))
      (is (= backup-path
             (.getBackupFile config)))
      (is (= "staging"
             (.getEnvironment config)))
      (is (= {"foo" "bar"}
             (.getCustomHeaders (.getCustomHttpHeadersProvider config))))
      (is (= "some-app"
             (-> config
                 (.getContextProvider)
                 (.getContext)
                 (.getAppName)
                 (.get))))
      (is (= subscriber
             (.getSubscriber config))))))

(comment
  (require '[unleash-client-clojure.builder :as b])
  (require '[unleash-client-clojure.unleash :as unleash])
  (def u (unleash/build (b/unleash-api "http://af-unleash-skad.eu1.appsflyer.com:8080") (b/app-name "test"))))
  
