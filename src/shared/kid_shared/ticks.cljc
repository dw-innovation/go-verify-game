(ns kid-shared.ticks
  (:require [cljs.core.async :as async :include-macros true]
            [reagent.core :as r]
            [kid-game.utils.log :as log]
            [clojure.spec.alpha :as s]))

;; # usage
;;
;;  start the ticker:
;;
;; (ticks/start!)
;;
;; Adjust the speed:
;;
;; (ticks/set-tick-speed! 2000)
;;
;; Add some repeating functions on ticks:
;;
;; (ticks/every 5 (fn [] (println "hello")))
;;
;; Supply keys to make them stoppable
;;
;; (ticks/every 1 :tick (fn [] (println :tick @num)))
;; (ticks/every 3 :tock (fn [] (println :tock @num)))
;;
;; (ticks/cancel :tick)
;; (ticks/cancel :tock)
;;
;; Schedule a function after n amounts of ticks
;;
;; (ticks/after 14 (fn [] (println "hellloooooooooooooooooooooo")))
;;
;; Cancel something you scheduled
;;
;; (ticks/after 14 :print-hi (fn [] (println "hi")))
;; (ticks/cancel :print-hi)
;;
;; Use ticks and waiting in an core.async channel:
;;
;; (async/go-loop []
;;   (async/<! (ticks/wait-chan 14))
;;   (println "from go-loop")
;;   (recur))

;; state:
(def ms-per-tick (atom 1000)) ; the speed
(defonce ticks (r/atom 0))    ; the total amount of ticks since started, also the atom listened to
(def paused (atom false))     ; paused?
;; tick-fn is an atom so you can stop the recursive function
;; by replacing the function living at the reference with a blank lambda
(defonce tick-fn (atom (fn [])))

(defn set-tick-speed! [ms]
  (reset! ms-per-tick ms))

(defn tick-speed [] @ms-per-tick)

(defn pause [] (reset! paused true))
(defn continue [] (reset! paused false))
(defn paused? [] @paused)

(defn start!
  "starts the ticker"
  []
  (reset! paused false)
  (reset! tick-fn (fn []
                    (when (not @paused)
                      (reset! ticks (inc @ticks)))
                    ;; recur
                    (js/window.setTimeout @tick-fn (tick-speed))))
  ;; start the tail recur:
  (.setTimeout js/window @tick-fn (tick-speed)))

(defn stop!
  "stops the ticker"
  []
  (reset! ticks 0)
  (reset! tick-fn (fn [])))

(defn every
  "run a function on ever n ticks. optionally supply a key, which you can also use to stop the
  repeating function."
  ([n funk] (every n (gensym) funk))
  ([n key funk] (add-watch ticks key
                           (fn [key atom old new]
                             (when (= 0 (mod new n)) (funk))))))

(defn cancel
  "stop a recurring function that had been initialized with every, use the same key passed to every"
  [key] (remove-watch ticks key))

(defn after
  "run the supplied function after n ticks, add key to allow cancelling"
  ([n fun] (after n (gensym) fun))
  ([n k fun] (let [s @ticks]
               (add-watch ticks k (fn [key a os ns]
                                    (when (> (- ns n) s)
                                      (fun)
                                      (cancel k)))))))

;; run function for n amount of ticks, on every tick
(defn for-ticks
  ([n fun] (for-ticks n (gensym fun)))
  ([n k fun] (doall (map #(after % fun) (range n)))))

(defn wait-chan
  "return a channel which will receive an item after n wait ticks"
  [n] (let [c (async/chan)]
        (after n (fn [] (async/put! c "elapsed")))
        c))

;; I guess this does not work in cljs, just clj.  putting here for posterity.
(defmacro with-ticker [& form]
  `(let ~['ticker ticks] ~@form))
