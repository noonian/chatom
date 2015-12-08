(ns chatom.web.core
  (:import [goog.net XhrIo])
  (:require [om.next :as om]
            cljsjs.react.dom ;om won't pull this in unless you require om.dom
            [chatom.web.parser :as parser]
            [chatom.web.pages :as pages]
            [chatom.web.ui.home :as home]
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

(defn normalize [value]
  (if (and (vector? value)
           (not (empty? value)))
    (map #(om/tree->db ui/RootView % true) value)
    (om/tree->db ui/RootView value true)))

(defn send [remotes merge-results]
  ;; (println "reconciler send function called with remotes:")
  ;; (pprint remotes)
  #_(let [full-query (:remote remotes)
        {:keys [query rewrite] :as res} (om/process-roots full-query)]
    (post-remote query
                 (fn [response]
                   (let [restructured-response (rewrite response)]
                     (println "---------")
                     (pprint res)
                     (pprint full-query)
                     (pprint query)

                   ;;   (pprint response)
                     #_(pprint restructured-response)
                     (println "----------")))))

  (when-let [query (:remote remotes)]
    (post-remote query #(do
                          #_(println "remote res:")
                          #_(pprint %)
                          (merge-results %)))))

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
