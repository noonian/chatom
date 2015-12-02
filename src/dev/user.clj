(ns user
  (:require [reloaded.repl :refer [system init start stop go reset]]
            [com.stuartsierra.component :as component]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.repl :refer :all]
            [clojure.pprint :as pprint :refer [pprint]]
            [chatom.system :as system]))

(reloaded.repl/set-init! system/new-system)
