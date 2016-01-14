(ns chatom.web.state
  (:require [chatom.web.pages :as pages]))

(def init-data
  {:app/current-page :page/home
   :route/args nil})

(def fake-remote-data
  {:user/rooms nil
   :app/rooms [{:id 0 :roomname "Om Next"}
               {:id 1 :roomname "Falcor"}
               {:id 2 :roomname "Relay"}
               {:id 3 :roomname "Demand Driven Architecture"}]})

(def mock-data (merge init-data fake-remote-data))
