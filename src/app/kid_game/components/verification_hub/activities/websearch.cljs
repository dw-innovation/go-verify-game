(ns kid-game.components.verification-hub.activities.websearch
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [clojure.string :as string]
            [kid-game.state :as state]))

;; FIXME: in some cases, this NS must be explicitly imported up the
;; requirement tree for changed to be picked up. no idea why. see issue #48

(defn <header> []
[:div.activity-header
 [:div.columns
 [:div.activity-icon
  [icons/browser-search]]
    [:div.activity-title "Web Search"]]])

;; Activity of type web-search, with it's corresponding dara
(defn <web-search> [{:as data
                     id :id
                     terms :terms
                     results :results}]
  [:div.activity-container.web-search
   [<header>]
   [:div.blooble-simulation
    [:div.blooble-logo
     [icons/blooble-logo]]
    [:div.search-bar
     [:input {:placeholder (string/join " + " terms)
              :disabled true}]]
    [:div.search-button
     [:button "Blooble Search"]]
    ]])
