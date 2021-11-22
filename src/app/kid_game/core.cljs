(ns kid-game.core
  (:require [reagent.core                                         :as r]
            [reagent.dom :as rd]
            [kid-game.utils.log                                   :as log]
            [kid-game.uikit                                   :as uikit]
            [kid-shared.data.posts                               :as posts-data]
            [kid-game.components.login.core                       :as <login>]
            [kid-game.state                                       :as state]
            [kid-game.business                                    :as business]
            [kid-game.components.meta                             :as <meta>]
            [kid-game.components.timeline.core                    :as <timeline>]
            [kid-game.components.notifications                    :as notifications]
            [kid-game.components.verification-hub.core            :as <verification-hub>]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-game.components.shared.icons :as icons]
            ;; these NS are imported here for dev hot-reloading.  for some reason it does not work without
            ;; them imported at the top of the tree
            [kid-game.components.verification-hub.activities.websearch]
            [kid-game.components.verification-hub.activities.polygon-search]
            [kid-game.components.verification-hub.activities.ris-crop]
            [kid-game.components.verification-hub.activities.ris-flip]
            ; end weird import
            [moment]))

(defn <game> []
  (let [size {:active "is-two-thirds active" :inactive "is-one-third is-unselectable"}
        timeline-active? (= (state/get-panel) :timeline)
        hub-active? (not timeline-active?)
        pointer-events {:active {:pointer-events "all"} :inactive {:pointer-events "none"}}]
    [:div {:class "game-container columns"}
     [notifications/<notifications>]

     ;; this meta panels is for during development
     (when @state/dev?
       [:div {:class "game-panel active column dev-panel"}
        [<meta>/<meta>]])

     [:div {:class ["game-panel column"
                    "game-timeline"
                    (if timeline-active?
                      (:active size)
                      (:inactive size))]
            :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))
            }
      [:div.click-stopper {:style (if timeline-active?
                                    (:active pointer-events)
                                    (:inactive pointer-events))}
       [<timeline>/<container>]]]

     [:div {:class ["game-panel column"
                    "game-verification-hub"
                    (cond (= (state/get-panel) :verification-hub) (:active size)
                          :else                                   (:inactive size))]
            :on-click (fn [ev] (.stopPropagation ev) (state/open-verification-hub))}
      [:div.click-stopper {:style (if (= (state/get-panel) :verification-hub)
                                    (:active pointer-events)
                                    (:inactive pointer-events))}
       [<verification-hub>/<container>]]]

     [:div.hub-arrow-shadow {:class (if timeline-active? "out" "in")}]
     [:div.hub-arrow {:class (if timeline-active? "out" "in")
                      :on-click (fn [] (if timeline-active?
                                         (state/open-verification-hub)
                                         (state/open-timeline)))}
        [icons/circle-right-arrow-blue]]
     ]))


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
        post-id    (-> (js/URLSearchParams. s) (.get "post"))
        uikit?     (-> (js/URLSearchParams. s) (.get "uikit"))]
    (when (and @state/dev?
               (not (state/has-player?))) (business/new-session! "dev-user"))
    (cond
      uikit? [uikit/<main-view>]
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
