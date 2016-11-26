(ns mycotrack-frame.pages.new-species
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [description-input-text]]))

(defn save-species-button [scientific-name common-name image-url image-attribution]
  [:button.btn.btn-primary.col-xs-12.input-lg {:label "Save" :type "submit" :tooltip "Create a new species"
                                               :on-click
                                               (fn [e]
                                                 (.preventDefault e)
                                                 (re-frame/dispatch
                                                  [:save-new-species
                                                   {
                                                    :scientificName @scientific-name,
                                                    :commonName @common-name,
                                                    :imageUrl @image-url,
                                                    :imageAttribution @image-attribution}]))
                                               } "Save"])

(defn new-species-form []
  (let [scientific-name (reagent/atom "")
        common-name (reagent/atom "")
        image-url (reagent/atom "")
        image-attribution (reagent/atom "")]
    (fn []  [:div.col-md-3.col-xs-12
             [description-input-text scientific-name "Scientific Name"]
             [description-input-text common-name "Common Name"]
             [description-input-text image-url "Image URL"]
             [description-input-text image-attribution "Image Attribution"]
             [save-species-button scientific-name common-name image-url image-attribution]])))

(defn new-species-title []
  [:h2 "New Species."])

(defn new-species-panel []
  [:div.col-xs-12 [new-species-title] [new-species-form]])
