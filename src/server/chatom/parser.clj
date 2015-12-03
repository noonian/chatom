(ns chatom.parser
  (:require [om.next.server :as om])
  (:refer-clojure :exclude [read]))

(defn dispatch [_ k _] k)

(defmulti read dispatch)

(defmulti mutate dispatch)

(defmethod read :default
  [{:keys [db query]} key params]
  (println "default read called")
  {:value "not-found"})

(defmethod mutate :default
  [{:keys [db query]} key params]
  (println "default mutate called")
  {:action #(do
              "foo!")})

(def parser
  (om/parser {:read read :mutate mutate}))
