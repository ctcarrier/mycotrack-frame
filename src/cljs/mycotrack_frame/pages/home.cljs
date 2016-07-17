(ns mycotrack-frame.pages.home
  (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.ui-comps :refer [loading-comp dropdown]]))

(defn project-list-rows [project-list]
  (if (nil? @project-list)
    (loading-comp)
    [:table.table.table-striped.table-hover
       [:thead
        [:tr
         [:th {:key "1"} "Date Created"]
         [:th {:key "2"} "Culture"]
         [:th {:key "3"} "Location"]
         [:th {:key "4"} "Count"]
         ]]
       [:tbody
        (if (nil? @project-list)
          "Loading..."
          (for [project @project-list]
            [:tr {:key (:_id project)}
             [:td {:key (str (:_id project) "_cd")} [:a {:href (str "#/batches/" (:_id project))} (:createdDate project)]]
             [:td {:key (str (:_id project) "_cn")} [:a {:href (str "#/batches/" (:_id project))} (-> project :culture :name)]]
             [:td {:key (str (:_id project) "_ln")}[:a {:href (str "#/batches/" (:_id project))} (-> project :location :name)]]
             [:td {:key (str (:_id project) "ct")} [:a {:href (str "#/batches/" (:_id project))} (:count project)]]]))]]))

(defn project-list-comp []
  (let [project-list (re-frame/subscribe [:project-list])
        selected-culture-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)]
    (fn []
      [:div.col-xs-12.pad-bottom [:div.row
             [:div.col-xs-3.col-md-1 [re-com/label :label "Culture:"]]
             [:div.col-xs-9.col-md-11 [dropdown
                             :ui-cultures
                             selected-culture-id
                             (fn [res]
                               (reset! selected-culture-id res)
                               (re-frame/dispatch [:set-selected-culture res]))]]]
       [:div.row
        [:div.col-xs-3.col-md-1 [re-com/label :label "Location:"]]
        [:div.col-xs-9.col-md-11 [dropdown
                        :ui-locations
                        selected-location-id
                        (fn [res] (reset! selected-location-id res)
                          (re-frame/dispatch [:set-selected-location res]))]]]
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
