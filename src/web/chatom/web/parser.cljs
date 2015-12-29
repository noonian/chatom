(ns chatom.web.parser
  (:require [om.next :as om]
            [cljs.pprint :refer [pprint]]))

(defn denormalize [query value state]
  (if-not query
    value
    (if (and (vector? value)
             (not (empty? value)))
      (mapv #(om/db->tree query % state) value)
      (om/db->tree query value state))))

(defmulti read om/dispatch)

(defmethod read :default
  [{:keys [state db query ast]} key params]
  (let [st (or db @state)]
    (if-let [[_ v] (find st key)]
      (let [val (denormalize query v @state)]
        {:value val})
      (do
        {:remote #_ast (assoc ast :query-root true)}))))

(defmethod read :app/pages
  [{:keys [ast parser state query]} key params]
  (let [st @state
        current-page-ref (get-in st [:app/routing :data :app/current-page])
        current-page (get-in st current-page-ref)
        page-id (:id current-page)
        page-query (get query (:id current-page))
        env {:state state :db current-page}
        value (parser env page-query)
        remote-query (parser env page-query :remote)]
    (cond-> {:value [value]}
      (not (empty? remote-query))
      (assoc
       :remote (-> (update-in ast [:query]
                     #(into {}
                        (for [[k v] %
                              :when (= k page-id)]
                          [k (with-meta remote-query {:query-root true})])))
                   #_(update-in [:children]
                       (fn [children]
                         (mapv #(assoc % :query-root true) children))))))))

(defmethod read :app/current-room
  [{:keys [parser state query ast] :as env} key params]
  (let [st @state
        link (:app/current-room st)
        env {:state state}
        local (om/db->tree query (get-in st link) st)
        remote (-> (om/query->ast [{link query}])
                   :children
                   first
                   (assoc :query-root true))]
    ;; (println "---------------")
    ;; (pprint local)
    ;; (pprint remote)
    {:value local
     :remote remote}))

(defmulti mutate om/dispatch)

(defmethod mutate :default
  [{:keys [ast] :as env} key params]
  {:remote ast})

(defmulti navigated-to (fn [page-id state] page-id))

(defmethod navigated-to :default
  [page-id state]
  (println "default navigated-to for page:" page-id)
  state)

(defmethod navigated-to :page/room
  [_ state]
  (println "navigated to :page/room")
  (let [room-id (long (get-in state [:app/routing :data :route/args :id]))]
    (assoc state :app/current-room [:room/by-id room-id])))

;; params will me a bidi match
(defmethod mutate 'app/set-page!
  [{:keys [state]} key params]
  (let [page-id (:handler params)
        args (:route-params params)]
    {:action #(swap! state
                (fn [state]
                  (navigated-to page-id
                    (update-in state [:app/routing :data] assoc
                               :route/args args
                               :app/current-page [page-id :data])
                    (navigated-to page-id state))))}))

(defonce parser
  (om/parser {:read read :mutate mutate}))
