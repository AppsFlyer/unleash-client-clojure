# unleash-client-clojure

[![Clojars Project](https://img.shields.io/clojars/v/unleash-client-clojure.svg)](https://clojars.org/unleash-client-clojure)
[![Coverage Status](https://coveralls.io/repos/github/AppsFlyer/unleash-client-clojure/badge.svg?branch=master)](https://coveralls.io/github/AppsFlyer/unleash-client-clojure?branch=master)

A Clojure library wrapping [unleash java client](https://github.com/Unleash/unleash-client-java)

## Usage

### Getting feature toggles

```clojure
(require '[unleash-client-clojure.unleash :as u]
         '[unleash-client-clojure.builder :as b])
;; a simple client
(def unleash (u/build (b/app-name "app-name") (b/unleash-api "http://unleash.herokuapp.com/api/")))
;; simple toggle
(u/enabled? unleash "Bit")
;; toggle with context
(require '[unleash-client-clojure.context :as c])
(def user-context (c/build (c/user-id "user-id")))
(u/enabled? unleash "toggle-name" user-context false)
;; more configuration can be passed by using the functions in the builder namespace
(require '[unleash-client-clojure.builder :as b])
(def unleash (u/build "app-name" "instance-id" "http://unleash.herokuapp.com/api/" (b/environment "staging") (b/fetch-toggles-interval 15)))
```

### Variant support

```clojure
(require '[unleash-client-clojure.variant :as v])
(u/get-variant unleash "DemoVariantWithPayload")
(let [x (u/get-variant unleash "DemoVariantWithPayload")]
  [(v/get-name x)
   (v/get-type x)
   (v/variant-enabled? x)
   (v/get-value x)])
```

### Advanced client config

```clojure
;; the builder namespace supports passing builder confgurations as functions
(require '[unleash-client-clojure.builder :as b])
(def unleash (u/build
               (b/app-name "app-name")
               (b/unleash-api "http://unleash.herokuapp.com/api/")
               (b/environment "staging")
               (b/fetch-toggles-interval 1)))
;; see the builder namespace for more available options
```

### Subscriber support

```clojure
;; one a subscriber can be pased to the client builder by passing
(b/subscriber my-subscriber)
;; the subscriber namespace has functions to help you build a subsriber
(require '[unleash-client-clojure.subscriber :as s])
;; pass a map with the keys [:on-error :on-event :toggle-evaluated
;; :toggles-fetched :client-metrics :client-registered :toggles-backed-up
;; :toggle-backup-restored] and callbacks you want to register.
;; any missing key defaults to no-op
;; this is a very noisy example:
(def unleash
  (u/build
    (b/app-name "app-name")
    (b/unleash-api "http://unleash.herokuapp.com/api/")
    (b/subscriber
      (s/build {:on-event println})))
```

#### TODO:

- [ ] Integration testing
