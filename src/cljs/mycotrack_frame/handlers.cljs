(ns mycotrack-frame.handlers
    (:require [re-frame.core :as re-frame :refer [debug dispatch]]
              [mycotrack-frame.db :as db]
              [ajax.core :refer [GET POST]]
              [clojure.walk :refer [keywordize-keys]]))

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
   (js/console.log "handled")
   (assoc db :species species)))

(re-frame/register-handler
 :set-selected-species
 standard-middlewares
 (fn [db [_ species-id]]
   (js/console.log "species selected")
   (assoc db :selected-species-id species-id)))

(defn handle-project-http-event [project-response]
  (re-frame/dispatch [:project-response (keywordize-keys (js->clj project-response))]))

(re-frame/register-handler
 :update-project-list
 standard-middlewares
 (fn [db [_]]
   (js/console.log "About to get!")
   (GET "/api/extendedProjects" {:handler handle-project-http-event
                                 :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]})
   db))

(re-frame/register-handler
 :project-response
 standard-middlewares
 (fn [db [_ project-list]]
   (js/console.log "Setting project list!")
   (js/console.log (clj->js project-list))
   (assoc db :project-list project-list)))
