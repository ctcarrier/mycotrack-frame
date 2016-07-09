(ns mycotrack-frame.handlers
    (:require [re-frame.core :as re-frame :refer [debug dispatch]]
              [mycotrack-frame.db :as db]))

(def standard-middlewares  [debug])

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel & remaining]]
   (dispatch (into [] remaining))
   (assoc db :active-panel active-panel)))

(re-frame/register-handler
 :species-response
 standard-middlewares
 (fn [db [_ species]]
   (assoc db :species species)))

(re-frame/register-handler
 :set-selected-species
 standard-middlewares
 (fn [db [_ species-id]]
   (assoc db :selected-species-id species-id)))

(re-frame/register-handler
 :project-response
 standard-middlewares
 (fn [db [_ project-list]]
   (assoc db :project-list project-list)))

(re-frame/register-handler
 :cleanup
 standard-middlewares
 (fn [db [_ key]]
   (dissoc db key)))
