(ns chatom.web.ui.room-list
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]))

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
      (html [:div.room name]))))

(def room (om/factory Room))

(defui RoomList
  Object
  (render [this]
    (let [{:keys [rooms]} (om/props this)]
      (html
       [:ul
        (for [r rooms]
          [:li {:key (:id r)}
           (room r)])]))))

(def room-list (om/factory RoomList))
