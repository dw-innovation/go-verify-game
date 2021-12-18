(ns kid-shared.ticks
  (:require [cljs.core.async :as async :include-macros true]
            [reagent.core :as r]
            [kid-game.utils.log :as log]
            [clojure.spec.alpha :as s]))

;; usage of these functions:
;;
;;  start the ticker:
;;  (start!)
;;
;; Add some repeating functions on ticks:
;;
;; (every 1 :tick (fn [] (println :tick @num)))
;; (every 3 :tock (fn [] (println :tock @num)))
;;
;; Stop one of the repeating fucnctions:
;;
;; (stop :tick)
;;
;; Schedule a function after n amounts of ticks
;;
;; (after 14 (fn [] (println "hellloooooooooooooooooooooo")))
;;
;; Use ticks in an core.async channel:
;;
;; (async/go-loop []
;;   (async/<! (wait-chan 14))
;;   (println "from go-loop" @num)
;;   (recur))

;; the config value for the amount of milliseconds per tick
(def ms-per-tick (atom 1000))

(defonce ticks (r/atom 0))

(def paused (atom false))

(defn pause [] (reset! paused true))
(defn continue [] (reset! paused false))

(defn paused? [] @paused)

(defonce tick-fn (atom (fn [])))
;; start the ticker!!
(defn start!
  "starts the ticker"
  []
  (reset! paused false)
  (reset! tick-fn (fn []
                    (when (not @paused)
                      (reset! ticks (inc @ticks)))
                    ;; recur
                    (js/window.setTimeout @tick-fn @ms-per-tick)))
  ;; start the tail recur:
  (.setTimeout js/window @tick-fn @ms-per-tick))

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

(defn stop
  "stop a recurring function that had been initialized with every, use the same key passed to every"
  [key] (remove-watch ticks key))

(defn after
  "run the supplied function after n ticks"
  [n fun] (let [k (gensym)
                s @ticks]
            (add-watch ticks k (fn [key a os ns]
                                 (when (> (- ns n) s)
                                   (fun)
                                   (stop k))))))

;; run function for n amount of ticks, on every tick
(defn for-ticks [n fun] (doall (map #(after % fun) (range n))))

(defn wait-chan
  "return a channel which will receive an item after n wait ticks"
  [n] (let [c (async/chan)]
        (after n (fn [] (async/put! c "elapsed")))
        c))

;; I guess this does not work in cljs, just clj.  putting here for posterity.
(defmacro with-ticker [& form]
  `(let ~['ticker ticks] ~@form))
