(defproject unleash-client-clojure "0.2.0"
  :description "A Clojure library wrapping https://github.com/Unleash/unleash-client-java"
  :url "https://github.com/AppsFlyer/unleash-client-clojure"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[no.finn.unleash/unleash-client-java "3.3.3"]]
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.10.1"]
                                  [clj-kondo "RELEASE"]
                                  [org.apache.logging.log4j/log4j-core "2.11.2"]]
                    :aliases      {"clj-kondo" ["run" "-m" "clj-kondo.main"]
                                   "lint"      ["run" "-m" "clj-kondo.main" "--lint" "src" "test"]}
                    :plugins     [[lein-ancient "0.6.15"]
                                  [lein-cloverage "1.2.0"]]
                    :global-vars {*warn-on-reflection* true}}})
