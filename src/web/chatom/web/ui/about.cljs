(ns chatom.web.ui.about
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]))

(defui AboutPage
  static om/Ident
  (ident [this props]
    [:page/about :data])
  static om/IQuery
  (query [this]
    [])
  Object
  (render [this]
    (let [props (om/props this)]
      (html
       [:div.page#home-page
        "The about page"]))))
