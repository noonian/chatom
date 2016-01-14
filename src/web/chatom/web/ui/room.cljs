(ns chatom.web.ui.room
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [cljs.pprint :refer [pprint]]))

(defui Room
  static om/Ident
  (ident [this props]
    [:room/by-id (:id props)])
  static om/IQuery
  (query [this]
    [:id :roomname :messages])
  Object
  (render [this]
    (let [{:keys [roomname messages] :as props} (om/props this)]
      (html
       [:article.room
        [:header [:h1 roomname]]
        [:section.messages
         [:ul
          (for [msg messages]
            [:li {:key (:id msg)} "a message"])]]]))))

(def room (om/factory Room))

(defui RoomPage
  static om/IQuery
  (query [this]
    [{:app/current-room (om/get-query Room)}])
  Object
  (render [this]
    (let [{:keys [:app/current-room]} (om/props this)]
      (html
       [:div.page#room-page
        "this is the room page"
        #_(room current-room)
        #_(when-not (empty? rooms)
          (room (first rooms)))]))))
