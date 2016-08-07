(ns mycotrack-frame.server
  (:require [mycotrack-frame.handler :refer [handler]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))

 (defn -main [& args]
   (let [port 3449]
     (run-jetty handler {:port port :join? false})))
