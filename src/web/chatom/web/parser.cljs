(ns chatom.web.parser
  (:require [om.next :as om]
            [chatom.web.pages :as pages]
            [cljs.pprint :refer [pprint]]))

(defn denormalize [query value state]
  (if-not query
    value
    (if (and (vector? value)
             (not (empty? value)))
      (mapv #(om/db->tree query % state) value)
      (om/db->tree query value state))))

(defmulti read om/dispatch)

(defn query-from-root [{:keys [parser state query ast] :as env}]
  (let [env' {:state state}
        local-val (parser env' query)
        remote-query (parser env' query :remote)]
    (cond-> {:value local-val}
      (seq remote-query) (assoc :remote (assoc ast :query remote-query)))))

(defn routing-read [{:keys [parser state query ast] :as env} key params]
  (let [st @state
        current-page (:app/current-page st)]
    (println current-page)
    (cond

      (= key current-page) (query-from-root env)

      (contains? pages/page-id->component key) {}

      :else
      (read env key params))))

(defmethod read :default
  [{:keys [state db query ast]} key params]
  (let [st (or db @state)]
    (if-let [[_ v] (find st key)]
      (let [val (denormalize query v @state)]
        {:value val})
      (do
        {:remote (assoc ast :query-root true)}))))

(defmethod read :navbar
  [env _ _]
  (query-from-root env))

(def current-page-path [:app/routing :data :app/current-page])

(defmethod read :app/current-room
  [{:keys [parser state query ast] :as env} key params]
  (let [st @state
        link (:app/current-room st)
        env {:state state}
        local (when link (om/db->tree query (get-in st link) st))
        remote (when link (first (:children (om/query->ast [{link query}]))))]
    (cond-> {:value local}
      remote (assoc :remote remote))))

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
                    (assoc state
                           :app/current-page page-id
                           :route/args args))))}))

(defonce parser
  (om/parser {:read routing-read :mutate mutate}))
