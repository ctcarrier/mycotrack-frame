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

(defn ^:export init []
  (routes/app-routes)
  (re-frame/dispatch-sync [:initialize-db])
  (mount-root))
