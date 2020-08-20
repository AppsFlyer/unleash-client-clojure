(ns unleash-client-clojure.unleash
  (:require [unleash-client-clojure.builder :as builder])
  (:import [no.finn.unleash DefaultUnleash]
           [no.finn.unleash.strategy Strategy]))

(defprotocol IUnleash
  (enabled? [this ^String toggle-name]
            [this ^String toggle-name ^boolean default-setting]))

(extend-protocol IUnleash
  DefaultUnleash
  (enabled?
    ([this toggle-name]
     (.isEnabled this toggle-name))
    ([this toggle-name default-setting]
     (.isEnabled this toggle-name default-setting))))

(defn build
  [app-name ^String instance-id ^String api & fs]
  (DefaultUnleash. (apply builder/build app-name instance-id api fs)
                   (into-array Strategy [])))

