(ns mycotrack-frame.handlers
  (:require [re-frame.core :as re-frame :refer [debug dispatch]]
            [secretary.core :as secretary]
            [mycotrack-frame.db :as db]
            [mycotrack-frame.http-utils :refer [GET-SECURE POST-SECURE]]
            [ajax.core :refer [GET POST]]
            [clojure.walk :refer [keywordize-keys]]))

(def standard-middlewares  [])

(defn handle-project-http-event [project-response]
  (re-frame/dispatch [:project-response (map #(into {:key (:_id %)} %) (keywordize-keys (js->clj project-response)))]))

(defn handle-auth-http-event [auth-response]
  )

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
 :set-auth-token
 (fn [db [_ auth-token]]
   (GET "/api/users" {:handler #(re-frame/dispatch [:auth-success])
                      :error-handler #(re-frame/dispatch [:auth-failure])
                                 :headers [:Authorization (str "Basic " auth-token)]})
   (assoc db :auth-status :pending :auth-token (str "Basic " auth-token))))

(re-frame/register-handler
 :auth-success
 (fn [db [_ auth-token]]
   (.assign js/location "#/")
   (assoc db :auth-status :success)))

(re-frame/register-handler
 :auth-failure
 (fn [db [_ auth-token]]
   (assoc db :auth-status :fail :auth-token nil)))

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
   (GET-SECURE "/api/extendedProjects" {:handler handle-project-http-event
                                 :params project-filter})
   db))

(re-frame/register-handler
 :save-new-project
 standard-middlewares
 (fn [db [_ project]]
   (POST-SECURE "/api/projects"
         {:params project
          :handler (fn [a, b, c]  ;;(swap! (:project-list db) conj project)
                     (js/console.log "All 3 vvv")
                     (js/console.log db)
                     ;; (secretary/dispatch! "/")
                     (.assign js/location "#/"))
          :format :json
          :response-format :json
          :keywords? true})
   db))
