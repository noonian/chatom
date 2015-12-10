(ns chatom.web.pages
  (:require [om.next :as om]
            [chatom.web.routes :as routes]
            [chatom.web.ui.home :as home]
            [chatom.web.ui.about :as about]
            [chatom.web.ui.room :as room]))

(defn map-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

;; approach inspired by https://github.com/jdubie/om-next-router-example

(def page-id->component
  {:page/home home/HomePage
   :page/about about/AboutPage
   :page/room room/RoomPage})

(def page-id->query
  (map-vals om/get-query page-id->component))

(def page-id->factory
  (map-vals om/factory page-id->component))
