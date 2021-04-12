(ns kid-game.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.components.chat.core :as <chat>]
            [kid-game.components.posts.core :as <posts>]
            [cljs.core.async :as async :include-macros true]))

(def log js/console.log)

(defn <login> []
  (let [v (atom nil)
        log-in (fn []
                 (state/open-chat)
                 (business/use-new-player! :name @v)
                 (socket/setup-websockets!))]
    (fn []
      [:div {:class "login-container"}
       [:div {:class "login"}
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
  [:div {:class "content-container"}
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

(reagent/render-component
 [<app>]
 (. js/document (getElementById "app")))
