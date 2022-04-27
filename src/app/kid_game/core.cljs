(ns kid-game.core
  (:require [reagent.core                                         :as r]
            [reagent.dom :as rd]
            [kid-game.utils.log                                   :as log]
            [kid-shared.data.blocks                :as blocks]
            [kid-game.components.modal :as modal]
            [kid-game.uikit                                   :as uikit]
            [kid-shared.data.posts                               :as posts-data]
            [kid-game.components.login.core                       :as <login>]
            [kid-game.state                                       :as state]
            [kid-game.business                                    :as business]
            [kid-game.components.meta                             :as <meta>]
            [kid-game.components.timeline.core                    :as <timeline>]
            [kid-game.components.tutorial.core                    :as tutorial]
            [kid-game.components.notifications                    :as notifications]
            [kid-game.components.verification-hub.core            :as <verification-hub>]
            [kid-game.components.verification-hub.activities.core :as activities]
            ;; these NS are imported here for dev hot-reloading.  for some reason it does not work without
            ;; them imported at the top of the tree
            [kid-game.components.verification-hub.activities.websearch]
            [kid-game.components.verification-hub.activities.polygon-search]
            [kid-game.components.verification-hub.activities.ris-crop]
            [kid-game.components.verification-hub.activities.ris-flip]
            ;; end weird import
            [kid-shared.generator :as gen]
            [moment]))

(defn <game> []
  (let [timeline-el (r/atom nil)]
    (fn []
      (let [size             {:active "active" :inactive "inactive"}
            scrolltop        (if @timeline-el (.-scrollTop @timeline-el) 300)
            active-panel     (state/get-panel)
            timeline-active? (= active-panel :timeline)
            hub-active?      (not timeline-active?)]

        (cond
          ;; the story generator is paused whenever the user is investigating, or not currently
          ;; scrolled to the top of the timeline
          ;; TODO kinda hacky, think of a better way
          (or hub-active?
              (>= scrolltop 40))              (when (not @gen/paused?) (gen/pause))
          (and @gen/paused? (< scrolltop 40)) (gen/continue))

        [:div {:class "game-container mt-0 ml-0"}
         [notifications/<notifications>]

         [:div.game-panels

          ;; this meta panels is for during development
          (when @state/dev?
            [:div {:class "game-panel dev-panel"}
             [<meta>/<meta>]])

          [:div {:id       "timeline"
                 :ref      (fn [el] (reset! timeline-el el))
                 :class    ["game-panel"
                            "game-timeline"
                            (if timeline-active?
                              (:active size)
                              (:inactive size))]
                 :on-click (fn [ev] (.stopPropagation ev) (state/open-timeline))}
           [<timeline>/<container>]]

          [:div {:class    ["game-panel"
                            "game-verification-hub"
                            (cond hub-active? (:active size)
                                  :else       (:inactive size))]
                 :on-click (fn [ev] (.stopPropagation ev) (state/open-verification-hub))}
           [<verification-hub>/<container>]]]]))))

(defn <one-post> [post-id]
  (let [post (-> (filter (fn [p] (= post-id (:id p))) posts-data/all-activity-posts)
                 (first))]
    (when post [:div.testing-environment
                [:div.post-in-list
                 [<timeline>/<post> (assoc post :game-state :live)]]
                (map (fn [activity]
                       ^{:key (:type activity)} ;; important to keep track of rendering
                       [activities/get-activity activity]) (:activities post))])))

(defn <impressum-meta> []
  (let [[toggle-credits! close-credits! <credits-modal>] (modal/make-modal)]
    [:div {:style {:position   "fixed"
                   :bottom     0
                   :left       0
                   :background "white"
                   :z-index    9999999}}
     [:a {:href     "#"
          :on-click toggle-credits!} "Credits | Legal Note"]
     [<credits-modal> (fn [] blocks/credits)]
     ]))

(defn <app> []
  [:<>
   (case (@state/app-state :active-panel)
     :verification-hub [<game>]
     :timeline         [<game>]
     :tutorial         [tutorial/<tutorial> business/on-tutorial-finished business/on-tutorial-cancelled]
     :login            [<login>/<form>])
   [<impressum-meta>]
   ])

(defn <routes> []
  ;; decide what to render in our app.  This is some junk hand-made routing
  (let [s       js/window.location.search ; get the ?var=val&var2=val2 from the url
        post-id (-> (js/URLSearchParams. s) (.get "post"))
        uikit?  (-> (js/URLSearchParams. s) (.get "uikit"))]
    (when (and @state/dev?
               (not (state/has-player?)))
      (business/on-logged-in "dev-user")
      (business/on-tutorial-finished))
    (cond
      uikit?  [uikit/<main-view>]
      post-id [<one-post> post-id]
      :else   [<app>])))


(def dom-root (. js/document (getElementById "app")))

(rd/render [<routes>] dom-root)

(defn ^:dev/after-load start []
  (js/console.log "reload")
  (rd/render [<routes>] dom-root))
