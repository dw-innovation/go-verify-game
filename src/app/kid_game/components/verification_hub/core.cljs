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
  (let [activities (:activities post)]
    [:div "investigating: " (:id post)
     ;; a post might have activities in it or it might not,
     ;; for now, just flat list them out, verification hub to come
     (for [activity activities]
       (let [dynamic-component (-> activity :component activities/get-activity)
             data (:data activity)]
         ;; tie the data of the post-activity to the mentioned component
         [dynamic-component data]))
     [:div ]]))


(defn <container> []
  (let [post (:post @state/verification-hub-state)]
    [:div
     [<header>]
     (if post
       [<investigate-post> post]
       [<default-view>])]))
