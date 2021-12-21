(ns kid-game.components.timeline.core
  (:require [cljs.js                  :refer [empty-state eval js-eval]]
            [cljs.core.async          :as async :include-macros true]
            [reagent.core             :as r]
            [kid-game.components.shared.icons :as icons]
            [kid-shared.generator     :as gen]
            [kid-game.state           :as state]
            [kid-game.business        :as business]
            [kid-game.utils.log       :as log]
            [kid-game.components.modal :as modal]
            [kid-shared.types.post    :as posts]
            (kid-shared.types.comment :as comment)
            [kid-shared.ticks         :as ticks]
            [kid-game.utils.core      :refer [timestamp-now new-uuid highlight-text]]
            ["../../react_components/compiled/js-utils.js" :as js-utils]
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

(defn <rainbow-progress> [percent]
  [:div
   [:div {:style {:flex 1}}
    [:div {:class ["is-background-grey" "br-2" "mb-2"]
           :style {:width "100%"
                   :height "10px"}}
     [:div {:class ["br-2"]
            :style {:width (str percent  "%")
                    :height "10px"
                    :transition "width .1s"
                    :background-color (js-utils/percentageToColor percent)}}]]]])

(defn <progress> [amnt total]
  (let [percent (js/Math.floor (* 100 (/ amnt total)))] [<rainbow-progress> percent])
  ;; [<bulma-progress> amnt total]
  )

(defn <post-progress> [{time-left :time-left
                        time-limit :time-limit}]
  [<progress> time-left time-limit])

(defn <debug-tags> [p]
  (if @state/dev?
    [:div.tags
     [:div {:class "tag is-light is-info is-family-monospace"} (:id p)]
     [:div {:class "tag is-light is-info is-family-monospace"} (:game-state p)]]
    [:span]))

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
   [:p (highlight-text copy)]])

(defn <post-media> [img]
  [:div.block
   [:img {:class "post-media" :src img}]])

(defn <post-actions> [p]
  (let [block! (fn [] (business/post-block! p))
        share! (fn [] (business/post-share! :comment "comment about post"
                                            :post p))]
    [:div.level.buttons {:style {:opacity 0 :animation "fadeIn 0.5s" :animation-delay "0.5s" :animation-fill-mode "forwards"}}
     [:div.level-item.level-left
      [:button {:class "button outline is-share-button" :on-click share!}
       [:span.icon [:i {:class "fas fa-share"}]] [:span "share"]]]
     [:div.level-item.level-right.pr-1
      [:button {:class "button outline is-block-button" :on-click block!}
       [:span.icon [:i {:class "fas fa-ban"}]] [:span "block"]]]]))

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
    [:div {:class (str "notification is-light " color)}
     [:section
      [:div.columns.is-centered.is-vcentered
       [:div.column.is-1
        [:div {:class (str "icon is-large " color)} [:i {:class (str "fas fa-2x " icon)}]]]
       [:div.column.is-8.is-offset-1 {:class color}
        [:div copy] (when (not (nil? lost?)) [:b  (if lost? "-" "+")
                                              (if points-result
                                                (.toLocaleString (js/Math.abs points-result))
                                                "nil") " points"])]]]]))

(defn <post-overlay> [{:as           p
                       fake-news?    :fake-news?
                       game-state    :game-state
                       points-result :points-result}]
  (case game-state
    :live nil
    :timed-out [<action-info-content> :timeout "Too late. This post has already gone viral."]
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

(defn <peeking-duck> [investigate!]
  [:div {:style {:position "absolute" ; note - this relies on the post having position:relative
                 :right "0"
                 :top "25px"
                 :animation "peek-out-animation 1s ease-in-out"
                 :animation-fill-mode "forwards"
                 :animation-delay "1s"
                 :width "150px"}}
   [:div {:on-click (fn [ev] ;; stop propagation because there is a global
                        ;; click to open panel, and we are specifically opening the other one
                      (println "clicked investigate")
                      (.stopPropagation ev)
                      (investigate!))
          :style {:animation "bounce-animation 10s ease-in-out infinite"
                  :cursor "pointer"
                  :position "relative"}}
    [icons/thomas-with-speech!?-bubble]]])

(defn <type-text> [;; destructure the post
                   {:as p
                    description :description
                    game-state :game-state ; either :live, :shared, :blocked, or :timed-out
                    author :by
                    comments :comments}]
  (let [investigate! (fn [] (business/post-investigate! p))]
    [:div.post-wrapper {:style {:position "relative"}}

     (when (= :live game-state) [<peeking-duck> investigate!])

     [:div {:class ["post" "post-type-text" game-state]
            :style {:position "relative" ; for the duck in <peeking-duck> to be position:absoluted correctly
                    :background-color "white" ; lazy for now
                    }}
      [<debug-tags> p]
      (case game-state
        :live      nil
        :shared    [<post-overlay> p]
        :blocked   [<post-overlay> p]
        :timed-out [<post-overlay> p]
        nil)
      [:div.columns.mr-0
       [:div.authorcolumn [<author-image> author]]
       [:div.infocolumn
        [<author-name> author]
        [<post-text> description]
        (when (:image p) [<post-media> (:image p)])
        (when (= game-state :live) [<post-actions> p])
        (when (= game-state :live) [<post-progress> p])
        [<post-comments> comments]]]]]))

(defn <type-re-post> [{:as p
                       author :by
                       comment :comment
                       original-post :post}]
  [:div.post-wrapper
   [:div {:class ["post" "post-type-re-post"]}
    [<debug-tags> p]
    [:div.columns.mr-0
     [:div.authorcolumn [<author-image> author]]
     [:div.infocolumn
      [<author-name> author]
      [<post-text> comment]
      [:div {:class "post-sub-post"} (<post> original-post)]]]]])

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
  (let [selected-post (state/get-post (:post @state/verification-hub-state))
        active? (= (:id p) (:id selected-post))]
    ^{:key (:id p)} ;; important to keep track of rendering
    [:div.post-section {:class (when active? "active")
                        :id (:id p)}
     [match-post p]]))

(defn <header> []
  [:div {:class ["panel-header" "timeline-header"]}
   [:img {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/bleeper_logo_main.png"
          :style {:height "100%"}}]
   [:h5 {:class "title is-5 has-text-white pl-5"}
    "BLEEPER | Home: " (-> @state/app-state :user :name)]])

(defn <thomas-says-we-are-done> []
  (let [[toggle close <modal>] (modal/make-modal)
        modal-content (fn [] [:div
                              [:p.mb-3 "Quack, quack! You've reached the end of this demo. I hope you enjoyed playing it. You can find your final score and stats at the bottom of the verification hub page."]
                              [:p.mb-3 "Still got a couple of minutes? Please fill out this feedback questionnaire (so the developers can improve the game): "]
                              [:p.mb-3
                               [:a {:href "https://tinyurl.com/kid-game-feedback"
                                    :target "_blank"} "https://tinyurl.com/kid-game-feedback"]]
                              [:p.mb-3 "Btw, we've also put together a nice verification toolbox/tutorial for you: "]
                              [:p.mb-3
                               [:a {:href "https://tinyurl.com/kid-verification-toolbox"
                                    :target "_blank"} "https://tinyurl.com/kid-verification-toolbox"]]
                              [:p.mb-3 "That's it for now. Have a good one. And never forget: Facts matter. Quack."]])]
    (fn []
      [:div.mb-5
       [<modal> modal-content]
       [:div.post-wrapper {:style {:position "relative"}}
        [<peeking-duck> toggle]
        [:div.post.post-type-text.p-5.pl-5 {:style {:position "relative"
                                                    :background-color "white"}}
         [:h4.is-4.title "You have reached the end of the demo."]
         [:p "Thanks for playing!"]
         [:p "Please click Thomas for further details ->"]
         [:br]
         [:br]]]])))

(defn <posts> []
  (let [timeline-height (r/atom 0)
        timeline-el (r/atom nil)]
    (fn []
      (let [selected-post (state/get-post (:post @state/verification-hub-state))
            scrolltop (if @timeline-el (.-scrollTop @timeline-el) 300)
            timeline-active? (= (state/get-panel) :timeline)
            hub-active? (not timeline-active?)
            posts (state/posts)]

        [css-transition-group {:class "timeline-posts"}
         (map-indexed (fn [index post]
                        [css-transition {:timeout 2000
                                         :key (:id post)
                                         :class-names "post-transition"}
                         ^{:key (:id post)} ;; important to keep track of rendering
                         [<post> post]])
                      posts)]))))

(defn <container> []
  [:div.timeline-container
   [<header>]
    ;; see css for functionality
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
   [:div.posts-list
    (if (> (count @gen/active-generators) 0)
      [:div.loader]
      [<thomas-says-we-are-done>])
    [<posts>]]])
