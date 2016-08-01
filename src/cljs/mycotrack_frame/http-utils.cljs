(ns mycotrack-frame.http-utils
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [ajax.core :refer [GET POST]]))

(defn standard-error-handler []
  (.assign js/location "#/login"))

(defn add-auth-vector [auth-token] #(into [] (concat [:Authorization auth-token] %1)))
(def add-auth #(update-in %1 [:headers] (add-auth-vector %2)))
(def add-error-handler #(assoc-in %1 [:error-handler] standard-error-handler))

(defn update-params [params auth-token]
  ((comp add-error-handler add-auth) params auth-token))

(defn GET-SECURE [url params]
  (let [auth-token (re-frame/subscribe [:auth-token])]
    (let [params-with-auth (update-params params @auth-token)]
      (js/console.log (str "Params " params-with-auth))
      (GET url params-with-auth))))

(defn POST-SECURE [url params]
  (let [auth-token (re-frame/subscribe [:auth-token])]
    (let [params-with-auth (update-params params auth-token)]
         (POST url params-with-auth))))
