(ns mycotrack-frame.pages.species-detail
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page]]
            [re-frame.core :as re-frame]))

(defn species-detail-comp []
  (let [selected-species (re-frame/subscribe [:selected-species])]
    (fn [] [:div.col-xs-12.pad-top
            [:p (get @selected-species "commonName")]
            [:p (get @selected-species "scientificName")]
            [:img {:src (get @selected-species "imageUrl") :title (get @selected-species "imageAttribution")}]])))

(defn species-detail-title []
  [re-com/title
   :label "This is the Species Detail Page."
   :level :level1])

(defn species-detail-panel []
  [re-com/v-box
   :gap "1em"
   :children [[species-detail-title] [link-to-home-page] [species-detail-comp]]])
