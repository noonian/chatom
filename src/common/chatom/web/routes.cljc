(ns chatom.web.routes
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/"
   {"" :page/home
    "about" :page/about}])

(def match-route (partial bidi/match-route routes))
(def path-for (partial bidi/path-for routes))
