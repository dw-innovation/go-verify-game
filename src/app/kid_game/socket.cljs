(ns kid-game.socket
  (:require [reagent.core :as reagent :refer [atom]]
            [chord.client :refer [ws-ch]]
            [clojure.spec.alpha :as s]
            [kid-shared.generator :as postgen]
            [kid-game.utils.log :as log]
            [kid-shared.types.messages :as messages]
            [kid-game.messaging :as messaging]
            [cljs.core.async :as async :refer [<! >! put!] :include-macros true]))

(log/debug "+++++++++++ welcome to the KID game.. starting server +++++++++=")

(enable-console-print!)

; listens to the send-chan and forwards them to the server for
; distribution
(defn connect-server-send [svr-chan]
  (async/go-loop []
    (when-let [msg (<! messaging/send-channel)] ; listen to send-chan
      (log/debug msg)
      (>! svr-chan msg) ; forward to server-chan
      (recur))))

;; connects a server websocket channel to our send and receive channels
(defn connect-server-receive
  [server-channel]
  (async/go-loop []
    ;; server sends wrapped in {:message}, extract that here
    (if-let [message (:message (<! server-channel))]
      (do
        (log/debug "received new server message" message)
        (async/>! messaging/receive-channel message)
        (recur))
      ; TODO actually close the websocket
      (log/debug "Websocket closed"))))


(defn get-websocket-url []
  ;; assume that the server is the same as whatever served us this frontend app
  (let [host js/window.location.hostname
        port js/window.location.port
        path js/window.location.pathname
        room (second (.split path "/"))
        ws-url (str "ws://" host ":" port "/" room "/ws")]
    (log/debug "created location"
               ":host" host
               ":port" port
               ":room" room)
    ws-url))

(defn get-room []
  (let [path js/window.location.pathname
        room (second (.split path "/"))]
    room))

;; tries to set up the websockets, returns true on success, false on failure
(defn try-setup-websockets! []
  (async/go
    (let [url (get-websocket-url)
          {:keys [ws-channel error]} (<! (ws-ch url))]
      (if error
        (do (log/debug "Something went wrong with the websocket"
                       error)
            false)
        (do (log/debug "successfully sending and receiving messages to websocket url:"
                       url)
            (connect-server-send ws-channel)
            (connect-server-receive ws-channel)
            true)))))

(defn setup-local-connection! []
  ;; attatch the posting alg to the receive channel,
  ;; this is what the server would usually do, but we are allowing it to run locally
  (postgen/attach-default-room-poster messaging/receive-channel)
  ;; connect the send channel directly to the receive channel
  ;; to emulate server pass-through
  (log/debug "connecting send channel to the receive channel")
  (async/go-loop []
    ;; whenever we receive a message
    (when-let [msg (<! messaging/send-channel)] ; listen to send-chan - when we receive a msg,
      (>! messaging/receive-channel msg) ; forward to server-chan
      (recur))))

(defn setup-socket! [player]
  (async/go
    ;; first, set up either a websocket or a local connection,
    ;; depending on if the websocket url actually works
    (if-let [success? (<! (try-setup-websockets!))]
      (do (log/debug "setup was successful")
          (log/debug success?))
      (do (log/debug "no websocket connection, trying local")
          (setup-local-connection!)))
    ;; then, send our user to that connection, in some way
    (messaging/send {:type ::messages/user-new
                     :body player})))
