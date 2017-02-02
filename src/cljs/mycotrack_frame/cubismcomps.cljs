(ns mycotrack-frame.cubismcomps
  (:require
   [re-com.core :as re-com]
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [cljs.core :as core :refer [rand-int]]
   [cljs-time.core :as time :refer [date-time now]]
   [cljs-time.format :as format
    :refer [formatter formatters instant->map parse unparse]]
   [mycotrack-frame.httputils :refer [GET-SECURE]]
   [goog.string :as gstring]
   [goog.string.format]))


(def d3 js/d3)
(def cubism js/cubism)
(def context (.size (.step (.context cubism 960) (* 10 60 1000)) 1240))
(def horizon (.height (.horizon context) 100))
(def axis-format (.format d3 "%H:%M"))
(def axis (.ticks (.axis context) 12))
(def rule (.rule context))

(defn temp-to-fahrenheit [celcius] (gstring/format "%d" (+ (* celcius 1.8) 32)))

(defn handle-sensor-http-event [callback convert-temp sensor-event]
  (let [data (clj->js (if (true? convert-temp)
                        (map #(temp-to-fahrenheit (% "result")) sensor-event)
                        (map #(% "result") sensor-event)))]
    (js/console.log data)
    (callback (clj->js nil) data)))

(defn dummy-data [start stop step callback]
  (let [range (clj->js (apply vector (map rand-int (range start stop))))]
    (js/console.log (str "Start: " (.toISOString start) " Stop: " (.toISOString stop)))
    (js/console.log range)
    (callback (clj->js nil) range)))

(defn time-as-seconds [date]
  (.getTime date))

(defn sensor-data [location metric convert-temp start stop step callback]
  (GET-SECURE (str "/api/sensorReadings?location=" location "&metric=" metric "&start=" (time-as-seconds start) "&end=" (time-as-seconds stop))
              {:handler (partial handle-sensor-http-event callback convert-temp)}))

(def metric-to-data-source {"basement humidity" (partial sensor-data "basement" "humidity" false) "basement temperature" (partial sensor-data "basement" "temperature" true) "library temperature" (partial sensor-data "library" "temperature" true) "library lux" (partial sensor-data "library" "lux" false)})

(defn mt-metric [name]
  (do
    (js/console.log "mt-metric")
    (.metric context (get metric-to-data-source name) name)))

(defn bind-graph []
  (let [metrics (clj->js ["basement humidity", "basement temperature", "library temperature", "library lux"])]
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
