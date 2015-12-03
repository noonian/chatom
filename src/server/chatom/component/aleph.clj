(ns chatom.component.aleph
  (:require [com.stuartsierra.component :as component]
            [aleph.http :as http]
            [ring.util.response :as response]
            [clojure.tools.logging :as log]))

(def not-found (response/not-found "Not found"))

(defrecord AlephServer [aleph-connection aleph-config]
  component/Lifecycle
  (start [this]
    (if aleph-connection
      this
      (let [{:keys [handler-fn port]} aleph-config
            handler (if handler-fn
                      (handler-fn this)
                      (constantly not-found))]
        (log/info (str "aleph server starting on port " port))
        (assoc this :aleph-connection (http/start-server handler {:port port})))))
  (stop [this]
    (when aleph-connection
      (log/info "shutting down aleph server...")
      (.close aleph-connection)
      (log/info "aleph server shutdown."))
    (assoc this :aleph-connection nil)))

(defn new-server
  "Required config options:
     :handler-fn -- A function that will be called with the AlephServer
                    component as its argument when the system is started.
                    The function should return a ring handler. Use this to
                    pass component dependencies to your ring handlers.
     :port -- The port the AlephServer will listen to."
  [config]
  (map->AlephServer {:aleph-config config}))
