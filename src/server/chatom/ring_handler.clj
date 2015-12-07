(ns chatom.ring-handler
  (:require [bidi.bidi :as bidi]
            [chatom.parser :as parser]
            [chatom.web.routes :as frontend-routes]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.util.response :as response]
            [cognitect.transit :as transit]
            [ring.middleware.format-params :refer [wrap-transit-json-params]]
            [ring.middleware.format-response :refer [wrap-transit-json-response]]
            [ring.middleware.content-type :refer [wrap-content-type]]
            [clojure.pprint :refer [pprint]]))

(defn print-request [req]
  (let [lhyphens "--------- "
        rhyphens " ---------"
        msg #(str lhyphens % rhyphens)]
    (println (msg "new ring request"))
    (pprint req)
    (when-let [body (:body req)]
      (pprint (str (slurp body))))
    (println (msg "end ring request"))))

(defn wrap-spy [handler]
  (fn [req]
    (print-request req)
    (handler req)))

(def routes
  ["/" {"api" :api}])

(def index-response
  (response/content-type
   (response/resource-response "index.html" {:root "public"})
   "text/html"))

(defn remote-api-response [{:keys [db-pool] :as deps} {:keys [body-params] :as req}]
  (println "remote-api-response called")
  (let [env {:db (:spec db-pool)}
        query (:body-params req)
        _ (pprint query)
        res (parser/parser env query)]
    ;; (println "remote-api result:")
    ;; (pprint query)
    ;; (pprint res)
    (response/response res)))

(defn handler [deps]
  (let [api-handler (-> #(remote-api-response deps %)
                        #_wrap-spy
                        wrap-transit-json-params
                        wrap-transit-json-response)]
    (fn [req]
      (if (frontend-routes/match-route (:uri req))
        index-response
        (let [match (bidi/match-route routes (:uri req))]
          (condp = (:handler match)
            :api (api-handler req)
            (response/content-type
             (response/not-found "Not found")
             "text/plain")))))))

(defn app [deps]
  (-> (handler deps)
      (wrap-resource "public")
      (wrap-content-type)))
