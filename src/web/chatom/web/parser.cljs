(ns chatom.web.parser
  (:require [om.next :as om]
            [cljs.pprint :refer [pprint]]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state db query ast]} key params]
  (let [st (or db @state)]
    (if-let [[_ v] (find st key)]
      (let [val (if query (om/db->tree query v st) v)]
        {:value val})
      {:remote ast})))

(defmethod read :app/pages
  [{:keys [parser state query]} key params]
  (let [st @state
        current-page-ref (get-in st [:app/routing :data :app/current-page])
        current-page (get-in st current-page-ref)
        page-query (get query (:id current-page))
        env {:state state :db current-page}
        value (parser env page-query)
        remote-query (parser env page-query :remote)]
    {:value [value]
     :remote (om/query->ast remote-query)}))

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
