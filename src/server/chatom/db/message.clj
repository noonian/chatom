(ns chatom.db.message
  (:require [chatom.db.sql.queries :as queries]
            [chatom.db.sql :as util :refer [run-query execute-query!]]
            [chatom.db.user :as user])
  (:refer-clojure :exclude [list]))

(def field-mapping
  {:created-by :created_by
   :room-id :room_id})

(def inverse-mapping
  (into {}
    (for [[k v] field-mapping]
      [v k])))

(defn create! [db data]
  (let [data (util/map-data field-mapping data)]
    (execute-query! db (queries/create-message data))))

(defn list [db room-id]
  (map (partial util/map-data inverse-mapping)
       (run-query db (queries/list-messages room-id))))
