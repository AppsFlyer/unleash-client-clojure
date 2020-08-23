# unleash-client-clojure

A Clojure library wrapping [unleash java client](https://github.com/Unleash/unleash-client-java)
## Usage

```clojure
(require '[unleash-client-clojure.unleash :as u])
;; a simple client
(def unleash (u/build "app-name" "instance-id" "http://unleash.herokuapp.com/api/"))
(u/enabled? unleash "Bit")
;; more configuration can be passed by using the functions in the builder namespace
(require '[unleash-client-clojure.builder :as b])
(def unleash (u/build "app-name" "instance-id" "http://unleash.herokuapp.com/api/" (b/environment "staging") (b/fetch-toggles-interval 15)))

```
