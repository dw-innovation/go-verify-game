(ns kid-game.core
  (:require [reagent.core :as r]
            [kid-shared.posts.stories :as stories]
            [kid-shared.generator :as gen]
            [kid-shared.posts.posts :as posts]
            [kid-game.components.verification-hub.activities.core :as activities]
            [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.components.chat.core :as <chat>]
            [kid-game.components.meta :as <meta>]
            [kid-game.components.notifications :as notifications]
            [kid-game.components.timeline.core :as <timeline>]
            [kid-game.components.verification-hub.core :as <verification-hub>]
            [kid-game.components.login.core :as <login>]
            [kid-game.example-activities :as ex]
            [lodash :as lodash]
            [moment]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))


(defn <game> []
  [:div.game-container
   [notifications/<notifications>]

   ;; this meta panels is for during development
   [:div.game-panel.active
    [<meta>/<meta>]]

   [:div {:class ["game-panel"
                  "game-timeline"
                  (when (= (state/panel) :timeline) "active")]
          :on-click (fn [ev]
                      (.stopPropagation ev)
                      (state/open-timeline))}
    [:div.game-timeline-inner
     [<timeline>/<container>]]]

   [:div {:class ["game-panel"
                  "game-verification-hub"
                  (when (= (state/panel) :verification-hub) "active")]
          ;; catch a fall through click action
          :on-click (fn [ev] (.stopPropagation ev) (state/open-verification-hub))}
    [:div.game-verification-hub-inner
     [<verification-hub>/<container>]]]])

(defn <app> []
  ;; if we explicitly set something at ?post, only load that, only for development, really!
  ;; if you're confused..... just ignore this!!! and concentrate again at the next comment
  (let [s js/window.location.search
        post-id (-> (js/URLSearchParams. s) (.get "post"))]
    (if post-id
      (let [post (-> (filter (fn [p] (= post-id (:id p))) posts/all-activity-posts) (first))]
        (when post [:div.testing-environment
                    [:div.post-in-list
                     [<timeline>/<post> (assoc post :game-state :live)]]
                    (map (fn [activity]
                        [:div {:style {:max-width "50rem"}}
                          [activities/get-activity activity]]) (:activities post))
                    ]))
      ;; otherwise load the whole game
      ;; this is the important stuf!!
      (case (:active-panel @state/app-state)
        :login [<login>/<form>]
        :verification-hub [<game>]
        :timeline [<game>]))))

; render the html component, if it exists
(defn maybe-bind-element [div-id <component>]
  (if-let [el (. js/document (getElementById div-id))]
    (do
      (log/debug "mounting component on #" div-id)
      (r/render-component [<component>] el))
    (log/warn "#" div-id "not found, skipping")))

(maybe-bind-element "app" <app>)

;; ignore these for now

(maybe-bind-element "examples" ex/<examples>)

(js/console.log "Here are the things we have loaded! these come from js, check shadow-cljs.edn")
(js/console.log lodash/_.map)
