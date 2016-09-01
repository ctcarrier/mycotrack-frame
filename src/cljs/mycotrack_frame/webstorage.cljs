(ns mycotrack-frame.webstorage
  (:require [reagent.cookies :as cookie]))

(defn set-item!
  "Set `key' in browser's localStorage to `val`."
  [key val]
  (cookie/set! key val))

(defn get-item
  "Returns value of `key' from browser's localStorage."
  [key]
  (cookie/get key))

(defn remove-item!
  "Remove the browser's localStorage value for the given `key`"
  [key]
  (cookie/remove! key))
