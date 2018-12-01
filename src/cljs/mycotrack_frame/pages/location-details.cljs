(ns mycotrack-frame.pages.location-details
  (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.uicomps :refer [loading-comp dropdown]]
              [cljsjs.moment]))

(defn formatted-date [date-string]
  (js/console.log date-string)
  (.format (js/moment date-string) "MM/DD/YY"))

(defn handle-project-click [project]
  (.assign js/location (str "#/batches/" (:_id project))))

(defn first-setting-form [active-location]
  [:div.row
   [:div.col-12 [:h1 "Create your first setting"]]
   [:div.col-12
    [:div.form-group
     [:label {:for "lowInput"} "Low Threshold"]
     [:input.form-control {:type "number" :id "lowInput" :placeholder "Low Threshold"}]]
    [:div.form-group
     [:label {:for "highInput"} "High Threshold"]
     [:input.form-control {:type "number" :id "highInput" :placeholder "High Threshold"}]]]])

(defn list-rows [location-settings active-location]
  (if (nil? @location-settings)
    [:div.offset-md-5.col-1 (loading-comp)]
    [:div.col-12
     (if (empty? @location-settings)
       [first-setting-form active-location]
       (for [location-setting @location-settings]
         [:div.col-md-12.mb-2 {:key (:_id location-setting) :on-click #(handle-project-click location-setting) :on-tap #(handle-project-click location-setting)}
          [:div.pane.mx-auto.text-center.align-middle
           (-> location-setting :name)]]))]))

(defn location-list-comp [active-location]
  (let [location-settings (re-frame/subscribe [:location-settings])]
    (fn []
      (list-rows location-settings active-location))))

(defn location-details-panel []
  (let [active-location (re-frame/subscribe [:active-location])]
    (fn []
      [:div.col-12
       [:span (:name @active-location)]
       [:div.row
        [location-list-comp active-location]]])))
