(ns kid-game.messaging
  (:require [kid-game.utils.log :as log]
            [kid-shared.types.messages :as messages]
            [cljs.core.async :as async :refer [<! >! put!] :include-macros true]))


;; The channel that this application will send messages to:
(defonce send-channel (async/chan))
;; the channel that this application will listen for messages on:
(defonce receive-channel (async/chan))
