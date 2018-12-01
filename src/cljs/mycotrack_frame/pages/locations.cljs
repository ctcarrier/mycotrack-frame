(ns mycotrack-frame.pages.locations
  (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.uicomps :refer [loading-comp dropdown]]
              [cljsjs.moment]))

(defn formatted-date [date-string]
  (js/console.log date-string)
  (.format (js/moment date-string) "MM/DD/YY"))

(defn handle-location-click [loc]
  (.assign js/location (str "#/locations/" (:_id loc))))

(defn location-list-rows [location-list]
  (if (nil? @location-list)
    [:div.offset-md-5.col-1 (loading-comp)]
    [:div.col-md-12
     [:div.row
      (for [location @location-list]
        [:div.col-md-3.col-12.mb-2 {:key (:_id location) :on-click #(handle-location-click location) :on-tap #(handle-location-click location)}
         [:div.pane.mx-auto.text-center.align-middle
          (-> location :name)]])]]))

(defn location-list-comp []
  (let [location-list (re-frame/subscribe [:locations])]
    (js/console.log @location-list)
    (fn []
      (location-list-rows location-list))))

(defn page-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label "Farm Locations"
       :level :level1])))

(defn location-panel []
  [:div.col-12
   [page-title]
   [:div.row
    [location-list-comp]]])
