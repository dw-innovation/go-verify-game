(ns kid-game.utils.core
  (:require [kid-shared.types.shared :as shared]
            [cljs.core :refer [random-uuid]]
            [clojure.spec.alpha :as s]))

;; mirrored utils
;;   utils that also exist in cljs server
;;
(defn timestamp-now [] (.getTime (js/Date.)))

(defn new-uuid [] (str (random-uuid)))

;; app only utils

(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn indexes [pred coll]
   (keep-indexed #(when (pred %2) %1) coll))

