(ns kid-shared.types.messages
  (:require [clojure.spec.alpha :as s]
            [kid-shared.types.user :as user]
            [kid-shared.types.chat :as chat]
            [kid-shared.types.shared :as shared]
            [kid-shared.types.post :as post]))

;;
;;  the messages we can send along the websocket
;;

(s/def :user-new/type #{::user-new})
(s/def :user-new/body ::user/user)
(s/def ::user-new (s/keys :req-un [:user-new/type
                                   :user-new/body]))

(s/def :user-left/type #{::user-left})
(s/def :user-left/body ::shared/id)
(s/def ::user-left (s/keys :req-un [:user-left/type
                                    :user-left/body]))

(s/def :user-init/type #{::user-init})
(s/def :user-init/body (s/coll-of ::user/user))
(s/def ::user-init (s/keys :req-un [:user-init/type
                                    :user-init/body]))

(s/def :chat-new/type #{::chat-new})
(s/def :chat-new/body ::chat/chat)
(s/def ::chat-new (s/keys :req-un [:chat-new/type
                                   :chat-new/body]))

(s/def :post-new/type #{::post-new})
(s/def :post-new/body ::post/post)
(s/def ::post-new (s/keys :req-un [:post-new/type
                                   :post-new/body]))

(s/def ::message (s/or ::chat-new ::chat-new
                       ::post-new ::post-new
                       ::user-init ::user-init
                       ::user-new ::user-new
                       ::user-left ::user-left))

(defn valid-message? [m] (s/valid? ::message m))

; explains why the message is not a valid one
(defn explain-message [m]
  (let [type (:type m)]
    ; if the message has a message type,
    ; explain what is wrong with the body
    ; TODO if is one of our types
    (if type
      (s/explain-data type m)
      ; otherwise, explain the whole message
      (s/explain-data ::message m))))

;;
;; bunk tests
;;
;;

(valid-message? {:type :user-new
                 :body {:id "hi"
                        :role :manipulator}})

(valid-message? {:type :user-init
                 :body (user/col-to-map [user/u1 user/u2])})
