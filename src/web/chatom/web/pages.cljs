(ns chatom.web.pages
  (:require [om.next :as om]
            [chatom.web.routes :as routes]))

(defn map-vals [f m]
  (into {} (for [[k v] m] [k (f v)])))

;; approach inspired by https://github.com/jdubie/om-next-router-example

(def page-id->component
  {:page/home nil
   :page/about nil
   :page/rooms nil
   :page/account nil})

(def page-id->query
  (map-vals om/get-query page-id->component))

(def page-id->factory
  (map-vals om/factory page-id->component))

(def page-id->route
  (reduce-kv
   (fn [acc page-id _]
     (assoc acc page-id (routes/path-for page-id)))
   {} page-id->component))
