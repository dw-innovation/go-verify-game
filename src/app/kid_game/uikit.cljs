(ns kid-game.uikit
  (:require [kid-shared.data.posts                 :as posts-data]
            [kid-shared.data.activities            :as activities-data]
            [kid-game.components.notifications     :as notifications]
            [kid-game.components.timeline.core     :as timeline]
            [kid-game.components.shared.icons      :as icons]
            [kid-game.components.modal             :as modal]
            [kid-game.components.verification-hub.activities.websearch :as websearch]
            [reagent.core                          :as r]
            [cljs.core.async                       :as async :include-macros true]))

(defn random-points [] (.toLocaleString (rand-int 3000)))

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

(defn <real-copy-notifications>
  "Actual copy for notifications from
   src/app/kid_game/business.cljs"
  []
  (let [copy #{{:type :success :copy "You blocked nonsense content, you won "}
               {:type :warning :copy "You blocked legit content, you lost "}
               {:type :success :copy "You shared legit content, you won "}
               {:type :warning :copy "You shared nonsense content, you lost "}}]
    [:div.columns
     (map (fn [e] [:div.column.is-one-fourth
                   [notifications/<notification>
                    {:active true
                     :type (:type e)
                     :text (str (:copy e) (random-points) " points")}]])
          copy)]))

(defn single-simple-post []
  (let [post (assoc posts-data/p1-climate-refugees-copenhagen?
                    :game-state :live)]
    [timeline/<post> post]))
(defn simple-repost [] (timeline/<post> posts-data/p1-climate-refugees-copenhagen-response))

(defn post-buttons []
  (let [post posts-data/p1-climate-refugees-copenhagen?]
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


(defn progress-bar []
  (let [post (r/atom posts-data/p1-climate-refugees-copenhagen?)
        exit-channel (attach-post-timer post)]    ;; which ends up as unused binding
    (fn []
      [timeline/<post-progress> @post])))

(defn <progress-bars> []
  [:div
   [:p "Decreasing.:"]
   [progress-bar :live]
   [:p.mt-4 "Timed out:"]
   (progress-bar)])

(defn <icons> []
  [:div
   [:p [:b "Logos, etc."]]
   [:div.level-left
    [:div {:style {:width "100px" :float "left"}}
     [icons/blooble-logo]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/thomas]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/thomas-with-speech!?-bubble]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/image-analysis]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/geolocation]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/recycle-search]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/award]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/stop-sign]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/browser-search]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/hourglass]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/search]]]

   [:p [:b "UI (outline)"]]
   [:div.level-left
    [:div {:style {:width "100px" :float "left"}}
     [icons/info-circle]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/circle-right-arrow-blue]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/crop-circle]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/close-unfilled]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/clock]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/check-unfilled]]]

   [:p.mt-4 [:b "UI (filled)"]]
   [:div.level-left
    [:div {:style {:width "100px" :float "left"}}
     [icons/close-filled]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/check-filled]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/right-arrow-filled]]]

   [:p.mt-4 [:b "UI (clear)"]]
   [:div.level-left
    [:div {:style {:width "100px" :float "left"}}
     [icons/x]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/checkmark]]
    [:div {:style {:width "100px" :float "left"}}
     [icons/right-arrow]]]])

(defn modal-content []
  [:div [:div {:style {:width "100px" :float "center"}}
         [icons/thomas]]
   [:h4.title.is-4 "What's all this then?"]
   [:p [:b "I am but a modal window and don't do much."] " I support all kinds of components passed into me."]
   [:p.mt-2 "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut 
        labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco 
        laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in 
        voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat 
        non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."]])

(defn <main-view> []
  [:div.container.game-container
   [modal/<modal> modal-content]
   [:div {:class "columns"}
    [:div {:class "column is-full"}
     [:hr]
     [:h2.title.is-2 "Notifications"]
     [<notifications>]
     [:h4.title.is-4 "Actual business copy:"]
     [<real-copy-notifications>]
     [:hr]
     [:h2.title.is-2 "Timeline content"]
     [:p "These can be " [:b "simple posts,"] "or " [:b "more complex reposts."]]
     [single-simple-post]
     [simple-repost]
     [:hr]
     [:h4.title.is-4 "Buttons"]
     [post-buttons]
     [:hr]
    ;;  [:h4.title.is-4 "Inline results"]
    ;;  [<overlays>]
    ;;  [:hr]
     [:h4.title.is-4 "Progress bar"]
     [<progress-bars>]
     [:hr]
     [:h4.title.is-4 "Icons"]
     [:div {:style {:overflow "hidden"}}
      [<icons>]]
     [:hr]
     [:h4.title.is-4 "Blooble Simulation"]
     [websearch/<search-simulation>
      "Here are my terms"
      (-> activities-data/financiel-web-search :data :results)
      false
      nil]
     [:hr]
     [:h4.title.is-4 "Modal"]
     [:p "Click the button to launch the modal"]
     [:button {:class "button outline" :on-click #(modal/toggle-modal)}
      [:span.icon [:i {:class "fas fa-window-restore"}]] [:span "Open modal"]]
     [:hr.mt-5]]]])
