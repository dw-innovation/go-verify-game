(ns kid-game.components.verification-hub.activities.websearch
  (:require [reagent.core :as r]
            [kid-game.components.shared.icons :as icons]
            [kid-game.components.verification-hub.activities.shared.ris-image-results :as image-results]
            [cljs.core.async :as async :include-macros true]
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

(defn <blooble-search> [placeholder search!]
  (let [v (r/atom nil)]
    [:form
     {:on-submit (fn [x] (.preventDefault x) (search!))}
     [:div.field
      [:div {:class "control has-icons-left has-icons-right"}
       [:input {:class "input"
                :type "text"
                :value @v
                :placeholder placeholder
                :disabled true
                :on-change #(reset! v (-> % .-target .-value))}]
       [:span {:class "icon is-small is-left"}
        [:i {:class "fa fa-search"}]]]]
     [:div.field
      [:div  {:class "control has-text-centered"}
       [:button {:class "button is-link"}
        [:span "Blooble search"]]]]]))

(defn <blooble-simulation> [terms-string ; a string to show in the search bar
                            results ; components to place after the break, or nil
                            loading ; bool, show the loading
                            search! ; function to run on click
                            ]
  [:div.blooble-simulation
   [:div.blooble-logo
    [icons/blooble-logo]]
   [<blooble-search> terms-string search!]
   (when results
     [:div.results
      [:b.results-count (count results) " search results found"]
      (for [res results]
        [:div.search-result
         [image-results/<search-result> res]])])
   (when loading
     [:div.loading-panel
      [image-results/<loading>]])])

;; Activity of type web-search, with it's corresponding dara
(defn <main> [{:as data
               id :id
               terms :terms
               loading-time :loading-time ; could be nil
               results :results}
              back!]
  (let [searched? (r/atom false)
        loading? (r/atom false)
        click! (fn []
                 (reset! loading? true)
                 (async/go (async/<! (async/timeout (or loading-time 2000)))
                           (reset! searched? true)
                           (reset! loading? false)))]
    (fn []
      [:div.activity-container.web-search
       [<header>]
       [<blooble-simulation>
        (string/join " + " terms)
        (if @searched? results nil)
        @loading?
        click!]
       [:div.columns.activity-actions
        [:div.column.action
         [:p "Ready to make a call?"]
         [:button {:on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))} "Back to timeline"]]
        [:div.column.action
         [:p "Investigate further?"]
         [:button {:on-click back!} "Back to hub"]]]])))