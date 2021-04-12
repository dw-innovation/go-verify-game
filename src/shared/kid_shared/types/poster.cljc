(ns kid-shared.types.poster
  (:require [kid-shared.types.shared :as shared]
            [kid-shared.types.user :as user]
            [kid-shared.types.post :as post]
            [kid-shared.resources.images :as images]
            [clojure.spec.test.alpha :as spec-test]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.alpha :as s]))

;;
;; A datatype meant to be able to post certain news
;;  periodically
;;
;;

(def deutsche-welle (user/create {:name "Deutsche Welle"
                                  :role :media
                                  :image images/dw-logo}))

(def reuters (user/create {:name "Reuters"
                           :role :media
                           :image images/reuters-logo}))

(def zeit (user/create {:name "Zeit.de"
                        :role :media}))

(def freiheit378 (user/create {:name "Freiheit378"
                               :role :manipulator
                               :image images/freiheit-logo}))

(def posters [deutsche-welle
              reuters
              zeit
              freiheit378])

(defn random-poster []
  (get posters (rand-int (count posters))))

(defn gen-random-post [] (-> (post/gen-random)
                             (assoc :by (random-poster))))
