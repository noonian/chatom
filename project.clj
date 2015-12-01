(defproject chatom "0.1.0-SNAPSHOT"
  :description "An example chat application implemented in Clojure and ClojureScript"
  :url "https://noonian.github.io/chatom"
  :license {:name "MIT License"
            :url "http://opensource.org/licenses/MIT"
            :year 2015
            :key "mit"}
  :dependencies [;; common
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "1.7.170" :scope "test"]
                 [org.clojure/core.async "0.2.374"]
                 [bidi "1.21.1"]
                 [levand/immuconf "0.1.0"]

                 ;; server
                 [aleph "0.4.1-beta2"]
                 [buddy "0.8.1"]
                 [com.stuartsierra/component "0.3.1"]
                 [com.h2database/h2 "1.4.190"]
                 [duct/hikaricp-component "0.1.0"]
                 [ring/ring "1.4.0"]
                 [ring-middleware-format "0.7.0"]
                 [yesql "0.5.1"]

                 ;; web
                 [org.omcljs/om "1.0.0-alpha25-SNAPSHOT" :scope "test"]
                 [sablono "0.4.0" :scope "test"]
                 [kibu/pushy "0.3.6" :scope "test"]
                 [devcards "0.2.1" :scope "test"]]
  :plugins [[lein-cljsbuild "1.1.1"]
            [lein-figwheel "0.5.0-1"]]
  :source-paths ["src/server" "src/common"]
  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]
  :cljsbuild {:builds
              [{:id "dev"
                :source-paths ["src/web" "src/common"]
                :figwheel {:on-jsload "chatom.web.core/on-js-reload"}
                :compiler {:main chatom.web.core
                           :asset-path "js/compiled/out"
                           :output-to "resources/public/js/compiled/chatom.js"
                           :output-dir "resources/public/js/compiled/out"
                           :source-map-timestamp true}}
               {:id "min"
                :source-paths ["src/web" "src/common"]
                :compiler {:output-to "resources/public/js/compiled/chatom.min.js"
                           :main chatom.core
                           :optimizations :advanced
                           :pretty-print false}}]}
  :figwheel {:css-dirs ["resources/public/css"]})
