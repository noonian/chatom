(ns chatom.db.lein
  (:require [chatom.db.util :as util]
            [chatom.system :as system]
            [chatom.component.config :as config]
            [com.stuartsierra.component :as component]
            [clojure.pprint :refer [pprint]]))

(defmacro defcmd [sym args & body]
  `(defn ~sym ~args
     (do
       ~@body
       (System/exit 0))))

(defcmd migrate! [env]
  (util/with-db [db env]
    (util/migrate! db)))

(defcmd rollback! [env]
  (util/with-db [db env]
    (util/rollback! db)))
