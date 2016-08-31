(ns mycotrack-frame.uicomps
  (:require [mycotrack-frame.links :refer [link-to-home-page link-to-about-page link-to-new-project-page]]
            [re-com.core :as re-com]
            [re-frame.core :as re-frame]))

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
  [:ul.topnav
   [:li [link-to-home-page]]
   [:li [link-to-about-page]]
   [:li [link-to-new-project-page]]])
