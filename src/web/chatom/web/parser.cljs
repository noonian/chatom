(ns chatom.web.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [env key params]
  {:value "not-found"})

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [{:keys [ast] :as env} key params]
  {:remote ast})

(defonce parser
  (om/parser {:read read :mutate mutate}))
