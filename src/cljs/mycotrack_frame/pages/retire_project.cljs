(ns mycotrack-frame.pages.retire-project
  (:require [re-com.core :as re-com]
            [re-frame.core :as re-frame]
            [reagent.core    :as    reagent]
            [mycotrack-frame.uicomps :refer [iso-formatter date-formatter]]
            [cljs-time.core :as time :refer [date-time now]]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse
                     formatter-local
                     parse-local parse-local-date
                     unparse-local unparse-local-date
                     with-default-year]]))

(defn retire-button [selected-project]
  (js/console.log (str "Got id: " (:_id selected-project)))
  [:button.btn.btn-danger.input-lg.col-xs-12.col-md-3
   {:on-click (fn []
                (re-frame/dispatch
                      [:retire-project
                       (assoc selected-project
                              :countSubstrateUsed (:count selected-project)
                              :count (:count selected-project)
                              :container (-> selected-project :container :_id)
                              :enabled false
                              :substrate (-> selected-project :substrate :_id)
                              :locationId (-> selected-project :location :_id)
                              :createdDate (unparse iso-formatter (now)))])
                )
    } "Retire >"])

(defn reactivate-button [selected-project]
  (js/console.log (str "Got id: " (:_id selected-project)))
  [:button.btn.btn-danger.input-lg.col-xs-12.col-md-3
   {:on-click #(re-frame/dispatch
                [:retire-project
                 (assoc selected-project
                        :countSubstrateUsed (:count selected-project)
                        :count (:count selected-project)
                        :container (-> selected-project :container :_id)
                        :enabled true
                        :substrate (-> selected-project :substrate :_id)
                        :locationId (-> selected-project :location :_id)
                        :createdDate (unparse iso-formatter (now)))])} "Reactivate >"])
