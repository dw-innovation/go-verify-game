(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.components.verification-hub.activities.ris-simple :as ris-simple]
            [kid-game.components.verification-hub.activities.ris-crop :as ris-crop]
            [kid-game.components.verification-hub.activities.websearch :as websearch]
            [kid-game.components.verification-hub.activities.polygon-search :as polygon-search]
            [kid-game.business :as business]))

;; this is the core for all of the activities,
;; and includes the mappings so that data can also
;;   call the component it is expecting

(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello"]])

(defn <not-found> [component-name]
  [:div
   [:h1 "The activity type was not found:" component-name]])

(defn match [{:as activity typ :type data :data} back!]
  (case typ
    :test [<test>]
    :reverse-image-simple [ris-simple/<main> data back!]
    :reverse-image-crop [ris-crop/<main> data back!]
    :web-search [websearch/<main> data back!]
    :polygon-search [polygon-search/<main> data back!]
    ; none of the above matched:
    [<not-found> typ]))


(defn get-activity [{:as activity id :id typ :type data :data} back!]
  [match activity back!])
