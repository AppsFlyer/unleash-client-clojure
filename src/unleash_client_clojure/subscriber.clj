(ns unleash-client-clojure.subscriber
  "Create a Clojure wrapper over the UnleashSubscriber interface."
  (:import (io.getunleash.event UnleashSubscriber UnleashEvent UnleashReady ToggleEvaluated)
           (io.getunleash.repository FeatureToggleResponse ToggleCollection)
           (io.getunleash.metric ClientMetrics ClientRegistration)
           (io.getunleash UnleashException)))

(deftype Subscriber [on-error
                     on-event
                     on-ready
                     toggle-evaluated
                     toggles-fetched
                     client-metrics
                     client-registered
                     toggles-backed-up
                     toggle-backup-restored]
  UnleashSubscriber
  (^void onError [_ ^UnleashException ex]
    (on-error ex))
  (^void on [_ ^UnleashEvent event]
    (on-event event))
  (^void onReady [_ ^UnleashReady ready]
    (on-ready ready))
  (^void toggleEvaluated [_ ^ToggleEvaluated evaluated]
    (toggle-evaluated evaluated))
  (^void togglesFetched [_ ^FeatureToggleResponse response]
    (toggles-fetched response))
  (^void clientMetrics [_ ^ClientMetrics metrics]
    (client-metrics metrics))
  (^void clientRegistered [_ ^ClientRegistration registration]
    (client-registered registration))
  (^void togglesBackedUp [_ ^ToggleCollection _collection]
    (toggles-backed-up))
  (^void toggleBackupRestored [_ ^ToggleCollection collection]
    (toggle-backup-restored collection)))

(defn- no-op [_])

(defn build
  "Expects a map of event types to event handlers.
  Returns an instance of UnleashSubscriber which would be called when the internal state of the Unleash client changes.
  This instance can be attached to the Unleash client by using unleash-client-clojure.builder/subscriber."
  [{:keys [on-error on-event on-ready toggle-evaluated
           toggles-fetched client-metrics client-registered
           toggles-backed-up toggle-backup-restored]
    :or   {on-error               no-op
           on-event               no-op
           toggle-evaluated       no-op
           toggles-fetched        no-op
           client-metrics         no-op
           client-registered      no-op
           toggles-backed-up      no-op
           toggle-backup-restored no-op}}]

  (Subscriber. on-error on-event on-ready toggle-evaluated toggles-fetched
               client-metrics client-registered toggles-backed-up toggle-backup-restored))
