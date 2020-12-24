(ns unleash-client-clojure.variant
  (:import [no.finn.unleash.variant Payload]
           [no.finn.unleash Variant]
           [java.util Optional]))

(defprotocol IVariant
  "A protocol to bridge the Java class no.finn.unleash.Variant into Clojure.
  The class is used in unit tests to change the state of a FakeUnleash."
  (variant-enabled? [this] "Returns whether the variant is active or not.")
  (get-name [this] "Returns the variant's name.")
  (payload [this] "Returns the variant's payload, if it's present and the variant is enabled, otherwise returns nil.")
  (get-type [this] "Returns the variant's type if it's present and the variant is enabled, otherwise returns nil.")
  (get-value [this] "Returns the variant's value, if present, otherwise returns nil."))

(deftype OptionalPayloadVariant [^Variant variant]
  IVariant
  (variant-enabled? [_]
    (.isEnabled variant))
  (get-name [_]
    (.getName variant))
  (payload [_]
    (when (.isEnabled variant)
      (let [option ^Optional (.getPayload variant)]
        (when (.isPresent option)
          (.get option)))))
  (get-type [this]
    (when (.isEnabled variant)
      (when-let [payload ^Payload (.payload this)]
        (.getType payload))))
  (get-value [this]
    (when-let [payload ^Payload (.payload this)]
      (.getValue payload))))


