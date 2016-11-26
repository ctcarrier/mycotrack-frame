(ns mycotrack-frame.pages.spawn-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text dropdown date-input-text iso-formatter date-formatter]]
            [cljs-time.core :as time :refer [date-time now]]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse
                     formatter-local
                     parse-local parse-local-date
                     unparse-local unparse-local-date
                     with-default-year]]))

(defn spawn-project-button [desc count spawn-count selected-container-id selected-substrate-id selected-location-id selected-project-id created-date]
  [:button.btn.btn-primary.col-xs-12.input-lg
   {:label "Save" :type "submit" :tooltip "Spawn"
    :on-click (fn [e]
                (js/console.log (str "Selected: " selected-project-id))
                (.preventDefault e)
                (re-frame/dispatch
                 [:spawn-project
                  @selected-project-id
                  {
                   :description @desc
                   :count @count
                   :countSubstrateUsed @spawn-count
                   :container @selected-container-id
                   :enabled true
                   :substrate @selected-substrate-id
                   :locationId @selected-location-id
                   :createdDate (unparse iso-formatter (parse date-formatter @created-date))}]))} "Save"])

(defn spawn-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom nil)
        spawn-count (reagent/atom nil)
        selected-project-id (re-frame/subscribe [:selected-project-id])
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)
        created-date (reagent/atom (unparse date-formatter (now)))]
    (fn []  [:form.col-md-4.col-xs-12.col-md-offset-4
             [:div.col-xs-12.pad-bottom-xs.text-center [:h2 "Spawn To"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text desc "Description"]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text spawn-count "Spawn Used"]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text count "Count"]]

             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-containers selected-container-id "Container" #((fn [] (reset! selected-container-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-substrate selected-substrate-id "Sustrate" #((fn [] (reset! selected-substrate-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-locations selected-location-id "Location" #((fn [] (reset! selected-location-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text created-date "Date"]]
             [:div.col-xs-12 [spawn-project-button desc count spawn-count selected-container-id selected-substrate-id selected-location-id selected-project-id created-date]]])))

(defn spawn-project-panel []
  [:div.col-xs-12 [spawn-project-form]])
