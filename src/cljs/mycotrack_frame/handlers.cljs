(ns mycotrack-frame.handlers
    (:require [re-frame.core :as re-frame :refer [debug dispatch]]
              [mycotrack-frame.db :as db]
              [ajax.core :refer [GET POST]]
              [clojure.walk :refer [keywordize-keys]]))

(def standard-middlewares  [debug])

(defn handle-project-http-event [project-response]
  (re-frame/dispatch [:project-response (map #(into {:key (:_id %)} %) (keywordize-keys (js->clj project-response)))]))

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
 :cultures-response
 standard-middlewares
 (fn [db [_ cultures]]
   (assoc db :cultures cultures)))

(re-frame/register-handler
 :set-selected-species
 standard-middlewares
 (fn [db [_ species-id]]
   (assoc db :selected-species-id species-id)))

(re-frame/register-handler
 :set-selected-culture
 standard-middlewares
 (fn [db [_ culture-id]]
   (assoc db :selected-culture-id culture-id)))

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

(re-frame/register-handler
 :update-project-list
 standard-middlewares
 (fn [db [_ project-filter]]
   (js/console.log "UPdate project")
   (js/console.log project-filter)
   (GET "/api/extendedProjects" {:handler handle-project-http-event
                                 :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]
                                 :params project-filter})
   db))
