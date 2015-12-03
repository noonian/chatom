(ns chatom.web.ui.core
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]))

(defui RootView
  static om/IQuery
  (query [this]
    ;; Om won't mount a component that doesn't request props
    [:foo])
  Object
  (render [this]
    (html
     [:div
      [:h1 "Welcome to ChatOm"]
      [:a {:href "javascript:void(0)"
           :on-click #(om/transact! this '[(remote/test!)])}
       "Test Om.next remote"]])))
