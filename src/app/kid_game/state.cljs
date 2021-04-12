(ns kid-game.state
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-shared.types.user :as user]
            [kid-shared.types.chat :as chat]
            [kid-game.utils.log :as log]
            [kid-game.utils.core :as utils]
            [cljs.core :refer [random-uuid]]
            [cljs.core.async :as async :include-macros true]))

;;
;;  data - should not be touched from
;;    outside world
;;
;;
;;
;;

(defonce app-state (atom {:text "Hello world!"
                          :active-panel :login
                          :user {}}))

(defonce points (atom 0))
(defonce msg-list (atom []))
(defonce users (atom {}))
(defonce post-list (atom [{:title "test post"
                           :type :post-text
                           :id "test"
                           :description "test-description"}]))

;;
;; state manipulator functions
;;

(defn open-chat [] (swap! app-state assoc :active-panel :chat))

;; set the app to work with a new user
(defn set-player [user]
       (swap! app-state assoc :user user))

(defn get-player [] (:user @app-state))

(defn get-users [] @users)

; prefer storage as a map
(defn set-users [user-list]
  (reset! users (user/col-to-map user-list)))

(defn add-user [user]
  (swap! users merge (user/col-to-map [user])))

(defn remove-user [user-id]
  (swap! users dissoc user-id))

(defn add-chat [chat]
  ; add the chat to the state
  (swap! msg-list conj chat))

(defn seen! [chat]
  (->> @msg-list
       (utils/indexes #(chat/same % chat))
       (first)
       (#(do
          (if % (swap! msg-list assoc % (chat/seen! chat)))))))

(defn message-list [] @msg-list)

(defn new-chats? [chats] (and (last chats)
                              (not (:seen (last chats)))))

(defn get-player-points [] @points)

(defn win-points [ps]
  (reset! points (+ @points ps))
  (log/debug "won points " ps)
  (log/debug "total points " @points))

(defn loose-points [ps]
  (reset! points (- @points ps))
  (log/debug "lost points " ps)
  (log/debug "total points " @points))

(defn posts [] (reverse @post-list))

(defn add-post [post] (swap! post-list conj post))
