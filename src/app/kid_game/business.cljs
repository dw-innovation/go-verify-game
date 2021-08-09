(ns kid-game.business
  (:require [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.utils.core :refer [timestamp-now new-uuid]]
            [kid-shared.types.messages :as messages]
            [kid-shared.types.post :as posts]
            [kid-shared.types.chat :as chat]
            [cljs.core.async :as async :include-macros true]))

;;
;;  the business logic of the framework
;;

(defn use-new-player! [& {:keys [name]}]
  (->> {:id (str (random-uuid))
        :name name
        :created (timestamp-now)
        :role :investigator}
       (state/set-player)))

(defn new-session! [player-name]
  (state/open-game)
  (use-new-player! :name player-name)
  (socket/setup-socket!))

(defn post-text-post! [& {:keys [title description fake-news?]}]
  (socket/send {:type ::messages/post-new
                :body {:type :post-text
                       :id (new-uuid)
                       :created (timestamp-now)
                       :title title
                       :time-limit 300
                       :fake-news? fake-news?
                       :by (state/get-player)
                       :description description}}))

(defn post-re-post! [& {:keys [comment post]}]
  (socket/send {:type ::messages/post-new
                :body {:type :re-post
                       :id (new-uuid)
                       :created (timestamp-now)
                       :time-limit 300
                       :by (state/get-player)
                       :comment comment
                       :post post}}))

; user, string -> state update
(defn chat-send! [& {:keys [to content]}]
  (socket/send (chat/create :from (state/get-player)
                            :to to
                            :content content)))

(defn chat-seen! [chat]
  (if-not (chat/seen? chat) (state/seen! chat)))

(defn group-chat-send! [& {:keys [content]}]
  (socket/send (chat/create :from (state/get-player)
                            :to nil ; a chat only from is group chat
                            :content content)))

(defn win-points! [points] (state/win-points points))
(defn loose-points! [points] (state/loose-points points))
