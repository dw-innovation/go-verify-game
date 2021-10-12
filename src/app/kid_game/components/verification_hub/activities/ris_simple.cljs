(ns kid-game.components.verification-hub.activities.ris-simple
  (:require [reagent.core :as r]
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
;; filled out lovingly by css
;; TODO alt for screenreaders
(defn <loading> [] [:div.lds-ring [:div] [:div] [:div] [:div]])

(defn <reverse-image-simple> [{:as data
                               result-images :result-images
                               main-image :main-image
                               result-search :result-search}]
  (let [dragged? (r/atom false)
        loading? (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)))]
    (fn []
      [:div.activity-container
       [:h3 "Simple Reverse Image Search"]
       [image-results/<dragger> main-image drag-done!]
       [css-transition-group {:class "transition-results"}
        (if @loading?
          [<loading>]
          (when @dragged?
            [css-transition {:class-names "ris-results-transition"}
             [:div.ris-results
              [:h3.ris-result-header "Pages with matching images:"]
              [image-results/<search-results> result-search]
              [:h3.ris-result-header "Similar images:"]
              [image-results/<image-results> result-images]]]))]
       [:hr]
       [:div.columns.is-centered
        [:div.column.is-4.has-text-centered
         [:h5.title.is-5 "Ready to make a call?"]
         [:button {:class "button"
                   :on-click (fn [] (state/open-timeline))}
          [:span.icon.is-small [:i {:class "fa fa-arrow-left"}]] [:span "Back to the timeline"]]]]])))
