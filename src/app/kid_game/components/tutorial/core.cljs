(ns kid-game.components.tutorial.core
  (:require [reagent.core                          :as r]
            [kid-game.business                     :as business]
            [kid-shared.data.blocks                :as blocks]
            [kid-game.components.shared.icons      :as icons]))

(defn slides-buttons [back! next! next-text]
  [:div.level
   [:button.level-left.is-grey {:on-click back!} "back"]
   [:button.level-right {:on-click next!} next-text]])

(def slides
  [(fn [back! done!]
     [:<>
      blocks/tutorial-slide-1
      [slides-buttons back! done! "O.K."]])
   (fn [back! done!]
     [:<>
      blocks/tutorial-slide-2
      [slides-buttons back! done! "Next"]])
   (fn [back! done!]
     [:<>
      blocks/tutorial-slide-3
      [slides-buttons back! done! "Let me play already!"]])])

(defn <slides> [cancel! finish! slides]
  (let [slide-index (r/atom 0)
        next?       (fn [] (< @slide-index (dec (count slides))))
        next!       (fn [] (reset! slide-index (inc @slide-index)))
        back?       (fn [] (> @slide-index 0))
        back!       (fn [] (reset! slide-index (dec @slide-index)))
        get-slide   (fn [] (slides @slide-index))]
    (fn []
      [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center has-background-light"}
       [:div {:class "columns is-justify-content-center contain-section-width center-section has-background-white p-5 pt-6 br-2"}
        [:div {:class "column is-12"}
         [(get-slide)
          (if (back?) back! cancel!)
          (if (next?) next! finish!)]
         ]

        ]
       [:div.columns.contain-section-width.center-section
        [:a.has-text-grey.is-italic {:href "#" :on-click finish!} "skip"]
        ]])))

(defn <tutorial> [finish! back!]
  [<slides> back! finish! slides])
