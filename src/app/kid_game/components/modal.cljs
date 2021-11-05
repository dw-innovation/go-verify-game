(ns kid-game.components.modal
  (:require [reagent.core :as r]))

(defn select-modal []
  (.getElementById js/document "modal"))

(defn toggle-modal []
  (let [el        (select-modal)
        classList (.-classList el)]
    (if (.contains classList "is-active")
      (.remove classList "is-active")
      (.add classList "is-active"))))

(defn <modal> [content]
  [:div {:class "modal"
         :id "modal"}
   [:div.modal-background]
   [:div {:class "box column is-5 p-5"
          :style {:z-index 10}} [content]]
   [:button.modal-close.is-large {:aria-label "close"
                                  :on-click #(toggle-modal)}]])