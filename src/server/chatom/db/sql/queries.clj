(ns chatom.db.sql.queries
  (:require [honeysql.core :as sql]
            [honeysql.helpers :refer [insert-into values]]))

(defn by-column [table column value]
  (sql/build :select :*
             :from :person
             :where [:= column value]))

(defn list-rooms []
  (sql/build :select :*
             :from :room))

(defn list-messages [room-id]
  (sql/build :select :*
             :from :message
             :where [:= :room_id room-id]))

(defn create-fn
  [table]
  (fn [data]
    (-> (insert-into table)
        (values [data]))))

(def create-user (create-fn :person))
(def create-room (create-fn :room))
(def create-message (create-fn :message))
