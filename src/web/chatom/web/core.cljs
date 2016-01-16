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
  (atom state/init-data #_state/mock-data))

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

(def base-query
  "This is the query used to normalize the client database. Because
   it is desirable that routing have no impact on the database
   format, we can't use the automatic normalization facilities
   provided by om out of the box."
  (into (om/get-query ui/RootView)
    (mapcat om/get-query (vals pages/page-id->component))))

(defn normalize [value]
  (if (and (vector? value)
           (not (empty? value)))
    (map #(om/tree->db base-query #_ui/RootView % true) value)
    (om/tree->db base-query #_ui/RootView value true)))

(defn send [remotes merge-results]
  (let [full-query (:remote remotes)
        {:keys [query rewrite] :as res} (om/process-roots full-query)]

    (post-remote query #(do
                          ;; (pprint %)
                          (pprint (rewrite (normalize %)))
                          (merge-results (normalize %))))))

(defonce reconciler
  (om/reconciler
   {:state app-state
    :parser parser/parser
    :normalize nil
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
