(ns mycotrack-frame.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]
              [clojure.walk :refer [keywordize-keys]]
              [ajax.core :refer [GET POST]]
              [reagent.ratom :refer [make-reaction]]))

(defn find-first
         [f coll]
         (first (filter f coll)))

(re-frame/register-sub
 :name
 (fn [db]
   (reaction (:name @db))))

(re-frame/register-sub
 :active-panel
 (fn [db _]
   (reaction (:active-panel @db))))

(defn handle-species-http-event [species-response]
  (re-frame/dispatch [:species-response (js->clj species-response)]))

(re-frame/register-sub
 :species
 (fn [db [_ type]]
   (let  [query-token (GET "/api/species" {:handler handle-species-http-event})]
     (make-reaction
      (fn [] (get-in @db [:species]))
      :on-dispose #(do (re-frame/dispatch [:cleanup [:species]]))))))

(re-frame/register-sub
 :selected-species-id
 (fn [db _]
   (js/console.log "Returning species id!")
   (reaction (:selected-species-id @db))))

(re-frame/register-sub
 :project-filter
 (fn [db [_]]
   (let [species-id    (re-frame/subscribe [:selected-species-id])]
     (js/console.log "Returning filter!")
     (reaction (into {} (filter (comp some? val) (hash-map :speciesId @species-id)))))))

(re-frame/register-sub
 :selected-species
 (fn [db [_]]
   (let [species-id    (reaction (get-in @db [:selected-species-id]))
         species-list  (reaction (get-in @db [:species]))]
     (js/console.log "Returning selected species!")
     (reaction (find-first #(= (get % "_id") @species-id) @species-list)))))

(re-frame/register-sub
 :ui-species
 (fn [db [_]]
   (let [species-list  (re-frame/subscribe [:species])]
     (reaction (map #(hash-map :id (get % "_id"), :label (get % "commonName"), :key (get % "_id")) @species-list)))))

(defn handle-project-http-event [project-response]
  (re-frame/dispatch [:project-response (keywordize-keys (js->clj project-response))]))

(re-frame/register-sub
 :project-list
 (fn [db [_ type]]
   (let  [project-filter (re-frame/subscribe [:project-filter])
          query-token (GET "/api/extendedProjects" {:handler handle-project-http-event
                                 :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]
                                                    :params @project-filter})]
     (js/console.log "Returning project list!")
     (make-reaction
      (fn [] (get-in @db [:project-list]))
      :on-dispose #(do (re-frame/dispatch [:cleanup [:project-list]]))))))
