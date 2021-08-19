(ns kid-game.components.verification-hub.activities.shared.ris-image-results
  (:require [reagent.core :as r]
            [kid-game.state :as state]))

(defn <dragger> [img done!]
  (let [done? (r/atom false)]
    (fn []
      [:div.ris-image-dragger
       [:div.ris-drag-blocks
        [:div.ris-drag-block-left.ris-drag-block
          (if @done?
            [:div.drag-target "done!"]
            [:div.ris-image-dragger-image
             [:img {:src img :draggable true}]])]
        [:div.ris-drag-block-center.ris-drag-block
         [:div.drag-arrow "->"]]
        [:div.ris-drag-block-right.ris-drag-block
         (if @done?
           [:div.ris-image-dragger-image [:img {:src img}]]
           [:div.drag-target {:on-drag-over (fn [e] (.preventDefault e))
                              :on-drag-enter (fn [e] (.preventDefault e))
                              :on-drop (fn []
                                             (println "mouse up")
                                             (reset! done? true)
                                             (done!)
                                             )} "drag here"])
         ]
        ]
       ])))

;; awaits vector of {:src ""}
(defn <image-results> [imgs]
  [:div.ris-image-results
  (for [img imgs]
    [:div.ris-image-result
     [:img {:src (:src img)}]])
   ]
  )

(defn <search-result> [{url :url title :title img-src :img-src date :date text :text}]
[:div.ris-search-result
     [:div.ris-search-result-url url]
     [:div.ris-search-result-title title]
     [:div.ris-search-result-columns
      [:div.ris-search-result-image
       [:img {:src img-src}]]
      [:div.ris-searh-result-details
       [:div.ris-search-result-details date]
       [:div.ris-search-result-text text]]]])

;; awaits vector of {:url :title :img-src :date :text}
(defn <search-results> [search-results]
  [:div.ris-search-results
  (for [res search-results] [<search-result> res])])
