(ns kid-game.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.components.chat.core :as <chat>]
            [kid-game.components.posts.core :as <posts>]
            [kid-game.example-activities :as ex]
            [lodash :as lodash]
            ["./react_components/compiled/intro.js" :as intro]
            [moment]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))

(def log js/console.log)

(defn <login> []
  (let [v (atom nil)
        log-in (fn []
                 (state/open-chat)
                 (business/use-new-player! :name @v)
                 (socket/setup-websockets!))]
    (fn []
      [:div.login-container
       [:div.login
        [:h1 "Login"]
        [(reagent/adapt-react-class intro/default) {}]
        [:form
         {:on-submit (fn [x] (.preventDefault x) (log-in))}
         [:input {:type "text"
                  :value @v
                  :placeholder "Pick a username"
                  :on-change #(reset! v (-> % .-target .-value))}]
         [:br]
         [:button {:type "submit"
                   :class "button-primary"} "Start chatting"]]]])))

(defn <game> []
  [:div.content-container
   [<chat>/<container>]
   [<posts>/<container>]
   [:header "hello"]
   [:div {:class "header"}
    [:h6 "KID game: room: " [:b socket/room]
     ": player: "
     [:b (:name (state/get-player))]
     ": points: "
     [:b (state/get-player-points)]]]])

(defn <app> []
  (case (:active-panel @state/app-state)
    :login [<login>]
    :chat [<game>]))

; render the html component, if it exists
(defn maybe-bind-element [div-id <component>]
  (if-let [el (. js/document (getElementById div-id))]
    (do
      (log/debug "mounting component on #" div-id)
      (reagent/render-component [<component>] el))
    (log/warn "#" div-id "not found, skipping")))

(maybe-bind-element "app" <app>)
(maybe-bind-element "examples" ex/<examples>)

(js/console.log "Here are the things we have loaded!")
(js/console.log lodash/_.map)
(js/console.log intro)
