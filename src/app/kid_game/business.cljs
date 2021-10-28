(ns kid-game.business
  (:require [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-game.utils.core :refer [timestamp-now new-uuid]]
            [kid-shared.types.messages :as messages]
            [kid-game.messaging       :as messaging]
            [kid-shared.generator     :as gen]
            [kid-shared.types.post :as posts]
            [kid-shared.types.chat :as chat]
            [kid-shared.data.stories :as stories]
            [kid-game.socket :as socket]
            [cljs.core.async :as async :include-macros true]))

;;
;;  the business logic of the framework
;;
(defn start-all-stories! []
  (gen/gen-run-story messaging/receive-channel stories/game-story))

(defn use-new-player! [& {:keys [name]}]
  (->> {:id (str (random-uuid))
        :name name
        :created (timestamp-now)
        :role :investigator}
       (state/set-player)))

(defn new-session! [player-name]
  (state/open-game)
  (use-new-player! :name player-name)
  (socket/setup-socket! (state/get-player)))

(defn logout [] (state/clear-player))

(defn post-text-post! [& {:keys [title description fake-news?]}]
  (messaging/send {:type ::messages/post-new
                   :body {:type :post-text
                          :id (new-uuid)
                          :created (timestamp-now)
                          :title title
                          :time-limit 300
                          :fake-news? fake-news?
                          :by (state/get-player)
                          :description description}}))

(defn win-points! [points] (state/win-points points))
(defn loose-points! [points] (state/loose-points points))

;; attatches a timer to a post, which will deprecate the post's time in the state
;; periodically.  additionally this function registers post :stop-timer! which
;; contains an anonymous function to kill this timer
(defn attach-post-timer! [post]
  (let [p (state/get-post post)
        exit-channel (async/chan)]
    ;; give the post an anonymous function that can stop the timer associated with it
    (state/update-post p :stop-timer! (fn [] (async/put! exit-channel "exit message")))
    ;; start the countdown loop
    (async/go-loop []
      (let [p (state/get-post post) ; get a fresh post on every loop
            ;; get the time left or instantiate the time left
            time-left (or (:time-left p) (:time-limit post) 0)]
        ;; either receive message on exit channel to end loop, or ping that timeout every 100ms
        (async/alt!
          exit-channel ([] (println "stopping post timer"))
          (async/timeout 100) ([]
                               (if (> time-left 0)
                                 ;; update the post's time left and keep the loop going
                                 (do (state/update-post p :time-left (dec time-left))
                                     (recur))
                                 ;; otherwise, transition the post's state to timed-out
                                 ;;
                                 (do (swap! state/stats assoc-in [:missed-deadlines] (inc (:missed-deadlines @state/stats)))
                                     (state/post-transition-state! p :timed-out)))))))))

;; only works if the timer has already been attatched
(defn stop-post-timer! [post]
  (if (fn? (:stop-timer! post)) ; is there a function at the expected key?
    ((:stop-timer! post)) ; then run it!
    (log/warn "post does not have a stopping function!")))

(defn add-post [post]
  ;; remove the post timer from below if it's already there
  (let [p (state/get-post post)] ; get the current live version of the post
    (when p (stop-post-timer! p))) ; if it's there, stop the timer
  ;; TODO validate that it's an actual valid post
  (state/add-post post)
  ;; attatch a time decreaser to the post, but only if time limiet
  (when (:time-limit post)
    ;; if it has a time limit, then it is a 'playable' post
    ;; so we give it a game state
    (state/post-transition-state! post :live)
    ;; we also attatch an async loop to start counting down
    (attach-post-timer! post)))


(defn post-investigate! [post]
  (state/open-verification-hub post))

(defn post-block! [post]
  (let [fake-news? (:fake-news? post)
        time-left (or (:time-left post) 20)
        points time-left
        {blocked-correctly :blocked-correctly
         missed-deadlines :missed-deadlines
         misleading-reposts :misleading-reposts} @state/stats]
    (if (:fake-news? post)
      (do (state/add-notification {:type :success
                                   :text (str "+" points " points")})
          (swap! state/stats assoc-in [:blocked-correctly] (inc blocked-correctly))
          (state/update-post post :points-result points)
          (win-points! time-left))
      (do (state/add-notification {:type :warning
                                   :text (str "-" points " points")})
          (state/update-post post :points-result (- points))
          (loose-points! time-left))))
  (stop-post-timer! post)
  (state/post-transition-state! post :blocked))

(defn post-share! [& {:keys [comment post]}]
  (let [fake-news? (:fake-news? post)
        time-left (or (:time-left post) 20)
        points time-left
        {blocked-correctly :blocked-correctly
         missed-deadlines :missed-deadlines
         misleading-reposts :misleading-reposts} @state/stats]
    (if (:fake-news? post)
      (do (state/add-notification {:type :warning
                                   :text (str "-" time-left " points")})
          (swap! state/stats assoc-in [:misleading-reposts] (inc misleading-reposts))
          (state/update-post post :points-result points)
          (loose-points! time-left))
      (do (state/add-notification {:type :success
                                   :text (str "+" points " points")})
          (state/update-post post :points-result (- points))
          (win-points! time-left)))
    (stop-post-timer! post)
    (state/post-transition-state! post :shared)))

; user, string -> state update
(defn chat-send! [& {:keys [to content]}]
  (messaging/send (chat/create :from (state/get-player)
                               :to to
                               :content content)))

(defn chat-seen! [chat]
  (if-not (chat/seen? chat) (state/seen! chat)))

(defn group-chat-send! [& {:keys [content]}]
  (messaging/send (chat/create :from (state/get-player)
                               :to nil ; a chat only from is group chat
                               :content content)))

(defn handle-message! [msg]
  ;; handles an incoming message, and affects the state accordingly.
  ;; returns true if everything went as expected, and false if something went wrong.
  ;; TODO should actually throw a variety of errors instead of true falsing
  ;; a message must have a type and a body
  (if-let [{:keys [type body]} msg]
    (do (log/debug "handling message" msg)
        (case type
          ;; eventually, the following functions should all call local funcs
          ;; and not delegate to state functions
          ::messages/user-init (state/set-users body)
          ::messages/chat-new (state/add-chat body)
          ::messages/user-new (state/add-user body)
          ::messages/user-left (state/remove-user body)
          ::messages/comment-new (state/add-post-comment body)
          ::messages/post-new (add-post body)
          ; default
          (log/debug "could not handle the message"))
        ;; not really true
        true)
    (do (log/warn "got a message we don't recognize as a message")
        false)))

(defn listen-to-receive-channel! []
  (async/go-loop []
    (if-let [msg (async/<! messaging/receive-channel)]
      (do (log/debug "got new message!!!!!!!!!!")
          (handle-message! msg)
          (recur))
      (println "receive channel got bad message"))))

(listen-to-receive-channel!)
