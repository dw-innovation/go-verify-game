(ns kid-game.components.notifications
  (:require [reagent.core             :as r]
            [kid-game.state           :as state]
            [react-transition-group]))

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

(defn <notification> [n]
  (when (:active n)
    (let [bg (copy-color (:type n))]
      [css-transition {:timeout     200
                       :key         (:time n)
                       :class-names "notification-transition"}
       ^{:key (:time n)} ;; important to keep track of rendering
       [:div.card.notification  [:div {:class ["card-content " bg]} [:b (:text n)]]]])))

(defn <notifications> []
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
  [css-transition-group {:class "notifications"}
   (map <notification> @state/notifications)])
