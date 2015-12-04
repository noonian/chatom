(ns chatom.web.ui.room-list
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.routes :as routes]))

(defui Room
  static om/Ident
  (ident [this props]
    [:room/by-id (:id props)])
  static om/IQuery
  (query [this]
    [:id :name])
  Object
  (render [this]
    (let [{:keys [id name]} (om/props this)]
      (html [:div.room
             [:a {:href (routes/path-for :page/room :id id)} name]]))))

(def room (om/factory Room))

(defui RoomList
  Object
  (render [this]
    (let [{:keys [rooms]} (om/props this)]
      (html
       [:ul.room-list
        (for [r rooms]
          [:li {:key (:id r)}
           (room r)])]))))

(def room-list (om/factory RoomList))
