(ns mycotrack-frame.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]
              [reagent.ratom :refer [make-reaction]]
              [ajax.core :refer [GET POST]]
              [clojure.walk :refer [keywordize-keys]]))

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
 :cultures
 (fn [db [_ type]]
   (let  [query-token (GET "/api/cultures" {
                                            :handler (fn [cultures-response] (re-frame/dispatch [:cultures-response (js->clj cultures-response)]))
                                            :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]})]
     (make-reaction
      (fn [] (get-in @db [:cultures]))
      :on-dispose #(do (re-frame/dispatch [:cleanup [:cultures]]))))))

(re-frame/register-sub
 :selected-species-id
 (fn [db [_]]
   (reaction (get @db :selected-species-id))))

(re-frame/register-sub
 :selected-culture-id
 (fn [db [_]]
   (reaction (get @db :selected-culture-id))))

(re-frame/register-sub
 :project-filter
 (fn [db [_]]
   (let [culture-id    (re-frame/subscribe [:selected-culture-id])]
     (reaction (into {} (filter (comp some? val) (hash-map :cultureId @culture-id)))))))

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
 :ui-cultures
 (fn [db [_]]
   (let [culture-list  (re-frame/subscribe [:cultures])]
     (reaction (map #(hash-map :id (get % "_id"), :label (get % "name"), :key (get % "_id")) @culture-list)))))

(re-frame/register-sub
 :sub-dynamic
 (fn sub-dynamic [_ _ [project-filter]]
   (js/console.log "Calling")
   (let [q (do
             (GET "/api/extendedProjects" {:handler (fn [project-response] (re-frame/dispatch [:project-response (map #(into {:key (:_id %)} %) (keywordize-keys (js->clj project-response)))]))
                                           :headers [:Authorization "Basic dGVzdEBteWNvdHJhY2suY29tOnRlc3Q="]
                                           :params project-filter}))]
     (reaction q))))

(re-frame/register-sub
 :project-list
 (fn [db [_]]
   (let  [project-filter (re-frame/subscribe [:project-filter])
          query-token (re-frame/subscribe [:sub-dynamic] [project-filter])]
     (make-reaction (fn []
                      (str @query-token)
                      (js/console.log "Return project list")
                      (:project-list @db))))))
