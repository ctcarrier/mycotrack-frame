(ns mycotrack-frame.handlers
  (:require [re-frame.core :as re-frame :refer [debug dispatch]]
            [secretary.core :as secretary]
            [mycotrack-frame.db :as db]
            [mycotrack-frame.httputils :refer [GET-SECURE POST-SECURE]]
            [ajax.core :refer [GET POST]]
            [clojure.walk :refer [keywordize-keys]]
            [mycotrack-frame.webstorage :as webstorage]))

(def standard-middlewares  [])

(defn handle-project-http-event [project-response]
  (js/console.log "PRoject response<<<<<<<<<<<")
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
   (webstorage/set-item! :auth-token (str "Basic " auth-token))
   (assoc db :auth-status :pending :auth-token (str "Basic " auth-token))))

(re-frame/register-handler
 :auth-success
 (fn [db [_ auth-token]]
   (.assign js/location "#/")
   (assoc db :auth-status :success)))

(re-frame/register-handler
 :auth-failure
 (fn [db [_ auth-token]]
   (webstorage/remove-item! :auth-token)
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
 :aggregate-response
 standard-middlewares
 (fn [db [_ aggregate]]
   (assoc db :aggregate aggregate)))

(re-frame/register-handler
 :set-selected-species
 standard-middlewares
 (fn [db [_ species-id]]
   (assoc db :selected-species-id species-id)))

(re-frame/register-handler
 :set-selected-project
 standard-middlewares
 (fn [db [_ project-id]]
   (assoc db :selected-project-id project-id)))

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
   (js/console.log "Settings project list")
   (assoc db :project-list project-list)))

(re-frame/register-handler
 :locations-response
 standard-middlewares
 (fn [db [_ location-list]]
   (js/console.log "Settings location list")
   (js/console.log (nth location-list 2))
   (assoc db :locations location-list)))

(re-frame/register-handler
 :location-settings-response
 standard-middlewares
 (fn [db [_ location-settings-list]]
   (js/console.log "Setting settings")
   (assoc db :location-settings location-settings-list)))

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
 :update-location-list
 standard-middlewares
 (fn [db [_]]
   (GET-SECURE "/api/locations" {:handler handle-location-http-event})
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

(re-frame/register-handler
 :spawn-project
 standard-middlewares
 (fn [db [_ parent-id project]]
   (POST-SECURE (str "/api/extendedProjects/" parent-id "/children")
         {:params project
          :handler (fn [a, b, c]
                     (.assign js/location "#/"))
          :format :json
          :response-format :json
          :keywords? true})
   db))

(re-frame/register-handler
 :retire-project
 standard-middlewares
 (fn [db [_ project]]
   (POST-SECURE (str "/api/extendedProjects/" (:_id project) "/children")
                {:params project
                 :handler (fn []
                            (.assign js/location "#/")                     )
                 :format :json
                 :response-format :json
                 :keywords? true})
   db))

(re-frame/register-handler
 :save-new-species
 standard-middlewares
 (fn [db [_ species]]
   (POST-SECURE "/api/species"
         {:params species
          :handler (fn []
                     (.assign js/location "#/"))
          :format :json
          :response-format :json
          :keywords? true})
   db))

(re-frame/register-handler
 :save-new-culture
 standard-middlewares
 (fn [db [_ culture]]
   (POST-SECURE "/api/cultures"
         {:params culture
          :handler (fn []
                     (.assign js/location "#/"))
          :format :json
          :response-format :json
          :keywords? true})
   db))
