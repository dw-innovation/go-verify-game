(ns kid-game.components.verification-hub.activities.shared.ris-image-results
  (:require [reagent.core :as r]
            [cljs.core.async :as async :include-macros true]
            [kid-game.state :as state]))


;; filled out lovingly by css
;; TODO alt for screenreaders
(defn <loading> [] [:div.lds-ring [:div] [:div] [:div] [:div]])

(defn <dragger> [img ; either a STRING for img src or an SVG
                 ; function on success:
                 done!
                 reset? ; optional component, resets the status, annoying hack :/
                 ]
  (let [done? (r/atom false)
        drag-component (if (string? img)
                         [:img {:src img}]
                         img)]
    (fn []
      [:div.ris-image-dragger
       [:div.ris-drag-blocks
        [:div.ris-drag-block-left.ris-drag-block
         (if @done?
           [:div.drag-target "done!"]
           [:div.ris-image-dragger-image {:draggable true
                                          :on-drop (fn [evt] (.preventDefault evt))}
            drag-component])]
        [:div.ris-drag-block-center.ris-drag-block
         [:span.icon [:i {:class "fas fa-arrow-right"}]]]
        [:div.ris-drag-block-right.ris-drag-block
         (if @done?
           [:div.ris-image-dragger-image drag-component]
           [:div {:class "drag-target box is-flex is-justify-content-center is-align-items-center"
                  :on-drag-over (fn [e] (.preventDefault e))
                  :on-drag-enter (fn [e] (.preventDefault e))
                  :on-drop (fn [e]
                             (.preventDefault e)
                             (reset! done? true)
                             (done!))} "Drag here"])]]])))

;; awaits vector of {:src ""}
(defn <image-results> [imgs]
  [:div.ris-image-results
   (for [img imgs]
     [:div.ris-image-result
      [:img {:src (:src img)}]])])

(defn <search-result> [{url :url title :title img-src :img-src date :date text :text}]
  [:div.ris-search-result
   [:div.ris-search-result-url url]
   [:div.ris-search-result-title title]
   [:div.ris-search-result-columns
    (when img-src
      [:div.ris-search-result-image
       [:img {:src img-src}]])
    [:div.ris-searh-result-details
     [:div.ris-search-result-details date]
     [:div.ris-search-result-text text]]]])

;; awaits vector of {:url :title :img-src :date :text}
(defn <search-results> [search-results]
  [:div.ris-search-results
   (for [res search-results] [<search-result> res])])
