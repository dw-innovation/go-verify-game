(ns kid-game.utils.log
  (:require [kid-shared.types.shared :as shared]
            [clojure.spec.alpha :as s]))

(defmacro debug [& args]
  `(do
     ;; in case we need more here
     ;; TODO only print if dev
     (println (str "[" ~(str (:name (:ns &env))) "]") "\n" ~@args)))
