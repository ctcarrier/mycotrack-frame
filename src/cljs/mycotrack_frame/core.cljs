(ns mycotrack-frame.core
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame :refer [dispatch]]
              [mycotrack-frame.handlers]
              [mycotrack-frame.subs]
              [mycotrack-frame.routes :as routes]
              [mycotrack-frame.views :as views]
              [mycotrack-frame.config :as config]
              [ajax.core :refer [GET POST]]))

(when config/debug?
  (println "dev mode"))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn handle-species-http-event [species-response]
  (dispatch [:species-response (js->clj species-response)]))

(defn init-http-data []
  (GET "/api/species" {:handler handle-species-http-event})
  (dispatch [:update-project-list]))

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root)
  (init-http-data))
