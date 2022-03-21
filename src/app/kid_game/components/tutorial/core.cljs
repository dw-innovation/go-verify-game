(ns kid-game.components.tutorial.core
  (:require [reagent.core                          :as r]
            [kid-game.business                     :as business]
            [kid-shared.data.blocks                :as blocks]
            [kid-game.components.shared.icons      :as icons]))

(def slides [[:div [:h1 "slide 1"]]
             [:div [:h1 "slide 2"]]
             [:div [:h1 "slide 3"]]])

(defn <tutorial> []
  (let [slides-index  (r/atom 0)
        next-slide?   (fn [] (> (count slides) (inc @slides-index)))
        next-slide!   (fn [] (when next-slide?
                               (reset! slides-index (inc @slides-index))))
        current-slide (fn [] (slides @slides-index))
        ]
    (fn []
      (let [slide (current-slide)]
        [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center has-background-light"}
         [:div {:class "columns is-justify-content-center contain-section-width center-section has-background-white p-6 br-2"}
          [:div {:class "column is-12 has-text-centered"}
           slide
           [:button {:on-click next-slide!} "next"]
           ]]]))))
