(ns kid-game.messaging
  (:require [kid-game.utils.log :as log]
            [kid-shared.types.messages :as messages]
            [cljs.core.async :as async :refer [<! >! put!] :include-macros true]))


;; The channel that this application will send messages to:
(defonce send-channel (async/chan))
;; the channel that this application will listen for messages on:
(defonce receive-channel (async/chan))

;; puts the messages on the send-chan
(defn send
  [msg]
  (if (messages/valid-message? msg)
    (do (log/debug "sending message: " msg)
        (async/put! send-channel msg))
    (do (log/warn "your message is invalid!")
        (log/warn (str (:type msg)))
        (log/warn (str (:body msg)))
        (log/warn (str (messages/explain-message msg)))
        (log/warn "sending anyways lol")
        (async/put! send-channel msg))))
