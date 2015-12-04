(ns chatom.web.ui.core
  (:require [om.next :as om :refer-macros [defui]]
            [sablono.core :as sab :refer-macros [html]]
            [chatom.web.ui.navbar :as navbar]
            [chatom.web.pages :as pages]
            [chatom.web.routes :as routes]
            [pushy.core :as pushy]))

(defui Page
  static om/Ident
  (ident [this props]
    ;; page-id must be first element since it is used in the join query
    [(:id props) :data])
  static om/IQuery
  (query [this]
    pages/page-id->query))

(defui RootView
  static om/IQuery
  (query [this]
    [{:app/navbar [{:data (om/get-query navbar/Navbar)}]}
     {:app/pages (om/get-query Page)}])
  Object
  (render [this]
    (let [{:keys [:app/navbar]} (om/props this)]
      (html
       [:div
        (navbar/navbar (:data navbar))
        [:h1 "Welcome to ChatOm"]
        [:a {:href "javascript:void(0)"
             :on-click #(om/transact! this '[(remote/test!)])}
         "Test Om.next remote"]]))))
