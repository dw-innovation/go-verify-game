(ns kid-game.state
  (:require [reagent.core :as reagent :refer [atom]]
            [com.rpl.specter :as s]
            [kid-shared.types.user :as user]
            [kid-shared.types.chat :as chat]
            [kid-game.utils.log :as log]
            [kid-game.utils.core :as utils]
            [cljs.core :refer [random-uuid]]
            [cljs.core.async :as async :include-macros true]))

;;
;;  data - should not be touched from
;;    outside world,
;;
;;    instead,
;;     use the supplied functions
;;

(defonce app-state (atom {:text "Hello world!"
                          ;; either login, timeline, or verification-hub
                          :active-panel :login ; start on the login page
                          :user {}}))

(defonce points (atom 0))
(defonce msg-list (atom []))
(defonce notifications (atom []))
(defonce users (atom {}))
(defonce post-list (atom [{:title "Welcome to the timeline!"
                           :type :post-text
                           :id "test"
                           :description "wait a second and you will start to see posts coming in"}]))

(defonce verification-hub-state (atom {}))

;;
;; state manipulator functions
;;
(defn open-timeline []
  (log/debug "opening the timeline")
  (swap! app-state assoc :active-panel :timeline))

(defn open-verification-hub
  ([post]
   (log/debug "opening verification hub")
   (swap! verification-hub-state assoc :post post)
   (swap! app-state assoc :active-panel :verification-hub))
  ([]
   (swap! app-state assoc :active-panel :verification-hub)))

(defn open-game [] (open-timeline))

(defn get-panel [] (:active-panel @app-state))

;; set the app to work with a new user
(defn set-player [user]
       (swap! app-state assoc :user user))

(defn get-player [] (:user @app-state))

(defn get-users [] @users)

; prefer storage as a map
(defn set-users [user-list]
  (reset! users (user/col-to-map user-list)))

(defn add-user [user]
  (swap! users merge (user/col-to-map [user])))

(defn remove-user [user-id]
  (swap! users dissoc user-id))

(defn add-chat [chat]
  ; add the chat to the state
  (swap! msg-list conj chat))

(defn seen! [chat]
  (->> @msg-list
       (utils/indexes #(chat/same % chat))
       (first)
       (#(do
          (if % (swap! msg-list assoc % (chat/seen! chat)))))))

(defn message-list [] @msg-list)

(defn new-chats? [chats] (and (last chats)
                              (not (:seen (last chats)))))

(defn get-player-points [] @points)

(defn win-points [ps]
  (reset! points (+ @points ps))
  (log/debug "won points " ps)
  (log/debug "total points " @points))

(defn loose-points [ps]
  (reset! points (- @points ps))
  (log/debug "lost points " ps)
  (log/debug "total points " @points))

;;
;;  posts
;;    the state actions that have to do with the posts
;;    IDENTITY of a post is its ID
;;
;;  frontend posts are _very similar_ to posts defined in types/post
;;    we do, however, add a few extra fields to track game lifecycle.
;;
;;    these are:
;;      post :game-state -> one of #{nil :live :blocked :shared :timed-out}
;;                          nil usually means the post is not an 'interactive' one
;;      post :time-left -> the time the post has left to completion, automatically
;;                         decremented
;;      post :points-result -> a record of how many points the user won or lost from this post
;;      post :stop-timer! -> a lambda function, so the post can stop it's own timer
;;

(defn posts [] @post-list)

(defn get-post [post] ;; get a full post by something post-like (min of {:id 'something'})
  ;; use specter to update the post at the path where the ids are the same
  (first (s/select [(s/filterer #(= (:id %) (:id post))) s/FIRST]
                   @post-list)))

;; updates a post _by merge_ -> unnaffected fields are kept
;; if you send this function {:id 2 :time-left 3}, the rest
;; of the fields, other than time-left will be left alone
;;   returns post
(defn upsert-post [post]
  (let [p (get-post post)
        updated-post (merge p post)]
    (->> (s/setval [(s/filterer #(= (:id %) (:id post))) s/FIRST]
                   updated-post @post-list)
         ;; update the post state
         (reset! post-list))
    ;; return post
    updated-post))

;; takes a list of field/values and updates the post with only those
;; use like (update-post p :disabled true :title "new title"), which will _only_ touch and update
;; disabled in the state
(defn update-post [post & {:as kv-map}] ;; destructure the rest of the args list to a map
  (upsert-post (merge {:id (:id post)} ;; add only the post id to the map, for identity's sake
                      kv-map)))

;; a very simple function to transition the state key, in lieu
;; of a full FSM
(defn post-transition-state! [post game-state]
  ;; check to see if it's a valid state, don't enforce any rules about
  ;; when state transitions are allowed, the business logic can do that
  (when (not (contains? #{:timed-out :shared :blocked :live} game-state))
    (log/error "this post can't transition to the state"))
  (-> post
      (assoc :game-state game-state)
      (upsert-post)))

(defn add-post [post]
  ;; remove the post if it is already in the state
  (let [posts (-> (remove #(= (:id post) (:id %)) @post-list)
                  ;; add the new post
                  (conj post))]
    ;; update the state
    (reset! post-list posts)))

(defn add-post-comment [comment]
  (let [post (get-post {:id (:post-id comment)})
        comments (or (:comments post) [])]
    (if post
      (update-post post :comments (conj comments comment))
      (log/warn "post id" (:post-id comment) "not found"))))
;;
;;   notifications
;;     IDENTITY of a notification is it's timestamp,
;;     which is used to reference it
;;
;;

(defn get-notification [n] ;; get a full notification by something notification-like (min of {:time 1122})
  ;; use specter to update the post at the path where the timestanps are the same
  (first (s/select [(s/filterer #(= (:time %) (:time n))) s/FIRST]
                   @notifications)))

(defn update-notification [n]
  (->> ;; use specter to update the post at the path where the ids are the same
        (s/setval [(s/filterer #(= (:time %) (:time n))) s/FIRST]
                  n @notifications)
        ;; update the post state
        (reset! notifications)))

(defn disable-notification [n]
  (update-notification (assoc n :active false)))

(defn add-notification [{:as n, typ :type, text :text}]
  ;; create the norification, adding to it the internal tracking information we need
  (let [new-n {:type typ
               :active true ; the UI will show the notification as long as it is active
               :text text
               ;; add the timestamp on adding to state
               :time (utils/timestamp-now)}]
    ;; add the new notification to the state
    (swap! notifications conj new-n)
    ;; in 2 seconds, disable that newly created notification:
    (async/go (async/<! (async/timeout 6000))
              (disable-notification new-n))))
