(defproject unleash-client-clojure "0.3.0"
  :description "A Clojure library wrapping https://github.com/Unleash/unleash-client-java"
  :url "https://github.com/AppsFlyer/unleash-client-clojure"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url  "https://www.eclipse.org/legal/epl-2.0/"}
  :deploy-repositories []
  ["releases" {:url "https://repo.clojars.org"
               :username :env/clojars_username
               :password :env/clojars_password
               :sign-releases false}] 
  ["snapshots" {:url "https://repo.clojars.org"
                :username :env/clojars_username
                :password :env/clojars_password
                :sign-releases false}]
  :dependencies [[no.finn.unleash/unleash-client-java "3.3.4" :exclusions [org.apache.logging.log4j/log4j-api]]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.3"]
                                  [clj-kondo "RELEASE"]
                                  [org.apache.logging.log4j/log4j-core "2.15.0"]]
                   :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]
                                  "lint"      ["run" "-m" "clj-kondo.main" "--lint" "src" "test"]}
                   :plugins      [[lein-ancient "1.0.0-RC3"]
                                  [lein-cloverage "1.2.2"]]
                   :global-vars  {*warn-on-reflection* true}}})
