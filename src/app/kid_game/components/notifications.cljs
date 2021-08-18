(ns kid-game.components.notifications
  (:require [reagent.core :as r]
            [kid-shared.posts.stories :as stories]
            [kid-shared.generator :as gen]
            [kid-game.messaging :as messaging]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [react-transition-group]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))

;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))

(defn <notifications> []
   ;; documentation for css transition group seems kind of tricky but is here:
   ;; https://reactcommunity.org/react-transition-group/
   [css-transition-group {:class "notifications"}
    (for [n @state/notifications]
      (when (:active n)
        [css-transition {:timeout 200
                         :key (:time n)
                         :class-names "notification-transition"}
         ^{:key (:time n)} ;; important to keep track of rendering
         [:div {:class ["notification" (:type n)]}
          (:text n)]]))])
