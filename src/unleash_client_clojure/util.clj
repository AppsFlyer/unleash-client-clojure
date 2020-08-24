(ns unleash-client-clojure.util
  (:import [java.util.function BiFunction]))

(deftype BiFunctionWrapper [f]
  BiFunction
  (apply [_ toggle-name context]
    (f toggle-name context)))

