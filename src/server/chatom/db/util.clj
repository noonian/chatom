(ns chatom.db.util
  (:require [ragtime.jdbc :as jdbc]
            [ragtime.repl :as repl]
            [chatom.component.config :as config]
            [com.stuartsierra.component :as component]
            [chatom.system :as system]))

(defn ragtime-conf [db]
  {:datastore (jdbc/sql-database db)
   :migrations (jdbc/load-resources "migrations")})

(defn migrate! [db]
  (repl/migrate (ragtime-conf db)))

(defn rollback! [db]
  (repl/rollback (ragtime-conf db)))

(defmacro with-db
  "Starts database pool with settings for specified environment
   and binds the connection the supplied symbol.

   Usage:
      (with-db [db :dev]
        (do-something-with-db db))"
  [binding & body]
  (let [[sym env] binding]
    `(let [env# (keyword ~env)
           config# (config/new-config env#)
           system# (system/new-system config#)
           started# (component/start-system system# [:db-pool])
           ~sym (get-in started# [:db-pool :spec])
           res# (do ~@body)
           stopped# (component/stop-system started# [:db-pool])]
       res#)))

(defmacro with-migrated-db [binding & body]
  (let [db-sym (first binding)]
    `(with-db ~binding
       (migrate! ~db-sym)
       ~@body)))
