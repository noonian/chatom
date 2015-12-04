(ns chatom.web.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defmethod read :default
  [env key params]
  {:value "not-found"})

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [{:keys [ast] :as env} key params]
  {:remote ast})

;; params will me a bidi match
(defmethod mutate 'app/set-page!
  [{:keys [state]} key params]
  (let [page-id (:handler params)
        args (:args params)]
    {:action
     #(swap! state
             (fn [state]
               (-> state
                   (assoc-in [:app/routing :route/args] args)
                   (assoc-in [:app/routing :app/current-page] [page-id :data]))))}))

(defonce parser
  (om/parser {:read read :mutate mutate}))
