(ns chatom.web.parser
  (:require [om.next :as om]
            [cljs.pprint :refer [pprint]]))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state query]} key params]
  (let [st @state]
    (if-let [[_ v] (find st key)]
      (let [val (if query (om/db->tree query v st) v)]
        {:value val})
      {:value "not-found"})))

(defmethod read :app/pages
  [{:keys [state query]} key params]
  (let [st @state
        current-page (get-in st [:app/routing :data :app/current-page])
        page-id (first current-page)
        page-query (get query page-id)
        val (om/db->tree page-query (get-in st current-page) st)]
    (if page-query
      {:value [(om/db->tree page-query (get-in st current-page) st)]}
      {:value [(get-in st current-page)]})))

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
