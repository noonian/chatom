(ns chatom.web.state)

(def init-data
  {:app/routing {:data {:app/current-page {:id :page/home}
                        :route/args nil}}
   :app/pages [{:id :page/login}
               {:id :page/home}
               {:id :page/about}
               {:id :page/account}]})
