(ns kid-game.dev-cards
  (:require [kid-shared.posts.posts            :as posts-data]
            [kid-game.components.notifications :as notifications]
            [kid-game.components.timeline.core :as timeline]
            [reagent.core :as r]
            [cljs.core.async                   :as async :include-macros true]))

(defn random-points [] (rand-int 3000))

(defn <notifications> []
  [:div.columns
   [:div.column.is-one-third
    [notifications/<notification>
     {:active true
      :type :success
      :text (str "+" (random-points) " points")}]]
   [:div.column.is-one-third
    [notifications/<notification>
     {:active true
      :type :warning
      :text (str "-" (random-points) " points")}]]
   [:div.column.is-one-third
    [notifications/<notification>
     {:active true
      :text "testing text"}]]])

(defn single-simple-post []
  (let [post (assoc posts-data/p1-climate-refugees-copenhagen
                    :game-state :live)]
    [timeline/<post> post]))
(defn simple-repost [] (timeline/<post> posts-data/p1-climate-refugees-copenhagen-response))

(defn post-buttons []
  (let [post posts-data/p1-climate-refugees-copenhagen]
    (timeline/<post-actions> post)))

(defn <overlays> []
  [:div.is-flex-direction-column
   [:p "The user didn't pick an action in time, it's too late:"]
   [:div (timeline/<post-overlay> {:game-state :timed-out})]
   [:p.mt-4 "The user shared fake news:"]
   [:div (timeline/<post-overlay> {:game-state :shared :fake-news? true})]
   [:p.mt-4 "The user shared genuine news content from a legit source:"]
   [:div (timeline/<post-overlay> {:game-state :shared :fake-news? false})]
   [:p.mt-4 "The user blocked a legit source of news:"]
   [:div (timeline/<post-overlay> {:game-state :blocked :fake-news? false})]
   [:p.mt-4 "The user blocked fake news:"]
   [:div (timeline/<post-overlay> {:game-state :blocked :fake-news? true})]])

(defn attach-post-timer [post-atom]
  (async/go-loop []
    (let [time-left (or (@post-atom :time-left) 4000)
          time-limit (or (@post-atom :time-limit) 5000)]
      (async/<! (async/timeout 100))
      (reset! post-atom (assoc @post-atom :game-state :live))
      (reset! post-atom
              (assoc @post-atom
                     :time-left (let [new-time-left (- time-left 1)]
                                  (if (< new-time-left 0)
                                    time-limit
                                    new-time-left))))
      (recur))))


(defn progress-bar [status]
  (let [post (r/atom posts-data/p1-climate-refugees-copenhagen)
        time (if (= status :live) (rand-int 4000) 0)
        exit-channel (attach-post-timer post)]
    (fn []
       [timeline/<post-progress> @post])))

(defn <progress-bars> []
  [:div
   [:p "Decreasing.:"]
   [progress-bar :live]
   [:p.mt-4 "Timed out:"]
   (progress-bar :timed-out)])

(defn <main-view> []
  [:div.container.game-container
   [:div {:class "columns"}
    [:div {:class "column is-full"}
     [:hr]
     [:h2.title.is-2 "Notifications"]
     [<notifications>]
     [:hr]
     [:h2.title.is-2 "Timeline content"]
     [:p "These can be " [:b "simple posts,"] "or " [:b "more complex reposts."]]
     [single-simple-post]
     [simple-repost]
     [:hr]
     [:h4.title.is-4 "Buttons"]
     [post-buttons]
     [:hr]
     [:h4.title.is-4 "Inline results"]
     [<overlays>]
     [:hr]
     [:h4.title.is-4 "Progress bar"]
     [<progress-bars>]]]])








