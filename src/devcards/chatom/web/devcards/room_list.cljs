(ns chatom.web.devcards.room-list
  (:require-macros [devcards.core :refer [defcard defcard-doc]])
  (:require [devcards.core :as dc]
            [sablono.core :as sab :refer-macros [html]]
            [om.next :as om]
            [chatom.web.ui.room-list :as room-list]))

(def sample-rooms
  [{:id 0 :name "Om Next"}
   {:id 1 :name "Falcor"}
   {:id 2 :name "Relay"}
   {:id 3 :name "Demand Driven Architecture"}])

(defcard "This is the sample data we will be working with:"
  sample-rooms)

(defcard "This is what a single `Room` looks like:"
  (room-list/room (first sample-rooms)))

(defcard "And this is the query for a `Room`:"
  (om/get-query room-list/Room))

(defcard "This is what a `RoomList` looks like:"
  (room-list/room-list {:rooms sample-rooms}))
