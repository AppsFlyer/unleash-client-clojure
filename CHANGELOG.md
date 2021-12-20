# Change Log
All notable changes to this project will be documented in this file. This change log follows the conventions of [keepachangelog.com](http://keepachangelog.com/).

## [0.4.0]
### Cahnged
- depend on latest Java client (4.4.1) and expose additional API.
- variant methods now return the raw `Variant` object. Additional fucntions to firther convert them to Clojure maps added.
### Added
- Additional configurations to the client (fallback starategy, bootstrap provider, proxy)
- better logging on unit tests

## [0.3.0]
### Changed
- removed direct log dependency originating upstream.
