(ns mycotrack-frame.pages.project-detail
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page link-to-spawn-page link-to-move-page link-to-contam-page]]
            [mycotrack-frame.uicomps :refer [iso-formatter date-formatter]]
            [re-frame.core :as re-frame]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse]]))

(defn culture-link [culture]
  [:a {:href (str "#/cultures/" (:_id culture))} (:name culture)])

(defn species-link [species]
  [:a {:href (str "#/species/" (:_id species))} [:img.max200 {:src (:imageUrl species) :title (:imageAttribution species)}]])

(defn project-detail-comp []
  (let [selected-project (re-frame/subscribe [:selected-project])]
    (fn [] [:div.col-xs-12.pad-top
            [:div.col-md-6.col-xs-12
             [:div.col-xs-12
              [:h4.pad-bottom-xs (str (:count @selected-project) " " (-> @selected-project :container :name) " of ") (culture-link (:culture @selected-project)) (str " on " (-> @selected-project :substrate :name))]
              [:h4.pad-bottom-xs (str "Created " (unparse date-formatter (parse iso-formatter (:createdDate @selected-project))))]]
             [:div.col-xs-12.pad-bottom-xs (link-to-spawn-page (:_id @selected-project))]
             [:div.col-xs-12.pad-bottom-xs (link-to-contam-page (:_id @selected-project))]
             [:div.col-xs-12 (link-to-move-page (:_id @selected-project))]
             [:div.col-xs-12 [:h3.border-light "Notes"]
              [:p (:description @selected-project)]]]
            [:div.col-md-6.col-xs-12 (species-link (:species @selected-project))]])))

(defn project-detail-title []
  [:h1 "This is the Project Detail Page."])

(defn project-detail-panel []
  [re-com/v-box
   :gap "1em"
   :children [[project-detail-comp]]])
