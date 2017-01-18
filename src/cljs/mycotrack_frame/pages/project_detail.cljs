(ns mycotrack-frame.pages.project-detail
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page link-to-spawn-page link-to-move-page link-to-contam-page]]
            [mycotrack-frame.pages.retire-project :refer [retire-button reactivate-button]]
            [mycotrack-frame.uicomps :refer [iso-formatter date-formatter loading-comp]]
            [re-frame.core :as re-frame]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse]]))

(defn culture-link [culture]
  [:a {:href (str "#/cultures/" (:_id culture))} (:name culture)])

(defn species-link [species]
  [:a {:href (str "#/species/" (:_id species))} [:img.max200 {:src (:imageUrl species) :title (:imageAttribution species)}]])

(defn project-detail-comp []
  (let [selected-project (re-frame/subscribe [:selected-project])]
    (fn []
      (if (nil? @selected-project)
        [:div.col-xs-offset-5.col-xs-1 (loading-comp)]
        (let [project-id (:_id @selected-project)]
             [:div.col-xs-12.pad-top
              (js/console.log "Got selected")
              (js/console.log project-id)
              [:div.col-md-6.col-xs-12
               [:div.col-xs-12
                [:h4.pad-bottom-xs (str (:count @selected-project) " " (-> @selected-project :container :name) " of ") (culture-link (:culture @selected-project)) (str " on " (-> @selected-project :substrate :name))]
                [:h4.pad-bottom-xs (str "Created " (unparse date-formatter (parse iso-formatter (:createdDate @selected-project))))]]
               [:div.col-xs-12.pad-bottom-xs (link-to-spawn-page project-id)]
               [:div.col-xs-12.pad-bottom-xs (link-to-contam-page project-id)]
               [:div.col-xs-12.pad-bottom-xs (link-to-move-page project-id)]
               (if (true? (:enabled @selected-project))
                 [:div.col-xs-12.pad-bottom-xs (retire-button @selected-project)]
                 [:div.col-xs-12.pad-bottom-xs (reactivate-button @selected-project)])
               [:div.col-xs-12 [:h3.border-light "Notes"]
                [:p (:description @selected-project)]]]
              [:div.col-md-6.col-xs-12 (species-link (:species @selected-project))]])))))

(defn project-detail-title []
  [:h1 "This is the Project Detail Page."])

(defn project-detail-panel []
  [:div.col-xs-12
   [:div.row
    [:div.col-xs-12
     [project-detail-comp]]]])
