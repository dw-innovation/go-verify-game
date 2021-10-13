(ns kid-game.components.verification-hub.activities.ris-crop
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
    [:div.activity-title "Reverse Image Search, with Crop"]]])

(defn get-svg-point [^js/SVGSVGElement svg x y]
  (let [point (.createSVGPoint svg)]
    (set! (.-x point) x)
    (set! (.-y point) y)
    point))

(defn scale-svg-point [^js/SVGSVGElement svg
                       ^js/SVGPoint svg-point]
  (.matrixTransform svg-point (-> svg (.getScreenCTM) (.inverse))))

;; given an svg DOM node and a js click event, extract the coordinates
;; relative to the svg's size
;; return [x y] or nil for failure
;; this code has been adapted from:
;; https://github.com/DW-ReCo/react-training-modules/blob/master/src/components/svg-click-image/index.tsx#L42
(defn get-svg-coordinates [^js/SVGSVGElement svg ; honestly, the typecasting here is just to get past a compiler error
                           ^js/SyntheticBaseEvent event]
  (let [point (get-svg-point svg (.-clientX event) (.-clientY event))]
    (let [cursor-point (scale-svg-point svg point)
          x (.-x cursor-point)
          y (.-y cursor-point)]
      [x y])))

;; (defn <rectangle2> [[x1 y1]
;;                     [x2 y2]]
;;   (let [min-x (min x1 x2)
;;         min-y (min y1 y2)
;;         max-x (max x1 x2)
;;         max-y (max y1 y2)]
;;     [:rect {:width (- max-x min-x)
;;             :height (- max-y min-y)
;;             :transform (str "translate(" min-x "," min-y ")")}]))

;; ; makes a rectangle out of two points!
(defn <rectangle> [[x1 y1]
                   [x2 y2]]
  (let [min-x (min x1 x2)
        min-y (min y1 y2)
        max-x (max x1 x2)
        max-y (max y1 y2)]
    [:rect {:width (- max-x min-x)
            :height (- max-y min-y)
            :transform (str "translate(" min-x "," min-y ")")
            }]))

(defn <cropper> [{:as data
                  url :main-image
                  [width height] :dimensions
                  crop-hit-box :crop-hit-box}
                 succeed!
                 fail!]
  (let [point-start (r/atom nil) ; save an [x y] position where we started the crop
        success-start (r/atom false)
        point-current (r/atom nil) ; save our current hovering point
        point-end (r/atom nil) ; save a [x y] position where we stopped the crop
        success-end (r/atom false)
        svg (r/atom nil) ; will be a ref to the svg element
        hit-box (r/atom nil) ; will be a ref to the svg element
        ; handle a click:
        mousedown! (fn [evt]
                     (when @svg
                       (let [[x y] (get-svg-coordinates @svg evt)]
                         (reset! point-end nil)
                         (reset! point-start [x y])
                         (println x y))))
        mouseup! (fn [evt]
                   (when @svg
                     (let [[x y] (get-svg-coordinates @svg evt)]
                       (reset! point-end [x y])
                       (if @success-end (js/alert "correct crop") (js/alert "wrong crop"))
                       (println x y))))
        mousemove! (fn [evt]
                     (when (and @svg @point-start (not @point-end))
                       (let [[x y] (get-svg-coordinates @svg evt)]
                         (reset! point-current [x y]))))
        ;; the following controls the switches for when the user did things correctly
        ;; testing to see if the points are in the fills of the path is too hard and janky
        ;; so we have a little hack here.  first correct click logs a true,
        ;; then the second can act on that.  the evaluation happens in the mouseup! above, where the state
        ;; of the switches is checked.
        first-success! (fn []
                         (reset! success-end false)
                         (reset! success-start true))
        second-success! (fn []
                          (if @success-start
                            (do (reset! success-end true)
                                (reset! success-start false))
                            (do (reset! success-start false)
                                (reset! success-end false))))
                     ]
    (fn []
      [:div.cropper
       [:svg {:ref #(reset! svg %)
              :viewBox (str "0 0 " width " " height)
              :on-mouse-down mousedown!
              :on-mouse-up mouseup!
              :on-mouse-move mousemove!}
        ; first our backroung image to span across the back of the svg
        [:image {:width width :heigh height :href url}]
        ; show the image that we have drawn
        (when (and @point-start @point-end)
          [:g.chosen-region
           [<rectangle> @point-start @point-end]])
        (when (and @point-start @point-current)
          [:g.drag-region
           [<rectangle> @point-start @point-current]])

        ;;[:path {:ref #(reset! hit-box %)
        ;;        :d "M9 600V9H609V600H9Z"}]
        [:g.hit-box {:ref #(reset! hit-box %)
                     :on-mouse-down first-success!
                     :on-mouse-up second-success!}
         crop-hit-box]]])))

(defn <main> [{:as data
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
      [:div.activity-container.ris-simple
       [<header>]
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
              [image-results/<image-results> result-images]]
             ]))]
[<cropper> data]
       [:hr]
       [:div.columns.activity-actions
        [:div.column.action
         [:p "Ready to make a call?"]
         [:button {:on-click (fn [] (state/open-timeline))} "Back to timeline"]]
        [:div.column.action
         [:p "Investigate further?"]
         [:button {:on-click (fn [] (state/open-timeline))} "Back to hub"]]]
       ])))
