(ns mycotrack-frame.pages.new-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text dropdown]]))

(defn save-project-button [desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]
  (fn [] [:button.btn.btn-primary {:tooltip "Create a new batch" :on-click (fn [] (re-frame/dispatch
                                                                                   [:save-new-project
                                                                                    {
                                                                                     :description @desc,
                                                                                     :count @count,
                                                                                     :cultureId @selected-culture-id,
                                                                                     :container @selected-container-id
                                                                                     :enabled true
                                                                                     :substrate @selected-substrate-id
                                                                                     :locationId @selected-location-id}]))}]))

(defn find-first
         [f coll]
         (first (filter f coll)))

(defn init-wizard-content [desc page count selected-culture-id selected-container-id selected-substrate-id selected-location-id]
  [
           {:name "Description" :content [:div.col-xs-12 [:div.col-xs-12 (str "Desc: " @desc)] [:div.col-xs-12 [description-input-text desc "Description"]]]}
           {:name "Count" :content [number-input-text count "Count"]}
           {:name "Culture" :content [dropdown :ui-cultures selected-culture-id #((fn [] (reset! selected-culture-id %)))]}
           {:name "Container" :content [dropdown :ui-containers selected-container-id #((fn [] (reset! selected-container-id %)))]}
           {:name "Substrate" :content [dropdown :ui-substrate selected-substrate-id #((fn [] (reset! selected-substrate-id %)))]}
           {:name "Location" :content [dropdown :ui-locations selected-location-id #((fn [] (reset! selected-location-id %)))]}
           {:name "Save" :content [:div.col-xs-12
                                        [:div.col-xs-12 "Desc " @desc]
                                        [:div.col-xs-12 "Count " @count]
                                        [:div.col-xs-12 "Container " @selected-container-id]
                                        [:div.col-xs-12 "Culture " @selected-culture-id]
                                        [:div.col-xs-12 "Substrate " @selected-substrate-id]
                                        [:div.col-xs-12 "Locations " @selected-location-id]
                                        [:div.col-xs-12 save-project-button desc count selected-culture-id selected-container-id selected-substrate-id selected-location-id]]}])

(defn new-project-form []
  (let [page (reagent/atom 0)
        desc (reagent/atom "")
        project-count (reagent/atom 1)
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)
        selected-location-id (reagent/atom nil)
        local-wizard-content (init-wizard-content page desc project-count selected-culture-id selected-container-id selected-substrate-id selected-location-id)]
    (fn []
      [:div.col-xs-12
       @page
       [:div.col-xs-2 [:button.btn.btn-primary (into {} (conj (if (<= @page 0) {:class "disabled"})
                                                              {:on-click #(swap! page dec)})) "<<"]]
       [:div.col-xs-4 (:name (nth local-wizard-content @page))]
       [:div.col-xs-2.col-xs-offset-4 [:button.btn.btn-primary (into {} (conj (if (>= @page (count local-wizard-content)) {:class "disabled"})
                                                                              {:on-click #(swap! page inc)})) ">>"]]
       [:div.col-xs-12 (:content (nth local-wizard-content @page))]])))

(defn new-project-title []
  [re-com/title
   :label "New Batch."
   :level :level1])

(defn new-project-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-project-title] [new-project-form]]])
