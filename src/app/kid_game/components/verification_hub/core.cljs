(ns kid-game.components.verification-hub.core
  (:require [kid-game.state :as state]
            [kid-game.components.verification-hub.activities.core :as activities]))

(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:h5 {:class "title is-5 is-white"} "Verification Hub"]])

(defn <default-view> []
  [:div.container [:h4.title.is-4 "I am the default verification hub view"]])

(defn <investigate-post> [post]
  (let [activities (:activities post)]
    [:div.container
     [:div.columns.is-centered
      [:div.column.is-8.mt-4
       [:div {:class "notification is-info is-light"}
        [:b "Currently investigating: "] "post ID #" (:id post)]]]
     ;; a post might have activities in it or it might not,
     ;; for now, just flat list them out, verification hub to come
     (for [activity activities]
       [activities/get-activity activity])]))


(defn <container> []
  (let [post (state/get-post ;; get it from the state again, because the verification hub post is a /copy/
              (:post @state/verification-hub-state))]
    [:div
     [<header>]
     (if post
       [<investigate-post> post]
       [<default-view>])]))
