(ns unleash-client-clojure.fake
  "Creating FakeUnleash instances that adhere to the IUnleash protocol."
  (:require [unleash-client-clojure.unleash :as u])
  (:import [unleash_client_clojure.variant OptionalPayloadVariant]
           [no.finn.unleash FakeUnleash]
           [no.finn.unleash UnleashContext Variant]
           [unleash_client_clojure.util BiFunctionWrapper]))

(extend-protocol u/IUnleash
  FakeUnleash
  (enabled?
    ([this toggle-name]
     (.isEnabled this toggle-name))
    ([this toggle-name fallback]
     (if (fn? fallback)
       (.isEnabled this ^String toggle-name (BiFunctionWrapper. fallback))
       (.isEnabled this ^String toggle-name ^boolean fallback)))
    ([this ^String toggle-name ^UnleashContext context fallback]
     (if (fn? fallback)
       (.isEnabled this ^String toggle-name ^UnleashContext context (BiFunctionWrapper. fallback))
       (.isEnabled this ^String toggle-name ^UnleashContext context ^boolean fallback))))

  (get-variant
    ([this toggle-name]
     (OptionalPayloadVariant. (.getVariant this ^String toggle-name)))
    ([this toggle-name default-variant]
     (OptionalPayloadVariant. (.getVariant this ^String toggle-name ^Variant default-variant))))

  (get-variant-with-context
    ([this toggle-name context]
     (OptionalPayloadVariant. (.getVariant this ^String toggle-name ^UnleashContext context)))
    ([this toggle-name context default-variant]
     (OptionalPayloadVariant. (.getVariant this ^String toggle-name ^UnleashContext context ^Variant default-variant))))

  (get-toggle-definition [_this _toggle-name]
    (throw (UnsupportedOperationException.)))

  (get-feature-toggle-names [this]
    (vec (.getFeatureToggleNames this))))

(defn build
  "Returns an instance of FakeUnleash for unit test purposes."
  []
  (FakeUnleash.))
