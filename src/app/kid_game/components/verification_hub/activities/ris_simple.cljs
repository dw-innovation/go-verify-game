(ns kid-game.components.verification-hub.activities.ris-simple
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
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

(defn <header> []
[:div.activity-header
 [:div.columns
 [:div.activity-icon
  [icons/recycle-search]]
    [:div.activity-title "Simple Reverse Image Search"]]])

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
       [<header>]
       [image-results/<dragger> main-image drag-done!]
       [:div.activity-step
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
       [:div.columns.activity-actions
        [:div.column.action
         [:p "Ready to make a call?"]
         [:button {:on-click (fn [] (state/open-timeline))} "Back to timeline"]]
        [:div.column.action
         [:p "Investigate further?"]
         [:button {:on-click back!} "Back to hub"]]]
       ])))
