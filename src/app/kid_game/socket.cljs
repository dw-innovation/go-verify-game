(ns kid-game.socket
  (:require [reagent.core :as reagent :refer [atom]]
            [chord.client :refer [ws-ch]]
            [clojure.spec.alpha :as s]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-shared.types.messages :as messages]
            [cljs.core.async :as async :refer [<! >! put!] :include-macros true]))

(log/debug "+++++++++++ welcome to the KID game.. starting server +++++++++=")
; assume that the server is the same as whatever served us this frontend app
(def host js/window.location.hostname)
(def port js/window.location.port)
(def path js/window.location.pathname)
; TODO look for some kind of path library
(def room (second (.split path "/"))) ; second because split puts blank before starting slash

(log/debug ":host" host
           ":port" port
           ":room" room)

(def ws-url (str "ws://" host ":" port "/" room "/ws"))

(enable-console-print!)

(log/debug "sending and receiving messages to websocket url:" ws-url)

(defonce send-chan (async/chan))

;; Websocket Routines (Processes)

;; puts the messages on the send-chan
(defn send
  [msg]
  (if (messages/valid-message? msg)
    (do
      (log/debug "sending message: " msg)
      (async/put! send-chan msg))
    (do
      (log/warn "your message is invalid!")
      (log/warn (str (:type msg)))
      (log/warn (str (:body msg)))
      (log/warn (str (messages/explain-message msg)))
      (log/warn "sending anyways lol")
      (async/put! send-chan msg))))
; legacy
(def send-msg send)

; listens to the send-chan and forwards them to the server for
; distribution
(defn start-process-forward-messages
  [svr-chan]
  (async/go-loop []
    (log/debug "send msgs init:")
    (when-let [msg (<! send-chan)] ; listen to send-chan
      (log/debug msg)
      (log/debug "msgs in msgs")
      (>! svr-chan msg) ; forward to server-chan
      (recur))))

; listens to the server-channel and handles the messages
(defn start-process-receive-messages
  [svr-chan]
  (async/go-loop []
    (if-let [{:keys [type body]} ; get the messages type and body of
             (:message (<! svr-chan))] ; the server that may have sent a real message
      (do
        (log/debug "got new message!!!!!!!!!!")
        (log/debug (str type)) (log/debug (str body))
        (case type
          ::messages/user-init (state/set-users body)
          ::messages/chat-new (state/add-chat body)
          ::messages/user-new (state/add-user body)
          ::messages/user-left (state/remove-user body)
          ::messages/post-new (state/add-post body)
          ; default
          (log/debug "the server sent a valid message that we did not handle!!"))
        (recur))
      ; TODO actually close the websocket
      (println "Websocket closed"))))

(defn setup-websockets! []
  (async/go
    (let [{:keys [ws-channel error]} (<! (ws-ch ws-url))]
      (log/debug error)
      (if error
        (log/debug "Something went wrong with the websocket")
        (do
          (send-msg {:type ::messages/user-new
                     :body (state/get-player)})
          (start-process-forward-messages ws-channel)
          (start-process-receive-messages ws-channel))))))
