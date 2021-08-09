(ns kid-game.components.login.core
  (:require [reagent.core :as r]
            [kid-game.state :as state]
            ["../../react_components/compiled/intro.js" :as intro]
            [kid-game.business :as business]))

(defn <form> []
  (let [v (r/atom nil)
        u (state/get-player)
        log-in #(business/new-session! @v)]
    (fn []
      ;; if we already have a player, we already have a session, so go to game
      (if (:id u) (state/open-game))
      ;; otherwise render the login container
      [:div.login-container
       [:div.login
        [:h1 "KID Game"]
        [(r/adapt-react-class intro/default) {}]
        [:form
         {:on-submit (fn [x] (.preventDefault x) (log-in))}
         [:input {:type "text"
                  :value @v
                  :placeholder "Pick a username"
                  :on-change #(reset! v (-> % .-target .-value))}]
         [:br]
         [:button {:type "submit"
                   :class "button-primary"} "Login"]]]])))
