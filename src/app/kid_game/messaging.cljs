(ns kid-game.messaging
  (:require [kid-game.state :as state]
            [kid-game.utils.log :as log]
            [kid-shared.types.messages :as messages]))

(defn handle-message! [msg]
  ;; handles an incoming message, and affects the state accordingly.
  ;; returns true if everything went as expected, and false if something went wrong.
  ;; TODO should actually throw a variety of errors instead of true falsing
  ;; a message must have a type and a body
  (if-let [{:keys [type body]} msg]
    (do (log/debug "handling message" msg)
        (case type
          ::messages/user-init (state/set-users body)
          ::messages/chat-new (state/add-chat body)
          ::messages/user-new (state/add-user body)
          ::messages/user-left (state/remove-user body)
          ::messages/post-new (state/add-post body)
          ; default
          (log/debug "could not handle the message"))
        ;; not really true
        true)
    (do (log/warn "got a message we don't recognize as a message")
        false)))
