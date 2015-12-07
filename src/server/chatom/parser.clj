(ns chatom.parser
  (:require [om.next.server :as om]
            [chatom.db.room :as room]
            [clojure.pprint :refer [pprint]])
  (:refer-clojure :exclude [read]))

(defn dispatch [_ k _] k)

(defmulti read dispatch)

(defmulti mutate dispatch)

(defmethod read :default
  [{:keys [db query] :as env} key params]
  (println "default read called")
  #_(pprint env)
  ;; not found
  {:value :not-found})

(defmethod read :app/rooms
  [{:keys [db query]} key params]
  (println ":app/rooms read called")
  (let [rooms (into [] (room/list db))]
    #_(pprint rooms)
    {:value rooms}))

(defmethod read :app/pages
  [{:keys [parser query ast] :as env} key params]
  (let [[page-id page-query] (first query)
        data (parser env page-query)
        res (assoc data :id page-id)]
    (println "foo")
    (pprint page-query)
    (pprint (parser env page-query))
    {:value [res]}))

(defmethod mutate :default
  [{:keys [db query]} key params]
  (println "default mutate called")
  {:action #(do
              "foo!")})

(def parser
  (om/parser {:read read :mutate mutate}))
