(ns kid-game.components.verification-hub.activities.shared.components
  (:require [reagent.core :as r]
            [cljs.core.async :as async :include-macros true]
            [kid-game.components.shared.icons :as icons]
            [kid-game.components.modal             :as modal]
            [kid-game.state :as state]))

(defn <modal-icon>
  ([modal-content] (<modal-icon> "" modal-content))
  ([subtext modal-content] ; a [:div]
   (let [[toggle-modal close-modal <modal>] (modal/make-modal)]
     [;return the mofal icon
      (fn []
        [:a {:class "has-text-grey is-small"
             :on-click #(toggle-modal)}
         subtext
         [:i {:class "fa fa-info-circle mr-1"}]])
     ; and the modal itself
      (fn [] [<modal> (fn [] modal-content)])])))

(defn <header> [^js/SVG icon
                ^string title
                ^string subtitle
                ^string modal-title
                ^vector modal-content]
  (let [[m-icon modal] (<modal-icon> (str "What is " title "? ") [:div [:h3.title.is-3 modal-title] modal-content])]
    [:div {:class "columns is-centered is-vcentered"}
     [:div {:class "column is-1"} [icon]]
     [:div {:class "is-11 pl-5 pt-5"}
      [:h3.title.is-3.level title]
      [m-icon]
      [modal]
      #_[:p.subtitle subtitle]]]))

(defn <activity-actions> [back!]
  [:div.activity-actions.pb-5.pt-5
   [:div.columns.mt-5.mb-5.contain-section-width.center-section.has-text-white
    [:div.column.has-text-centered
     [:p "Ready to make a call?"]
     [:button {:class "button is-primary is-inverted is-outlined"
               :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))}
      [:span.icon [:i {:class "fas fa-newspaper-o"}]] [:span "Back to timeline"]]]
    [:div.column.has-text-centered
     [:p "Investigate further?"]
     [:button {:class "button is-primary is-inverted is-outlined"
               :on-click back!}
      [:span.icon [:i {:class "fas fa-search"}]] [:span "Back to hub"]]]]])

(defn <blooble-simulation> [children]
  [:div.blooble-simulation
   [:div.blooble-logo
    [icons/blooble-logo]]
   children])
