(ns chatom.component.http-kit
  (:require [com.stuartsierra.component :as component]
            [org.http-kit.server :as http]
            [ring.util.response :as response]
            [clojure.tools.logging :as log]))

(def not-found (response/not-found "Not found"))

(defrecord HttpKitServer [shutdown-server http-kit-config]
  component/Lifecycle
  (start [this]
    (if shutdown-server
      this
      (let [{:keys [handler-fn port]} http-kit-config
            handler (if handler-fn
                      (handler-fn this)
                      (constantly not-found))]
        (log/info (str "http-kit server starting on port " port))
        (assoc this :shutdown-server (http/run-server handler {:port port})))))
  (stop [this]
    (when shutdown-server
      (log/info "shutting down http-kit server.")
      (shutdown-server))
    (assoc this :shutdown-server nil)))

(defn new-server
  "Required config options:
     :handler-fn -- A function that will be called with the AlephServer
                    component as its argument when the system is started.
                    The function should return a ring handler. Use this to
                    pass component dependencies to your ring handlers.
     :port -- The port the HttpKitServer will listen to."
  [config]
  (map->HttpKitServer {:http-kit-config config}))
