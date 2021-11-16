(ns kid-game.utils.log
  (:require [kid-shared.types.shared :as shared]
            [clojure.spec.alpha :as s]))


;;
;;  debug macro
;;   use like: (log/debug "handling message" msg)
;;
;;  this will result in something like "[kid-game.business] handling message {}"
;;   - the namespace has automatically been added
;;
;;   if you want to mute all other debug calls, then you can pass the :only flag like:
;;     (log/debug :only "handling message" msg)
;;     - this will mute all other prints except those with the only flag
;;
(defmacro debug [& args]
  `(do
     ;; set the print-all? atom to false, if :only has been passed as a flag
     (when (some #(= :only %) [~@args]) (reset! log/print-all? false))
     ;; in case we need more here
     ;; TODO only print if dev
     (when (or (some #(= :only %) [~@args]) @log/print-all?)
              (println (str "[" ~(str (:name (:ns &env))) "]") "\n" ~@args))))
