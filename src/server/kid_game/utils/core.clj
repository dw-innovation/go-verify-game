(ns kid-game.utils.core
  (:require
   [medley.core :refer [random-uuid]]))

;; mirrored utils
;;   utils that also exist in cljs app

(defn timestamp-now [] (quot (System/currentTimeMillis) 1000))

(defn new-uuid [] (str (random-uuid)))

;; server utils
