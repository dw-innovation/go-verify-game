(ns kid-game.components.modal
  (:require [reagent.core :as r]))

(defn select-modal []
  (.getElementById js/document "modal"))

(defn toggle-modal []
  "Toggle the `.is-active` class on the modal,
   making it in turns visible and hidden.
   This is not part of the Bulma standard lib."
  (let [el        (select-modal)
        classList (.-classList el)]
    (if (.contains classList "is-active")
      (.remove classList "is-active")
      (.add classList "is-active"))))

(defn close-modal []
  "Explicitly close the modal by removing the
   `.is-active` class"
  (let [el        (select-modal)
        classList (.-classList el)]
    (if (.contains classList "is-active")
      (.remove classList "is-active"))))

(defn setup-esc-listener []
  "Close the modal if Escape key is pressed 
   while the modal is visible."
  (.addEventListener
   js/document "keydown"
   (fn [e]
     (if (= (.-keyCode e) 27) (close-modal)))))

(defn <modal> [content]
  (fn []
    (let [esc-listener (setup-esc-listener)]
      [:div {:class "modal"
             :id "modal"}
       [:div {:class "modal-background"
              :on-click #(close-modal)}]
       [:div {:class "box column is-5 p-5"
              :style {:z-index 10}} [content]]
       [:button.modal-close.is-large {:aria-label "close"
                                      :on-click #(toggle-modal)}]])))