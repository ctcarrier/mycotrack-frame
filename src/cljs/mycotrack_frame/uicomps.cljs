(ns mycotrack-frame.uicomps
  (:require [mycotrack-frame.links :refer [link-to-home-page link-to-about-page link-to-new-project-page link-to-aggregate-page]]
            [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [cljs-time.core :as time :refer [date-time now]]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse]]))

(defn dropdown [key selected-id placeholder on-change]
  (let [models   (re-frame/subscribe [key])]
    (fn []
      [:select.form-control.input-lg {:on-change on-change}
       [:option {:value "" :required "required" :disabled "disabled" :selected "selected" :hidden "hidden"} placeholder]
       (for [model @models]
         [:option {:value (:key model) :key (:key model)} (:label model)])])))

(defn description-input-text [desc placeholder]
  [:input.form-control.input-lg {:type "text" :placeholder placeholder :value @desc :on-change #(reset! desc (-> % .-target .-value))}])

(defn password-input-text [desc placeholder]
  [:input.form-control.input-lg {:type "password" :placeholder placeholder :value @desc :on-change #(reset! desc (-> % .-target .-value))}])

(defn email-input-text [desc placeholder]
  [:input.form-control.input-lg {:type "email" :placeholder placeholder :value @desc :on-change #(reset! desc (-> % .-target .-value))}])

(defn parse-int [s]
    (js/parseInt s))

(defn number-input-text [model placeholder]
  [:input.form-control.input-lg {:type "number" :placeholder placeholder :value @model :on-change #(reset! model (parse-int (-> % .-target .-value)))}])

(defn loading-comp []
  [re-com/throbber :size :large])

(defn navbar []
  [:div.navbar.navbar-expand-lg.navbar-light.bg-light.mb-5
   [:button.navbar-toggler {:data-toggle "collapse" :data-target "#navbarSupportedContent" :aria-controls "navbarSupportedContent" :aria-expanded "false" :aria-label "Toggle navigation"}
    [:span.navbar-toggler-icon]]
   [:div.collapse.navbar-collapse {:id "navbarSupportedContent"}
    [:ul.navbar-nav.mr-auto
     [:li.nav-item [:a.nav-link {:href (link-to-home-page)} "Home"]]
     [:li.nav-item [:a.nav-link {:href (link-to-about-page)} "About"]]
     [:li.nav-item [:a.nav-link {:href (link-to-aggregate-page)} "Aggregations"]]]]])

(def date-formatter (formatter "MM-dd-YYYY"))
(def day-month-formatter (formatter "MM-dd"))
(def iso-formatter (formatters :date-time))

(defn date-input-text [model placeholder]
  [:input.form-control.input-lg {:type "text" :placeholder placeholder :default-value @model :on-blur #(reset! model (unparse iso-formatter (parse date-formatter (-> % .-target .-value))))}])
