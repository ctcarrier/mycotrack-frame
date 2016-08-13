(ns mycotrack-frame.pages.new-species
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [description-input-text]]))

(defn save-species-button [scientific-name common-name image-url]
  [re-com/button
   :label            "Save"
   :tooltip          "Save new species"
   :tooltip-position :below-center
   :on-click          (fn [] (re-frame/dispatch
                              [:save-new-species
                               {
                                :scientificName @scientific-name,
                                :commonName @common-name,
                                :imageUrl @image-url}]))
   :class             "btn-primary"])

(defn new-species-form []
  (let [scientific-name (reagent/atom "")
        common-name (reagent/atom "")
        image-url (reagent/atom "")]
    (fn []  [:div.col-md-3.col-xs-12
             [description-input-text scientific-name "Scientific Name"]
             [description-input-text common-name "Common Name"]
             [description-input-text image-url "Image URL"]
             [save-species-button scientific-name common-name image-url]])))

(defn new-species-title []
  [re-com/title
   :label "New Species."
   :level :level1])

(defn new-species-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-species-title] [new-species-form]]])
