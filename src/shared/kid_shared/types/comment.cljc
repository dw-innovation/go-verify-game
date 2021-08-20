(ns kid-shared.types.comment
  (:require [kid-shared.types.shared :as shared]
            [kid-shared.types.user :as user]
            [kid-game.utils.core :refer [new-uuid timestamp-now]]
            [clojure.spec.test.alpha :as spec-test]
            [clojure.test.check :as check]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

(s/def ::text string?)
(s/def ::post-id ::shared/id)
(s/def ::by ::user/user)

(s/def ::comment (s/keys :req-un [::post-id
                                  ::by
                                  ::text]))

(defn id [c] (:text c))

(defn comment? [c] (s/valid? ::comment c))
(defn why-not? [c] (s/explain ::comment c))
