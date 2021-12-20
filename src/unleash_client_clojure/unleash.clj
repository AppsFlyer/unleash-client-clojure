(ns unleash-client-clojure.unleash
  (:require [unleash-client-clojure.builder :as builder]
            [unleash-client-clojure.util]
            [unleash_client_clojure.variant])
  (:import [no.finn.unleash DefaultUnleash EvaluatedToggle]
           [no.finn.unleash.strategy Strategy]
           [no.finn.unleash UnleashContext Variant]
           [no.finn.unleash.variant Payload]
           [java.util Optional]
           [unleash_client_clojure.util BiFunctionWrapper]))

(defn payload->map [^Payload payload]
  {:type (.getType payload)
   :value (.getValue payload)})

(defn variant->map [^Variant v]
  (when v
    {:name (.getName v)
     :payload (let [payload ^Optional (.getPayload v)]
                (when (.isPresent payload)
                  (payload->map (.get payload))))
     :enabled? (.isEnabled v)}))

(defn evaluated-toggles->map [^EvaluatedToggle et]
  (when et
    {:enabled? (.isEnabled et)
     :name (.getName et)
     :variant (variant->map (.getVariant et))}))

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
  (get-feature-toggle-names [this] "Returns a sequence of all the feature toggles' names known to this client.")
  (more [this])
  (evaluate-all-toggles [this] 
                        [this context])
  (register-count [this toggle-name variant-name])
  (count-variant [this toggle-name variant-name]))

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
     (.getVariant this ^String toggle-name))
    ([this toggle-name default-variant]
     (.getVariant this ^String toggle-name ^Variant default-variant)))
  (get-variant-with-context
    ([this toggle-name context]
     (.getVariant this ^String toggle-name ^UnleashContext context))
    ([this toggle-name context default-variant]
     (.getVariant this ^String toggle-name ^UnleashContext context ^Variant default-variant)))
  (get-toggle-definition [this toggle-name]
    (.orElse ^Optional (.getFeatureToggleDefinition this toggle-name)
             nil))
  (more [this]
    (.more this))
  (evaluate-all-toggles 
    ([this]
     (vec (.evaluateAllToggles (more this))))
    ([this context]
     (vec (.evaluateAllToggles (more this) ^UnleashContext context))))
  (get-feature-toggle-names [this]
    (vec (.getFeatureToggleNames (more this))))
  (register-count [this toggle-name enabled?]
    (.count (more this) toggle-name enabled?))
  (count-variant [this toggle-name variant-name]
    (.countVariant (more this) toggle-name variant-name)))
    

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
