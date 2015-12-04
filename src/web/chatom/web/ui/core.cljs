(ns chatom.web.ui.core
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.ui.navbar :as navbar]
            [chatom.web.pages :as pages]
            [chatom.web.routes :as routes]
            [pushy.core :as pushy]
            [cljs.pprint :refer [pprint]]))

(defui Page
  static om/Ident
  (ident [this props]
    ;; page-id must be first element since it is used in the join query
    [(:id props) :data])
  static om/IQuery
  (query [this]
    pages/page-id->query))

(defn nav-handler [c]
  (fn [match]
    (println "nav event:")
    (pprint match)
    (om/transact! c `[(app/set-page! ~match)])))

(defui RootView
  static om/IQuery
  (query [this]
    [{:app/routing [(om/get-query navbar/Navbar)]}
     {:app/pages (om/get-query Page)}])
  Object
  (initLocalState [this]
    {:html5-history (pushy/pushy (nav-handler this) routes/match-route)})
  (componentWillMount [this]
    (pushy/start! (om/get-state this :html5-history)))
  (componentWillUnmount [this]
    (pushy/stop! (om/get-state this :html5-history)))
  (render [this]
    (let [{:keys [:app/navbar]} (om/props this)]
      (html
       [:div
        (navbar/navbar navbar)
        [:h1 "Welcome to ChatOm"]
        [:a {:href "javascript:void(0)"
             :on-click #(om/transact! this '[(remote/test!)])}
         "Test Om.next remote"]]))))
