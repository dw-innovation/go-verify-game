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
     posts/p1-climate-refugees-copenhagen-with-comments
     5
     posts/p10-snow-cannons-story
     3
     posts/p7-danes-crossing?
     posts/p9-flooding-dams?])

(def financial-times
  [posts/p2-financial-gdp
   20
   posts/p2-financial-gdp-repost-1
   10
   posts/p3-financiel-gdp?-story
   10
   posts/p3-financiel-gdp-repost-1
   15
   posts/p3-financiel-gdp-repost-2])

(def global-story
  [
   1 posts/p17-megacorp-food-fan-story
   12 posts/p18-rita-talks-about-bowie
   6 posts/p13-megacorp-buying-spree-story
   12 posts/p16-satellite-image?-story
   12 posts/p15-algae-deluxe-story
   5 posts/p12-megacorp-salad
   10 posts/p14-megacorp-employment

   12 posts/p6-urine-bottles?-story
   12 posts/p8-megacorp-traffic?-story
   12 posts/p11-megacorp-prisons?-story
   12 posts/p5-luxembourgian-president?
   12 posts/p4-minister-in-city?

   20
   ])

(def all-stories [["game-story (all)" global-story]
                  ["rita" posts/p16-satellite-image?-story]
                  ["urine-bottkes" posts/p6-urine-bottles?-story]
                  ["climate" climate]
                  ["financial times" financial-times]
                  ])
