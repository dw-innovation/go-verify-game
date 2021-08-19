(ns kid-game.components.verification-hub.activities.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [cljs.core.async :as async :include-macros true]
            [kid-game.business :as business]))

;; this is the core for all of the activities,
;; and includes the mappings so that data can also
;;   call the component it is expecting


;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))

(defn <test> [arg]
  [:div
   [:h1 "I am a test component hello"]])

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
              [image-results/<image-results> result-images]]]))
        ]
       [:hr]
       [:h3.ris-result-header "Ready to make a call?"]
       [:button {:on-click (fn [] (state/open-timeline))} "<- Back to the timeline"]
     ])))


(defn <not-found> [component-name]
  [:div
   [:h1 "The activity was not found:" component-name]])


(defn get-activity [{:as activity typ :type data :data}]
  (case typ
    :test [<test> data]
    :reverse-image-simple [<reverse-image-simple> data]
    [<not-found> typ]))
