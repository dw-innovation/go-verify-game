(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.components.verification-hub.activities.ris-simple :as ris-simple]
            [kid-game.components.verification-hub.activities.websearch :as websearch :refer [<web-search>]]
            [kid-game.business :as business]))

;; this is the core for all of the activities,
;; and includes the mappings so that data can also
;;   call the component it is expecting

(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello"]])

(defn <not-found> [component-name]
  [:div
   [:h1 "The activity was not found:" component-name]])

(defn match [{:as activity typ :type data :data}]
  (case typ
    :test [<test>]
    :reverse-image-simple [ris-simple/<reverse-image-simple> data]
    :web-search [<web-search> data]
    [<not-found> typ]))


(defn get-activity [{:as activity id :id typ :type data :data}]
  [match activity])
