(ns mycotrack-frame.views
    (:require [re-frame.core :as re-frame]
              [re-com.core :as re-com]
              [reagent.core    :as    reagent]))

;;links

(defn link-to-species-list-page []
  [re-com/hyperlink-href
   :label "go to Species List Page"
   :href "#/species"])

(defn link-to-home-page []
  [re-com/hyperlink-href
   :label "go to Home Page"
   :href "#/"])

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "go to About Page"
   :href "#/about"])

;; forms  []
(defn simple-dropdown []
  (let [selected-species-id (reagent/atom nil)
        species   (re-frame/subscribe [:ui-species])]
    (fn []
      (js/console.log (clj->js @species))
      [re-com/v-box
       :gap      "10px"
       :children [[:p "Species Dropdown."]
                  [re-com/h-box
                   :gap      "10px"
                   :align    :center
                   :children [[re-com/single-dropdown
                               :choices   @species
                               :model     selected-species-id
                               :width     "300px"
                               :on-change #((fn [] (reset! selected-species-id %)
                                              (re-frame/dispatch [:set-selected-species %])))]
                              [:div
                               (js/console.log "Displaying selection")
                               (js/console.log (clj->js @selected-species-id))
                               [:strong "Selected species: "]
                               (if (nil? @selected-species-id)
                                 "None"
                                 (str (clj->js @selected-species-id)))]]]]])))


;; comps

(defn species-list-comp []
  (let [species-list (re-frame/subscribe [:species])]
    (fn [] [:div.col-xs-12.pad-top (for [species @species-list]
                                      [:a {:key (get species "_id") :href (str "#/species/" (get species "_id"))}
                                       [:div.image-tile.col-xs-5
                                        [:p (get species "commonName")]
                                        [:p (get species "scientificName")]
                                        [:div {:key (get species "_id")}
                                         [:img {:src (get species "imageUrl")}]]]])])))

(defn project-list-comp []
  (let [project-list (re-frame/subscribe [:project-list])]
    (fn [] [:div.col-xs-12 [simple-dropdown]
            [:table.table
             [:thead
              [:tr
               [:th "Date Created"]
               [:th "Count"]
               [:th "Species"]
               [:th "Culture"]
               [:th "Description"]
               ]]
             [:tbody
              (for [project @project-list]
                [:tr
                 [:td (:createdDate project)]
                 [:td (:count project)]
                 [:td (-> project :species :commonName)]
                 [:td (-> project :culture :name)]
                 [:td (:description project)]])]]])))

(defn species-detail-comp []
  (let [selected-species (re-frame/subscribe [:selected-species])]
    (fn [] :div.col-xs-12.pad-top
      [:p (get @selected-species "commonName")]
      [:p (get @selected-species "scientificName")]
      [:img {:src (get @selected-species "imageUrl")}])))

;; home

(defn home-title []
  (let [name (re-frame/subscribe [:name])]
    (fn []
      [re-com/title
       :label (str "Hello from " @name ". This is the Home Page.")
       :level :level1])))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [link-to-about-page] [link-to-species-list-page] [project-list-comp]]])

;; about

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])

;; species-list

(defn species-list-title []
  [re-com/title
   :label "This is the Species Page2."
   :level :level1])

(defn species-list-panel []
  [re-com/v-box
   :gap "1em"
   :children [[species-list-title] [link-to-home-page] [species-list-comp]]])

;; species-detail

(defn species-detail-title []
  [re-com/title
   :label "This is the Species Detail Page."
   :level :level1])

(defn species-detail-panel []
  [re-com/v-box
   :gap "1em"
   :children [[species-detail-title] [link-to-home-page] [species-detail-comp]]])

;; main

(defmulti panels identity)
(defmethod panels :home-panel [] [home-panel])
(defmethod panels :about-panel [] [about-panel])
(defmethod panels :species-list-panel [] [species-list-panel])
(defmethod panels :species-detail-panel [] [species-detail-panel])
(defmethod panels :default [] [:div])

(defn main-panel []
  (let [active-panel (re-frame/subscribe [:active-panel])]
    (fn []
      [re-com/v-box
       :height "100%"
       :children [(panels @active-panel)]])))
