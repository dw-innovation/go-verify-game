(ns kid-game.core
  (:require [reagent.core                                         :as r]
            [reagent.dom :as rd]
            [kid-game.utils.log                                   :as log]
            [kid-shared.data.posts                               :as posts-data]
            [kid-game.dev-cards                                 :as dev-cards]
            [kid-game.components.login.core                       :as <login>]
            [kid-game.state                                       :as state]
            [kid-game.business                                    :as business]
            [kid-game.components.meta                             :as <meta>]
            [kid-game.components.timeline.core                    :as <timeline>]
            [kid-game.components.notifications                    :as notifications]
            [kid-game.components.verification-hub.core            :as <verification-hub>]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-game.components.verification-hub.activities.websearch]
            [moment]))

(defn <game> []
  [:div {:class "game-container columns"}
   [notifications/<notifications>]

   ;; this meta panels is for during development
   [:div {:class "game-panel active column is-one-quarter"}
    [<meta>/<meta>]]

   [:div {:class ["game-panel column"
                  "game-timeline"
                  (cond (= (state/get-panel) :timeline) "is-two-thirds active"
                        :else                           "is-one-quarter")]
          :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))}
    [:div.game-timeline-inner
     [<timeline>/<container>]]]

   [:div {:class ["game-panel column"
                  "game-verification-hub"
                  (cond (= (state/get-panel) :verification-hub) "is-half active"
                        :else                                   "is-one-third")]
          :on-click (fn [ev] (.stopPropagation ev) (state/open-verification-hub))}
    [:div.game-verification-hub-inner
     [<verification-hub>/<container>]]]])


(defn <one-post> [post-id]
  (let [post (-> (filter (fn [p] (= post-id (:id p))) posts-data/all-activity-posts) (first))]
    (when post [:div.testing-environment
                [:div.post-in-list
                 [<timeline>/<post> (assoc post :game-state :live)]]
                (map (fn [activity]
                       ^{:key (:type activity)} ;; important to keep track of rendering
                        [activities/get-activity activity]) (:activities post))])))

(defn <app> []
  (cond
    (state/has-player?) [<game>]
    :else [<login>/<form>]))

(defn <routes> []
  ;; decide what to render in our app.  This is some junk hand-made routing
  (let [s js/window.location.search ; get the ?var=val&var2=val2 from the url
        post-id    (-> (js/URLSearchParams. s) (.get "post")) ; extract &post=
        dev-cards? (-> (js/URLSearchParams. s) (.get "dev-cards"))
        dev?       (-> (js/URLSearchParams. s) (.get "dev"))]
    (cond
      dev? (do (and (not (state/has-player?)) (business/new-session! "dev-user"))
               [<app>])
      dev-cards? [dev-cards/<main-view>]
      post-id [<one-post> post-id]
      :else [<app>])))

; render the html component, if it exists
(defn maybe-bind-element [div-id <component>]
  (if-let [el (. js/document (getElementById div-id))]
    (do
      (log/debug "mounting component on #" div-id)
      (rd/render [<component>] el))
    (log/warn "#" div-id "not found, skipping")))

(maybe-bind-element "app" <routes>)
