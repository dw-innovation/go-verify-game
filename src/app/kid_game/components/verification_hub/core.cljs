(ns kid-game.components.verification-hub.core
  (:require [kid-game.state :as state]
            [clojure.set :refer [union intersection]]
            [reagent.core                                         :as r]
            [kid-game.components.shared.icons :as icons]
            [kid-game.utils.core :as utils :refer [in? find-first]]
            [kid-game.components.verification-hub.activities.core :as activities]))

(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:h5 {:class "title is-5 is-white"} "Verification Hub"]])

(defn <default-view> []
  [:div.container [:h4.title.is-4 "I am the default verification hub view"]])

(defn <investigate-post> [{post :post
                           activity-type :activity-type
                           back! :back!}]
  (let [activities (:activities post)
        activity (find-first (fn [a] (= (:type a) activity-type)) activities)]
    [:div.columns.is-centered
     [:div.column.is-8.mt-4
      [activities/get-activity activity back!]]]))

(defn <title> [] [:div {:class "columns is-centered mb-5"}
                  [:div {:class "column is-6  has-text-centered"}
                   [:h3.title.is-4 "This is Thomas"]
                   [:h5.subtitle "He's your verification compagnon, a kind of personal tutor. Oh, he's also a duck."]]])

(defn <thomas> [] [:div.thomas.columns.is-centered
                   [:div.column.is-3
                    [:div.thomas-icon
                     [icons/thomas]]]])

(defn <hub-icon> [{icon :icon
                   title :title
                   on-click! :fn}]
  [:a.hub-icon {:on-click on-click!}
   [icon]
   [:div.has-text-centered.has-text-black title]])

(defn <hub-icons> [[{icon :icon
                     title :title
                     on-click! :fn} & rest :as actions]]
  [:div.has-text-centered
   [:h4.title.is-4 "Thomas recommends you try one of the following"]
   [:p.subtitle "Click on one of the options to start the corresponding activity"]
   [:div.icons.columns.mt-5
    (map <hub-icon> actions)]])

(defn <hub-home> [{post :post
                   change-panel! :change-panel!}]
  (let [post-activities (:activities post)
        available-activities (map :type post-activities)
        choose-activity! (fn [& chosen-activity-typs]
                           (fn []
                             (if (not post)
                               (state/add-notification {:type :warning
                                                        :text "Choose a post first"})
                               (let [hits (intersection (set chosen-activity-typs)
                                                        (set available-activities))]
                                 (if (empty? hits)
                                   (state/add-notification {:type :warning
                                                            :text "Choose a different activity"})
                                   (change-panel! (first hits)))))))
        points @state/points
        {blocked-correctly :blocked-correctly
         misleading-reposts :misleading-reposts
         missed-deadlines :missed-deadlines} @state/stats
        actions [{:icon icons/browser-search
                  :title "Web Search"
                  :fn (choose-activity! :web-search)}
                 {:icon icons/recycle-search
                  :title "Reverse image search"
                  :fn (choose-activity! :reverse-image-crop)}
                 {:icon icons/image-analysis
                  :title "Image analysis"
                  :fn (choose-activity! :polygon-search)}
                 {:icon icons/geolocation
                  :title "Geolocation"
                  :fn (choose-activity! :geolocation)}]]
    [:div
     [<thomas>]
     [<title>]
     [:hr]
     [<hub-icons> actions]

     [:div.scores
      [:div {:class "tile is-parent is-vertical"}
       [:h5 {:class "title has-text-white"} "Your score:"]
       [:p {:class "subtitle has-text-white"} [:b (.toLocaleString points)] " points"]]]

     [:div.stats.columns.is-centered
      [:div.stat.column.is-2.has-text-centered
       [icons/award]
       [:div [:b blocked-correctly] " Blocked correctly"]]
      [:div.stat.column.is-2.has-text-centered
       [icons/stop-sign]
       [:div [:b misleading-reposts] " Misleading reposts"]]
      [:div.stat.column.is-2.has-text-centered
       [icons/hourglass]
       [:div [:b missed-deadlines] " Missed deadlines"]]]]))

(defn <container> []
  (let [active-panel (r/atom :hub) ; or activity type such as :reverse-image-crop
        change-panel! (fn [activity-type] (reset! active-panel activity-type))
        back-to-hub! (fn [] (change-panel! :hub))]
    (fn []
      (let [post (state/get-post (:post @state/verification-hub-state))
            points @state/points]
        [:div
         [<header>]
         [:div.hub-container
          (case @active-panel
            :hub [<hub-home> {:post post :change-panel! change-panel!}]
            [<investigate-post> {:post post
                                 :activity-type @active-panel
                                 :back! back-to-hub!}])]]))))
