(ns mycotrack-frame.db
  (:require [mycotrack-frame.webstorage :as webstorage]))

(def default-db
  {:name "Mycotrack"
   :auth-token (webstorage/get-item :auth-token)})
