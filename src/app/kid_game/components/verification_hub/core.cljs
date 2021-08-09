(ns kid-game.components.verification-hub.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.business :as business]))


(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:div "Verification Hub"]])

(defn <container> []
  [:div
   [<header>]
   [:h1 "I am the verification hub"]])
