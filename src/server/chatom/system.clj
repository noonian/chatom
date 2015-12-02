(ns chatom.system
  (:require [com.stuartsierra.component :as component]
            [chatom.component.aleph :as aleph]
            [chatom.component.immuconf :as immuconf]
            [duct.component.hikaricp :as hikaricp]))

(defn new-system
  ([] (new-system :dev))
  ([env]
   (let [config-resources (cond-> ["resources/config.edn"]
                            (= env :prod) (conj "resources/prod.edn"))
         config (immuconf/new-config config-resources)
         ;; we need some runtime config before actually starting the sytem
         started (component/start config)
         db-uri (immuconf/get started :database :uri)]
     (component/system-using
      (component/system-map
       :config config
       :db-pool (hikaricp/hikaricp {:uri db-uri})
       :http-server (aleph/new-server {:port 8080}))
      {:http-server [:config :db-pool]}))))
