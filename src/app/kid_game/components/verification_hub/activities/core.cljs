(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.business :as business]))

;; this is the core for all of the activities,
;; and includes the mappings so that data can also
;;   call the component it is expecting


(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello"]])

(defn <reverse-image-simple> [{:as data
                               result-images :result-images
                               main-image :main-image}]
  [:div.activity-container
   [:h3 "Simple Reverse Image Search"]
   [image-results/<dragger> main-image]
   [image-results/<results> result-images]
   ])

(defn <not-found> [component-name]
  [:div
   [:h1 "The activity was not found:" component-name]])

(defn get-activity [{:as activity typ :type data :data}]
  (case typ
    :test [<test> data]
    :reverse-image-simple [<reverse-image-simple> data]
    (do (log/warn "could not find activity" typ)
        [<not-found> typ])))
