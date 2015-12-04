(ns chatom.web.ui.home
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.ui.room-list :as room-list]))

(defui HomePage
  ;; static om/Ident
  ;; (ident [this props]
  ;;   [(:id props) :data])
  static om/IQuery
  (query [this]
    [{:user/rooms (om/get-query room-list/Room)}
     {:app/rooms (om/get-query room-list/Room)}])
  Object
  (render [this]
    (let [props (om/props this)]
      (html
       [:div.page#home-page
        "The home page"]))))
