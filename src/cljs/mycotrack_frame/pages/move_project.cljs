(ns mycotrack-frame.pages.move-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text dropdown]]))

(defn move-project-button [desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]
  [:button.btn.btn-primary.col-xs-12.input-lg {:label "Save" :type "submit" :tooltip "Create a new batch"
                                               :on-click (fn [e]
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
                                                              :locationId @selected-location-id}]))} "Save"])

(defn move-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom 1)
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)]
    (fn []  [:form.col-md-4.col-xs-12.col-md-offset-4
             [:div.col-xs-12.pad-bottom-xs.text-center [:h2 "New Batch"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text desc "Description"]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text count "Count"]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-cultures selected-culture-id "Culture" #((fn [](reset! selected-culture-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-containers selected-container-id "Container" #((fn [] (reset! selected-container-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-substrate selected-substrate-id "Sustrate" #((fn [] (reset! selected-substrate-id (-> % .-target .-value))))]]
             [:div.col-xs-12.pad-bottom-xs [dropdown :ui-locations selected-location-id "Location" #((fn [] (reset! selected-location-id (-> % .-target .-value))))]]
             [:div.col-xs-12 [move-project-button desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]]])))

(defn move-project-panel []
  [:div.col-xs-12 [move-project-form]])
