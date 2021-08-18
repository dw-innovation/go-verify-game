(ns kid-game.components.verification-hub.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-game.components.timeline.core :as timeline]
            [kid-game.business :as business]))


(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:div "Verification Hub"]])

(defn <default-view> []
  [:div [:h1 "I am the default verification hub view"]])

(defn <investigate-post> [post]
  (let [activities (:activities post)]
    [:div "investigating: " (:id post)
     ;; [:div.hub-post
     ;;  [timeline/match-post post]]
     ;; a post might have activities in it or it might not,
     ;; for now, just flat list them out, verification hub to come
     (for [activity activities]
       [activities/get-activity activity])
     [:div ]]))


(defn <container> []
  (let [post (state/get-post ;; get it from the state again, because the verification hub post is a /copy/
              (:post @state/verification-hub-state))]
    [:div
     [<header>]
     (if post
       [<investigate-post> post]
       [<default-view>])]))
