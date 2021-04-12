(ns kid-shared.types.post
  (:require [kid-shared.types.shared :as shared]
            [kid-shared.types.user :as user]
            [clojure.spec.test.alpha :as spec-test]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

(s/def ::title string?)
(s/def ::description string?)
(s/def ::time pos-int?)
(s/def ::fake-news? boolean?)
(s/def ::by ::user/user)

(s/def :post-text/type #{:post-text})
(s/def :post-default/type #{:post-default})
(s/def :re-post/type #{:re-post})

(s/def ::post-text (s/keys :req-un [:post-text/type
                                    ::shared/id
                                    ::shared/created
                                    ::title
                                    ::time
                                    ::fake-news?
                                    ::by
                                    ::description]))

(s/def ::re-post (s/keys :req-un [:re-post/type
                                  ::shared/created
                                  ::shared/id
                                  ::by
                                  ::comment
                                  ::time
                                  ::post]))

(s/def ::post-default (s/keys :req-un [:post-default/type
                                       ::shared/created
                                       ::shared/id
                                       ::title
                                       ::description]))

(s/def ::post (s/or ::post-default ::post-default
                    ::re-post ::re-post
                    ::post-text ::post-text))

(gen/generate (s/gen ::post-text))

(defn gen-random [] (merge
                     (gen/generate (s/gen ::post-text))
                     {:function! '(fn []
                                    (js/alert "hi, this function was sent from the server over websockets"))}))
