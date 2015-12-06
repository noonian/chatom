(ns chatom.web.ui.home
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.ui.room-list :as room-list]
            [cljs.pprint :refer [pprint]]))

(defui HomePage
  static om/Ident
  (ident [this props]
    [(:id props) :data])
  static om/IQuery
  (query [this]
    [:id ;every page must request its id or it won't get prop
     {:user/rooms (om/get-query room-list/Room)}
     {:app/rooms (om/get-query room-list/Room)}])
  Object
  (render [this]
    (let [props (om/props this)]
      #_(println "--------------")
      #_(pprint props)
      (html
       [:div.page#home-page
        "The home page"
        [:section
         [:header [:h1 "Joined rooms"]]
         (room-list/room-list {:rooms (:user/rooms props)})]]))))
