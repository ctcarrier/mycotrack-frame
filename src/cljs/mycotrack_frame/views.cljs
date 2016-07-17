(ns mycotrack-frame.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.pages.home :refer [home-panel]]
              [mycotrack-frame.pages.about :refer [about-panel]]
              [mycotrack-frame.pages.species-list :refer [species-list-panel]]
              [mycotrack-frame.pages.species-detail :refer [species-detail-panel]]
              [mycotrack-frame.pages.new-project :refer [new-project-panel]]
              [mycotrack-frame.ui-comps :refer [navbar]]))


(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :species-list-panel [] [species-list-panel])
(defmethod panels :species-detail-panel [] [species-detail-panel])
(defmethod panels :new-project-panel [] [new-project-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:container-fluid
       [:div.col-xs-12
        [navbar]
        (panels @active-panel)]])))
