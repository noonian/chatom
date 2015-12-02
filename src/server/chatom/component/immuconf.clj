(ns chatom.component.immuconf
  (:require [com.stuartsierra.component :as component]
            [immuconf.config :as immuconf])
  (:refer-clojure :exclude [get]))

(defrecord ImmuconfConfig [resources config]
  component/Lifecycle
  (start [this]
    (if config
      this
      (assoc this :config (apply immuconf/load resources))))
  (stop [this]
    (assoc this :config nil)))

(defn new-config [resources]
  (map->ImmuconfConfig {:resources resources}))

(defn get [component & kws]
  (apply immuconf/get (:config component) kws))
