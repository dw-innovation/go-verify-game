(ns kid-shared.types.chat
  (:require [kid-shared.types.shared :as shared]
            [kid-game.utils.core :refer [timestamp-now]]
            [kid-shared.types.user :as user]
            [kid-shared.utils :refer [deep-merge]]
            [clojure.spec.alpha :as s]))

(s/def ::content string?)
(s/def ::from ::user/user)
(s/def ::to ::user/user)
(s/def ::seen boolean?)

(s/def ::chat (s/keys :req-un [::shared/created
                               ::from
                               ::content]
                      :opt-un [::to])) ; in the case there is no 'to' then it is a group message

; a cheap way to see if the messages are the same messages.
; in the future, decide what an identity of a message actually is.
(defn same [c1 c2]
  (= (:created c1)
     (:created c2)))

(defn seen? [c] (:seen c))
(defn seen! [c] (assoc c :seen true))

; this actually creates a 'server message', not just a new chat.
; also i would like to spec creation.  naja, we can do this later
(defn create
  "create a new server-message-chat"
  ([& {:keys [from to content]}]
   (deep-merge {:type :kid-shared.types.messages/chat-new
                :created (timestamp-now)
                :body {:from from
                       :created (timestamp-now)
                       :content content}}
               ;; is there a nicer way to do this?
               (if to
                 {:body {:to to}} {}))))

; see if the chat is between users, ugly but precise for now
(defn between? [u1 u2 chat]
  (or (and (user/same? u1 (:from chat))
           (user/same? u2 (:to chat)))
      (and (user/same? u2 (:from chat))
           (user/same? u1 (:to chat)))))

; see if the chat belongs to the group chat
(defn group-chat? [chat] (= (:to chat) nil))

(def all-between (fn [u1 u2 chats]
                     (filter #(between? u1 u2 %) chats)))
