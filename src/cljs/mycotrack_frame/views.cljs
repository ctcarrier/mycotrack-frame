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
   :label "Mycotrack"
   :href "#/"
   :class "Brand"])

(defn link-to-new-project-page []
  [re-com/hyperlink-href
   :label "New +"
   :href "#/new-batch"])

(defn link-to-about-page []
  [re-com/hyperlink-href
   :label "About"
   :href "#/about"])

;; forms  []

(defn dropdown [key selected-id on-change]
  (let [models   (re-frame/subscribe [key])]
    (fn []
      [re-com/v-box
       :gap      "10px"
       :children [[re-com/h-box
                   :gap      "10px"
                   :align    :center
                   :children [[re-com/single-dropdown
                               :choices   @models
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

(defn save-project-button [desc count selected-culture-id selected-container-id selected-substrate-id]
  [re-com/button
   :label            "New Batch"
   :tooltip          "Create a new batch"
   :tooltip-position :below-center
   :on-click          (fn [] (re-frame/dispatch [:save-new-project
                                                 {
                                                  :description @desc,
                                                  :count @count,
                                                  :cultureId @selected-culture-id,
                                                  :container @selected-container-id
                                                  :enabled true
                                                  :substrate @selected-substrate-id}]))
   :class             "btn-primary"])

(defn new-project-form []
  (let [desc (reagent/atom "")
        count (reagent/atom "")
        selected-container-id (reagent/atom nil)
        selected-culture-id (reagent/atom nil)
        selected-substrate-id (reagent/atom nil)]
    (fn []  [:div [description-input-text desc]
             [number-input-text count]
             [dropdown :ui-cultures selected-culture-id #((fn [] (reset! selected-culture-id %)))]
             [dropdown :ui-containers selected-container-id #((fn [] (reset! selected-container-id %)))]
             [dropdown :ui-substrate selected-substrate-id #((fn [] (reset! selected-substrate-id %)))]
             [save-project-button desc count selected-culture-id selected-container-id selected-substrate-id]])))

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

(defn loading-comp []
  "Loading")

(defn navbar []
  [:ul.topnav
   [:li [link-to-home-page]]
   [:li [link-to-about-page]]])

(defn project-list-rows [project-list]
  (if (nil? @project-list)
    (loading-comp)
    [:table.table.table-striped.table-hover
       [:thead
        [:tr
         [:th "Date Created"]
         [:th "Count"]
         [:th "Species"]
         [:th "Culture"]
         [:th "Description"]
         ]]
       [:tbody
        (if (nil? @project-list)
          "Loading..."
          (for [project @project-list]
            [:tr
             [:td [:a {:href (str "#/batches/" (:_id project))} (:createdDate project)]]
             [:td [:a {:href (str "#/batches/" (:_id project))} (:count project)]]
             [:td [:a {:href (str "#/batches/" (:_id project))} (-> project :species :commonName)]]
             [:td [:a {:href (str "#/batches/" (:_id project))} (-> project :culture :name)]]
             [:td [:a {:href (str "#/batches/" (:_id project))} (:description project)]]]))]]))

(defn project-list-comp []
  (let [project-list (re-frame/subscribe [:project-list])
        selected-culture-id (reagent/atom nil)]
    (fn [] [:div.col-xs-12
            [dropdown :ui-cultures selected-culture-id #((fn [] (reset! selected-culture-id %)
                                                      (re-frame/dispatch [:set-selected-culture %])))]
            (project-list-rows project-list)])))

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
       :label "Active Batches"
       :level :level1])))

(defn home-panel []
  [re-com/v-box
   :gap "1em"
   :children [[home-title] [link-to-new-project-page] [project-list-comp]]])

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

;; new project

(defn new-project-title []
  [re-com/title
   :label "New Batch."
   :level :level1])

(defn new-project-panel []
  [re-com/v-box
   :gap "1em"
   :children [[new-project-title] [new-project-form]]])

;; main

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
      [re-com/v-box
       :height "100%"
       :children [[navbar]
                  (panels @active-panel)]])))
