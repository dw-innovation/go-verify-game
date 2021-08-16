(ns kid-game.core
  (:require [reagent.core :as r]
            [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.components.chat.core :as <chat>]
            [kid-game.components.timeline.core :as <timeline>]
            [kid-game.components.verification-hub.core :as <verification-hub>]
            [kid-game.components.login.core :as <login>]
            [kid-game.example-activities :as ex]
            [lodash :as lodash]
            [moment]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))

(defn <header> []
[:div {:class "header"}
    [:h6 "KID game: room: " [:b (socket/get-room)]
     ": player: "
     [:b (:name (state/get-player))]
     ": points: "
     [:b (state/get-player-points)]]])

(defn <notifications> []
  [:div.notifications
   (for [n @state/notifications]
     (when (:active n)
       [:div {:class ["notification" (:type n)]}
        (:text n)]))])

(defn <game> []
  [:div.game-container
   [<notifications>]
   [<header>]

   [:div {:class ["game-panel" "game-timeline" (when (= (state/panel) :timeline) "active")]
          :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))}
    [:div.game-timeline-inner
     [<timeline>/<container>]]]

   [:div {:class ["game-panel" "game-verification-hub" (when (= (state/panel) :verification-hub) "active")]
          ;; catch a fall through click action
          :on-click (fn [ev] (.stopPropagation ev) (state/open-verification-hub))}
    [:div.game-verification-hub-inner
     [<verification-hub>/<container>]]]])

(defn <app> []
  (case (:active-panel @state/app-state)
    :login [<login>/<form>]
    :verification-hub [<game>]
    :timeline [<game>]))

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
