(ns kid-game.components.verification-hub.core
  (:require [kid-game.state :as state]
            [clojure.set :refer [union intersection]]
            [reagent.core                                         :as r]
            [kid-game.components.verification-hub.activities.shared.components :as components]
            [kid-game.components.shared.icons :as icons]
            [kid-game.utils.core :as utils :refer [in? find-first]]
            [kid-game.components.timeline.core :as timeline]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-shared.data.blocks :as blocks]))

(defn <header> []
  [:div {:class ["panel-header" "verification-hub-header"]}
   [:div.level {:style {:width "100%"}}
    [:div.level-left
     [:h5.level-item {:class "title is-5 has-text-white"} "Verification Hub"]]
    [:div.level-right
     [:span.level-item {:class    "icon is-left"
                        :style    {:cursor "pointer"}
                        :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))}
      [:i {:class "fa fa-times"}]]]]])

(defn <default-view> []
  [:div.container [:h4.title.is-4 "I am the default verification hub view"]])

(defn <investigate-post> [{post          :post
                           activity-type :activity-type
                           back!         :back!}]
  (let [activities (:activities post)
        activity   (find-first (fn [a] (= (:type a) activity-type)) activities)]
    [:div.columns
     [:div.column.is-12.mt-4
      [activities/get-activity activity back!]]]))

(defn <title> [] [:div {:class "columns is-centered mb-5"}
                  [:div {:class "column is-6  has-text-centered"}
                   [:h5.subtitle "Pick a verification action:"]]])

(defn <izzy> [] [:div.izzy.columns.is-centered.pt-6
                 [:div.column.is-3
                  [:div.izzy-icon
                   {:title "This is Izzy, a verification specialist – ready to help you analyze dubious content!"}
                   [icons/izzy]]]])

(defn <hub-icon> [{icon          :icon
                   title         :title
                   available?    :available?
                   modal-content :modal-content
                   tooltip       :tooltip
                   on-click!     :fn}]
  (let [[<icon> <modal>] (if modal-content
                           (components/<modal-icon> modal-content)
                           [components/<empty> components/<empty>])]
    ^{:key title}
    [:<>
     [:a.hub-icon {:on-click on-click!
                   :title    tooltip
                   :style    {:opacity (if available? 1 0.3)}}
      [icon]
      [:div.has-text-centered.has-text-black title]]
     [<modal>]
     [:div {:style {:margin-left "-.8rem"}}
      [<icon>]]]))

(defn <hub-icons> [[{icon       :icon
                     title      :title
                     available? :available?
                     on-click!  :fn} & rest :as actions]]
  [:div.has-text-centered
   [:div.icons.columns.mt-5
    (map <hub-icon> actions)]])

(defn <hub-home> [{post          :post
                   change-panel! :change-panel!}]
  (let [post-activities                        (:activities post)
        available-activities                   (map :type post-activities)
        in-game?                               (= :live (:game-state post))
        ;; given a list of activities, return only those available
        available                              (fn [& activity-types] (intersection (set activity-types)
                                                                                    (set available-activities)))
        available?                             (fn [& activity-types] (not (empty? (apply available activity-types))))
        choose-activity!                       (fn [& chosen-activity-typs]
                                                 (fn []
                                                   (if (not post)
                                                     (state/add-notification {:type :warning
                                                                              :text "Choose a post first"})
                                                     (if (not in-game?)
                                                       (state/add-notification {:type :warning
                                                                                :text "Out of time"})
                                                       (let [hits (apply available chosen-activity-typs)]
                                                         (if (empty? hits)
                                                           (state/add-notification {:type :warning
                                                                                    :text "Choose a different activity"})
                                                           (change-panel! (first hits))))))))
        points                                 @state/points
        {blocked-correctly  :blocked-correctly
         misleading-reposts :misleading-reposts
         missed-deadlines   :missed-deadlines} @state/stats
        actions                                [{:icon          icons/browser-search
                                                 :title         "Web Search"
                                                 :tooltip       "Perform a keyword search on the web, using Boolean operators."                                                 
                                                 :modal-content blocks/web-search-explanation
                                                 :available?    (available? :web-search)
                                                 :fn            (choose-activity! :web-search)}
                                                {:icon          icons/recycle-search
                                                 :title         "Reverse image search"
                                                 :tooltip       "Search the web for copies of this image with a reverse image search"
                                                 :modal-content blocks/ris-explanation
                                                 :available?    (available? :reverse-image-crop :reverse-image-simple :reverse-image-flip)
                                                 :fn            (choose-activity! :reverse-image-crop :reverse-image-simple :reverse-image-flip)}
                                                {:icon          icons/image-analysis
                                                 :title         "Image analysis"
                                                 :tooltip       "Has this image been altered? Take a closer look to find potential clues."
                                                 :modal-content blocks/image-analysis-explanation
                                                 :available?    (available? :polygon-search)
                                                 :fn            (choose-activity! :polygon-search)}
                                                {:icon       icons/geolocation
                                                 :title      "Geolocation"
                                                 :available? (available? :geolocation)
                                                 :fn         (choose-activity! :geolocation)}]]
    [:div
     [:div.contain-section-width.center-section
      [<izzy>]
      [<title>]
      [:hr]
      [<hub-icons> actions]]

     [:div.scores
      [:div.contain-section-width.center-section
       [:div {:class "tile is-parent is-vertical"}
        [:h5 {:class "title has-text-white"} "Your score:"]
        [:p {:class "subtitle has-text-white"} [:b (.toLocaleString points)] " points"]]]]

     [:div.stats.columns.is-centered
      [:div.stat.column.is-2.has-text-centered
       [icons/award]
       [:div [:b blocked-correctly] " Blocked nonsense"]]
      [:div.stat.column.is-2.has-text-centered
       [icons/stop-sign]
       [:div [:b misleading-reposts] " Shared nonsense"]]
      [:div.stat.column.is-2.has-text-centered
       [icons/hourglass]
       [:div [:b missed-deadlines] " Missed deadlines"]]]]))

(defn <container> []
  (let [active-panel  (r/atom :hub) ; or activity type such as :reverse-image-crop
        change-panel! (fn [activity-type] (reset! active-panel activity-type))
        back-to-hub!  (fn [] (change-panel! :hub))]
    ;; listen to the state... not the best way to handle it but very cool that you can!
    (add-watch state/verification-hub-state :hub-component-watcher
               ;; do something when the selected post changes!!
               (fn [key atom old-state new-state]
                 (when (not (= (-> old-state :post :id)
                               (-> new-state :post :id)))
                   ;; go back to the hub if you switch posts
                   (change-panel! :hub))))
    (fn []
      (let [post       (state/get-post (:post @state/verification-hub-state))
            time-left? (> (:time-left post) 0)
            in-game?   (= :live (:game-state post))
            <home>     [<hub-home> {:post          (dissoc post :time-left) ; dissoc to not trigger a rerender every time it changes
                                    :change-panel! change-panel!}]]
        [:div
         [<header>]

         (when @state/dev?
           [:div.tags
            [:div {:class "tag is-light is-info is-family-monospace"} "investigating: " (:id post)]
            [:div {:class "tag is-light is-info is-family-monospace"} "panel: " @active-panel]
            [:div {:class "tag is-light is-info is-family-monospace"} "time-left: " (:time-left post)]
            [:div {:class "tag is-light is-info is-family-monospace"} "post is: " (:game-state post)]])

         [:div.m-3.hub-progress
          {:title "Quick! Verify this post – then decide whether to share or block it, before it goes viral!"}
          [timeline/<post-progress> post]]

         [timeline/<post-overlay> post]

         [:div.hub-container
          (cond
            (not time-left?)       components/<empty>
            (not in-game?)         <home> 
            (= @active-panel :hub) <home> 
            :else                  [<investigate-post> {:post          post
                                                        :activity-type @active-panel
                                                        :back!         back-to-hub!}])]]))))
