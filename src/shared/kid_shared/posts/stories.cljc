(ns kid-shared.posts.stories (:require [kid-shared.posts.posters :as posters]
                                              [kid-shared.posts.posts :as posts]))

;;
;;
;;  How to construct a story:
;;
(def a-new-racism
  ;; create a couple of posts:
  (let [original posts/p1-climate-refugees-copenhagen
        repost-1 {:type :re-post
                  :id "p1-climate-refugees-copenhagen-rp-1"
                  :post original
                  :by posters/tktktktktk
                  :comment "Would these people treat their country like they treat their kind host, Italy? Methinks not. #notAllWelcome"}
        repost-2 {:type :re-post
                  :id "p1-climate-refugees-copenhagen-rp-2"
                  :post original
                  :by posters/tktktktktk
                  :comment "Too much is too much, when will the EU stop bickering and fix this flooding (pardon the pun) of so-called climate refugees? #notallWelcome"}]
    ;; create a "story" out of those posts.
    ;; you can attatch that story in kid-shared/generator.cljc
    [2
     original
     20
     repost-1
     20
     repost-2]))


(def default-story
  [
   1
   posts/climate-3
   ;; 2
   ;; {:type :post-text
   ;;  :description "first random post"
   ;;  :by posters/rita-moonberg}
   ;; 4
   ;; {:type :post-text
   ;;  :description "second random post"
   ;;  :by posters/zeit}
   ;; 4
   ;; posts/climate-3
   ;; 15
   ;; posts/migrant-1
   ;; 2
   ;; {:type :post-text
   ;;  :description "hello!  some random post"
   ;;  :by posters/freiheit378}
   ])

(def all-stories [["a new racism" a-new-racism]
                  ["default" default-story]])
