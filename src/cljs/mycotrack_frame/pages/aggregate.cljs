(ns mycotrack-frame.pages.aggregate
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page link-to-spawn-page link-to-move-page link-to-contam-page]]
            [mycotrack-frame.uicomps :refer [iso-formatter date-formatter day-month-formatter loading-comp]]
            [re-frame.core :as re-frame]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse]]))

(defn aggregate-comp []
  (let [aggregate (re-frame/subscribe [:aggregate])]
    (fn []
      (js/console.log (clj->js @aggregate))
      (if (nil? @aggregate)
        [:div.col-xs-offset-5.col-xs-1 (loading-comp)]
        [:div.col-xs-12
         [:div.col-xs-12
          [:h3.pad-bottom-xs.pad-top-xs "Totals"]
          [:div.col-xs-6 "Past week"]
          [:div.col-xs-6 (:pastWeek @aggregate)]]
         [:div.col-xs-12.pad-bottom-sm.border-light
          [:div.col-xs-6 "Past month"]
          [:div.col-xs-6 (:pastMonth @aggregate)]]
         [:div.col-xs-12
          [:h3.pad-bottom-xs.pad-top-xs "Bags per week"]
          (for [week (:weeklyCount @aggregate)]
            [:div.col-xs-12
             [:div.col-xs-6 (unparse day-month-formatter (parse iso-formatter (:timestamp week)))]
             [:div.col-xs-6 (:count week)]])]]))))

(defn aggregate-title []
  [:h1 "This is the Aggregation Page."])

(defn aggregate-panel []
  [:div.col-xs-12
   [:div.row
    [:div.col-xs-12.bold-panel
     [aggregate-comp]]]])
