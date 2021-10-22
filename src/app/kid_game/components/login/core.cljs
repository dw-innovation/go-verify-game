(ns kid-game.components.login.core
  (:require [reagent.core      :as r]
            [kid-game.state    :as state]
            [kid-game.business :as business]
            ["../../react_components/compiled/intro.js" :as intro]))

(defn <form> []
  (let [v (r/atom nil)
        u (state/get-player)
        log-in #(business/new-session! @v)]
    (fn []
      [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center"}
       [:div {:class "columns is-justify-content-center"}
        [:div {:class "column is-4"}
         [:h1 {:class "title"} "KID Game"]
         [:h5 {:class "subtitle"} "Welcome, what is your name?"]
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
