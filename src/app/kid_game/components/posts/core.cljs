(ns kid-game.components.posts.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.utils.log :as log]
            [kid-shared.types.post :as posts]
            [kid-game.utils.core :refer [timestamp-now new-uuid]]
            [cljs.js :refer [empty-state eval js-eval]]
            [cljs.core.async :as async :include-macros true]))

(defn <input> [] [:div])
  ;; (let [title (atom nil)
  ;;       description (atom nil)
  ;;       fake-news? (atom false)
  ;;       submit-post (fn []
  ;;                     (business/post-text-post! :title @title
  ;;                                               :description @description
  ;;                                               :fake-news? @fake-news?)
  ;;                     (reset! title nil)
  ;;                     (reset! description nil))]
  ;;   (fn []
  ;;     [:div {:class "post-add"}
  ;;      [:form
  ;;       {:on-submit (fn [x]
  ;;                     (.preventDefault x)
  ;;                     (submit-post))}
  ;;       [:input {:type "text"
  ;;                :value @title
  ;;                :placeholder "post title"
  ;;                :on-change #(reset! title (-> % .-target .-value))}]
  ;;       [:input {:type "text"
  ;;                :value @description
  ;;                :placeholder "post description"
  ;;                :on-change #(reset! description (-> % .-target .-value))}]
  ;;       [:br]
  ;;       [:input {:type "checkbox"
  ;;                :id "fake-news"
  ;;                :name "fake-news"
  ;;                :value @fake-news?
  ;;                :on-change #(reset! fake-news? (-> % .-target .-checked))}]
  ;;       [:label {:for "fake-news"} "fake news!"]
  ;;       [:button {:type "submit"} "new post"]
  ;;       [:hr]]])))

(declare <post>)

(defn <progress> [amnt total]
  ;(log/debug (str (* 100 (/ amnt total)) "%"))
  [:div {:class "progress-bar"}
   [:div {:class "progress-inner"
          :style {:width (str (* 100 (/ amnt total)) "%")}}]])

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

(defn <type-text> [{title :title
                  description :description
                  fake-news? :fake-news?
                  {:as author author-name :name} :by
                  time :time
                  :as p}]
  (let [time-left (atom time)
        finished? (atom false)
        finished! (fn []
                    (reset! finished? true)
                    (reset! time-left 0))
        active? (fn [] (or (< 0 @time-left) @finished?))
        interval 100
        reduce-time! (fn []
                       (reset! time-left (if (< 0 @time-left)
                                           (dec @time-left)
                                           0)))
        ; choices
        repost! (fn []
                  (business/post-re-post! :comment "comment about post"
                                          :post p)
                  (if fake-news?
                    (business/loose-points! @time-left)
                    (business/win-points! @time-left))
                  (finished!))]
    ; TODO unhook this interval once 0
    ; or maybe keep it like this, so that you could get a "time-boost"
    ; by pumping up time-left
    ; (js/setInterval reduce-time! interval)
    (fn []
      [:div
       [:div {:class "post-user"}
        [:small {:class "posted-by-text"} "posted by "]
        [<author> author]]
       [:div {:class "post-text"
              :style {:background-color "white"
                      :padding "1rem"}}
        [<progress> @time-left (:time p)]
        ;; [:div (if (:fake-news? p) "fake" "real")]
        [:small "title"]
        [:div {:class "post-title"} title]
        ;; [:small "description"]
        [:div {:class "post-description"
               :style {:font-size "1.5rem"}} description]
        [:br]
        (if (:image p) [:img {:src (:image p)
                              :style {:width "50%"}}])

        [:div {:class "post-actions"
               :style {:position "absolute" :right 0 :margin-top "-30px" :margin-right "20px"}}
         [:button {:on-click repost!} "re-post"]]
        [:br]
        ]
       [:div {:class "meta"
              :style {:padding "1rem"}}
        [:div {:style {:color "grey"}} (:subtext p)]
        [:div {:style {:color "grey"}} (if (:fake-news? p) "(is fake)" "(is real)")]
        [:br]
        [:br]
        ]])))

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
  ^{:key (new-uuid)}
  [:div {:class "post"}
   [match-post p]])

(defn <container> []
  [:div {:class "post-container"}
   [<input>]
   (map <post> (state/posts))])
