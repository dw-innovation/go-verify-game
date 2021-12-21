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
            [kid-shared.ticks :as ticks]
            [kid-game.socket :as socket]
            [cljs.core.async :as async :include-macros true]))

;;
;;  the business logic of the framework
;;
(defn start-all-stories! []
  (gen/gen-run-story messaging/receive-channel stories/global-story))

(defn use-new-player! [& {:keys [name]}]
  (->> {:id (str (random-uuid))
        :name name
        :created (timestamp-now)
        :role :investigator}
       (state/set-player)))

(defn new-session! [player-name]
  (ticks/start!)
  (state/open-game)
  (use-new-player! :name player-name)
  ;; TODO only start all stories if websocket fails
  ;; (socket/setup-socket! (state/get-player))
  (when (not @state/dev?) (start-all-stories!))
  )

(defn logout []
  (reset! state/dev? false)
  (state/clear-player))

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
        time-limit (:time-limit p)
        ticks-dec-key (gensym) ;; generate symbols to track the timers
        ticks-stop-key (gensym)
        decrease-time! (fn [] (let [p (state/get-post post) ; get a fresh post on ever excecution
                                    time-left (or (:time-left p) (:time-limit post) 0)]
                                (state/update-post p :time-left (dec time-left))))
        time-out! (fn [] (do (swap! state/stats assoc-in [:missed-deadlines] (inc (:missed-deadlines @state/stats)))
                             (state/post-transition-state! p :timed-out)))
        cancel! (fn [] (do (ticks/cancel ticks-dec-key)
                           (ticks/cancel ticks-stop-key)))]
    (state/update-post p :stop-timer! cancel!)
    (ticks/for-ticks time-limit ticks-dec-key decrease-time!)
    (ticks/after time-limit ticks-stop-key time-out!)))

;; only works if the timer has already been attatched
(defn stop-post-timer! [post]
  (let [p (state/get-post post)]
    (if (fn? (:stop-timer! p)) ; is there a function at the expected key?
      ((:stop-timer! p)) ; then run it!
      (log/warn "post does not have a stopping function!"))))

(defn add-post [post]
  ;; remove the post timer from below if it's already there
  (let [p (state/get-post post)] ; get the current live version of the post
    (when p (stop-post-timer! p))) ; if it's there, stop the timer
  ;; TODO validate that it's an actual valid post
  (state/add-post post)
  ;; add the live game post states
  (state/update-post post :game-state nil
                     :investigated? false)
  ;; attatch a time decreaser to the post, but only if time limiet
  (when (:time-limit post)
    ;; if it has a time limit, then it is a 'playable' post
    ;; so we give it a game state
    (state/post-transition-state! post :live)
    ;; we also attatch an async loop to start counting down
    (attach-post-timer! post)))


(defn post-investigate! [post]
  (state/update-post post :investigated? true)
  (state/open-verification-hub post)
  (.scrollIntoView (js/document.getElementById (:id post)) (clj->js {"behavior" "smooth"
                                                                     "block"    "center"
                                                                     "inline"     "center"})))

(defn notify [typ text]
  (state/add-notification {:type typ :text text}))

(defn inc-stat [stat]
  {:pre [(stat #{:blocked-correctly :missed-deadlines :misleading-reposts})]}
  (let [s (stat @state/stats)]
    (swap! state/stats assoc-in [stat] (inc s))))

(defn do-blocked-irrelevant []
  (notify :info (str "That wasn't tremendously useful, was it? 0 points")))

(defn do-shared-irrelevant []
  (notify :info (str "That wasn't tremendously useful, was it? 0 points")))

(defn calc-points [status {time-left :time-left time-limit :time-limit investigated? :investigated}]
  {:pre [(status #{:blocked-correctly :blocked-wrong :shared-correctly :shared-wrong})
         (every? some? [time-left time-limit])]}
  (let [time-left-fraction (/ time-left time-limit)]
    (-> (case status
          :blocked-correctly (* time-left-fraction       (if investigated? 200 100))
          :blocked-wrong     (* (+ 1 time-left-fraction) (if investigated? 200 100))
          :shared-correctly  (* time-left-fraction       (if investigated? 200 100))
          :shared-wrong      (* (+ 1 time-left-fraction) (if investigated? 200 100))
          0)
        (js/Math.floor))))

(defn do-blocked-correctly [post]
  {:pre [(posts/is-game-post? post)]}
  (let [points (calc-points :blocked-correctly post)]
    (win-points! points)
    (inc-stat :blocked-correctly)
    (notify :success (str "You blocked nonsense content, you won " points " points"))
    (state/update-post post :points-result points)))

(defn do-blocked-wrong [post]
  {:pre [(posts/is-game-post? post)]}
  (let [points (calc-points :blocked-wrong post)]
    (loose-points! points)
    (notify :warning (str "You blocked legit content, you lost " points " points"))
    (state/update-post post :points-result (- points))))

(defn do-shared-correctly [post]
  {:pre [(posts/is-game-post? post)]}
  (let [points (calc-points :shared-correctly post)]
    (win-points! points)
    (notify :success (str "You shared legit content, you won " points " points"))
    (state/update-post post :points-result points)))

(defn do-shared-wrong [post]
  {:pre [(posts/is-game-post? post)]}
  (let [points (calc-points :shared-wrong post)]
    (loose-points! points)
    (inc-stat :misleading-reposts)
    (notify :warning (str "You shared nonsense content, you lost " points " points"))
    (state/update-post post :points-result (- points))))


(defn post-action! [action post]
  {:pre [(posts/is-game-post? post)
         (action #{:share :block})]}
  (let [shared? (= action :share)
        blocked? (= action :block)
        {fake-news? :fake-news?
         game-state :game-state} post
        live-post? (= game-state :live)
        dead-post? (not live-post?)
        legit-news? (not fake-news?)]
    (cond
      ;; shared filler content:
      (and dead-post? blocked?) (do-blocked-irrelevant)
      (and dead-post? shared?) (do-shared-irrelevant)
      (and live-post? blocked? fake-news?) (do-blocked-correctly post)
      (and live-post? blocked? legit-news?) (do-blocked-wrong post)
      (and live-post? shared? fake-news?) (do-shared-wrong post)
      (and live-post? shared? legit-news?) (do-shared-correctly post)
      :else (log/debug "no matched cases for the post!"))
    (stop-post-timer! post)
    (state/post-transition-state! post (if blocked? :blocked :shared))))

(defn post-share! [& {:keys [comment post]}]
  (post-action! :share post))

(defn post-block! [post]
  (post-action! :block post))

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
    (do 
      ;; (log/debug "handling message" msg)
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
      (do
        ;; or hide if (state/dev? true)
        ;; (log/debug "got new message!!!!!!!!!!")
          (handle-message! msg)
          (recur))
      (log/debug "receive channel got bad message"))))

(listen-to-receive-channel!)
