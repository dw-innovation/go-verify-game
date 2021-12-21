; server - state
(ns kid-game.state
  (:require
   [clojure.pprint :as pp]
   [clojure.core.async :as async]
   [kid-shared.post-generator :as postgen]
   [medley.core :refer [random-uuid]]))

(defn log [& xs] (dorun (map pp/pprint xs)))

(defn new-room-channel [] (async/chan 1 (map #(assoc % :id (random-uuid)))))

(def state (atom {:rooms {}}))

(defn get-rooms [] (:rooms @state))
(defn get-rooms-list [] (vals (get-rooms)))
(defn get-room [id] ((get-rooms) id))

(defn create-room [id]
  (let [channel (new-room-channel)
        mult    (async/mult channel)]
    ;; create the room in the state
    (->> {:id      id
          :channel channel
          :mult    mult
          :posters (postgen/attach-default-room-poster channel)
          :users   {}}
         ((fn [x] (log "created room!" x) x))
         (assoc-in @state [:rooms id])
         (reset! state)))
  ;; after room has been created, return what you just got
  (get-room id))

(defn get-or-create-room [id]
  (or (get-room id) (create-room id)))

(defn get-user-map [room-id]
  (get-in @state [:rooms room-id :users]))

(defn get-user-list [room-id] (vals (get-user-map room-id)))

(defn add-user [room-id user-id user-name]
  (reset! state
          (assoc-in @state [:rooms room-id :users user-id] user-name)))

(defn remove-user [room-id user-id]
  (reset! state
          (update-in @state [:rooms room-id :users] dissoc user-id)))

