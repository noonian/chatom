(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.logging :as log]
            [clojure.repl :refer :all]
            [clojure.pprint :as pprint :refer [pprint]]
            [chatom.system :as system]
            [chatom.component.config :as config]
            [chatom.db.sql.queries :as queries]
            [honeysql.core :as sql]
            [clojure.java.jdbc :as jdbc]
            [chatom.db.user :as users]
            [chatom.db.room :as room]))

(reloaded.repl/set-init! #(system/new-system (config/new-config :dev)))

(defn config [& kws]
  (apply config/get (:config system) kws))

(defn db []
  (get-in system [:db-pool :spec]))
