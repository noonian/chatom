(ns chatom.web.core
  (:require [om.next :as om]
            cljsjs.react.dom ;om won't pull this in unless you require om.dom
            [chatom.web.parser :as parser]
            [chatom.web.state :as state]
            [chatom.web.ui.core :as ui]
            [cljs.pprint :refer [pprint]]))

(enable-console-print!)

(defonce app-state
  (atom state/init-data))

(defn send [remotes merge-results]
  (println "reconciler send function called with remotes:")
  (pprint remotes))

(defonce reconciler
  (om/reconciler
   {:state app-state
    :parser parser/parser
    :normalize true
    :remotes [:remote]
    :send send}))

(defn mount-components! []
  (om/add-root! reconciler ui/RootView (.getElementById js/document "app")))

(defn ^:export setup! []
  (mount-components!))

(defn on-js-reload []
  (println "chatom.web.core reloaded by figwheel"))
