(ns kid-shared.posts.stories (:require [kid-shared.posts.posters :as posters]
                                              [kid-shared.posts.posts :as posts]))

;; here's the deal about stories.  they are a vector.  they can contain
;; different data types.  they are played out in order. a number means wait that amount
;; of seconds.  a map is passed through clojure spec to figure out what it is, then a
;; new message is created.  another vector is considered a sub-story, and
;; recursively calls this function.

;;
;;
;;  How to construct a story:
;;
(def a-new-racism
  ;; create a couple of posts:
  (let [original posts/p1-climate-refugees-copenhagen
        comment-1 {:post-id (:id original)
                   :by posters/tktktktktk
                   :text "Would these people treat their country like they treat their kind host, Italy? Methinks not. #notAllWelcome"}
        comment-2 {:post-id (:id original)
                   :by posters/tktktktktk
                   :text "Too much is too much, when will the EU stop bickering and fix this flooding (pardon the pun) of so-called climate refugees? #notallWelcome"}]
    ;; create a "story" out of those posts.
    ;; you can attatch that story in kid-shared/generator.cljc
    [
     ;; a number means wait this amount of seconds
     2
     ;; a post will create a new post in the timeline
     original
     20
     ;; a comment create's a new comment
     comment-1
     ;; a vector, i.e. another story will generate a new story
     [3 posts/climate-3 5 {:post-id (:id posts/climate-3)
                           :by posters/tktktktktk
                           :text "i agree!"}]
     20
     comment-2]))


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
