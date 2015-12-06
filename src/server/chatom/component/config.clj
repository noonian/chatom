(ns chatom.component.config
  (:require [chatom.component.immuconf :as immuconf])
  (:refer-clojure :exclude [get]))

(defn new-config [env]
  (immuconf/new-config
   (cond-> ["resources/config.edn"]
     (= env :prod) (conj "resources/test.edn")
     (= env :prod) (conj "resources/prod.edn"))))

(def get immuconf/get)
