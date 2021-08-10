(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.business :as business]))


(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello" arg]])
