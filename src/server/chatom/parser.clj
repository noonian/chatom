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
  {:value :not-found})

(defmethod read :app/rooms
  [{:keys [db query]} key params]
  (let [rooms (into [] (room/list db))]
    {:value rooms}))

(defmethod read :room/messages
  [env key params]
  (println ":room/by-id read called")
  {:value :not-found})

(defmethod read :app/pages
  [{:keys [parser query ast] :as env} key params]
  (let [[page-id page-query] (first query)
        data (parser env page-query)
        res (assoc data :id page-id)]
    {:value [res]}))

(defmethod mutate :default
  [{:keys [db query]} key params]
  (println "default mutate called")
  {:action #(do
              "foo!")})

(def parser
  (om/parser {:read read :mutate mutate}))
