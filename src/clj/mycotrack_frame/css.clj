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
  [:div.pane
   {
    :height (px 200)
    :width (px 200)
    :background-color "grey"}
   [:&:hover
    {
     :background-color "blue"
     :cursor "pointer"}]]
  (at-media {:max-width (px 978)}
            [:.container {:padding 0 :margin 0}]
            [:.body {:padding 0}])
  )
