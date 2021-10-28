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
                                   (change-panel! (first hits)))))))]
      [:div.is-flex.is-justify-content-space-evenly
       [:div.hub-icon.column
        {:on-click (choose-activity! :web-search)}
        [icons/browser-search]
        [:p "Web Search"]]
       [:div.hub-icon.column
        {:on-click (choose-activity! :reverse-image-simple :reverse-image-crop)}
        [icons/recycle-search]
        [:p "Reverse Image Search"]]
      [:div.hub-icon.column
        {:on-click (choose-activity! :polygon-search)}
        [icons/image-analysis]
       [:p "Image Analysis"]]
      [:div.hub-icon.column
        {:on-click (choose-activity! :geolocation)}
        [icons/geolocation]
       [:p "Geolocation"]] ]))

(defn <title> [] [:div.activity-hub-title.has-centered-text.is-centered.columns
                  [:div.column.is-centered
                   [:h3.title.is-3 "Verification Hub"]]])


(defn <container> []
  (let [active-panel (r/atom :hub) ; or activity type such as :reverse-image-crop
        change-panel! (fn [activity-type] (reset! active-panel activity-type))
        back-to-hub! (fn [] (change-panel! :hub))]
    (fn []
      (let [post (state/get-post (:post @state/verification-hub-state))
            points @state/points]
        [:div
         [<header>]

            [:div {:class "notification is-info is-light"}
             [:b "Currently investigating: "] "post ID #" (:id post)]

         [<title>]
         [:div.thomas]
         [:div.container
            (case @active-panel
              :hub [<hub-home> {:post post :change-panel! change-panel!}]
              [<investigate-post> {:post post
                                   :activity-type @active-panel
                                   :back! back-to-hub!}])
            [:div.scores
             "Score: " points " points"]
            [:div.stats.columns
             [:div.stat.column
              [icons/award]
              [:div (@state/stats :blocked-correctly) " Blocked Correctly"]]
             [:div.stat.column
              [icons/stop-sign]
              [:div (@state/stats :misleading-reposts) " Misleading Reposts"]]
             [:div.stat.column
              [icons/hourglass]
              [:div (@state/stats :missed-deadlines) " Missed Deadlines"]]]]]))))
