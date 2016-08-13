(ns mycotrack-frame.pages.new-culture
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [description-input-text dropdown]]))

(defn save-culture-button [name selected-species-id]
  [re-com/button
   :label            "Save"
   :tooltip          "Save new culture"
   :tooltip-position :below-center
   :on-click          (fn [] (re-frame/dispatch
                              [:save-new-culture
                               {
                                :name @name,
                                :speciesId @selected-species-id}]))
   :class             "btn-primary"])

(defn new-culture-form []
  (let [name (reagent/atom "")
        selected-species-id (reagent/atom nil)]
    (fn []  [:div.col-md-3.col-xs-12
             [description-input-text name "Name"]
             [dropdown :ui-species selected-species-id #((fn [] (reset! selected-species-id %)))]
             [save-culture-button name selected-species-id]])))

(defn new-culture-title []
  [re-com/title
   :label "New Culture."
   :level :level1])

(defn new-culture-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-culture-title] [new-culture-form]]])
