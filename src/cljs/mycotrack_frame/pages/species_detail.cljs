(ns mycotrack-frame.pages.species-detail
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page]]
            [re-frame.core :as re-frame]))

(defn species-detail-comp []
  (let [selected-species (re-frame/subscribe [:selected-species])]
    (fn [] [:div.col-xs-12.pad-top
            [:div.col-md-6.col-xs-12
             [:h3 (get @selected-species "scientificName")]
             [:h4 (str "'" (get @selected-species "commonName") "'")]]
            [:div.col-md-6.col-xs-12
             [:img {:src (get @selected-species "imageUrl") :title (get @selected-species "imageAttribution")}]]])))

(defn species-detail-title []
  [re-com/title
   :label "This is the Species Detail Page."
   :level :level1])

(defn species-detail-panel []
  [re-com/v-box
   :gap "1em"
   :children [[species-detail-comp]]])
