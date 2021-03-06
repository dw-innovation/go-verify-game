(ns kid-game.components.verification-hub.activities.ris-simple
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.components.verification-hub.activities.shared.components :as components]
            [kid-shared.data.blocks :as blocks]
            [cljs.core.async :as async :include-macros true]
            [kid-game.state :as state]))

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/

(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))

(defn <main> [{:as           data
               result-images :result-images
               main-image    :main-image
               result-search :result-search}
              back!]
  (let [dragged?   (r/atom false)
        loading?   (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)))]
    (fn []
      [:div.activity-container.ris-simple

       [:div {:class "activity-header"}
        [components/<header> icons/recycle-search "Reverse Image Search" "See where this image might come from"
         "Reverse Image Search Explanation"
         blocks/ris-explanation]]

       [:div {:class "activity-steps"}
        [:div.activity-step.contain-section-width.center-section
         [image-results/<dragger> main-image drag-done!]
         [css-transition-group {:class "transition-results"}
          (if @loading?
            [:div {:class "is-flex is-justify-content-center"} [image-results/<loading>]]
            (when @dragged?
              [css-transition {:class-names "ris-results-transition"
                               :timeout     2}
               [image-results/<blooble-image-results> result-search]]))]]]

       [:div {:class "activity-footer"}
        [components/<activity-actions> back!]]])))
