(ns mycotrack-frame.pages.auth
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [loading-comp]]
            [goog.crypt.base64 :as b64]
            [mycotrack-frame.webstorage :as webstorage]))

(defn auth-button [email password]
  [:button.btn.btn-primary.col-xs-12.input-lg
   {:label "Login"
    :type "submit"
    :tooltip "Login"
    :on-click
    (fn []
      (webstorage/set-item! :login-email @email)
      (re-frame/dispatch
       [:set-auth-token
        (b64/encodeString (str @email ":" @password))]))
    } "Save"])

(defn password-input-text [desc placeholder]
  [:div.form-group
   [:label {:for "authPassword"} "Password"]
   [:input.form-control.input-lg {:id "authPassword" :type "password" :placeholder placeholder :value @desc :on-change #(reset! desc (-> % .-target .-value))}]])

(defn email-input-text [desc placeholder]
  [:div.form-group
   [:label {:for "authEmail"} "Email Address"]
   [:input.form-control.input-lg {:id "authEmail" :type "email" :placeholder placeholder :value @desc :on-change #(reset! desc (-> % .-target .-value))}]])

(defn auth-form []
  (let [email (reagent/atom (webstorage/get-item :login-email))
        password (reagent/atom "")]
    (fn [] [:form
            [email-input-text email "Email"]
            [password-input-text password "Password"]
            [auth-button email password]])))

(defn page-title []
  [:h1 "Login"])

(defn auth-panel []
  (let [auth-status (re-frame/subscribe [:auth-status])]
    [:div.col-md-4.offset-md-4.col-12
     [:div.card.border-primary.mb-3
      [:div.card-header
       [page-title]]
      [:div.card-body
       (if (= @auth-status :pending)
         [loading-comp]
         [auth-form])]]]))
