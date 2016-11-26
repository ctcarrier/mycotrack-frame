(ns mycotrack-frame.pages.contam-project
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

(defn contam-project-button [desc count selected-project created-date]
  [:button.btn.btn-primary.col-xs-12.input-lg
   {:label "Save" :type "submit" :tooltip "D'oh!"
    :on-click (fn [e]
                (js/console.log (str "Selected: " (:_id @selected-project)))
                (js/console.log @created-date)
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
                   :locationId (-> @selected-project :location :_id)
                   :contaminated true
                   :createdDate (unparse iso-formatter (parse date-formatter @created-date))}]))} "Save"])

(defn contam-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom nil)
        selected-project (re-frame/subscribe [:selected-project])
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)
        created-date (reagent/atom (unparse date-formatter (now)))]
    (fn []  [:form.col-md-4.col-xs-12.col-md-offset-4
             [:div.col-xs-12.pad-bottom-xs.text-center [:h2 "Record Contamination"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text desc "Description"]]
             [:div.col-xs-12.pad-bottom-xs [number-input-text count "Count"]]
             [:div.col-xs-12.pad-bottom-xs [description-input-text created-date "Date"]]
             [:div.col-xs-12 [contam-project-button desc count selected-project created-date]]])))

(defn contam-project-panel []
  [:div.col-xs-12 [contam-project-form]])
