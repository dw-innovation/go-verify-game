(ns kid-shared.ticks
  (:require [cljs.core.async :as async :include-macros true]
            [reagent.core :as r]
            [clojure.spec.alpha :as s]))

;; the config value for the amount of milliseconds per tick
(def ms-per-tick (atom 500))

(defonce ticks (r/atom 0))

(def paused (atom false))

(defn pause [] (reset! paused true))
(defn continue [] (reset! paused false))

(defn paused? [] @paused)

(defn tick []
  ; (println "tick" @num)
  (when (not @paused)
    (reset! ticks (inc @ticks)))
  (js/window.setTimeout tick @ms-per-tick))

(.setTimeout js/window tick @ms-per-tick)

(defn every
  ([n funk] (every n (gensym) funk))
  ([n key funk] (add-watch ticks key
                           (fn [key atom old new]
                             (when (= 0 (mod new n)) (funk))) )))

(defn stop [key] (remove-watch ticks key))

;; do a function in n ticks
(defn after [n fun] (let [k (gensym)
                          s @ticks]
                   (add-watch ticks k (fn [key a os ns]
                                      (when (> (- ns n) s)
                                        (fun)
                                        (stop k))))))

;; run function for n amount of ticks, on every tick
(defn for-ticks [n fun] (doall (map #(after % fun) (range n))))

;; returns a channel you can wait on for n amount of ticks
(defn wait-chan [n] (let [c (async/chan)]
                      (after n (fn [] (async/put! c "elapsed")))
                      c))

;; (every 1 :tick (fn [] (println :tick @num)))
;; (every 3 :tock (fn [] (println :tock @num)))
;; (stop :tick)

;; (after 14 (fn [] (println "hellloooooooooooooooooooooo")))


;; (async/go-loop []
;;   (async/<! (wait-chan 14))
;;   (println "from go-loop" @num)
;;   (recur))
