(ns chatom.web.state)

(def initial-page [:page/home :data])

(def init-data
  {:app/routing {:app/current-page initial-page
                 :route/args nil}
   :app/pages [{:id :page/login}
               {:id :page/home}
               {:id :page/about}
               {:id :page/account}]})
