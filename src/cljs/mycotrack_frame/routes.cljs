(ns mycotrack-frame.routes
    (:require-macros [secretary.core :refer [defroute]])
    (:import goog.History)
    (:require [secretary.core :as secretary]
              [goog.events :as events]
              [goog.history.EventType :as EventType]
              [re-frame.core :as re-frame]))

(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

(defn app-routes []
  (secretary/set-config! :prefix "#")
  ;; --------------------
  ;; define routes here
  (defroute "/" []
    (re-frame/dispatch [:set-active-panel :home-panel]))

  (defroute "/about" []
    (re-frame/dispatch [:set-active-panel :about-panel]))

  (defroute "/species/:id" [id]
    (js/console.log "Coming")
    (js/console.log id)
    (re-frame/dispatch [:set-active-panel :species-detail-panel :set-selected-species id]))

  (defroute "/batches/:id" [id]
    (re-frame/dispatch [:set-active-panel :project-detail-panel :set-selected-project id]))

  (defroute "/species" []
    (js/console.log "SPecies page")
    (re-frame/dispatch [:set-active-panel :species-list-panel]))

  (defroute "/new-batch" []
    (re-frame/dispatch [:set-active-panel :new-project-panel]))

  (defroute "/login" []
    (re-frame/dispatch [:set-active-panel :auth-panel]))

  ;; --------------------
  (hook-browser-navigation!))
