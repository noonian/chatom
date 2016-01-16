(ns chatom.web.ui.core
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.ui.navbar :as navbar]
            [chatom.web.pages :as pages]
            [chatom.web.routes :as routes]
            [pushy.core :as pushy]
            [cljs.pprint :refer [pprint]]))

(defn nav-handler [c]
  (fn [match]
    (om/transact! c `[(app/set-page! ~match) ~(:handler match)])))

(defn page-joins
  ([page-queries]
   (for [[page-id query] page-queries
         :when (not (nil? query))]
     ;; each page is represented as a join
     {page-id query}))
  ([page-queries page-id]
   (page-joins {page-id (get page-queries page-id)})))

(defui RootView
  static om/IQuery
  (query [this]
    (into [:app/current-page
           {:navbar (om/get-query navbar/Navbar)}]
      (page-joins pages/page-id->query)))
  Object
  (initLocalState [this]
    (letfn [(on-route-change [match]
              (om/transact! this `[(app/set-page! ~{:handler (:handler match)
                                                    :route-params (:route-params match)
                                                    :component this} #_match)
                                   ~(:handler match)
                                   ~(om/get-query this)]))]
      {:html5-history (pushy/pushy on-route-change #_(nav-handler this) routes/match-route)}))
  (componentWillMount [this]
    (pushy/start! (om/get-state this :html5-history)))
  (componentWillUnmount [this]
    (pushy/stop! (om/get-state this :html5-history)))
  (render [this]
    (let [{:keys [:app/current-page navbar] :as props} (om/props this)
          render-page (pages/page-id->factory current-page)
          page-data (get props current-page)]
      (pprint props)
      (html
       [:div
        (navbar/navbar navbar)
        [:div.debug (pr-str props)]
        [:h1 "Welcome to ChatOm"]
        [:a {:href "javascript:void(0)"
             :on-click #(om/transact! this '[(remote/test!)])}
         "Test Om.next remote"]
        (render-page page-data)]))))
