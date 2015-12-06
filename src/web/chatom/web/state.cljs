(ns chatom.web.state)

(def init-data
  {:user/rooms [{:id 0 :name "Om Next"}
                {:id 1 :name "Falcor"}
                {:id 2 :name "Relay"}
                {:id 3 :name "Demand Driven Architecture"}]
   :app/routing {:data {:app/current-page {:id :page/home}
                        :route/args nil}}
   :app/pages [{:id :page/login}
               {:id :page/home}
               {:id :page/about}
               {:id :page/account}]})
