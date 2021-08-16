(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.business :as business]))

;; this is the core for all of the activities,
;; and includes the mappings so that data can also
;;   call the component it is expecting


(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello" arg]])

(defn <not-found> []
  [:div
   [:h1 "The activity was not found"]])

(defn get-activity [component-name]
  (case component-name
    :test <test>
    (do (log/warn "could not find activity" component-name)
        <not-found>)))
