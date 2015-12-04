(ns chatom.web.ui.navbar
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.routes :as routes]
            [cljs.pprint :refer [pprint]]))

(defui Navbar
  static om/Ident
  (ident [this props]
    [:app/routing :data])
  static om/IQuery
  (query [this]
    [{:app/current-page [:id]}])
  Object
  (render [this]
    (let [{:keys [:app/current-page] :as props} (om/props this)
          nav-links [[:page/home "Home"]
                     [:page/about "About"]]]
      (html
       [:nav.navbar
        [:ul
         (for [[page-id text] nav-links
               :let [active? (= page-id (:id current-page))]]
           [:li {:key page-id
                 :class (when active? "active")}
            [:a {:href (routes/path-for page-id)} text]])]]))))

(def navbar (om/factory Navbar))
