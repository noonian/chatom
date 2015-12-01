(ns chatom.web.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [env key params]
  {:value "not-found"})

(defmulti mutate om/dispatch)

(defonce parser
  (om/parser {:read read :mutate mutate}))
