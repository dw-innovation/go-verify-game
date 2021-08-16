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
;;    outside world
;;
;;
;;
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

(defn panel [] (:active-panel @app-state))

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
;;
;;

(defn posts [] @post-list)

(defn update-post [post]
  (->>
   ;; use specter to update the post at the path where the ids are the same
   (s/setval [(s/filterer #(= (:id %) (:id post))) s/FIRST]
             post @post-list)
   ;; update the post state
   (reset! post-list)))

(defn get-post [post] ;; get a full post by something post-like (min of {:id 'something'})
  ;; use specter to update the post at the path where the ids are the same
  (first (s/select [(s/filterer #(= (:id %) (:id post))) s/FIRST]
                   @post-list)))

(defn disable-post! [post]
  (update-post (assoc post :disabled true)))

(defn attatch-post-timer [post]
  (async/go-loop []
    (let [p (get-post post)
          time-left (or (:time-left p) (:time-limit post) 0)]
      (when (> time-left 0) (do (-> (assoc p :time-left (dec time-left)) ; deprecate time left and save
                                    (update-post))
                                (async/<! (async/timeout 100)) ; wait for the interval
                                (recur))))))

(defn add-post [post]
  ;; TODO validate that it's an actual valid post
  ;; remove the post if it is already in the state
  (let [posts (-> (remove #(= (:id post) (:id %)) @post-list)
                  ;; add the new post
                  (conj post))]
    ;; update the state
    (reset! post-list posts))
  ;; attatch a time decreaser to the post, but only if time limiet
  (when (:time-limit post) (attatch-post-timer post)))

;;
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
    (async/go (async/<! (async/timeout 3000))
              (disable-notification new-n))))
