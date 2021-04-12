(ns kid-game.components.chat.core
  (:require [reagent.core :as reagent :refer [atom]]
            [kid-game.state :as state]
            [kid-game.business :as business]
            [kid-game.utils.log :as log]
            [kid-game.utils.core :as utils]
            [kid-shared.types.chat :as chat]
            [kid-shared.types.user :as user]))

(defn <input> [& {:keys [send!]}]
  (let [v (atom nil)]
    (fn []
      [:div {:class "text-input"}
       [:form
        {:on-submit (fn [x]
                      (.preventDefault x)
                      (when-let [msg @v] (send! msg))
                      (reset! v nil))}
        [:div {:style {:display "flex"
                       :flex-direction "column"}}
         [:input {:type "text"
                  :value @v
                  :placeholder "message..."
                  :on-change #(reset! v (-> % .-target .-value))}]
         [:button {:type "submit"
                   :class "send-button"} "Send"]]]])))

(defn <chat> [chat]
  (business/chat-seen! chat) ; mark it as seen on render. little hack
  ^{:key (:created chat)}
  [:p
   [:b
    (str (-> chat :from :name))]
   ": "
   (str (:content chat))])

(defn <history> [with-user chats back send!]
  (reagent/create-class
    {:reagent-render (fn [with-user chats back send] ; with-user must be here, too
                       [:div {:class "history"}
                        [:div {:class "chat"}
                         [:div {:class "messages"}
                          (map <chat> chats)]
                         [:div {:class "input"}
                          [<input> :send! send!]]]])
     :component-did-update (fn [this]
                             (let [node (reagent/dom-node this)]
                               (set! (.-scrollTop node) (.-scrollHeight node))))}))

(defn <last-chat> [chats]
  [:div {:class "latest-message"}
   [:b (->> chats last :from :name)]
   ": "
   (:content (last chats))])

(defn <user> [& {:keys [select! user selected?]}]
  [:div {:class (str "chat-select "
                     "chat-user "
                     (if (state/new-chats? (:chats user))  "chat-unseen " "chat-seen ")
                     (if selected? "selected"))
         :on-click select!}
   [:div {:class "chat-title"} (:name user)]
   [<last-chat> (:chats user)]])

(defn <users> [& {:keys [users
                         selected-user
                         select-user!
                         group-chats]}]
  [:div {:class "sidebar"}
   [<user> ; fake a user for the group chat for now...
    :select! #(select-user! nil)
    :user {:name "group chat"
           :chats group-chats}
    :selected? (= nil selected-user)]
   (for [[k v] users]
     ^{:key k} [<user>
                :select! #(select-user! v)
                :user v
                :selected? (user/same? v selected-user)])])

(defn <container> []
  (let [panel (atom :users) ; :chat
        selected-user (atom nil)
        select-user! (fn [u]
                       (reset! panel :chat)
                       (reset! selected-user u))
        deselect-user! (fn []
                         (reset! panel :users))
        send! (fn [msg] (if @selected-user
                          (business/chat-send! :to (dissoc @selected-user :chats)
                                               :content msg)
                          (business/group-chat-send! :content msg)))
        ; chats (state/message-list) ; update does not work if this is here....
        ; it doesnt seem to 'track' the result of fn state/message-list
        ; function to see if a message is between the two users
        messages-with (fn [u messages]
                        (filter #(chat/between? u (state/get-player) %) messages))
        group-messages (fn [messages]
                         (filter chat/group-chat? messages))
        current-chats (fn [messages]
                        (if @selected-user
                          (messages-with @selected-user messages)
                          (group-messages messages)))
        user-with-chats (fn [u messages]
                          (assoc u :chats (messages-with u messages)))
        ]
    (fn []
      [:div {:class "chat-container"}
       [<history>
        @selected-user
        (current-chats (state/message-list))
        deselect-user!
        send!]
       [<users>
        :users (utils/map-values #(user-with-chats % (state/message-list)) (state/get-users))
        :group-chats (group-messages (state/message-list))
        :selected-user @selected-user
        :select-user! select-user!]])))
