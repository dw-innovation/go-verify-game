(ns kid-game.components.login.core
  (:require [reagent.core                          :as r]
            [kid-game.state                        :as state]
            [kid-game.business                     :as business]
            [kid-shared.data.blocks                :as blocks]
            [kid-game.components.modal             :as modal]
            [kid-game.components.shared.icons      :as icons]))

(defn modal-content []
  [:div [:div {:style {:width "100px" :float "center"}}
         [icons/thomas]]
   [:div.mt-2 blocks/game-explanation]])

(defn <thomas-icon> []
  [:div {:class "has-border-hub-primary has-background-white"
         :style {:width            "100px"
                 :background-color "red"
                 :height           "100px"
                 :margin           "0 auto -50px auto"
                 :border-radius    "50px"
                 :overflow         "hidden"
                 :position         "relative"
                 :z-index          2}}
   [icons/thomas]])

(defn <login-title> []
  [:<>
   [:h1 {:class "title is-text-hub-primary mb-6"} blocks/game-tagline-title]
   [:h5 {:class "subtitle mb-6"}
    "You've heard about that whole \"fake news\" thing,"
    [:br] "haven't you?"
    [:a {:class "is-text-hub-primary"
         :on-click #(modal/toggle-modal)}
     [:span {:class "pl-1 is-small"}
      [:i {:class "fa fa-info-circle mr-1"}]]]]])

(defn <form> []
  (let [v      (r/atom nil)
        u      (state/get-player)
        log-in #(business/new-session! @v)]
    (fn []
      [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center has-background-light"}
       [modal/<modal> modal-content]
       [:div {:class "columns is-justify-content-center contain-section-width center-section has-background-white p-6 br-2"}
        [:div {:class "column is-12 has-text-centered"}
         (<login-title>)
         (<thomas-icon>)

         [:form
          {:on-submit (fn [x] (.preventDefault x) (log-in))
           :class     "br-2"
           :style     {:overflow "hidden"
                       :position "relative"
                       :z-index  1}} ;; for the border radius

          [:div {:class "has-background-hub-primary p-6"}
           ;; input field
           [:div.field.pt-4
            [:div {:class "control has-icons-left has-icons-right"}
             [:input {:class       "input"
                      :type        "text"
                      :value       @v
                      :placeholder "Pick a username"
                      :on-change   #(reset! v (-> % .-target .-value))}]
             [:span {:class "icon is-small is-left"}
              [:i {:class "fa fa-user"}]]]]]
          [:div {:class "has-background-hub-secondary p-3"}

           ;; button field
           [:div.field
            [:div.control
             [:button {:class "button is-login-button"}
              [:span {:class "icon is-small"}
               [:i {:class "fa fa-sign-in"}]]
              [:span "Login"]]]]]]]]])))
