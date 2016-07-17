(ns mycotrack-frame.pages.about
  (:require [re-com.core :as re-com]
            [mycotrack-frame.links :refer [link-to-home-page]]))

(defn about-title []
  [re-com/title
   :label "This is the About Page."
   :level :level1])

(defn about-panel []
  [re-com/v-box
   :gap "1em"
   :children [[about-title] [link-to-home-page]]])
