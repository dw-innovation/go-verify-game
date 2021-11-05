(ns kid-game.components.timeline.core
  (:require [cljs.js                  :refer [empty-state eval js-eval]]
            [cljs.core.async          :as async :include-macros true]
            [reagent.core             :as r]
            [kid-game.state           :as state]
            [kid-game.business        :as business]
            [kid-game.utils.log       :as log]
            [kid-shared.types.post    :as posts]
            [kid-shared.types.comment :as comment]
            [kid-game.utils.core      :refer [timestamp-now new-uuid]]
            [react-transition-group]
            [lodash]
            [moment]))


;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))

(declare <post>)

(defn <progress> [amnt total]
  (let [percent (* 100 (/ amnt total))]
    [:progress {:class "progress is-primary is-small"
                :value percent
                :max 100}]))

(defn <post-progress> [{time-left :time-left
                        time-limit :time-limit}]
  [<progress> time-left time-limit])

(defn <debug-tags> [p]
  [:div.tags
   [:div {:class "tag is-light is-info is-family-monospace"} (:id p)]
   [:div {:class "tag is-light is-info is-family-monospace"} (:game-state p)]])

(defn <author-image> [{:as author
                       image :image
                       name :name}]
  [:div.author-image (when image [:img {:src image
                                        :alt (str name " profile image")}])])

(defn <author-name> [{:as author name :name
                      handle :handle}]
  [:div.level.mb-1
   [:div.level-left
    [:div.level-item [:h6 {:class "title is-6 m-0"}     name]]
    [:div.level-item [:p  {:class "has-text-grey ml-0"} handle]]]])

(defn <post-text> [copy]
  [:div.block.mb-3
   [:p copy]])

(defn <post-media> [img]
  [:div.block
   [:figure {:class "image is-16by9"}
    [:img {:class "post-media" :src img}]]])

(defn <post-actions> [p]
  (let [investigate! (fn [] (business/post-investigate! p))
        block! (fn [] (business/post-block! p))
        share! (fn [] (business/post-share! :comment "comment about post"
                                            :post p))]
    [:div.buttons
     [:button {:class "button outline" :on-click share!}
      [:span.icon [:i {:class "fas fa-share"}]] [:span "share"]]
     [:button {:class "button outline" :on-click block!}
      [:span.icon [:i {:class "fas fa-ban"}]] [:span "block"]]
     [:button {:class "button outline" :on-click (fn [ev] ;; stop propagation because there is a global
                                        ;; click to open panel, and we are specifically opening the other one
                                                   (.stopPropagation ev)
                                                   (investigate!))}
      [:span.icon [:i {:class "fas fa-search"}]] [:span "investigate"]]]))

(defn <action-info-content> [result copy points-result]
  (let [icon (case result :won  "fa-check"
                   :lost        "fa-ban"
                   :timeout     "fa-clock-o"
                   nil)
        color (case result :won "is-success"
                    :lost       "is-danger"
                    :timeout    "is-info"
                    nil)
        lost? (case result :lost true
                    :won false
                    nil)]
    [:div {:class (str "notification my-5 is-light " color)}
     [:section
      [:div.columns.is-centered.is-vcentered
       [:div.column.is-1
        [:div {:class (str "icon is-large " color)} [:i {:class (str "fas fa-2x " icon)}]]]
       [:div.column.is-8.is-offset-1 {:class color}
        [:div copy] (when (not (nil? lost?)) [:b  (if lost? "-" "+") (if points-result
                                                                       (.toLocaleString points-result)
                                                                       "nil") " points"])]]]]))

(defn <post-overlay> [{:as           p
                       fake-news?    :fake-news?
                       game-state    :game-state
                       points-result :points-result}]
  (case game-state
    :live nil
    :timed-out [<action-info-content> :timeout "You ran out of time to react to this post."]
    :shared (if fake-news?
              [<action-info-content> :lost "You shared nonsense content" points-result]
              [<action-info-content> :won "You shared legit content" points-result])
    :blocked (if fake-news?
               [<action-info-content> :won "You blocked nonsense content" points-result]
               [<action-info-content> :lost "You blocked legit content" points-result])
    nil))

(defn <comment> [{:as comment
                  text :text
                  author :by}]
  [:div.comment
   [:div.comment-inner
    [:div.comment-inner-left
     [<author-image> author]]
    [:div.comment-inner-right
     [:div.comment-author (:name author) [:span.comment-handle (:handle author)]]
     [:div.comment-text text]]]])

(defn <post-comments> [comments]
  [css-transition-group {:class "post-comments"}
   (for [comment comments]
     [css-transition {:timeout     2000
                      :key         (comment/id comment)
                      :class-names "post-transition"}
      ^{:key (comment/id comment)} ;; important to keep track of rendering
      [<comment> comment]])])

(defn <type-text> [;; destructure the post
                   {:as p
                    description :description
                    game-state :game-state ; either :live, :shared, :blocked, or :timed-out
                    author :by
                    comments :comments}]
  [:div {:class ["post" "post-type-text my-5" game-state]}
   [<debug-tags> p]
   [:div.columns.mr-0
    [:div.authorcolumn [<author-image> author]]
    [:div.infocolumn
     [<author-name> author]
     [<post-text> description]
     (when (:image p) [<post-media> (:image p)])
     (when (= game-state :live) [<post-actions> p])
     (when (= game-state :live) [<post-progress> p])
     [<post-comments> comments]
     (case game-state
       :live      nil
       :shared    [<post-overlay> p]
       :blocked   [<post-overlay> p]
       :timed-out [<post-overlay> p]
       nil)]]])

(defn <type-re-post> [{:as p
                       author :by
                       comment :comment
                       original-post :post}]
  [:div {:class ["post" "post-type-re-post"]}
   [<debug-tags> p]
   [:div.columns.mr-0
    [:div.authorcolumn [<author-image> author]]
    [:div.infocolumn
     [<author-name> author]
     [<post-text> comment]
     [:div {:class "post-sub-post"} (<post> original-post)]]]])

(defn match-post [p]
  (case (:type p)
    :post-text    [<type-text> p]
    :re-post      [<type-re-post> p]
    ; post type not found:
    (do
      (log/warn "no matching component for post type" (str p))
      (log/warn "rendering default post")
      [<type-text> p])))

(defn <post> [p]
  ^{:key (:id p)} ;; important to keep track of rendering
  [match-post p])

(defn <header> []
  [:div {:class ["panel-header" "timeline-header"]}
   [:h5 {:class "title is-5 is-white"} "Your timeline"]])

(defn <container> []
  [:div.timeline-container
   [<header>]
   (when (empty? (state/posts))
     [:button {:on-click (fn [] (business/start-all-stories!))}"start all stories"])
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
   [css-transition-group {:class "timeline-posts"}
    (map-indexed (fn [index post]
                   [css-transition {:timeout 2000
                                    :key (:id post)
                                    :class-names "post-transition"}
                    ^{:key (:id post)} ;; important to keep track of rendering
                    [<post> post]])
                 (state/posts))]])
