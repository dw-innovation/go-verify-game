(ns kid-game.components.timeline.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.utils.log :as log]
            [kid-shared.types.post :as posts]
            [kid-game.utils.core :refer [timestamp-now new-uuid]]
            [lodash]
            [moment]
            [cljs.js :refer [empty-state eval js-eval]]
            [cljs.core.async :as async :include-macros true]))

(declare <post>)

(defn <progress> [amnt total]
  ;(log/debug (str (* 100 (/ amnt total)) "%"))
  [:div {:class "progress-bar"}
   [:div {:class "progress-inner"
          :style {:width (str (* 100 (/ amnt total)) "%")}}]])

(defn <post-progress> [{time-left :time-left
                        time-limit :time-limit}]
  [<progress> time-left time-limit])

(defn <author> [{:as author
                 name :name
                 image :image}]
  [:div {:class "post-author"}
   [:div {:class "post-author-image"}
              (if image [:img {:src image}])]
   [:div {:class "post-author-name"}
    [:b name]]])



(defn evaluate [form]
  (eval (empty-state)
        form
        {:eval       js-eval
         :source-map true
         :context    :expr}
        (fn [result] (:value result))))

(defn <type-text> [;; destructure the post
                   {:as p
                    title :title
                    description :description
                    fake-news? :fake-news?
                    ;; destructure the author:
                    {:as author author-name :name} :by
                    time-limit :time-limit}]
  (let [
        ;; a post can be considered 'active' in a variety of cases
        active? (fn []
                           ;; or we had a time limit, and it ran out
                           (not (and time-limit (>= 0 (:time-left p)))))
        investigate! (fn [] (println "clicked investigate")
                       (business/investigate! p))
        block! (fn [])
        ; choices
        repost! (fn []
                  (business/post-re-post! :comment "comment about post"
                                          :post p)
                  (if fake-news?
                    (business/loose-points! (:time-left p))
                    (business/win-points! (:time-left p))))]
      [:div {:class ["post" "post-text" (if (active?) "active" "inactive")]}
       [:div {:class "post-user"}
        [:small {:class "posted-by-text"} "posted by "]
        [<author> author]]
       [:div {:class "post-text"
              :style {:background-color "white"
                      :padding "1rem"}}
        (when time-limit [<post-progress> p])
        ;; [:div (if (:fake-news? p) "fake" "real")]
        [:small "title"]
        [:div {:class "post-title"} title]
        ;; [:small "description"]
        [:div {:class "post-description"
               :style {:font-size "1.5rem"}} description]
        [:br]
        (if (:image p) [:img {:src (:image p)
                              :style {:width "50%"}}])
        [:div {:class "post-actions"}
         [:button {:on-click repost!} "re-post"]
         [:button {:on-click block!} "block"]
         [:button {:on-click (fn [ev] ;; stop propagation because there is a global
                               ;; click to open panel, and we are specifically opening the other one
                               (.stopPropagation ev)
                               (investigate!))}
          "investigate"]]
        [:br]
        ]
       [:div {:class "meta"
              :style {:padding "1rem"}}
        [:div {:style {:color "grey"}} (:subtext p)]
        [:div {:style {:color "grey"}} (if (:fake-news? p) "(is fake)" "(is real)")]
        [:br]
        [:br]
        ]]))

(defn <type-re-post> [{:as p
                author :by
                comment :comment
                original-post :post}]
  [:div {:class "re-post"}
   [:div {:class "post-user"}
    [:small {:class "posted-by-text"} "re-posted by "]
    [<author> author]]
   [:div {:class "post-commebt"} comment]
   [:div {:class "post-sub-post"} (<post> original-post)]])

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
  ^{:key (:id p)}
  [:div {:class "post"}
   [match-post p]])

(defn <header> []
  [:div {:class ["panel-header" "timeline-header"]}
   [:div "Bleeper Network"]])

(defn <container> []
  [:div.timeline-container
   [<header>]
   [:button {:on-click #(business/investigate! {})} "investigate"]
   (map <post> (state/posts))])
