(ns kid-shared.posts.posters (:require [kid-shared.types.post :as post]
                                       [kid-shared.types.user :as user]
                                       [kid-shared.resources.images :as images]))

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

(def eu-innovation (user/create {:name "eu-innovation"
                                 :role :media
                                 :image images/freiheit-logo}))

(def rita-moonberg (user/create {:name "rita-moonberg"
                                 :role :media
                                 :image images/freiheit-logo}))
(def posters [deutsche-welle
              reuters
              zeit
              freiheit378
              rita-moonberg
              eu-innovation])

(defn random-poster []
  (get posters (rand-int (count posters))))

(defn gen-random-post [] (-> (post/gen-random)
                             (assoc :by (random-poster))))
