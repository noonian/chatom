(ns chatom.web.state)

(def initial-page [:page/home :data])

(def init-data
  {:app/navbar {:data {:nav/current-page initial-page}}
   :app/pages [{:id :page/login}
               {:id :page/home}
               {:id :page/about}
               {:id :page/account}]})
