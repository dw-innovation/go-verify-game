(ns kid-game.components.verification-hub.activities.shared.ris-image-results)

(defn <dragger> [img done!]
  [:div.ris-image-dragger
   [:div.ris-image-dragger-image
    [:img {:src img}]]
   [:button {:on-click done!} "drag image"]
   ])

(defn <results> [imgs]
  [:div.ris-image-results
  (for [img imgs]
    [:div.ris-image-result
     [:img {:src (:src img)}]])
   ]
  )
