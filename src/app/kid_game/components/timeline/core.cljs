(ns kid-game.components.timeline.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.utils.log :as log]
            [kid-shared.types.post :as posts]
            [kid-game.utils.core :refer [timestamp-now new-uuid]]
            [lodash]
            [moment]
            [react-transition-group]
            [cljs.js :refer [empty-state eval js-eval]]
            [cljs.core.async :as async :include-macros true]))


;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))


(declare <post>)

(defn <progress> [amnt total]
  ;(log/debug (str (* 100 (/ amnt total)) "%"))
  [:div {:class "progress-bar"}
   [:div {:class "progress-inner"
          :style {:width (str (* 100 (/ amnt total)) "%")}}]])

(defn <post-progress> [{time-left :time-left
                        time-limit :time-limit}]
  [<progress> time-left time-limit])


(defn <author-image> [{:as author
                       image :image
                       name :name}]
   [:div {:class "post-author-image"}
    (when image [:img {:src image
                       :alt (str name " profile image")}])])

(defn <author-name> [{:as author name :name
                      handle :handle}]
  [:div {:class "post-author-name"}
   [:span.name name] [:span.handle handle]])

(defn <author> [{:as author
                 name :name
                 image :image}]
  [:div {:class "post-author"}
   [<author-image> author]
   [<author-name> author]])

;; will center the elements in a post overlay
(defn <post-overlay> [element]
  [:div.post-overlay
   [:div.post-overlay-inner element]])

(defn <type-text> [;; destructure the post
                   {:as p
                    title :title
                    description :description
                    ;; fake-news? either true or false
                    fake-news? :fake-news?
                    ;; game-state is either :live, :shared, :blocked, or :timed-out
                    game-state :game-state
                    ;; destructure the author:
                    {:as author author-name :name} :by
                    time-limit :time-limit}]
  (let [investigate! (fn [] (business/post-investigate! p))
        block! (fn [] (business/post-block! p))
        share! (fn [] (business/post-share! :comment "comment about post"
                                            :post p))]
    [:div {:class ["post" "post-type-text" game-state]}
     [:div.post-inner
      [:div.post-left-column
       [<author-image> author]]
      [:div.post-right-column
       [<author-name> author]
       [:div {:class "post-text"}
        [:small (:game-state p)]
        [:div.post-description description]]
       (when (:image p) [:img.post-image {:src (:image p)}])
       (when (= game-state :live)
         [:div {:class "post-actions"}
          [:button {:on-click share!} "share"]
          [:button {:on-click block!} "block"]
          [:button {:on-click (fn [ev] ;; stop propagation because there is a global
                                ;; click to open panel, and we are specifically opening the other one
                                (.stopPropagation ev)
                                (investigate!))}
           "investigate"]])
       (when (= game-state :live) [<post-progress> p])
       ]
      (case game-state
        :live nil
        :timed-out (if fake-news?
                     [<post-overlay> [:span "✗ This post went viral, and you did not react to it.  It was nonsense content"]]
                     [<post-overlay> [:span "🗸 You ran out of time to react to this post, it was legit content."]])
        :shared (if fake-news?
                     [<post-overlay> [:span "✗ You shared nonsense content"]]
                     [<post-overlay> [:span "🗸 You shared legit content"]])
        :blocked (if fake-news?
                     [<post-overlay> [:span "🗸 You blocked nonsense content"]]
                     [<post-overlay> [:span "✗ You blocked legit content"]])
        nil
        )
     ]]))

(defn <type-re-post> [{:as p
                author :by
                comment :comment
                original-post :post}]
  [:div {:class ["post" "post-type-re-post"]}
   [:div.post-inner
    [:div.post-left-column
     [<author-image> author]]
    [:div.post-right-column
     [<author-name> author]
     [:div {:class "post-description"} comment]
     [:div {:class "post-sub-post"} (<post> original-post)]]]])

(defn match-post [p]
  (case (:type p)
    :post-default [<type-text> p]
    :post-text [<type-text> p]
    :re-post [<type-re-post> p]
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
   [:div "Bleeper Network"]])



(defn <container> []
  [:div.timeline-container
   [<header>]
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
   [css-transition-group {:class "timeline-posts"}
    (map-indexed (fn [index post]
                   [css-transition {:timeout 2000
                                    :key (:id post)
                                    :class-names "post-transition"}
                    ^{:key (:id post)} ;; important to keep track of rendering
                    [:div.post-in-list
                     [<post> post]]])
                 (state/posts))]])
