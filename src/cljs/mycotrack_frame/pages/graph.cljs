(ns mycotrack-frame.pages.graph
  (:require [re-com.core :as re-com]
            [mycotrack-frame.cubismcomps :refer [cubism-graph]]
            [mycotrack-frame.pages.retire-project :refer [retire-button reactivate-button]]
            [mycotrack-frame.uicomps :refer [iso-formatter date-formatter loading-comp]]
            [re-frame.core :as re-frame]
            [cljs-time.format :as format
             :refer [formatter formatters instant->map parse unparse]]))

(defn graph-panel []
  [:div#yoyoyo.col-xs-12
   [:div.row
    [:div.col-xs-12
     [cubism-graph]]]])
