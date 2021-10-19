(ns kid-game.components.verification-hub.activities.shared.svg-utils
  (:require [reagent.core :as r]
            [cljs.core.async :as async :include-macros true]
            [kid-game.state :as state]))



(defn get-svg-point [^js/SVGSVGElement svg x y]
  (let [point (.createSVGPoint svg)]
    (set! (.-x point) x) ; mutate the values in js, i see no way around this
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
