(ns kid-shared.data.stories (:require [kid-shared.data.authors :as posters]
                                              [kid-shared.data.posts :as posts]))

;; here's the deal about stories.  they are a vector.  they can contain
;; different data types.  they are played out in order. a number means wait that amount
;; of seconds.  a map is passed through clojure spec to figure out what it is, then a
;; new message is created.  another vector is considered a sub-story, and
;; recursively calls this function.

;;
;;
;;  How to construct a story:
;;
(def climate
    ;; create a "story" out of those posts.
    ;; you can attatch that story in kid-shared/generator.cljc
    [2 ; a number means wait this amount of seconds
     ;; a post will create a new post in the timeline
     posts/p1-climate-refugees-copenhagen
     3
     ;; a comment create's a new comment
     {:post-id (:id posts/p1-climate-refugees-copenhagen)
      :by posters/tktktktktk
      :text "Would these people treat their country like they treat their kind host, Italy? Me thinks not. #notAllWelcome"}
     ;; a vector, i.e. another story will generate a new story
     ;; [3 posts/climate-3 5 {:post-id (:id posts/climate-3)
     ;;                       :by posters/tktktktktk
     ;;                       :text "i agree!"}]
     5
     posts/p10-snow-cannons-story
     3
     posts/p7-danes-crossing
     10
     {:post-id (:id posts/p1-climate-refugees-copenhagen)
      :by posters/tktktktktk
      :text "Too much is too much, when will the EU stop bickering and fix this flooding (pardon the pun) of so-called climate refugees? #notallWelcome"}
     20
     posts/p1-climate-refugees-copenhagen-response
     5
     posts/p9-flooding-dams])

(def financial-times
  [posts/p2-financial-gdp
   20
   posts/p2-financial-gdp-repost-1
   10
   posts/p3-financiel-gdp
   3
   {:post-id (:id posts/p3-financiel-gdp)
    :by posters/patriot88
    :text "Not a surprise if you ask me. Bloody Baltic bastards. #NotAllWelcome"}
   10
   posts/p2-financial-gdp-repost-2
   3
   {:post-id (:id posts/p3-financiel-gdp)
    :by posters/groceries4you
    :text "Met two of them at my store yesterday. Guy #1 pretended to apply for a job while guy #2 apparently shoved a two dozen chocolate bars into his fancy backpack. Goddamn thieves is what they are."}
   10
   posts/p3-financiel-gdp-repost-1
   15
   posts/p3-financiel-gdp-repost-2])

(def minister-in-the-city
  [posts/p4-minister-in-city])

(def luxembourg
  [posts/p5-luxembourgian-president])

(def megacorp
  [posts/megacorp-filler-story
   3
   posts/p6-urine-bottles
   15
   posts/p8-megacorp-traffic
   4
   posts/p11-megacorp-prisons-story])

(def game-story
  [minister-in-the-city
   14
   megacorp
   4
   climate
   15
   financial-times])

(def all-stories [["game-story (all)" game-story]
                  ["bulgarian president" luxembourg]
                  ["climate" climate]
                  ["megacorp" megacorp]
                  ["financial times" financial-times]
                  ["minister in a strange city" minister-in-the-city]])
