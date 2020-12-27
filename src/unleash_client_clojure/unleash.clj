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
  "An abstraction of an Unleash client."
  (enabled?
    [this ^String toggle-name]
    [this ^String toggle-name fallback]
    [this ^String toggle-name ^UnleashContext context fallback]
    "Returns 'true' whether the toggle is enabled, 'false' otherwise. Can also provide a custom fallback option in case
    the toggle isn't found via 'fallback', or a toggle evaluation context via 'context'.")
  (get-variant
    [this ^String toggle-name]
    [this ^String toggle-name ^Variant default-variant] "Returns the instance's variant, useful for unit tests.")
  (get-variant-with-context
    [this ^String toggle-name ^UnleashContext context]
    [this ^String toggle-name ^UnleashContext context ^Variant default-variant]
    "Returns the instance's variant while evaluating the supplied context, useful for unit tests.")
  (get-toggle-definition [this toggle-name] "Returns a toggle's definition.")
  (get-feature-toggle-names [this] "Returns a sequence of all the feature toggles' names known to this client."))

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
  "Expects to be applied with a variadic number of builder-setter functions.
  Returns an instance of DefaultUnleash that supports AppsFlyer-specific strategies."
  [& builder-param-setters]
  (DefaultUnleash. (apply builder/build builder-param-setters)
                   (into-array Strategy [])))

(defn build-with-custom-strategies
  "Expects to be applied with a sequence of strategies and a variadic number of builder-setter functions.
  Returns an instance of DefaultUnleash that supports the provided strategies."
  [strategies & builder-param-setters]
  (DefaultUnleash. (apply builder/build builder-param-setters)
                   (into-array Strategy strategies)))
