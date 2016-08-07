(ns mycotrack-frame.pages.auth
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text password-input-text email-input-text dropdown loading-comp]]
            [goog.crypt.base64 :as b64]
            [mycotrack-frame.webstorage :as webstorage]))

(defn auth-button [email password]
  [re-com/button
   :label            "Login"
   :tooltip          "Login"
   :tooltip-position :below-center
   :on-click          (fn []
                        (webstorage/set-item! :login-email @email)
                        (re-frame/dispatch
                              [:set-auth-token
                               (b64/encodeString (str @email ":" @password))]))
   :class             "btn-primary"])

(defn auth-form []
  (let [email (reagent/atom (webstorage/get-item :login-email))
        password (reagent/atom "")]
    (fn []  [:div.col-xs-12.col-md-3 [email-input-text email "Email"]
             [password-input-text password "Password"]
             [auth-button email password]])))

(defn page-title []
  [re-com/title
   :label "Login"
   :level :level1])

(defn auth-panel []
  (let [auth-status (re-frame/subscribe [:auth-status])]
    [:div.col-xs-12
     [:div.row [page-title]]
     [:div.row (if (= @auth-status :pending)
                 [loading-comp]
                 [auth-form])]]))
