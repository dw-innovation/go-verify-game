(ns kid-game.components.verification-hub.activities.ris-crop
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [react-transition-group]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.components.verification-hub.activities.shared.svg-utils :as svg-utils]
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
  [:div {:class "columns is-centered is-vcentered"}
   [:div {:class "column is-1"} [icons/recycle-search]]
   [:div {:class "title is-3 pl-5"
          :style {:color "var(--color-hub-primary)"}}
    "Reverse Image Search, with Crop"]])

(defn <actions> []
  [:div.columns.activity-actions.tile.notification.is-success
   [:div.column.has-text-centered
    [:p "Ready to make a call?"]
    [:button {:class "button is-primary is-inverted is-outlined"
              :on-click (fn [] (state/open-timeline))}
     [:span.icon [:i {:class "fas fa-newspaper-o"}]] [:span "Back to timeline"]]]
   [:div.column.has-text-centered
    [:p "Investigate further?"]
    [:button {:class "button is-primary is-inverted is-outlined"
              :on-click (fn [] (state/open-timeline))}
     [:span.icon [:i {:class "fas fa-search"}]] [:span "Back to hub"]]]])

;; takes two points ([x y]) (in any order)
;; and returns [offset-x offset-y width height] of the resulting rectangle
(defn extract-rectangle [[x1 y1]
                         [x2 y2]]
  (let [min-x (min x1 x2)
        min-y (min y1 y2)
        max-x (max x1 x2)
        max-y (max y1 y2)]
    [min-x min-y (- max-x min-x) (- max-y min-y)]))

;; ; makes a rectangle out of two points!
(defn <rectangle> [[x1 y1 :as p1]
                   [x2 y2 :as p2]]
  (let [[offset-x offset-y width height] (extract-rectangle p1 p2)]
    [:rect {:width width
            :height height
            :transform (str "translate(" offset-x "," offset-y ")")}]))

;; note -> this component returns not a component, but a vector of components and internal state,
;; which parent components are welcome to use
;; returns [svg-cropper   ; component to allow the user to crop an svg
;;          cropped-svg   ; a component that is the internal cropped area of the image
;;          correct-crop? ; a _reagent atom_ which contains a bool on if the crop is correctly inside the hit box
;;          ]
;; so one svg get's returned that is the cropped region of the original image
(defn cropper-components [{:as data
                           url :main-image
                           [width height] :dimensions
                           crop-hit-box :crop-hit-box}
                          succeed!
                          fail!]
  (let [point-first (r/atom nil) ; save an [x y] position where we started the crop
        correct-first? (r/atom false)
        point-intermediate (r/atom nil) ; save our current hovering point
        point-second (r/atom nil) ; save a [x y] position where we stopped the crop
        correct-second? (r/atom false)
        svg (r/atom nil) ; will be a ref to the svg element
        hit-box (r/atom nil) ; will be a ref to the svg element
        correct-crop? (r/atom false)
        ; handle a click:
        mousedown! (fn [evt] (let [[x y] (svg-utils/get-svg-coordinates @svg evt)]
                               (reset! point-second nil)
                               (reset! point-first [x y])))
        mouseup! (fn [evt] (let [[x y] (svg-utils/get-svg-coordinates @svg evt)]
                             (reset! point-second [x y])
                             (if @correct-second?
                               (when succeed! (succeed!) (reset! correct-crop? true))
                               (when fail! (fail!) (reset! correct-crop? false)))))
        mousemove! (fn [evt] (when (and @svg @point-first (not @point-second))
                               (let [[x y] (svg-utils/get-svg-coordinates @svg evt)]
                                 (reset! point-intermediate [x y]))))
        ;; the following controls the switches for when the user did things correctly
        ;; testing to see if the points are in the fills of the path is too hard and janky
        ;; so we have a little hack here.  first correct click logs a true,
        ;; then the second can act on that.  the evaluation happens in the mouseup! above, where the state
        ;; of the switches is checked.
        first-success! (fn []
                         (reset! correct-second? false)
                         (reset! correct-first? true))
        second-success! (fn []
                          (if @correct-first?
                            (do (reset! correct-second? true)
                                (reset! correct-first? false))
                            (do (reset! correct-first? false)
                                (reset! correct-second? false))))
        <cropper-component> (fn []
                              [:div.cropper
                               [:svg {:ref #(reset! svg %)
                                      :viewBox (str "0 0 " width " " height)
                                      :on-mouse-down mousedown!
                                      :on-mouse-up mouseup!
                                      :on-mouse-move mousemove!}
                                        ; first our backroung image to span across the back of the svg
                                [:image {:width width :heigh height :href url}]
                                        ; show the image that we have drawn
                                (when (and @point-first @point-second)
                                  [:g.chosen-region
                                   [<rectangle> @point-first @point-second]])
                                (when (and @point-first @point-intermediate)
                                  [:g.drag-region
                                   [<rectangle> @point-first @point-intermediate]])
                                [:g.hit-box {:ref #(reset! hit-box %)
                                             :on-mouse-down first-success!
                                             :on-mouse-up second-success!}
                                 crop-hit-box]]])
        <cropped-svg> (fn []
                        (if (and @point-first @point-second)
                          (let [[offset-x offset-y rectangle-width rectangle-height] (extract-rectangle @point-first @point-second)]
                            [:svg {:viewBox (str offset-x " " offset-y " " rectangle-width " " rectangle-height)}
                             [:image {:width width :heigh height :href url}]])
                          [:svg {:viewBox (str "0 0 " width " " height)}
                           [:image {:width width :heigh height :href url}]]))]
    [<cropper-component> <cropped-svg> correct-crop?]))

(defn <drag-step> [drag-img search-results image-results done!]
  (let [dragged? (r/atom false)
        loading? (r/atom false)
        drag-done! (fn []
                     (reset! loading? true)
                     (async/go (async/<! (async/timeout 2000))
                               (reset! dragged? true)
                               (reset! loading? false)
                               (done!)))]
    (fn []
      [:div
       [image-results/<dragger> drag-img drag-done!]
       [css-transition-group {:class "transition-results" :timeout 100}
        (if @loading?
          [image-results/<loading>]
          (when @dragged?
            [css-transition {:class-names "ris-results-transition" :timeout 100}
             [:div.ris-results
              [:h3.ris-result-header "Pages with matching images (" (count search-results) ")"]
              [image-results/<search-results> search-results]
              [:h3.ris-result-header "Similar images (" (count image-results) ")"]
              [image-results/<image-results> image-results]]]))]])))

(defn <main> [{:as data
               result-images :result-images
               main-image :main-image
               result-search :result-search
               result-search-after-crop :result-search-after-crop}]
  (let [cropping-step (r/atom (fn [])) ; the steps rely on eachother so initialize empty
        second-drag-step (r/atom (fn [])) ; the steps rely on eachother so initialize empty
        cropped-correctly? (r/atom false)
        make-second-step (fn [] (fn [c] [:div
                                         [:h3 "Try searching again, with the cropped image"]
                                         [<drag-step> [c] (if @cropped-correctly? result-search-after-crop []) [] (fn [])]]))
        cropped-correctly! (fn []
                             (reset! cropped-correctly? true)
                             (reset! second-drag-step (make-second-step)))
        cropped-wrong! (fn []
                         (reset! cropped-correctly? false)
                         (reset! second-drag-step (make-second-step)))
        [<cropper> <cropped-svg>] (cropper-components data cropped-correctly! cropped-wrong!)

        make-cropping-step (fn [] (fn [] [:div
                                          [:h3.is-3 "no results? Crop the image"]
                                          [:p "by clicking and dragging"]
                                          [:div [<cropper>]]]))
        <cropping-step> (fn [] [@cropping-step])
        <first-drag-step> (fn [] [<drag-step> main-image result-search result-images (fn [] (reset! cropping-step (make-cropping-step)))])
        <second-drag-step> (fn [] [@second-drag-step <cropped-svg>])]
    (fn []
      [:div.activity-container.ris-simple
       [<header>]
       [:div.activity-step
        [<first-drag-step>]]
       [:div.activity-step
        [<cropping-step>]]
       [:div.activity-step
        [<second-drag-step>]]
       [<actions>]])))
