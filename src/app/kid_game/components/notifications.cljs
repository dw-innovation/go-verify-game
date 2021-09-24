(ns kid-game.components.notifications
  (:require [reagent.core             :as r]
            [react-transition-group]
            [kid-game.state           :as state]))

;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))

(defn copy-color [type]
  (cond
    (= type :success)     "has-background-success-light has-text-success"
    (= type :warning)     "has-background-danger-light has-text-danger"
    :else                 "has-background-warning-light has-text-warning"))

(defn <notifications> []
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
  [css-transition-group {:class "notifications"}
   (for [n @state/notifications]
     (when (:active n)
       (let [bg (copy-color (:type n))]
         [css-transition {:timeout     200
                          :key         (:time n)
                          :class-names "notification-transition"}
          ^{:key (:time n)} ;; important to keep track of rendering
          [:div.card [:div {:class ["card-content " bg]} [:b (:text n)]]]])))])
