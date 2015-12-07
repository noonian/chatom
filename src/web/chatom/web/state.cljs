(ns chatom.web.state
  (:require [chatom.web.pages :as pages]))

(def init-data
  {#_#_:user/rooms [{:id 0 :name "Om Next"}
                {:id 1 :name "Falcor"}
                {:id 2 :name "Relay"}
                {:id 3 :name "Demand Driven Architecture"}]
   :app/routing {:data {:app/current-page {:id :page/home}
                        :route/args nil}}
   :app/pages (mapv (fn [[page-id _]] {:id page-id}) pages/page-id->component)})
