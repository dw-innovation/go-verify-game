(ns kid-game.components.meta
  (:require [reagent.core :as r]
            [kid-shared.posts.stories :as stories]
            [kid-shared.generator :as gen]
            [kid-game.messaging :as messaging]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [cljs.core.async :as async :include-macros true]
            [kid-game.utils.log :as log]))


(defn <meta> []
  (let [gens (fn [] @gen/active-generators)]
    (fn []
      [:div.meta
       [:h5 "Information just for development:"]
       [:h6 "KID game: room: " [:b "xxx"]
        ": player: "
        [:b (:name (state/get-player))]
        ": points: "
        [:b (state/get-player-points)]]
       [:h5 "Stories"]
       [:p "click on a story below to run it in the timeline"]
       (for [[name story] stories/all-stories]
         [:div [:button {:on-click (fn [] (gen/gen-run-story messaging/receive-channel story) )} name]]
         )
       [:p "active stories: " (count (gens))]

       [:button {:on-click (fn [] (gen/kill-all-posters) )} "stop all stories"]


       [:h5 "Notifications"]

       [:button {:on-click (fn [] (state/add-notification {:type :info
                                                           :text "hello!"}) )}
        "trigger notification"]

       [:button {:on-click (fn [] (state/add-notification {:type :warning
                                                          :text "bad user! lost points"}) )}
        "trigger warning notification"]

       ])))
