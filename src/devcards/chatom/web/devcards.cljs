(ns chatom.web.devcards
  (:require-macros [devcards.core :refer [defcard]])
  (:require [devcards.core :as dc]
            [sablono.core :as sab :refer-macros [html]]
            chatom.web.devcards.room-list))

(defcard
  "This namespace should require any other namespaces containing devcards")
