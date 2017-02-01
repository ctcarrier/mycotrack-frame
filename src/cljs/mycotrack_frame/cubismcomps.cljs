(ns mycotrack-frame.cubismcomps
  (:require
   [re-com.core :as re-com]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [cljs.core :as core :refer [rand-int]]
   [cljs-time.core :as time :refer [date-time now]]
   [cljs-time.format :as format
    :refer [formatter formatters instant->map parse unparse]]
   [mycotrack-frame.httputils :refer [GET-SECURE]]))


(def d3 js/d3)
(def cubism js/cubism)
(def context (.size (.step (.context cubism 960) (* 10 60 1000)) 1440))
(def horizon (.height (.extent (.horizon context) (clj->js [50 100])) 100))
(def axis-format (.format d3 "%H:%M"))
(def axis (.tickFormat (.axis context) axis-format))
(def rule (.rule context))

(defn handle-sensor-http-event [callback sensor-event]
  (let [data (clj->js (map #(% "result") sensor-event))]
    (js/console.log data)
    (callback (clj->js nil) data)))

(defn dummy-data [start stop step callback]
  (let [range (clj->js (apply vector (map rand-int (range start stop))))]
    (js/console.log (str "Start: " (.toISOString start) " Stop: " (.toISOString stop)))
    (js/console.log range)
    (callback (clj->js nil) range)))

(defn time-as-seconds [date]
  (.getTime date))

(defn sensor-data [start stop step callback]
  (GET-SECURE (str "/api/sensorReadings?location=basement&metric=humidity&start=" (time-as-seconds start) "&end=" (time-as-seconds stop))
              {:handler (partial handle-sensor-http-event callback)})
  )

(defn mt-metric [name]
  (do
    (js/console.log "mt-metric")
   (.metric context sensor-data name)))

(defn bind-graph []
  (let [metrics (clj->js ["humidity"])]
    (js/console.log "Binding")
    (.metric horizon mt-metric)
    (-> d3
        (.select "#graph")
        (.selectAll ".axis")
        (.data (clj->js ["top" "bottom"]))
        (.enter)
        (.append "div")
        (.attr "class" #(str % " axis"))
        (.each #(this-as this (.call (.select d3 this) (.orient axis %)))))
    (-> d3
        (.select "#graph")
        (.append "div")
        (.attr "class" "rule")
        (.call rule))
    (-> d3
        (.select "#graph")
        (.selectAll ".horizon")
        (.data (clj->js metrics))
        (.enter)
        (.insert "div" ".bottom")
        (.attr "class" "horizon")
        (.call horizon))))

(defn stop-graph []
  (.stop context))

(defn cubism-graph
  []
  (reagent/create-class
   {:component-did-mount
    #(bind-graph)
    :component-will-unmount
    #(stop-graph)

    :display-name  "cubism-graph"

    :reagent-render
    (fn []
      (js/console.log "Giving graph")
      [:div#graph])}))
