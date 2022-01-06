(ns unleash-client-clojure.builder
  "Create and configure builders that build instances of UnleashConfig."
  (:import (io.getunleash CustomHttpHeadersProvider UnleashContextProvider)
           (io.getunleash.util UnleashConfig UnleashConfig$Builder UnleashScheduledExecutor)
           (io.getunleash.event UnleashSubscriber)
           (io.getunleash.strategy Strategy)
           (io.getunleash.repository ToggleBootstrapProvider)
           (java.net Proxy)))

(defn build
  "Expects to be applied with a variadic number of arguments, each of which is a function that expects an
  UnleashConfig$Builder instance.
  Returns an instance of UnleashConfig that had all of its parameters set by said functions.

  Using this building pattern allows users to manipulate the builder instance in ways that aren't
  implemented in this library by passing a function that expects an UnleashConfig$Builder.

  Example:
  (build (app-name \"test-app\")
         (unleash-api \"http://example.unleash.com/api\"))"
  ^UnleashConfig [& builder-param-setters]
  (let [builder (UnleashConfig$Builder.)]
    (.build ^UnleashConfig$Builder
            (reduce
              (fn
                ([] builder)
                ([builder set-builder-param]
                 (set-builder-param builder)))
              builder
              builder-param-setters))))

(defn app-name
  "Expects an application name.
  Returns a function that expects an UnleashConfig$Builder, and sets the appName property of the builder."
  [^String name]
  (fn [^UnleashConfig$Builder builder]
    (.appName builder name)))

(defn instance-id
  "Expects an instance ID.
  Returns a function that expects an UnleashConfig$Builder, and sets the instanceId property of the builder."
  [^String id]
  (fn [^UnleashConfig$Builder builder]
    (.instanceId builder id)))

(defn unleash-api
  "Expects a URL for an Unleash server.
  Returns a function that expects an UnleashConfig$Builder, and sets the unleashAPI property of the builder."
  [^String api]
  (fn [^UnleashConfig$Builder builder]
    (.unleashAPI builder api)))

(defn custom-http-header
  "Expects an HTTP header name and value.
  Returns a function that expects an UnleashConfig$Builder, and adds the relevant HTTP header name and value to the headers used by the builder.
  These headers would be added to outgoing HTTP requests sent by the Unleash client to the Unleash server.

  Can be used multiple times to set multiple headers.

  Example:
  (build [(unleash-api \"http://example.unleash.com/api\")
          (custom-http-header \"X-AF-HEADER-1\" \"a\")
          (custom-http-header \"X-AF-HEADER-2\" \"b\")
          (custom-http-header \"X-AF-HEADER-3\" \"c\")])"
  [header-name header-value]
  (fn [^UnleashConfig$Builder builder]
    (.customHttpHeader builder header-name header-value)))

(defn send-metrics-interval
  "Expects a long that marks the interval (in seconds) of sending metrics.
  Returns a function that expects an UnleashConfig$Builder, and sets the sendMetricsInterval property of the builder."
  [^Long interval-seconds]
  (fn [^UnleashConfig$Builder builder]
    (.sendMetricsInterval builder interval-seconds)))

(defn fetch-toggles-interval
  "Expects a long that marks the interval (in seconds) of fetching toggles' states from the Unleash server.
  Returns a function that expects an UnleashConfig$Builder, and sets the fetchTogglesInterval property of the builder."
  [^Long interval-seconds]
  (fn [^UnleashConfig$Builder builder]
    (.fetchTogglesInterval builder interval-seconds)))

(defn synchronous-fetch-on-initialisation
  "Expects a boolean that controls if toggles are fetched synchronously when the Unleash client is initialised.
  Returns a function that expects an UnleashConfig$Builder, and sets the synchronousFetchOnInitialisation property of
  the builder."
  [enable?]
  (fn [^UnleashConfig$Builder builder]
    (.synchronousFetchOnInitialisation builder enable?)))

(defn enable-proxy-authentication-by-jvm-properties
  "Expects a boolean that controls if authentication against an HTTP proxy would use the JVM settings http.proxyUser and
  http.proxyPassword.
  Returns a function that expects an UnleashConfig$Builder, and sets the isProxyAuthenticationByJvmProperties property of the builder."
  []
  (fn [^UnleashConfig$Builder builder]
    (.enableProxyAuthenticationByJvmProperties builder)))

(defn backup-file
  "Expects a backup file path, into which the Unleash client would backup its state to be used when the Unleash server
  is unavailable. By defeault this would be unleash-repo.json in the directory set by the JVM property java.io.tmpdir.
  Returns a function that expects an UnleashConfig$Builder, and sets the backupFile property of the builder."
  [^String backup-file]
  (fn [^UnleashConfig$Builder builder]
    (.backupFile builder backup-file)))

(defn environment
  "Expects an application environment.
  Returns a function that expects an UnleashConfig$Builder, and sets the environment property of the builder."
  [^String environment]
  (fn [^UnleashConfig$Builder builder]
    (.environment builder environment)))

(defn custom-http-header-provider
  "Expects an instance of CustomHttpHeadersProvider, which adds headers to outgoing HTTP requests that are sent to the
  Unleash server, based on the contents of the request itself.
  Returns a function that expects an UnleashConfig$Builder, and sets the custom-http-headers-provider property of the
  builder."
  [^CustomHttpHeadersProvider provider]
  (fn [^UnleashConfig$Builder builder]
    (.customHttpHeadersProvider builder provider)))


(defn unleash-context-provider
  "Expects an instance of UnleashContextProvider, which would provide the context for calls of enabled? instead of
  having it provided as an arguments in the call site.
  Returns a function that expects an UnleashConfig$Builder, and sets the contextProvider property of the builder."
  [^UnleashContextProvider provider]
  (fn [^UnleashConfig$Builder builder]
    (.unleashContextProvider builder provider)))

(defn scheduled-executor
  "Expects an instance of UnleashScheduledExecutor, which would be used to periodically send usage metrics. The interval
  of sending those metrics can be set with 'send-metrics-interval'.
  Returns a function that expects an UnleashConfig$Builder, and sets the scheduledExecutor property of the builder."
  [^UnleashScheduledExecutor executor]
  (fn [^UnleashConfig$Builder builder]
    (.scheduledExecutor builder executor)))

(defn subscriber
  "Expects an instance of UnleashSubscriber, which would be notified when the Unleash client's internal state changes.
  Returns a function that expects an UnleashConfig$Builder, and sets the subscriber property of the builder."
  [^UnleashSubscriber subscriber]
  (fn [^UnleashConfig$Builder builder]
    (.subscriber builder subscriber)))

(defn fallback-strategy
  [^Strategy fallback-strategy]
  (fn [^UnleashConfig$Builder builder]
    (.fallbackStrategy builder fallback-strategy)))

(defn 
  toggle-bootstrap-provider
  "Proxy has a sungle read() method that should return a Json of the form obtained by /api/client/features"
  [^ToggleBootstrapProvider toggle-bootstrap-provider]
  (fn [^UnleashConfig$Builder builder]
    (.toggleBootstrapProvider builder toggle-bootstrap-provider)))

(defn set-proxy
  ([^Proxy proxy]
   (fn [^UnleashConfig$Builder builder]
     (.proxy builder proxy)))
  ([^Proxy proxy ^String proxy-user ^String proxy-password]
   (fn [^UnleashConfig$Builder builder]
     (.proxy builder proxy proxy-user proxy-password))))
