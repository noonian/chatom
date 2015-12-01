(ns chatom.web.routes
  (:require [bidi.bidi :as bidi]))

(def routes
  ["/"
   {"" :page/index
    "about" :page/about
    "my-rooms" :page/rooms
    "account" :page/account}])

(def match-route (partial bidi/match-route routes))
(def path-for (partial bidi/path-for routes))
