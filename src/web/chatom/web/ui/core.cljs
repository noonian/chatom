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
    pages/page-id->query)
  Object
  (render [this]
    (let [{:keys [id] :as props} (om/props this)
          render-page (pages/page-id->factory id)]
      (pprint props)
      (if render-page
        (render-page props)
        (html [:div.page#not-found-page "Page not found"])))))

(def page (om/factory Page))

(defn nav-handler [c]
  (fn [match]
    #_(println "nav event:")
    #_(pprint match)
    (om/transact! c `[(app/set-page! ~match) :app/routing])))

(defui RootView
  static om/IQuery
  (query [this]
    [{:app/routing [{:data (om/get-query navbar/Navbar)}]}
     {:app/pages (om/get-query Page)}])
  Object
  (initLocalState [this]
    {:html5-history (pushy/pushy (nav-handler this) routes/match-route)})
  (componentWillMount [this]
    (pushy/start! (om/get-state this :html5-history)))
  (componentWillUnmount [this]
    (pushy/stop! (om/get-state this :html5-history)))
  (render [this]
    (let [{:keys [:app/routing :app/pages] :as props} (om/props this)
          page-id (get-in routing [:data :app/current-page :id])
          current-page (first (filter #(= page-id (:id %)) pages))]
      (pprint page-id)
      (pprint pages)
      (html
       [:div
        (navbar/navbar (:data routing))
        [:h1 "Welcome to ChatOm"]
        [:a {:href "javascript:void(0)"
             :on-click #(om/transact! this '[(remote/test!)])}
         "Test Om.next remote"]
        (page current-page)]))))
