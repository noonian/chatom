(ns chatom.web.state)

(def init-data
  {:app/current-page [:page/home :data]
   :app/pages [{:id :page/login}
               {:id :page/home}
               {:id :page/about}
               {:id :page/account}]})
