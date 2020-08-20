(ns unleash-client-clojure.fake
  (:require [unleash-client-clojure.unleash :as unleash])
  (:import [no.finn.unleash FakeUnleash]))

(defprotocol IFakeUnleash
  (enable-all [this])
  (disable-all [this])
  (reset-all [this])
  (enable [this & features])
  (disable [this & features])
  (reset [this & features]))
  
(extend-protocol unleash/IUnleash
  FakeUnleash
  (enabled?
    ([this toggle-name]
     (.isEnabled this toggle-name))
    ([this toggle-name default-setting]
     (.isEnabled this ^String toggle-name ^boolean default-setting)))
  (get-feature-toggle-names [this]
    (.getFeatureToggleNames this)))

(extend-protocol IFakeUnleash
  FakeUnleash
  (enable-all [this]
    (.enableAll this))
  (disable-all [this]
    (.disableAll this))
  (reset-all [this]
    (.resetAll this))
  (enable [this & features]
    (.enable this (into-array String features)))
  (disable [this & features]
    (.disable this (into-array String features)))
  (reset [this & features]
    (.reset this (into-array String features))))
  
(defn build [] (FakeUnleash.)) 

