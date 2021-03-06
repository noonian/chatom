(ns chatom.db.room
  (:require [chatom.db.sql.queries :as queries]
            [chatom.db.sql :as util :refer [run-query execute-query!]]
            [chatom.db.user :as user])
  (:refer-clojure :exclude [list]))

(def field-mapping
  {:created-by :created_by})

(def inverse-mapping
  (into {}
    (for [[k v] field-mapping]
      [v k])))

(defn create! [db data]
  (execute-query! db (queries/create-room (util/map-data field-mapping data))))

(defn list [db]
  (map (partial util/map-data inverse-mapping)
       (run-query db (queries/list-rooms))))
