(ns kid-game.handler
  (:require
   [org.httpkit.server :as hk]
   [kid-game.state :as state]
   [clojure.pprint :as pp]
   [chord.http-kit :refer [with-channel]]
   [kid-shared.types.messages :as messages]
   [kid-shared.types.poster :as poster]
   [kid-game.utils.core :as utils]
   [compojure.core :refer :all]
   [compojure.route :as route]
   [clojure.core.async :as async :refer [go go-loop alt! >! <!]]
   [ring.util.response :as resp]
   [medley.core :refer [random-uuid]]))

(defn log [& xs] (dorun (map pp/pprint xs)))


(defn handle-message [client-id message client-ws-ch room]
  (log "received message" message)
  (go
    (let [{:keys [body type]} message
          room-ch (room :channel)
          room-id (room :id)]
      (log body)
      (log type)
      (if (= type ::messages/user-new)
        ; we have received a new user -> send them the list of users
        ; as well as pushing the message down the room channel
        (let [user body]
          (state/add-user room-id client-id user)
          (log room-id)
          (>! room-ch  {:type ::messages/user-init
                        :id (random-uuid) ; send the users to the client, don't know
                                          ; why uuid needed, messages might not include
                                          ; it at all
                        :created (utils/timestamp-now) ; also timestamp, possibly not
                        :body (state/get-user-list room-id)})
          (>! room-ch message)) ; pass the message
        ;; if not new user message
        ; just pass the message as is
        (>! room-ch message)))))


(defn connections-handler
  "create and manage the websocket connection between the browser and the server"
  [req]
  (with-channel req ws-ch ; ws-ch comes from chord, stands for "websocket channel"
    (log "we received a new websocket connection!")
    (log req)
    (let [room-tap (async/chan) ; this seems to just make an anonymous channel?
          client-connection ws-ch ; rename what chord gave us
          client-id (random-uuid)
          params (req :params)
          ; TODO if not room error
          room-id (params :room)
          room (state/get-or-create-room room-id)
          room-channel (room :channel)
          room-mult (room :mult)]
      ; subscribe to the room channel locally
      (async/tap room-mult room-tap) ; client channel taps the main channel
      (go-loop []
        (log "go!" "go!")
        (alt! ; whichever channel you receive on, do the thing
          client-connection ([{:keys [message]}] ; received something on the websocket, sent by a client
                             (log "received a message from the client-connection" message)
                             (if message
                               (do
                                 (handle-message client-id message client-connection room)
                                 (recur))
                               (do
                                 (async/untap room-mult room-tap) ; unsubscribe from the room
                                 ; tell the other clients the user left
                                 ; TODO client-id isn't user id any more
                                 ; and there should be proper linking to ::messages/user-left
                                 (>! room-channel {:type :user-left
                                                   :body client-id})
                                 (state/remove-user room-id client-id))))

          room-tap ([message] ; received something from the room channel, send to client
                    (log "received a message on the room channel" message)
                    (if message
                      (do
                        (>! ws-ch message)
                        (recur))
                      (async/close! ws-ch))))))))

(defroutes app
  (GET "/testing" [] "hhhh")
  (GET "/:room/ws" [room] connections-handler) ; the websocket connection
  (GET "/:room" [room] (resp/resource-response "index.html" {:root "public"}))
  (route/resources "/")
  (route/not-found "<h1>Page not found</h1>"))
