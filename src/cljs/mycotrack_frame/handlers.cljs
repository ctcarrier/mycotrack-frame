(ns mycotrack-frame.handlers
  (:require [re-frame.core :as re-frame :refer [debug dispatch]]
            [secretary.core :as secretary]
            [mycotrack-frame.db :as db]
            [ajax.core :refer [GET POST]]
            [clojure.walk :refer [keywordize-keys]]))

(def standard-middlewares  [])

(defn handle-project-http-event [project-response]
  (re-frame/dispatch [:project-response (map #(into {:key (:_id %)} %) (keywordize-keys (js->clj project-response)))]))

(re-frame/register-handler
 :initialize-db
 (fn  [_ _]
   db/default-db))

(re-frame/register-handler
 :set-active-panel
 (fn [db [_ active-panel & remaining]]
   (js/console.log "DB vvv")
   (js/console.log db)
   (js/console.log active-panel)
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
 :farm-response
 standard-middlewares
 (fn [db [_ farm]]
   (assoc db :farm farm)))

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
 :set-selected-location
 standard-middlewares
 (fn [db [_ location-id]]
   (assoc db :selected-location-id location-id)))

(re-frame/register-handler
 :project-response
 standard-middlewares
 (fn [db [_ project-list]]
   (assoc db :project-list project-list)))

(re-frame/register-handler
 :cleanup
 standard-middlewares
 (fn [db [_ key]]
   (js/console.log key)
   (dissoc db key)))

(re-frame/register-handler
 :update-project-list
 standard-middlewares
 (fn [db [_ project-filter]]
   (GET "/api/extendedProjects" {:handler handle-project-http-event
                                 :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]
                                 :params project-filter})
   db))

(re-frame/register-handler
 :save-new-project
 standard-middlewares
 (fn [db [_ project]]

   (POST "/api/projects"
         {:params project
          :handler (fn [a, b, c]  ;;(swap! (:project-list db) conj project)
                     (js/console.log "All 3 vvv")
                     (js/console.log db)
                     ;; (secretary/dispatch! "/")
                     (.assign js/location "#/")
                     )
          :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]
          :error-handler #((js/console.log "Error saving project"))
          :format :json
          :response-format :json
          :keywords? true})
   db))
