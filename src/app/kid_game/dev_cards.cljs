(ns kid-game.dev-cards
  (:require [kid-game.utils.log :as log]
            [kid-game.components.notifications :as notifications]
            [kid-game.components.timeline.core :as timeline]
            [kid-shared.posts.posts :as posts-data]
            [kid-shared.types.messages :as messages]
            [cljs.core.async :as async :refer [<! >! put!] :include-macros true]))


(defn <component> []
  [:div
   [:h2 "hello world"]
   [notifications/<notification> {:active true
                                  :type :success
                                  :text "testing text"}]
   [notifications/<notification> {:active true
                                  :type :warning
                                  :text "testing text"}]
   [timeline/<post> posts-data/p1-climate-refugees-copenhagen]

   ])
