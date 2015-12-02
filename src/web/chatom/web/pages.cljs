(ns chatom.web.pages
  (:require [om.next :as om]
            [chatom.web.routes :as routes]
            [chatom.web.ui.home :as home]
            [chatom.web.ui.about :as about]))

(defn map-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

;; approach inspired by https://github.com/jdubie/om-next-router-example

(def page-id->component
  {:page/home home/HomePage
   :page/about about/AboutPage})

(def page-id->query
  (map-vals om/get-query page-id->component))

(def page-id->factory
  (map-vals om/factory page-id->component))

(def page-id->route
  (into {}
    (for [page-id (keys page-id->component)]
      [page-id (routes/path-for page-id)])))
