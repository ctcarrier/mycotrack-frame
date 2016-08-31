(ns mycotrack-frame.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]
              [mycotrack-frame.pages.home :refer [home-panel]]
              [mycotrack-frame.pages.about :refer [about-panel]]
              [mycotrack-frame.pages.species-list :refer [species-list-panel]]
              [mycotrack-frame.pages.species-detail :refer [species-detail-panel]]
              [mycotrack-frame.pages.project-detail :refer [project-detail-panel]]
              [mycotrack-frame.pages.new-project :refer [new-project-panel]]
              [mycotrack-frame.pages.spawn-project :refer [spawn-project-panel]]
              [mycotrack-frame.pages.contam-project :refer [contam-project-panel]]
              [mycotrack-frame.pages.move-project :refer [move-project-panel]]
              [mycotrack-frame.pages.new-species :refer [new-species-panel]]
              [mycotrack-frame.pages.new-culture :refer [new-culture-panel]]
              [mycotrack-frame.pages.auth :refer [auth-panel]]
              [mycotrack-frame.uicomps :refer [navbar]]))


(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :species-list-panel [] [species-list-panel])
(defmethod panels :species-detail-panel [] [species-detail-panel])
(defmethod panels :project-detail-panel [] [project-detail-panel])
(defmethod panels :new-project-panel [] [new-project-panel])
(defmethod panels :spawn-project-panel [] [spawn-project-panel])
(defmethod panels :contam-project-panel [] [contam-project-panel])
(defmethod panels :move-project-panel [] [move-project-panel])
(defmethod panels :new-species-panel [] [new-species-panel])
(defmethod panels :new-culture-panel [] [new-culture-panel])
(defmethod panels :auth-panel [] [auth-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [:container-fluid
       [:div.row
        [navbar]
        (panels @active-panel)]])))
