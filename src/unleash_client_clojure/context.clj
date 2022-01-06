(ns unleash-client-clojure.context
  "Create and configure builders that build instances of UnleashContext."
  (:import (io.getunleash UnleashContext UnleashContext$Builder)))

(defn build
  "Expects to be applied with a variadic number of arguments, each of which is a function that expects an
  UnleashContext$Builder instance.
  Returns an instance of UnleashContext that had all of its parameters set by said functions.

  Using this building pattern allows users to manipulate the builder instance in ways that aren't
  implemented in this library by passing a function that expects an UnleashContext$Builder.

  Example:
  (build (app-name \"test-app\")
         (environment \"STG\"))

  The UnleashContext instance can be attached to an Unleash client via
  unleash-client-clojure.builder/unleash-context-provider."
  ^UnleashContext
  [& builder-param-setters]
  (let [builder (UnleashContext$Builder.)]
    (.build ^UnleashContext$Builder
            (reduce
              (fn
                ([] builder)
                ([builder set-builder-param]
                 (set-builder-param builder)))
              builder
              builder-param-setters))))

(defn get-app-name
  "Returns the app's name from the context."
  [^UnleashContext ctx]
  (.get (.getAppName ctx)))

(defn get-environment
  "Returns the environment from the context."
  [^UnleashContext ctx]
  (.get (.getEnvironment ctx)))

(defn get-user-id
  "Returns the user ID from the context."
  [^UnleashContext ctx]
  (.get (.getUserId ctx)))

(defn get-session-id
  "Returns the session ID from the context."
  [^UnleashContext ctx]
  (.get (.getSessionId ctx)))

(defn get-remote-address
  "Returns the remote address from the context."
  [^UnleashContext ctx]
  (.get (.getRemoteAddress ctx)))

(defn get-property
  "Returns a context property"
  [^UnleashContext ctx ^String property-name]
  (.get (.getProperties ctx) property-name))

(defn get-by-name
  "Returns a context property, explicitly looking for:
  - environment
  - appName
  - userId
  - sessionId
  - remoteAddress
  Otherwise, behaves as 'get-property'."
  [^UnleashContext ctx ^String contextName]
  (.get (.getByName ctx contextName)))

(defn app-name
  "Expects an application's name.
  Returns a function that expects an UnleashContext$Builder, and sets the appName property of the builder."
  [^String app-name]
  (fn [^UnleashContext$Builder builder]
    (.appName builder app-name)))

(defn environment
  "Expects an application environment.
  Returns a function that expects an UnleashContext$Builder, and sets the environment property of the builder."
  [^String environment]
  (fn [^UnleashContext$Builder builder]
    (.environment builder environment)))

(defn user-id
  "Expects a user ID.
  Returns a function that expects an UnleashContext$Builder, and sets the userId property of the builder."
  [^String id]
  (fn [^UnleashContext$Builder builder]
    (.userId builder id)))

(defn session-id
  "Expects a session ID.
  Returns a function that expects an UnleashContext$Builder, and sets the sessionId property of the builder."
  [^String id]
  (fn [^UnleashContext$Builder builder]
    (.sessionId builder id)))

(defn remote-address
  "Expects a remote address (IP address). Used by RemoteAddressStrategy.
  Returns a function that expects an UnleashContext$Builder, and sets the remoteAddress property of the builder."
  [^String address]
  (fn [^UnleashContext$Builder builder]
    (.remoteAddress builder address)))

(defn add-property
  "Expects a property name-value pair.
  Returns a function that expects an UnleashContext$Builder, and adds the key-value pair to the context's properties."
  [^String name ^String value]
  (fn [^UnleashContext$Builder builder]
    (.addProperty builder name value)))
