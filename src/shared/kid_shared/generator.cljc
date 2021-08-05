;;
;;  A post generator can be attached to a message channel, either on the server (which will send messages down the ws-channel)
;;    or directly to a frontend receive-channel, to let the application run without a server
;;
(ns kid-shared.generator
  (:require [clojure.spec.alpha :as s]
            [kid-shared.posts.posts :as posts]
            [kid-shared.posts.stories :as stories]
            [kid-shared.types.messages :as messages]
            [clojure.core.async :as async]))


;; keep a list of all the active posters
(def active-generators (atom []))

;; attach a posting loop to the room
;; TODO exit this go loop, too
;;
;; send-channel -> exit-channel
(defn gen-every-fifteen [send-channel]
  (let [exit-channel (async/chan)]
    (async/go-loop []
      (async/alt!
        ;; once every x seconds, do the following action
        (async/timeout 15000) ([]
                               (async/>! send-channel {:type ::messages/post-new
                                                       :body (rand-nth posts/examples)})
                               ;; and then continue the loop
                               (recur))
        exit-channel ([msg]
                      (println "received exit message" msg))))
    ;; return the exit channel:
    exit-channel))

;; takes a story, defined as [num, post, num, post, num, post]
;; and plays it to a channel, waiting on each num, and then
;; posting each post
(defn gen-run-story [send-channel story]
  (let [exit-channel (async/chan)
        story-channel (async/chan)]
    (async/go-loop []
      (print "looping")
      (async/alt!
        exit-channel ([] (println "stopping the story, forever"))
        story-channel ([story-item]
                       (when (number? story-item)
                         (do (println "received number, waiting")
                             (async/<! (async/timeout (* 1000 story-item)))))
                       (when (map? story-item)
                         (do (println "new story item ")
                             (async/>! send-channel {:type ::messages/post-new
                                                     :body story-item})))
                       (when story-item (recur))
                       )))
    (async/onto-chan! story-channel story)
    exit-channel
    ))


;; channel -> exit-channel
(defn attach-default-room-poster [room-channel]
  (let [loop-exit-channel (gen-run-story room-channel stories/default-story)]
    ;; add the new loop to our list of active channels
    (swap! active-generators conj loop-exit-channel)
    ;; return the channel as well to whatever called it
    loop-exit-channel))

(defn kill-all-posters []
  (for [c @active-generators]
    (do
      (async/put! c "kill-all-posters closed the channel"))))
