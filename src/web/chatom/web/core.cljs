(ns chatom.web.core
  (:import [goog.net XhrIo])
  (:require [om.next :as om]
            cljsjs.react.dom ;om won't pull this in unless you require om.dom
            [chatom.web.parser :as parser]
            [chatom.web.state :as state]
            [chatom.web.ui.core :as ui]
            [cognitect.transit :as transit]
            [om.transit :as om-transit]
            [cljs.pprint :refer [pprint]]))

(enable-console-print!)

(def remote-url "/api")

(defonce app-state
  (atom state/init-data))

(defn post-remote [data cb]
  (.send XhrIo remote-url
         (fn [e]
           (let [xhr (.-target e)
                 response-data (.getResponseText xhr)
                 response (transit/read (om-transit/reader) response-data)]
             (cb response)))
         "POST"
         (transit/write (om-transit/writer) data)
         #js {"Content-Type" "application/transit+json"}))

(defn send [remotes merge-results]
  (println "reconciler send function called with remotes:")
  (pprint remotes)
  (when-let [query (:remote remotes)]
    (post-remote query merge-results)))

(defonce reconciler
  (om/reconciler
   {:state app-state
    :parser parser/parser
    :normalize true
    :remotes [:remote]
    :send send}))

(defn parse [query & args]
  (let [env {:state app-state}]
    (apply parser/parser env query args)))

(defn mount-components! []
  (om/add-root! reconciler ui/RootView (.getElementById js/document "app")))

(defn ^:export setup! []
  (mount-components!))

(defn on-js-reload []
  (mount-components!)
  (println "chatom.web.core reloaded by figwheel"))
