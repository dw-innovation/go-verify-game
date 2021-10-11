(ns kid-game.components.verification-hub.activities.websearch
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [kid-game.state :as state]))

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

(defn <header> []
[:div.activity-header
    [:div.activity-icon]
    [:div.activity-title "Web Search"]])

;; Activity of type web-search, with it's corresponding dara
(defn <web-search> [{:as data
                     id :id
                     terms :terms
                     results :results}]
  [:div.activity-container.web-search
   [<header>]
   [icons/blooble-logo]
   [icons/thomas-color-3]
   ")_ooofff"])
