(ns chatom.component.immuconf
  (:require [immuconf.config :as immuconf])
  (:refer-clojure :exclude [get]))

(defrecord ImmuconfConfig [resources config])

(defn new-config [resources]
  (map->ImmuconfConfig
   {:resources resources
    :config (apply immuconf/load resources)}))

(defn get [component & kws]
  (apply immuconf/get (:config component) kws))
