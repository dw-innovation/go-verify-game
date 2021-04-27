(ns kid-game.example-activities
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-game.socket :as socket]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.components.chat.core :as <chat>]
            [kid-game.components.posts.core :as <posts>]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))

; outside functions
(def global-points (atom 0))
(defn award! [ps] (swap! global-points + ps))
(defn penalize! [ps] (swap! global-points - ps))

; state machine
(declare fsm-1)
(declare transition)
(declare get-options)
(declare update-state)

(defn option [key & {:keys [interactive? if then]}]
  {:key key
   :interactive? interactive?
   :if if
   :then then})

(defn update-state [atom-state fsm partial-state]
  ; (println (merge @atom-state partial-state))
  (let [new-state (merge @atom-state partial-state)]
    ; update the passed atom
    (reset! atom-state new-state)
    ; run the fsm on the new state:
    (->> fsm
         (get-options atom-state)
         ((fn [o] (println o) o))
         (filter #(not (:interactive? %)))
         ((fn [o] (println o) o))
         (filter #(:if %))
         ((fn [o] (println (count o)) (println (type o)) o))
         (map (fn [o] (println "hi") (println o) ((:then o))))
         doall
         )
  ))

(defn get-options [state fsm]
  (let [goto (fn [status] (update-state state fsm {:status status}))
        status (:status @state)
        f (fsm state goto)]
    (if-let [options (status f)]
      options
      (println "status does not exist in fsm!"))))

(defn available-options [state fsm]
  (->> (get-options state fsm)
       (filter :interactive?)
       (filter :if)))


(def activity-1
  {:id "activity-1"
   :description "description of activity 1"
   :fake-news? false
   :image "path-to-image"
   :status :start
   :time 30
   :comments {"not true" :negative
              "so true" :positive}})


















; an fsm is a function that acts on a current state.  the fsm should be applied to the state on
; every state change to trigger those changes
(def fsm-1
  (fn [state goto]
    (let [time (:time @state)
          fake-news? (:fake-news @state)
          >> goto]
      {:start [(option :flag! :if (> time 200) :interactive? true
                       :then (fn []
                               (if fake-news? (award! 10) (penalize! 10))
                               (>> :flagged)))
               (option :repost! :if true :interactive? true
                       :then (fn []
                               (if fake-news? (penalize! 20) (award! 10))
                               (>> :reposted)))
               (option :time-out! :if (< time 1)
                       :then (fn []
                               (penalize! 30)
                               (>> :timed-out)))]
       :flagged [(option :go-home :if true :interactive? true
                         :then (fn []
                                 (penalize! 5)
                                 (>> :start)))]
       :timed-out []
       :reposted [(option :go-home :if true :interactive? true
                         :then (fn []
                                 (penalize! 5)
                                 (>> :start)))]})))

(defn choose [option]
  ((:then option)))




(defn <option> [o] [:button {:on-click (fn [] (choose o))
                             :title (:key o)} (:key o)] )

(def state (atom activity-1))

(defn <activity-1> []
  (let [state (atom activity-1)
        options (fn [] (available-options state fsm-1))
        reduce-time (fn [] (let [time (:time @state)
                                new-time (if (< 0 time) (dec time) 0)]
                             (if (> (:time @state) 0) (update-state state fsm-1 {:time new-time}))))]
    (js/setInterval #(reduce-time) 100)
    (fn []
      [:div
       [:time-left (:time @state)]
       [:div "current state: " (:status @state)]
       [:div "options: "  (map <option> (options))]
       [:h4 "yo"]])))

(defn <examples> [] [:div [:h3 @global-points] [<activity-1>]])
