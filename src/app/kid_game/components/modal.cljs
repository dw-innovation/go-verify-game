(ns kid-game.components.modal)

(defn select-modal []
  (.getElementById js/document "modal"))

(defn toggle-modal
  "Toggle the `.is-active` class on the modal,
   making it in turns visible and hidden.
   This is not part of the Bulma standard lib."
  []
  (let [el        (select-modal)
        classList (.-classList el)]
    (cond (.contains classList "is-active")
          (.remove classList "is-active")
          :else (.add classList "is-active"))))

(defn close-modal
  "Explicitly close the modal by removing the
   `.is-active` class"
  []
  (let [el        (select-modal)
        classList (.-classList el)]
    (when (.contains classList "is-active")
      (.remove classList "is-active"))))

(defn setup-esc-listener
  "Close the modal if Escape key is pressed 
   while the modal is visible."
  []
  (.addEventListener
   js/document "keydown"
   (fn [e]
     (when (= (.-keyCode e) 27) (close-modal)))))

(defn <modal> [content]
  (fn []
    (let [esc-listener (setup-esc-listener)]   ;; which ends up being an unused binding...
      [:div {:class "modal"
             :id    "modal"}
       [:div {:class    "modal-background"
              :on-click #(close-modal)}]
       [:div {:class "box column is-5 p-5"
              :style {:z-index 10}} (if content [content] "no content")]
       [:button.modal-close.is-large {:aria-label "close"
                                      :on-click   #(toggle-modal)}]])))

(defn make-modal
  "returns [^fn toggle-modal, ^fn close-modal, ^component <modal>]"
  []
  (let [unique-id    (gensym)
        select-modal (fn [] (.getElementById js/document unique-id))
        toggle-modal (fn [] (let [el        (select-modal)
                                  classList (.-classList el)]
                              (cond (.contains classList "is-active")
                                    (.remove classList "is-active")
                                    :else (.add classList "is-active"))))
        close-modal  (fn [] (let [el        (select-modal)
                                  classList (.-classList el)]
                              (when (.contains classList "is-active")
                                (.remove classList "is-active"))))
        <modal>      (fn [content]
                       [:div {:class "modal"
                              :id    unique-id}
                        [:div {:class    "modal-background"
                               :on-click #(close-modal)}]
                        [:div {:class "box column is-5 p-5"
                               :style {:z-index 10}}
                         (if content [content] "no content")
                         [:button.is-large {:aria-label "close"
                                            :title      "close"
                                            :on-click   #(toggle-modal)}
                          "close"]]
                        [:button.modal-close.is-large {:aria-label "close"
                                                       :on-click   #(toggle-modal)}]])]
    ;; attatch the event listener:
    (.addEventListener js/document "keydown"
                       (fn [e]
                         (when (= (.-keyCode e) 27) (close-modal))))
    ;; return the closures and component:
    [toggle-modal close-modal <modal>]))
