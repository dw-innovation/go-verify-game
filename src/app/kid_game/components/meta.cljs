(ns kid-game.components.meta
  (:require [reagent.core             :as r]
            [kid-game.state           :as state]
            [kid-game.business        :as business]
            [kid-game.utils.log       :as log]
            [kid-game.messaging       :as messaging]
            [kid-shared.generator     :as gen]
            [kid-shared.posts.posts   :as posts]
            [kid-shared.posts.stories :as stories]
            [cljs.core.async          :as async :include-macros true]))


(defn <meta> []
  (let [gens (fn [] @gen/active-generators)]
    (fn []
      [:div
       [:div {:class "tile box"}
        [:h5 {:class "title is-5"} "🛠 Dev info"]]
       [:div {:class "tile box"}
        [:ul
         [:li "Player: " [:b (:name (state/get-player))]]
         [:li "Score: "  [:b (state/get-player-points)]]]]
       
       [:nav.panel
        [:p.panel-heading "Run a story"]
        (for [[name story] stories/all-stories]
          [:a {:class "panel-block"
               :on-click (fn [] (gen/gen-run-story messaging/receive-channel story) )}
           [:span.panel-icon [:i {:class "fa fa-book" :aria-hidden "true"}]]
           [:span name]])]

       [:div {:class "tile box is-flex-direction-column"}
        [:p "active stories: " (count (gens))]
        [:button {:class "button is-danger is-light"
                  :on-click (fn [] (gen/kill-all-posters) )} "stop all stories"]]

       [:div {:class "tile box is-flex-direction-column"}
        [:p {:class "title is-5"} "Test notifications"]
        [:div.buttons
         [:button {:class "button is-warning is-light"
                   :on-click (fn [] (state/add-notification {:type :info
                                                             :text "hello!"}) )}
          "trigger notification"]
         
         [:button {:class "button is-warning is-light"
                   :on-click (fn [] (state/add-notification {:type :warning
                                                             :text "bad user! lost points"}) )}
          "trigger warning notification"]]]
       
       

       [:div {:class "tile box is-flex-direction-column"}
        [:p {:class "title is-5"} "Posts"]
        (for [p posts/all-activity-posts]
          [:div
           [:a {:href (str "?post=" (:id p))}
            (:id p)]])]])))
