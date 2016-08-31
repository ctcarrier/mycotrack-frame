(ns mycotrack-frame.pages.auth
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [number-input-text description-input-text password-input-text email-input-text dropdown loading-comp]]
            [goog.crypt.base64 :as b64]
            [mycotrack-frame.webstorage :as webstorage]))

(defn auth-button [email password]
  [:button.btn.btn-primary.input-lg.col-xs-12 {:label "Login" :type "submit" :tooltip "Login"
                                               :on-click (fn [e]
                                                           (.preventDefault e)
                                                           (webstorage/set-item! :login-email @email)
                                                           (re-frame/dispatch
                                                            [:set-auth-token
                                                             (b64/encodeString (str @email ":" @password))]))} "Login"])

(defn auth-form []
  (let [email (reagent/atom (webstorage/get-item :login-email))
        password (reagent/atom "")]
    (fn []  [:form.col-xs-12.col-md-4.col-md-offset-4
             [:h2.text-center "Login"]
             [:div.col-xs-12.pad-bottom-xs [email-input-text email "Email"]]
             [:div.col-xs-12.pad-bottom-xs [password-input-text password "Password"]]
             [:div.col-xs-12 [auth-button email password]]])))

(defn auth-panel []
  (let [auth-status (re-frame/subscribe [:auth-status])]
    [:div.col-xs-12
     [:div.row (if (= @auth-status :pending)
                 [loading-comp]
                 [auth-form])]]))
