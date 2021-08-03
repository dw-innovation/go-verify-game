;;
;;  A post generator can be attached to a message channel, either on the server (which will send messages down the ws-channel)
;;    or directly to a frontend receive-channel, to let the application run without a server
;;
(ns kid-shared.post-generator
  (:require [clojure.spec.alpha :as s]
            [kid-shared.types.poster :as poster]
            [kid-shared.types.messages :as messages]
            [clojure.core.async :as async]))


;; keep a list of all the active posters
(def posters-state (atom []))

(defn post-to-channel [channel]
  (async/go
    (async/>! channel {:type ::messages/post-new
                       :body (rand-nth poster/examples)})))

;; attach a posting loop to the room
;; TODO exit this go loop, too
;;
;; send-channel -> exit-channel
(defn attach-poster [send-channel poster-fn]
  (let [exit-channel (async/chan)]
    (async/go-loop []
      (async/alt!
        ;; once every x seconds, do the following action
        (async/timeout 15000) ([]
                                (poster-fn send-channel)
                                ;; and then continue the loop
                                (recur))
        exit-channel ([msg]
                      (println "received exit message" msg))))
    ;; return the exit channel:
    exit-channel))


;; channel -> exit-channel
(defn attach-default-room-poster [room-channel]
  (let [loop-exit-channel (attach-poster room-channel post-to-channel)]
    ;; add the new loop to our list of active channels
    (swap! posters-state conj loop-exit-channel)
    ;; return the channel as well to whatever called it
    loop-exit-channel))

(defn kill-all-posters []
  (for [c @posters-state]
    (do
      (async/>!! c "kill-all-posters closed the channel"))))
