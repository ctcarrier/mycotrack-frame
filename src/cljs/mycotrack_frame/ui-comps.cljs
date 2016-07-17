(ns mycotrack-frame.ui-comps
  (:require [mycotrack-frame.links :refer [link-to-home-page link-to-about-page link-to-new-project-page]]
            [re-com.core :as re-com]
            [re-frame.core :as re-frame]))

(defn dropdown [key selected-id on-change]
  (let [models   (re-frame/subscribe [key])]
    (fn []
      [re-com/v-box
       :gap      "10px"
       :children [[re-com/h-box
                   :gap      "10px"
                   :align    :center
                   :children [[re-com/single-dropdown
                               :choices   (cons {:id nil :label "none" :key "none"} @models)
                               :model     @selected-id
                               :width     "300px"
                               :on-change on-change]]]]])))

(defn description-input-text [desc]
  [re-com/input-text
   :model            @desc
   :width            "300px"
   :placeholder      "Enter description"
   :on-change        #(reset! desc %)
   :change-on-blur?  "true"])

(defn number-input-text [model]
  [re-com/input-text
   :model            @model
   :width            "300px"
   :placeholder      "Enter number"
   :on-change        #(reset! model %)
   :change-on-blur?  "true"])

(defn loading-comp []
  "Loading")

(defn navbar []
  [:ul.topnav
   [:li [link-to-home-page]]
   [:li [link-to-about-page]]
   [:li [link-to-new-project-page]]])
