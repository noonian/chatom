(ns chatom.web.style
  (:require [garden.color :as color :refer [rgb]]
            [garden.units :refer [em px percent]]
            [garden.selectors :as sel]
            [normalize.core :refer [normalize]]
            [mesh.grid :as grid]
            [mesh.typography :as typo]
            [mesh.utils :as mesh-utils]))

(def color
  {:p0 "#2F4275"
   :p1 "#AAC0FF"
   :p2 "#5D72AE"
   :p3 "#111C3B"
   :p4 "#000000"

   :s0 "#393379"
   :s1 "#B2ACFF"
   :s2 "#6862B4"
   :s3 "#16133D"
   :s4 "#000000"

   :t0 "#AD933A"
   :t1 "#FFE99F"
   :t2 "#FFE079"
   :t3 "#584711"
   :t4 "#000000"

   :c0 "#AD883A"
   :c1 "#FFE09F"
   :c2 "#FFD479"
   :c3 "#584111"
   :c4 "#000000"})

(def white (rgb 253 253 253))

(defn make-nav [clazz]
  [clazz {:width (percent 100)
          :height (px 46)
          :background-color white
          :margin-bottom (px 20)}
   [:a :ul :li {:display "inline-block"
                :color (color :p0)}]
   [:ul [:a {:margin-right (px 20)}]]])

;; Base html
(def base
  [[:* {:box-sizing "border-box"}]
   [:html {:font-size (em 1)
           :line-height 1.5
           :height (percent 100)
           :text-align "left"}]
   [:body {:width (percent 100)
           :height (percent 100)
           :color (color :p0)}]
   [:a {:text-decoration "none"}]
   [:button :input {:outline "none"}]])

(def util-classes
  [[:.center {:margin [[0 "auto"]]}]
   [:.container {:margin [[0 "auto"]]
                 :height (percent 100)
                 :width (percent 80)}]
   [:.page {:height (percent 100)}]
   (make-nav :nav.navbar)])

(def chatom-style
  [normalize
   base
   util-classes])
