(ns chatom.db.joplin
  (:require [joplin.repl :as repl]
            [chatom.component.immuconf :as immuconf]
            [chatom.system :as system]))

;; -------------------------------------------------------------------
;; lein aliases

(defn joplin-config [env]
  (let [config (system/new-config env)
        db-uri (immuconf/get config :database :uri)]
    {:migrators {:sql-mig "resources/joplin/migrators/sql"}
     #_#_:seeds {:sql-seed "seeds.sql/run"}
     :databases {:sql-db {:type :sql :url db-uri}}
     :environments {:dev [{:db :sql-db
                           :migrator :sql-mig
                           #_#_:seed :sql-seed}]
                    :prod [{:db :sql-db
                            :migrator :sql-mig}]}}))

(defn migrate-db [env]
  (let [conf (joplin-config env)]
    (repl/migrate conf (keyword env) :sql-db))
  (System/exit 0))

(defn seed-db [env]
  (let [conf (joplin-config env)]
    (repl/seed conf (keyword env) :sql-db))
  (System/exit 0))

(defn rollback-db [env & [num]]
  (let [conf (joplin-config env)]
    (when num
      (repl/rollback conf (keyword env) :sql-db (Long/parseLong num))))
  (System/exit 0))

(defn reset-db [env]
  (let [conf (joplin-config env)]
    (repl/reset conf (keyword env) :sql-db))
  (System/exit 0))

(defn pending-migrations [env]
  (let [conf (joplin-config env)]
    (repl/pending conf (keyword env) :sql-db))
  (System/exit 0))

(defn create-migration [id]
  (let [conf (joplin-config :dev)]
    (when id
      (repl/create conf :dev :sql-db id)))
  (System/exit 0))
