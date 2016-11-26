(ns mycotrack-frame.pages.move-project
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

(defn move-project-button [desc count selected-location-id selected-project created-date]
  [:button.btn.btn-primary.col-xs-12.input-lg
   {:label "Save" :type "submit" :tooltip "Onward!"
    :on-click (fn [e]
                (js/console.log (str "Selected: " (:_id @selected-project)))
                (.preventDefault e)
                (re-frame/dispatch
                 [:spawn-project
                  (:_id @selected-project)
                  {
                   :description @desc
                   :countSubstrateUsed @count
                   :count @count
                   :container (-> @selected-project :container :_id)
                   :enabled true
                   :substrate (-> @selected-project :substrate :_id)
                   :locationId @selected-location-id
                   :contaminated false
                   :createdDate (unparse iso-formatter (parse date-formatter @created-date))}]))} "Save"])

(defn move-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom nil)
        selected-project (re-frame/subscribe [:selected-project])
        selected-location-id (reagent/atom nil)
        created-date (reagent/atom (unparse date-formatter (now)))]
    (fn []  [:form.col-md-4.col-xs-12.col-md-offset-4
             [:div.col-xs-12.pad-bottom-xs.text-center [:h2 "Record Contamination"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text desc "Description"]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-locations selected-location-id "Location" #((fn [] (reset! selected-location-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text count "Count"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text created-date "Date"]]
             [:div.col-xs-12 [move-project-button desc count selected-location-id selected-project created-date]]])))

(defn move-project-panel []
  [:div.col-xs-12 [move-project-form]])
