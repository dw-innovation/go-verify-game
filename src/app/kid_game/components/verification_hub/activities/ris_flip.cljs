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

;; TODO write function spec to check this pred
(defn is-step-function?
  "to be a step function, it must return [atom, component]"
  [ret] (let [[status <component>] ret]
          (and (atom? status)
               (status #{:strted :failed :succeeded})
               (fn? <component>))))

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

;; documentation for css transition group seems kind of tricky but is here:
;; https://reactcommunity.org/react-transition-group/
(def css-transition-group
  (r/adapt-react-class react-transition-group/TransitionGroup))

(def css-transition
  (r/adapt-react-class react-transition-group/CSSTransition))


(defn drag-step []
  "returns a vector of type [atom, reagent-function] where atom is the status of the step"
  (let [status (r/atom :started)
        dragged? (r/atom false)
        loading? (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)
                               (async/<! (async/timeout 1000))
                               (reset! status :succeeded)
                               ))]
    [status
    (fn [{img-src :img-src
          search-results :search-results}]
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
              [:hr]]]))]])]))

(defn flip-step []
  "returns a vector of type [atom, reagent-function] where atom is the status of the step"
  (let [status (r/atom :started)
        flip! (fn []
                (reset! status :flipping)
                (async/go
                  (async/<! (async/timeout 2000)) ;sync this timout with animation length on line +2
                  (reset! status :succeeded)))
        animation-css {:animation "flip-animate 2s ease-in-out"
                       :animation-fill-mode "forwards"}]
    [status
     (fn [{img-src :img-src}]
        [:div {:class "is-flex is-justify-content-center mb-5"}
         [:div
          [:div
           [:h3.title.is-3 "No Results?"]
           [:p.subtitle "Try flipping the image and searching again"]
           ]
          [:div.mt-3 {:style (when (#{:flipping :succeeded} @status) animation-css)}
           [:img {:src img-src}]]
          [:div
           [:button {:on-click flip!} "Flip!"]]
          ]]
        )]))


(defn <main> [{:as data
               main-image :main-image
               flipped-image :flipped-image
               search-results :result-search} back!]
  (let [[s1 <s1>] (drag-step)
        [s2 <s2>] (flip-step)
        [s3 <s3>] (drag-step)]
    (fn []
      [:div.activity-container
       [:div.activity-step.contain-section-width.center-section
        [<s1> {:img-src main-image :search-results []}]]
       [:div.activity-step.contain-section-width.center-section
        (when (= @s1 :succeeded) [<s2> {:img-src main-image}])]
       [:div.activity-step.contain-section-width.center-section
        (when (#{:succeeded} @s2) [<s3> {:img-src flipped-image
                                         :search-results search-results}])]])))
