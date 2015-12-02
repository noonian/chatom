(ns chatom.system
  (:require [com.stuartsierra.component :as component]
            [chatom.component.aleph :as aleph]
            [chatom.component.immuconf :as immuconf]
            [duct.component.hikaricp :as hikaricp]))

(defn new-config [env]
  (immuconf/new-config
   (cond-> ["resources/config.edn"]
     (= env :prod) (conj "resources/prod.edn"))))

(defn new-system
  ([] (new-system :dev))
  ([env]
   (let [config (new-config env)
         db-uri (immuconf/get config :database :uri)]
     (component/system-using
      (component/system-map
       :config config
       :db-pool (hikaricp/hikaricp {:uri db-uri})
       :http-server (aleph/new-server {:port 8080}))
      {:http-server [:config :db-pool]}))))
