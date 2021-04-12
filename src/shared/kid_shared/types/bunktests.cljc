(ns kid-shared.types.bunktests
  (:require [kid-shared.types.user :as user]
            [kid-shared.types.messages :as message]
            [clojure.spec.alpha :as s]))
;; (ns kid-shared.types.tests
;;   (:require [kid-shared.types.tests :as sut]
;;             #?(:clj [clojure.test :as t]
;;                :cljs [cljs.test :as t :include-macros true])))

;;
;;  checks
;;


(s/valid? ::user/role "aaa") ; should be false
(s/valid? ::user/role :manipulator) ; should be true

(s/valid? ::user/user {:id "bob" :role :manipulator})

(def test-message-1 {:type :new-user
                     :body {:id "bob"
                            :role :manipulator}})

(def test-message-2 {:type :new-post
                     :body {:id "post 23"
                            :title "testing post number one"
                            :description "this is a description"}})

(s/valid? ::message/message test-message-1)
(s/valid? ::message/message test-message-2)
