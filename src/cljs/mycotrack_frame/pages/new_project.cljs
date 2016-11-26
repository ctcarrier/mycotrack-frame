(ns mycotrack-frame.pages.new-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text dropdown iso-formatter date-formatter date-input-text]]
            [cljs-time.core :as time :refer [date-time now]]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse
                     formatter-local
                     parse-local parse-local-date
                     unparse-local unparse-local-date
                     with-default-year]]))

(defn save-project-button [desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id created-date]
  [:button.btn.btn-primary.col-xs-12.input-lg {:label "Save" :type "submit" :tooltip "Create a new batch"
                                               :on-click (fn [e]
                                                           (js/console.log "Here here")
                                                           (js/console.log @created-date)
                                                           (.preventDefault e)
                                                           (re-frame/dispatch
                                                            [:save-new-project
                                                             {
                                                              :description @desc,
                                                              :count @count,
                                                              :cultureId @selected-culture-id,
                                                              :container @selected-container-id
                                                              :enabled true
                                                              :substrate @selected-substrate-id
                                                              :locationId @selected-location-id
                                                              :createdDate (unparse iso-formatter (parse date-formatter @created-date))}]))} "Save"])

(defn new-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom 1)
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)
        created-date (reagent/atom (unparse date-formatter (now)))]
    (fn []  [:form.col-md-4.col-xs-12.col-md-offset-4
             [:div.col-xs-12.pad-bottom-xs.text-center [:h2 "New Batch"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text desc "Description"]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text count "Count"]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-cultures selected-culture-id "Culture" #((fn [](reset! selected-culture-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-containers selected-container-id "Container" #((fn [] (reset! selected-container-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-substrate selected-substrate-id "Sustrate" #((fn [] (reset! selected-substrate-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-locations selected-location-id "Location" #((fn [] (reset! selected-location-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text created-date "Date"]]
             [:div.col-xs-12 [save-project-button desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id created-date]]])))

(defn new-project-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-project-form]]])
