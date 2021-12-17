(ns kid-game.components.meta
  (:require [kid-game.state           :as state]
            [kid-game.business        :as business]
            [kid-game.messaging       :as messaging]
            [kid-shared.generator     :as gen]
            [kid-shared.data.posts    :as posts]
            [kid-shared.ticks         :as ticks]
            [kid-shared.data.stories  :as stories]))

(defn <meta> []
  (let [gens (fn [] @gen/active-generators)]
    (fn []
      [:div
       [:div {:class "tile box"}
        [:h5 {:class "title is-5"} "🛠 Dev info"]]

       [:div.title.box
        [:ul [:li "ticks: " @ticks/ticks]]
        [:button.button.is-danger.is-light {:on-click #(ticks/pause)} "pause"]
        [:button.button.is-danger.is-light {:on-click #(ticks/continue)} "continue"]
        ]
       [:div.title.box
        [:button.button.is-danger.is-light {:on-click #(business/logout)} "<- Logout of dev"]]
       [:div {:class "tile box"}
        [:ul
         [:li "Player: " [:b (:name (state/get-player))]]
         [:li "Score: "  [:b (state/get-player-points)]]]]

       [:nav.panel
        [:p.panel-heading "Run a story"]
        (for [[name story] stories/all-stories]
          ^{:key name}
          [:a {:class "panel-block"
               :on-click (fn [] (gen/gen-run-story messaging/receive-channel story))}
           [:span.panel-icon [:i {:class "fa fa-book" :aria-hidden "true"}]]
           [:span name]])]

       [:div {:class "tile box is-flex-direction-column"}
        [:p "active stories: " (count (gens))]
        [:button {:class "button is-danger is-light"
                  :on-click (fn [] (gen/kill-all-posters))} "stop all stories"]]

       [:div {:class "tile box is-flex-direction-column"}
        [:p {:class "title is-5"} "Test notifications"]
        [:div.buttons
         [:button {:class "button is-warning is-light"
                   :on-click (fn [] (state/add-notification {:type :success
                                                             :text "+tktktk points"}))}
          "test: success notification"]
         [:button {:class "button is-warning is-light"
                   :on-click (fn [] (state/add-notification {:type :warning
                                                             :text "-tktktk points"}))}
          "test: warning notification"]]]

       [:div {:class "tile box is-flex-direction-column"}
        [:p {:class "title is-5"} "Posts"]
        (for [p posts/all-activity-posts]
          ^{:key (:id p)}
          [:div
           [:hr]
           (:id p)
           [:div [:a {:href (str "?post=" (:id p))}
                  "open"]]
           [:div [:a {:href "#"
                      :on-click (fn [] (business/add-post p))}
                  "inject into timeline"]]])]])))
