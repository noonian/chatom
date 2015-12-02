(ns chatom.system
  (:require [com.stuartsierra.component :as component]
            [chatom.component.aleph :as aleph]))

(defn new-system []
  (component/system-map))
