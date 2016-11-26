(ns mycotrack-frame.pages.new-culture
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [description-input-text dropdown]]))

(defn save-culture-button [name selected-species-id]
  [:button.btn.btn-primary.col-xs-12.input-lg {:label "Save" :type "submit" :tooltip "Create a new culture"
                                               :on-click
                                               (fn [e]
                                                 (.preventDefault e)
                                                 (re-frame/dispatch
                                                  [:save-new-culture
                                                   {
                                                    :name @name,
                                                    :speciesId @selected-species-id}]))
                                               } "Save"])

(defn new-culture-form []
  (let [name (reagent/atom "")
        selected-species-id (reagent/atom nil)]
    (fn []  [:div.col-md-3.col-xs-12
             [description-input-text name "Name"]
             [dropdown :ui-species selected-species-id "Species:" #((fn [] (reset! selected-species-id (-> % .-target .-value))))]
             [save-culture-button name selected-species-id]])))

(defn new-culture-title []
  [:h2 "New Culture"])

(defn new-culture-panel []
  [:div.col-xs-12 [new-culture-title] [new-culture-form]])
