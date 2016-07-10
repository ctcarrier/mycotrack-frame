(ns mycotrack-frame.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]
              [reagent.ratom :refer [make-reaction]]
              [ajax.core :refer [GET POST]]))

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
 (fn [db [_]]
   (reaction (get @db :selected-species-id nil))))

(re-frame/register-sub
 :project-filter
 (fn [db [_]]
   (let [species-id    (re-frame/subscribe [:selected-species-id])]
     (reaction (into {} (filter (comp some? val) (hash-map :speciesId @species-id)))))))

(re-frame/register-sub
 :selected-species
 (fn [db [_]]
   (let [species-id    (reaction (get-in @db [:selected-species-id]))
         species-list  (reaction (get-in @db [:species]))]
     (reaction (find-first #(= (get % "_id") @species-id) @species-list)))))

(re-frame/register-sub
 :ui-species
 (fn [db [_]]
   (let [species-list  (re-frame/subscribe [:species])]
     (reaction (map #(hash-map :id (get % "_id"), :label (get % "commonName"), :key (get % "_id")) @species-list)))))

(re-frame/register-sub
 :project-list
 (fn [db [_ type]]
   (let  [project-filter (re-frame/subscribe [:project-filter])]
     (make-reaction (fn [] ((js/console.log "Returning in the sub2")
                             (js/console.log "1")
                             (js/console.log project-filter)
                             (js/console.log "2")
                             (re-frame/dispatch [:update-project-list @project-filter])
                             (:project-list @db)))))))
