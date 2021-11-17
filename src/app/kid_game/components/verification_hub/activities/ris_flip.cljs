(ns kid-game.components.verification-hub.activities.ris-flip
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.components.verification-hub.activities.shared.svg-utils :as svg-utils]
            [kid-shared.data.blocks :as blocks]
            [kid-game.components.verification-hub.activities.shared.components :as components]
            [cljs.core.async :as async :include-macros true]
            [kid-game.state :as state]))

(defn atom? [x]
  (try (do @x true) (catch :default e false)))
;; TODO move these to tests
(= false (atom? 3))
(= true (atom? (atom 3)))
(= true (atom? (r/atom 3)))

(defn is-step-function?
  "to be a step function, it must return [atom, component]"
  [ret] (let [[status <component>] ret]
          (and (atom? status)
               (status #{:strted :failed :succeeded})
               (fn? <component>))))

(defn empty-step []
  [(r/atom :idle) (fn [] [:<> "empty"])])

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))


(defn <drag-step> [{img-src :img-src
                    search-results :search-results}]
  "returns a vector of type [atom, reagent-function] where atom is the status of the step"
  (let [status (r/atom :started)
        dragged? (r/atom false)
        loading? (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)
                               ))]
    (fn []
      [:div
       [image-results/<dragger> img-src drag-done!]
       [css-transition-group {:class "transition-results" :timeout 100}
        (if @loading?
          [:div {:class "is-flex is-justify-content-center"} [image-results/<loading>]]
          (when @dragged?
            [css-transition {:class-names "ris-results-transition" :timeout 100}
             [:div
              [:h4.title.is-4.mb-2 "Results from your image search"]
              [:h3.ris-result-header [:b (count search-results)] " page(s) with matching images"]
              [image-results/<search-results> search-results]
              [:hr]]]))]])))

(defn flip-step [{img-src :img-src text :text} ]
  "returns a vector of type [atom, reagent-function] where atom is the status of the step"
  (let [status (r/atom :started)]
    [status
     (fn []
       [:<>
        [:hr]
        text
        [:button {:on-click (fn [] (reset! status :succeeded) (println "won"))} "win me"]
        [:button {:on-click (fn [] (reset! status :failed) (println "failed"))} "fail me"]
        ])]))


(defn <main> [{} back!]
  (let [[s1 <s1>] (flip-step {:text "first-step"})]
    (fn []
      (let [[s2 <s2>] (do  (case @s1 :succeeded (flip-step {:text "you did it!"})
                                 (empty-step)))]
        [(fn []
           (let [[s3 <s3>] (flip-step {:text (case @s2 :succeeded "second task done"
                                                   :failed "second task failed"
                                                   "")})]
             [(fn []
                [:div
                 [<s1>]
                 [<s2>]
                 [<s3>]])]))]))))
