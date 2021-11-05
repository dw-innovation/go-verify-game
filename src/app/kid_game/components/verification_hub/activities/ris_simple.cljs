(ns kid-game.components.verification-hub.activities.ris-simple
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.components.verification-hub.activities.shared.components :as components]
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

(defn <main> [{:as data
               result-images :result-images
               main-image :main-image
               result-search :result-search}
              back!]
  (let [dragged? (r/atom false)
        loading? (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)))]
    (fn []
      [:div.activity-container.ris-simple
       [components/<header> icons/recycle-search "Reverse Image Search" "See where this image might come from"
        "Reverse Image Search Explanation"
        [:p "more text"]]
       [:div.activity-step.contain-section-width.center-section
        [image-results/<dragger> main-image drag-done!]
        [css-transition-group {:class "transition-results"}
         (if @loading?
           [image-results/<loading>]
           (when @dragged?
             [css-transition {:class-names "ris-results-transition"}
              [:div.ris-results
               [:h3.ris-result-header "Pages with matching images:"]
               [image-results/<search-results> result-search]
               [:h3.ris-result-header "Similar images:"]
               [image-results/<image-results> result-images]]]))]]
       [:hr]
        [components/<activity-actions> back!]])))
