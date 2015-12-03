(ns chatom.db.user
  (:require [chatom.db.sql.queries :as queries]
            [chatom.db.sql :refer [run-query execute-query!]]
            [buddy.hashers :as hash]))

(defn create! [db {:keys [username password]}]
  (let [crypted (hash/encrypt password)]
    (execute-query! db
      (queries/create-user
       {:username username
        :crypted_password crypted}))))

(defn by-username [db username]
  (-> (run-query db
        (queries/by-column :user :username username)
        :result-set-fn first)
      (dissoc :crypted_password)))

(defn lookup [db username password]
  "Returns a user with given username iff the user exists and the users
   :crypted_password matches the provided password."
  [db {:keys [username password]}]
  (let [user (run-query db
               (queries/by-column :room :username username)
               :result-set-fn first)]
    (when (and user (hash/check password (:crypted_password user)))
      (dissoc user :crypted_password))))
