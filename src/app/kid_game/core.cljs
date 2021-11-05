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
            ;; these NS are imported here for dev hot-reloading.  for some reason it does not work without
            ;; them imported at the top of the tree
            [kid-game.components.verification-hub.activities.websearch]
            [kid-game.components.verification-hub.activities.polygon-search]
            [kid-game.components.verification-hub.activities.ris-crop]
            ; end weird import
            [moment]))

(defn <game> [& {:keys [dev?]}]
  (let [size (if dev?
               {:active "is-half active" :inactive "is-one-quarter is-unselectable"}
               {:active "is-two-thirds active" :inactive "is-one-third is-unselectable"})
        pointer-events {:active {:pointer-events "all"} :inactive {:pointer-events "none"}}]
    [:div {:class "game-container columns"}
     [notifications/<notifications>]

     ;; this meta panels is for during development
     (when dev?
       [:div {:class "game-panel active column is-one-quarter p-5"}
        [<meta>/<meta>]])

     [:div {:class ["game-panel column"
                    "game-timeline"
                    (cond (= (state/get-panel) :timeline) (:active size)
                          :else                           (:inactive size))]
            :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))
            }
      [:div.click-stopper {:style (if (= (state/get-panel) :timeline)
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
      [<verification-hub>/<container>]]]]))


(defn <one-post> [post-id]
  (let [post (-> (filter (fn [p] (= post-id (:id p))) posts-data/all-activity-posts) (first))]
    (when post [:div.testing-environment
                [:div.post-in-list
                 [<timeline>/<post> (assoc post :game-state :live)]]
                (map (fn [activity]
                       ^{:key (:type activity)} ;; important to keep track of rendering
                       [activities/get-activity activity]) (:activities post))])))

(defn <app> [& {:keys [dev]}]
  (let [is-dev? (r/atom dev)]
    (fn []
      (cond
        (state/has-player?) [<game> :dev? @is-dev?]
        :else [<login>/<form>]))))

(defn <routes> []
  ;; decide what to render in our app.  This is some junk hand-made routing
  (let [s js/window.location.search ; get the ?var=val&var2=val2 from the url
        post-id    (-> (js/URLSearchParams. s) (.get "post"))
        uikit?     (-> (js/URLSearchParams. s) (.get "uikit"))
        dev?       (-> (js/URLSearchParams. s) (.get "dev"))]
    (cond
      dev? (do (and (not (state/has-player?)) (business/new-session! "dev-user"))
               [<app> :dev true])
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
