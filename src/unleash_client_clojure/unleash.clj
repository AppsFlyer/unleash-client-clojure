(ns unleash-client-clojure.unleash
  (:require [unleash-client-clojure.builder :as builder]
            [unleash-client-clojure.util]
            [unleash_client_clojure.variant])
  (:import [no.finn.unleash DefaultUnleash]
           [no.finn.unleash.strategy Strategy]
           [no.finn.unleash UnleashContext Variant]
           [java.util Optional]
           [unleash_client_clojure.util BiFunctionWrapper]
           [unleash_client_clojure.variant OptionalPayloadVariant]))

(defprotocol IUnleash
  (enabled?
    [this ^String toggle-name]
    [this ^String toggle-name fallback]
    [this ^String toggle-name ^UnleashContext context fallback])
  (get-variant
    [this ^String toggle-name]
    [this ^String toggle-name ^Variant default-variant])
  (get-variant-with-context
    [this ^String toggle-name ^UnleashContext context]
    [this ^String toggle-name ^UnleashContext context ^Variant default-variant])
  (get-toggle-definition [this toggle-name])
  (get-feature-toggle-names [this]))

(extend-protocol IUnleash
  DefaultUnleash
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
  (get-toggle-definition [this toggle-name]
    (.orElse ^Optional (.getFeatureToggleDefinition this toggle-name)
             nil))
  (get-feature-toggle-names [this]
    (vec (.getFeatureToggleNames this))))

(defn build
  [app-name ^String instance-id ^String api & fs]
  (DefaultUnleash. (apply builder/build app-name instance-id api fs)
                   (into-array Strategy [])))

(defn build-with-custom-strategies
  [app-name ^String instance-id ^String api strategies & fs]
  (DefaultUnleash. (apply builder/build app-name instance-id api fs)
                   (into-array Strategy strategies)))
