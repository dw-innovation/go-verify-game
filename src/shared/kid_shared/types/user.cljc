(ns kid-shared.types.user
  (:require [clojure.spec.alpha :as s]
            [kid-game.utils.core :refer [new-uuid timestamp-now]]
            [kid-shared.types.shared :as shared]))

(s/def ::role #{:manipulator :media :investigator :neutral})

(s/def ::name string?)

(s/def ::user (s/keys :req-un [::shared/id
                               ::shared/created
                               ::name
                               ::role]))

(s/def ::user-map (s/map-of ::shared/id ::user))

(def default-user {:id (new-uuid)
                   :created (timestamp-now)})

(defn create [{:as partial-user
               name :name
               role :role}]
  (merge default-user partial-user))

(def u1 (create {:name "User 1" :role :manipulator}))
(def u2 (create {:name "User 2" :role :manipulator}))

(defn col-to-map [us] (zipmap (map :id us) us))
(col-to-map [u1 u2])

(defn same? [u1 u2]
  (= (:id u1) (:id u2)))

(same? u1 u2) ; bunk test
