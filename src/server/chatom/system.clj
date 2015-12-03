(ns chatom.system
  (:require [com.stuartsierra.component :as component]
            [chatom.component.aleph :as aleph]
            [chatom.component.http-kit :as http-kit]
            [chatom.ring-handler :as ring-handler]
            [chatom.component.immuconf :as immuconf]
            [duct.component.hikaricp :as hikaricp]))

(defn new-config [env]
  (immuconf/new-config
   (cond-> ["resources/config.edn"]
     (= env :prod) (conj "resources/prod.edn"))))

(defn new-system
  ([] (new-system (new-config :dev)))
  ([config]
   (let [db-uri (immuconf/get config :database :uri)
         port (immuconf/get config :server :port)]
     (component/system-using
      (component/system-map
       :config config
       :db-pool (hikaricp/hikaricp {:uri db-uri})
       :http-server (http-kit/new-server {:handler-fn ring-handler/app :port port}))
      {:http-server [:config :db-pool]}))))
