(ns kid-game.components.verification-hub.activities.polygon-search
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [kid-game.components.verification-hub.activities.shared.svg-utils :as svg-utils]
            [cljs.core.async :as async :include-macros true]
            [clojure.string :as string]
            [kid-game.state :as state]))

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

(defn <header> []
[:div.activity-header
 [:div.columns
  [:div.activity-icon
   [icons/image-analysis]]
  [:div.activity-title "Image Analysis"]]])

(defn click-image-svg [^String image-url ; the svg background image
                       [{^Vector shape :shape
                         ^String message :message
                         :as polygon}
                        & rest :as initial-polygons] ; a vector of shapes {shape message}
                       [width height :as image-dimensions] ; a vector of image dimenstions
                       correct-click! ;a function excecuted on a correct click
                       wrong-click! ; a function excecuted on wrong click
                        ]
  (let [svg (r/atom nil) ; a ref to the svg
        container (r/atom nil) ; a ref to the container
        polygons (r/atom initial-polygons)
        markers (r/atom []) ; an atom to hold a list of the markers where the user has clicked
        is-finished (fn [] (every? :visible? @polygons))
        finish! (fn [] (state/add-notification {:type :success
                                                :text "Found all the strange things"}))
        failed! (fn [evt] (if (not (is-finished))
                            (do (.preventDefault evt)
                                (state/add-notification {:type :warn
                                                         :text "Nothing strange here"})
                                (let [[x y] (svg-utils/get-svg-coordinates @svg evt)]
                                  (reset! markers (conj @markers {:x x :y y :color "red"}))
                                  (println @markers)))
                            (do (state/add-notification {:type :warn
                                                         :text "Already found all the strange things"}))))
        succeeded! (fn [polygon]
                     (fn [evt]
                       (if (not (is-finished))
                         (do (.preventDefault evt)
                             (.stopPropagation evt)
                             (state/add-notification {:type :success
                                                      :text (:message polygon)})
                             ;; add a marker where the user successfully clicked:
                             (let [[x y] (svg-utils/get-svg-coordinates @svg evt)]
                               (reset! markers (conj @markers {:x x :y y :color "green"})))
                             ;; associate the shape with success
                             (reset! polygons (map (fn [p] (if (= p polygon) (assoc p :visible? true) p)) @polygons))
                             (when (is-finished) (finish!)))
                         (do (state/add-notification {:type :warn
                                                      :text "Already found all the strange things"})))))
        <polygon> (fn [{visible? :visible?
                        shape :shape
                        :as polygon}]
                    [:g.polygon [:a {:href "#" :on-click (succeeded! polygon)}
                                 [:g {:opacity (if visible? 1 0.2)}
                                  shape]]])
        <marker> (fn [{x :x y :y color :color}]
                   [:rect {:x x :y y :fill color :width 10 :height 10}])
        ]
    (fn []
      [:div {:ref #(reset! container %)}
       [:svg {:ref #(reset! svg %)
              :viewBox (str "0 0 " width " " height)
              :preserve-aspect-ratio "xMinYMin meet"}
        [:a {:href "#" :on-click failed!}
         [:image {:width width :height height :href image-url}]
         (map <polygon> @polygons)
         (map <marker> @markers)]]])))

;; Activity of type web-search, with it's corresponding dara
(defn <main> [{:as data
               [x y :as dimensions] :dimensions
               image-url :main-image
               polygons :polygons}]
 (let []
    (fn []
      [:div.activity-container.image-analysis
       [<header>]
       [:div.activity-step
        [:div.activity-description "Mark the parts of the image that look weird to you.  Place pointer, click, and start finsing polygons."]
        [click-image-svg image-url polygons dimensions (fn []) (fn [])]]
       [:div.columns.activity-actions
        [:div.column.action
         [:p "Ready to make a call?"]
         [:button {:on-click (fn [] (state/open-timeline))} "Back to timeline"]]
        [:div.column.action
         [:p "Investigate further?"]
         [:button {:on-click (fn [] (state/open-timeline))} "Back to hub"]]]])))
