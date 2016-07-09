(ns mycotrack-frame.subs
    (:require-macros [reagent.ratom :refer [reaction]])
    (:require [re-frame.core :as re-frame]))

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

(re-frame/register-sub
 :species
 (fn [db _]
   (reaction (:species @db))))

(re-frame/register-sub
 :selected-species-id
 (fn [db _]
   (reaction (:selected-species-id @db))))

(re-frame/register-sub
 :selected-species
 (fn [db [_]]
   (let [species-id    (reaction (get-in @db [:selected-species-id]))
         species-list  (reaction (get-in @db [:species]))]
     (reaction (find-first #(= (get % "_id") @species-id) @species-list)))))

(re-frame/register-sub
 :ui-species
 (fn [db [_]]
   (let [species-list  (reaction (get-in @db [:species]))]
     (reaction (map #(hash-map :id (get % "_id"), :label (get % "commonName"), :key (get % "_id")) @species-list)))))


(re-frame/register-sub
 :project-list
 (fn [db [_]]
   (reaction (:project-list @db))))
