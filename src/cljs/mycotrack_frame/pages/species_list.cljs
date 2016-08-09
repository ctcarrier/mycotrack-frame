(ns mycotrack-frame.pages.species-list
  (:require [re-frame.core :as re-frame]
            [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page]]))

(defn species-list-comp []
  (let [species-list (re-frame/subscribe [:species])]
    (fn [] [:div.col-xs-12.pad-top (for [species @species-list]
                                      [:a {:key (get species "_id") :href (str "#/species/" (get species "_id"))}
                                       [:div.image-tile.col-xs-5
                                        [:p (get species "commonName")]
                                        [:p (get species "scientificName")]
                                        [:div {:key (get species "_id")}
                                         [:img {:src (get species "imageUrl") :title (get species "imageAttribution")}]]]])])))

(defn species-list-title []
  [re-com/title
   :label "This is the Species Page2."
   :level :level1])

(defn species-list-panel []
  [re-com/v-box
   :gap "1em"
   :children [[species-list-title] [link-to-home-page] [species-list-comp]]])
