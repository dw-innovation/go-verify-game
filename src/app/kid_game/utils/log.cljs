(ns kid-game.utils.log
  (:require [kid-shared.types.shared :as shared]
            [clojure.spec.alpha :as s]))

(def debug println)
(def warn println)
(def error js/console.error)
