(ns mycotrack-frame.links
  (:require [re-com.core :as re-com]))

(defn link-to-species-list-page []
  [re-com/hyperlink-href
   :label "go to Species List Page"
   :href "#/species"])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "Mycotrack"
   :href "#/"
   :class "Brand"])

(defn link-to-new-project-page []
  [:div
   [:a.btn.btn-primary.hidden-xs {:href "#/new-batch"} "New Batch"]
   [:a.btn.btn-primary.visible-xs-block {:href "#/new-batch"} "New +"]])

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "About"
   :href "#/about"])
