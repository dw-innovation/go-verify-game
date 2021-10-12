(ns kid-shared.types.post
  (:require [kid-shared.types.shared :as shared]
            [kid-shared.types.user :as user]
            [clojure.spec.test.alpha :as spec-test]
            [clojure.test.check :as check]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

(s/def ::description string?)
(s/def ::time-limit pos-int?)
(s/def ::fake-news? boolean?)
(s/def ::by ::user/user)

(s/def :post-text/type #{:post-text})
(s/def :re-post/type #{:re-post})

(s/def ::post-text (s/keys :req-un [:post-text/type
                                    ::shared/id
                                    ::by
                                    ::description]
                           :opt-un [::shared/created
                                    ::time-limit
                                    ::fake-news?]))


(s/def ::re-post (s/keys :req-un [:re-post/type
                                  ::shared/id
                                  ::by
                                  ::comment
                                  ::post]
                         :opt-un [::shared/created]))


(s/def ::post (s/or ::re-post ::re-post
                    ::post-text ::post-text))

(defn post? [p] (s/valid? ::post p))
(defn why-not? [p] (s/explain ::post p))

(gen/generate (s/gen ::post-text))

(defn gen-random [] (merge
                     (gen/generate (s/gen ::post-text))
                     ;; {:function! '(fn [v]
                     ;;                (println v)
                     ;;                (println "hi, this function was sent from the server over websockets"))}
                     ))
