(ns mycotrack-frame.css
  (:require [garden.def :refer [defstyles]]
            [garden.selectors :refer [> last-child]]
            [garden.stylesheet :refer [at-media]]
            [garden.units :refer [px em]]))

(defstyles screen
  [:body {:color "black"}]
  [:.level1 {:color "black"}]
  [:.pad-bottom {:padding-bottom (px 200)}]
  [:.pad-bottom-sm {:padding-bottom (px 50)}]
  [:.pad-bottom-xs {:padding-bottom (px 15)}]
  [:.background-light {:background-color "#f2f2f2"}]
  [:.border-light {:border-bottom "1px solid grey"}]
  [:.pad-top {:padding-top (px 20)}]
  [:.pad-top-xs {:padding-top (px 15)}]
  [:.right {:float "right"}]
  [:.max200 {"max-width" (px 200)}]
  [:.contaminated {:color "red"}]
  [:.bold-panel
   {
    :font-size (em 1.5)}
   [:h3
    {
     :text-align "center"
     :background-color "#7c3400"
     :color "white"}]]
  [:.table-hover
   [:tbody
    [(> :tr:hover :td) {:cursor "pointer"}]]]
  [:ul.topnav
   {
    :list-style-type "none"
    :margin-left (px 15)
    :margin-right (px 15)
    :padding 0
    :overflow "hidden"
    :background-color "#333"}
   [:li:last-child
    {:float "right"}]
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
      :font-size (em 1.25)}]
    [:a:hover {:background-color "#111"}]
    [:a.brand
     {
      :font-family "\"Palatino Linotype\", \"Book Antiqua\", Palatino, serif"
      :font-size (em 1.75)}]]
   [:li.icon {:display "none"}]]
  [:select:invalid {:color "#999"}]
  (at-media {:max-width (px 978)}
            [:.container {:padding 0 :margin 0}]
            [:.body {:padding 0}])
  [:.horizon {:border-bottom "solid 1px #000" :overflow "hidden" :position "relative"}
   [:canvas {:display "block"}]
   [:.title :.value {:bottom 0 :line-height (px 30) :margin "0 6px" :position "absolute" :text-shadow "0 1px 0 rgba(255,255,255,.5)" :white-space "nowrap"}]
   [:.title {:left 0}]
   [:.value {:right 0}]
   [:.line {:background "#000" :z-index 2}]]
  [:.rule {:z-index 3}]
  [:.axis {:font "10px sans-serif" :pointer-events "none" :z-index 2}
   [:text {:-webkit-transition "fill-opacity 20ms linear"}]
   [:path {:display "none"}]
   [:line {:stroke "000" :shape-rendering "crispEdges"}]
   ]
  [:.line {:background "#000" :opacity ".2" :z-index 2}])
