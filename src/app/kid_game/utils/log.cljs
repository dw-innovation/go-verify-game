(ns kid-game.utils.log
  (:require [kid-shared.types.shared :as shared]
            [clojure.spec.alpha :as s]))

(def debug println)
(def warn js/console.warn)
(def error js/console.error)
