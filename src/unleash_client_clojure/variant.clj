(ns unleash-client-clojure.variant
  (:import [no.finn.unleash.variant Payload]
           [no.finn.unleash Variant]
           [java.util Optional]))

(defprotocol IVariant
  (variant-enabled? [this])
  (get-name [this])
  (payload [this])
  (get-type [this])
  (get-value [this]))

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


