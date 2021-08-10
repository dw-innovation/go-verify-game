(ns kid-game.components.verification-hub.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-game.business :as business]))


(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:div "Verification Hub"]])

(defn <default-view> []
  [:div [:h1 "I am the default verification hub view"]])

(defn <investigate-post> [post]
  (let [activities (:activities post)
        first-activity (first activities)]
    (println "p[][[[[[[[[[[[[[[]]]]]]]]]]]]]]")
    (println activities)
    [:div "investigating: " (:id post)
     (when first-activity (let [component (:component first-activity)
                                data (:data first-activity)
                                ]
                            (println "yyyyyyyyyyyyy")
                            (println data)
                            ))
     [:div ]]))


(defn <container> []
  (let [post (:post @state/verification-hub-state)]
    [:div
     [<header>]
     (if post
       [<investigate-post> post]
       [<default-view>])]))
