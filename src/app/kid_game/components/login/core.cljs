(ns kid-game.components.login.core
  (:require [reagent.core                          :as r]
            [kid-game.state                        :as state]
            [kid-game.business                     :as business]
            [kid-game.components.modal             :as modal]
            [kid-shared.data.blocks :as blocks]
            [kid-game.components.shared.icons      :as icons]
            ["../../react_components/compiled/intro.js" :as intro]))

(defn modal-content []
  [:div [:div {:style {:width "100px" :float "center"}}
         [icons/thomas]]
   [:h4.title.is-4 blocks/game-tagline-title]
   [:p.mt-2 blocks/game-explanation]])

(defn <form> []
  (let [v (r/atom nil)
        u (state/get-player)
        log-in #(business/new-session! @v)]
    (fn []
      [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center"}
       [modal/<modal> modal-content]
       [:div {:class "columns is-justify-content-center"}
        [:div {:class "column is-4"}
         [:h1 {:class "title"} blocks/game-tagline-title]
         [:h5 {:class "subtitle"} blocks/game-tagline-welcome]
         [:a {:class "level level-left has-text-grey"
              :on-click #(modal/toggle-modal)}
          [:span {:class "icon is-small"}
           [:i {:class "fa fa-info-circle mr-1"}]]
          "Hold on, what is KID?"]
         [:form
          {:on-submit (fn [x] (.preventDefault x) (log-in))}

          ;; input field
          [:div.field
           [:div {:class "control has-icons-left has-icons-right"}
            [:input {:class "input"
                     :type "text"
                     :value @v
                     :placeholder "Pick a username"
                     :on-change #(reset! v (-> % .-target .-value))}]
            [:span {:class "icon is-small is-left"}
             [:i {:class "fa fa-user"}]]]]

          ;; button field
          [:div.field
           [:div.control
            [:button {:class "button is-link"}
             [:span {:class "icon is-small"}
              [:i {:class "fa fa-sign-in"}]]
             [:span "Login"]]]]]]]])))
