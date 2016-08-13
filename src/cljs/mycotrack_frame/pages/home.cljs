(ns mycotrack-frame.pages.home
  (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.uicomps :refer [loading-comp dropdown]]
              [cljsjs.moment]))

(defn formatted-date [date-string]
  (.format (js/moment date-string "YYYY MM DD") "MM/DD/YY"))

(defn handle-project-click [project]
  (.assign js/location (str "#/batches/" (:_id project))))

(defn project-list-rows [project-list]
  (if (nil? @project-list)
    [:div.col-xs-offset-5.col-xs-1 (loading-comp)]
    [:table.table.table-striped.table-hover
       [:thead
        [:tr.hidden-xs
         [:th {:key "1"} "Created"]
         [:th {:key "2"} "Culture"]
         [:th {:key "3"} "Location"]
         [:th {:key "4"} "Count"]
         ]]
       [:tbody
        (for [project @project-list]
          [:tr {:key (:_id project) :on-click #(handle-project-click project) :on-touch-start #(handle-project-click project)}
           [:td {:key (str (:_id project) "_cd")} (formatted-date (:createdDate project))]
           [:td {:key (str (:_id project) "_cn")} (-> project :culture :name)]
           [:td {:key (str (:_id project) "_ln")} (-> project :location :name)]
           [:td.hidden-xs {:key (str (:_id project) "ct")} (:count project)]])]]))

(defn culture-filter-comp [switch]
  (let [culture-list (re-frame/subscribe [:ui-cultures])]
    (fn []
      [:div.col-xs-12
       (for [culture @culture-list]
         [:button.btn.btn-primary
          {
           :key (:key culture)
           :on-click (fn []
                       (re-frame/dispatch [:set-selected-culture (:key culture)])
                       (swap! switch not))}
          (:label culture)])])))

(defn location-filter-comp [switch]
  (let [location-list (re-frame/subscribe [:ui-locations])]
    (fn []
      [:div.col-xs-12
       (for [model @location-list]
         [:button.btn.btn-primary
          {
           :key (:key model)
           :on-click (fn []
                       (re-frame/dispatch [:set-selected-location (:key model)])
                       (swap! switch not))}
          (:label model)])])))

(defn culture-filter-button [selected-culture switch]
  [:button.btn
   {
    :class (if (nil? @selected-culture) "" "btn-primary")
    :on-click #(swap! switch not)}
   (str "Culture: " (if (nil? @selected-culture) "None" (:label @selected-culture)))])

(defn location-filter-button [selected-location switch]
  [:button.btn
   {
    :class (if (nil? @selected-location) "" "btn-primary")
    :on-click #(swap! switch not)}
   (str "Location: " (if (nil? @selected-location) "None" (:label @selected-location)))])

(defn clear-filter-comp []
  [:div.col-xs-12.pad-bottom-xs
   [:label "Filters"]
   " (" [:a {:href "#" :on-click (fn [e]
                                   (.preventDefault e)
                                   (re-frame/dispatch [:set-selected-culture nil])
                                   (re-frame/dispatch [:set-selected-location nil]))}
         "clear"] ")"])

(defn project-list-comp []
  (let [project-list (re-frame/subscribe [:project-list])
        selected-culture (re-frame/subscribe [:selected-culture])
        selected-location (re-frame/subscribe [:selected-location])
        selected-culture-id (re-frame/subscribe [:selected-culture-id])
        selected-location-id (re-frame/subscribe [:selected-location-id])
        culture-filter-open (reagent/atom (boolean false))
        location-filter-open (reagent/atom (boolean false))]
    (fn []
      [:div
       [:div.row.pad-bottom-sm.border-light.background-light
        [clear-filter-comp]
        [:div.col-xs-4.col-md-2 [culture-filter-button selected-culture culture-filter-open]]
        [:div.col-xs-4.col-md-2 [location-filter-button selected-location location-filter-open]]
        [:div.col-xs-12 (if (false? @culture-filter-open) {:style {:display "none"}} {})
         [culture-filter-comp culture-filter-open]]
        [:div.col-xs-12 (if (false? @location-filter-open) {:style {:display "none"}} {})
         [location-filter-comp location-filter-open]]]
       [:div.row [:div.col-xs-12(project-list-rows project-list)]]])))

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label "Active Batches"
       :level :level1])))

(defn home-panel []
  [:div.col-xs-12
   [:div.row
    [:div.col-xs-3
     [home-title]]]
   [:div.row
    [:div.col-xs-12
     [project-list-comp]]]])
