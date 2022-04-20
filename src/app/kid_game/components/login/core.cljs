(ns kid-game.components.login.core
  (:require [reagent.core                          :as r]
            [kid-game.business                     :as business]
            [kid-shared.data.blocks                :as blocks]
            [kid-game.components.shared.icons      :as icons]))

(defn modal-content []
  [:div [:div {:style {:width "100px" :float "center"}}
         [icons/izzy]]
   [:div.mt-2 blocks/game-explanation]])

(defn <izzy-icon> []
  [:div {:class "has-border-hub-primary has-background-white"
         :style {:width            "100px"
                 :background-color "red"
                 :height           "100px"
                 :margin           "0 auto -50px auto"
                 :border-radius    "50px"
                 :overflow         "hidden"
                 :position         "relative"
                 :z-index          2}}
   [icons/izzy]])

(defn <login-title> []
  [:<>
   [:h1 {:class "title is-text-hub-primary mb-6"} blocks/game-tagline-title]
   [:h5 {:class "subtitle mb-6"}
    "You've heard about that whole \"fake news\" thing,"
    [:br] "haven't you?"
    ]
   [:p.mb3 "We've created this little game to teach you the basics of verification."]])

(defn <form> []
  (let [name   (r/atom nil)
        log-in (fn [] (business/on-logged-in @name))]
    (fn []
      [:div {:class "hero is-fullheight is-align-content-center is-justify-content-center has-background-light"}
       [:div {:class "columns is-justify-content-center contain-section-width center-section has-background-white p-6 br-2"}
        [:div {:class "column is-12 has-text-centered"}
         (<login-title>)
         (<izzy-icon>)
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
                      :value       @name
                      :placeholder "Pick a user name"
                      :on-change   #(reset! name (-> % .-target .-value))}]
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
