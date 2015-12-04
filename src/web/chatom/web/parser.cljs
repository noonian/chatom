(ns chatom.web.parser
  (:require [om.next :as om]
            [cljs.pprint :refer [pprint]]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query]} key params]
  (let [st @state]
    (if-let [[_ v] (find st key)]
      {:value (om/db->tree query v st)}
      {:value "not-found"})))

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
                   (assoc-in [:app/routing :data :route/args] args)
                   (assoc-in [:app/routing :data :app/current-page] [page-id :data]))))}))

(defonce parser
  (om/parser {:read read :mutate mutate}))
