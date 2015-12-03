(ns chatom.parser
  (:require [om.next.server :as om]
            [clojure.tools.logging :as log])
  (:refer-clojure :exclude [read]))

(defn dispatch [_ k _] k)

(defmulti read dispatch)

(defmulti mutate dispatch)

(defmethod read :default
  [{:keys [db query]} key params]
  (log/debug "default read called")
  {:value "not-found"})

(defmethod mutate :default
  [{:keys [db query]} key params]
  (log/debug "default mutate called")
  {})

(def parser
  (om/parser {:read read :mutate mutate}))
