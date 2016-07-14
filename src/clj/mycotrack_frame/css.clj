(ns mycotrack-frame.css
  (:require [garden.def :refer [defstyles]]
            [garden.selectors :refer [>]]))

(defstyles screen
  [:body {:color "black"}]
  [:.level1 {:color "black"}]
  [:.table-hover
   [:tbody
    [(> :tr:hover :td) {:cursor "pointer"}]]]
  [:ul.topnav
   {
    :list-style-type "none"
    :margin 0
    :padding 0
    :overflow "hidden"
    :background-color "#333"}
   [:li
    {:float "left"}
    [:a
     {
      :display "inline-block"
      :color "#f2f2f2"
      :text-align "center"
      :padding "14px 16px"
      :text-decoration "none"
      :transition "0.3s"
      :font-size "1.25em"}]
    [:a:hover {:background-color "#111"}]
    [:a.brand
     {
      :font-family "\"Palatino Linotype\", \"Book Antiqua\", Palatino, serif"
      :font-size "1.75em"}]]
   [:li.icon {:display "none"}]])
