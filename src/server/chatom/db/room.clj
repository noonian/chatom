(ns chatom.db.room
  (:require [chatom.db.sql.queries :as queries]
            [chatom.db.sql :refer [run-query execute-query!]]
            [chatom.db.user :as user])
  (:refer-clojure :exclude [list]))

(defn create! [db {:keys [roomname created-by]}]
  (execute-query! db
    (queries/create-room
     {:roomname roomname
      :created_by created-by})))

(defn list [db]
  (run-query db (queries/list-rooms)))
