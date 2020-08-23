(ns unleash-client-clojure.context
  (:import [no.finn.unleash UnleashContext UnleashContext$Builder]))

(defn build ^UnleashContext 
  [& fs]
  (let [bldr (UnleashContext$Builder.)]
    (.build ^UnleashContext$Builder
            (reduce
              (fn
                ([] bldr) 
                ([bldr f]
                 (f bldr)))
              bldr
              fs))))

(defn app-name [^String app-name]
  (fn [^UnleashContext$Builder bldr]
    (.appName bldr app-name)))

(defn environment [^String environment]
  (fn [^UnleashContext$Builder bldr]
    (.environment bldr environment)))

(defn user-id [^String id]
  (fn [^UnleashContext$Builder bldr]
    (.userId bldr id)))

(defn session-id [^String id]
  (fn [^UnleashContext$Builder bldr]
    (.sessionId bldr id)))

(defn remote-address [^String address]
  (fn [^UnleashContext$Builder bldr]
    (.remoteAddress bldr address)))

(defn add-property [^String name ^String value]
  (fn [^UnleashContext$Builder bldr]
    (.addProperty bldr name value)))

