(ns kid-game.components.verification-hub.activities.shared.ris-image-results
  (:require [reagent.core :as r]
            [cljs.core.async :as async :include-macros true]
            [kid-game.components.verification-hub.activities.shared.components :as components]
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
           [:div {:class "drag-target box is-flex is-justify-content-center is-align-items-center"}
            [:span.icon [:i {:class "fas fa-file-image-o"}]]]
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

(defn <search-result> [{url :url title :title img-src :img-src date :date text :text}]
  ^{:key title}
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
   (map <search-result> search-results)])

;; awaits vector of {:url :title :img-src :date :text}
(defn <ris-results> [results]
  [components/<blooble-simulation>
   [:div.ris-results
    [:h4.title.is-4.mb-2 "Results from your image search"]
    [:h3.ris-result-header [:b (count results)] " page(s) with matching images"]
    [<search-results> results]]])
