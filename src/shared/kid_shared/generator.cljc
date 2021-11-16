;;
;;  A post generator can be attached to a message channel, either on the server (which will send messages down the ws-channel)
;;    or directly to a frontend receive-channel, to let the application run without a server
;;
(ns kid-shared.generator
  (:require [clojure.spec.alpha :as s]
            [reagent.core :as r]
            [kid-shared.data.posts :as posts-data]
            [kid-shared.types.post :as posts]
            [kid-shared.types.comment :as comment]
            [kid-game.utils.log :as log]
            [kid-shared.data.stories :as stories]
            [kid-shared.types.messages :as messages]
            [clojure.core.async :as async]))


;; keep a list of all the active posters
(def active-generators (r/atom []))

;; takes a story, defined as [num, post, comment, [story], num, comment, post, num, post]
;; and plays it to a channel, waiting on each num, and then
;; posting each post
(defn gen-run-story
  ;; if you don't specify a wait time, it's 0
  ([send-channel story] (gen-run-story send-channel story 0))
  ;; run the story:
  ([send-channel story initial-wait-time]
  (let [exit-channel (async/chan)
        story-channel (async/chan)]
    ;; add the exit channel to the active generators list, so we can kill it
    (swap! active-generators conj exit-channel)
    ;; attatch the loop to run the story
    (async/go-loop []
      (log/debug "new story item received!")
      (async/alt!
        exit-channel ([] (println "stopping the story, forever")
                      ;; remove this channel from the active generators
                      (reset! active-generators (remove #(= % exit-channel) @active-generators)))
        ;; received a new story item on the channel
        ;; here's the deal about stories.  they are a vector.  they can contain
        ;; different data types.  they are played out in order. a number means wait that amount
        ;; of seconds.  a map is passed through clojure spec to figure out what it is, then a
        ;; new message is created.  another vector is considered a sub-story, and
        ;; recursively calls this function.
        story-channel ([story-item]
                       (cond
                         (number? story-item) (do (log/debug "story item is a number, waiting")
                                                  (async/<! (async/timeout (* 1000 story-item))))
                         (posts/post? story-item) (do (log/debug "story item is a post")
                                                      (async/>! send-channel {:type ::messages/post-new
                                                                              :body story-item}))
                         (comment/comment? story-item) (do (log/debug "story item is a comment")
                                                           (async/>! send-channel {:type ::messages/comment-new
                                                                                   :body story-item}))
                         (vector? story-item) (do (log/debug "story item is another story, starting generator")
                                                  (gen-run-story send-channel story-item))
                         :else (log/warn "we don't recognize this as a story item: " story-item))
                       ;; we await 'false to end the channel.  call the exit
                       ;; channel if we found false
                       (if story-item
                         (recur)
                         (do (async/put! exit-channel "close msg")
                             (recur))))))

    (async/go (async/<! (async/timeout initial-wait-time))
              ;; play the story vector on to the story channel, which will be consumed by the above loop
              ;; sending false is the message that the story is over
              (async/onto-chan! story-channel (conj story false)))
    ;; return the exit channel, so that clients of this function can also end the loop
    exit-channel)))

;; channel -> exit-channel
(defn attach-default-room-poster [room-channel])

(defn kill-all-posters []
  (log/debug "killing all posters: " @active-generators)
  (doall (for [c @active-generators]
    (do
      (log/debug "trying to close channel")
      (async/put! c "kill-all-posters closed the channel")))))
