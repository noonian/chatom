(ns chatom.db.sql
  (:require [chatom.db.sql.queries :as queries]
            [honeysql.core :as sql]
            [buddy.hashers :as hash]
            [clojure.java.jdbc :as jdbc]))

(defn execute-query! [db query & opts]
  (apply jdbc/execute! db (sql/format query) opts))

(defn run-query [db query & opts]
  (apply jdbc/query db (sql/format query) opts))
