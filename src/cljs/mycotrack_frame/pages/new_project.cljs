(ns mycotrack-frame.pages.new-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text dropdown]]))

(defn save-project-button [desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]
  [re-com/button
   :label            "New Batch"
   :tooltip          "Create a new batch"
   :tooltip-position :below-center
   :on-click          (fn [] (re-frame/dispatch
                              [:save-new-project
                               {
                                :description @desc,
                                :count @count,
                                :cultureId @selected-culture-id,
                                :container @selected-container-id
                                :enabled true
                                :substrate @selected-substrate-id
                                :locationId @selected-location-id}]))
   :class             "btn-primary"])

(defn new-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom "")
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)]
    (fn []  [:div [description-input-text desc "Description"]
             [number-input-text count]
             [dropdown :ui-cultures selected-culture-id #((fn [] (reset! selected-culture-id %)))]
             [dropdown :ui-containers selected-container-id #((fn [] (reset! selected-container-id %)))]
             [dropdown :ui-substrate selected-substrate-id #((fn [] (reset! selected-substrate-id %)))]
             [dropdown :ui-locations selected-location-id #((fn [] (reset! selected-location-id %)))]
             [save-project-button desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]])))

(defn new-project-title []
  [re-com/title
   :label "New Batch."
   :level :level1])

(defn new-project-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-project-title] [new-project-form]]])
