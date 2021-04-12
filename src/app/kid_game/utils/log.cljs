(ns kid-game.utils.log
  (:require [kid-shared.types.shared :as shared]
            [clojure.spec.alpha :as s]))

(def debug js/console.log)
(def warn js/console.warn)
(def error js/console.error)
