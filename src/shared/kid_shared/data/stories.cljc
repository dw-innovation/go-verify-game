(ns kid-shared.data.stories (:require [kid-shared.data.authors :as posters]
                                      [kid-shared.data.posts :as posts]))

;; here's the deal about stories.  they are a vector.  they can contain
;; different data types.  they are played out in order. a number means wait that amount
;; of seconds.  a map is passed through clojure spec to figure out what it is, then a
;; new message is created.  another vector is considered a sub-story, and
;; recursively calls this function.


(def global-story
  [1 posts/p17-megacorp-food-fan-story
   6 posts/p13-megacorp-buying-spree-story
   12 posts/p16-satellite-image?-story
   5 posts/filler-new-beachfronts
   12 posts/p15-algae-deluxe-story
   12 posts/p18-rita-talks-about-bowie
   5 posts/p12-megacorp-salad
   10 posts/p20-river-thames-estuary?-story

   5 posts/filler-osint-tool

   10 posts/p14-megacorp-employment
   12 posts/p6-urine-bottles?-story
   5 posts/filler-wink-corona
   5 posts/filler-osint-funding
   12 posts/p8-megacorp-traffic?-story

   7 posts/filler-bowie-boat

   6 posts/filler-mil-tutorials

   12 posts/p11-megacorp-prisons?-story

   12 posts/filler-quote-factitious-fictitious

   5 posts/filler-bowie-sightings
   12 posts/p5-luxembourgian-president?

   6 posts/filler-mil-dossier

   12 posts/p4-minister-in-city?
   5 posts/filler-blockchain
   6 posts/filler-quote-wet-rag
   13 posts/p19-chinese-minister-of-finance?
   9 posts/filler-bowie-quote

   10 posts/p1-climate-refugees-copenhagen-with-comments

   10 posts/p2-financial-gdp
   5  posts/p2-financial-gdp-repost-1

   12 posts/filler-megacorp-labor-laws-story ;; this one should have some time after it

   12 posts/p21-eu-commission-hearing?

   10 posts/p10-snow-cannons-story

   6 posts/filler-mil-media-literacy

   10 posts/p7-danes-crossing?

   10 posts/p3-financiel-gdp?-story

   6 posts/filler-apple-banana

   10 posts/p3-financiel-gdp-repost-1
   ;; 15 posts/p3-financiel-gdp-repost-2
   3 posts/p9-flooding-dams?
   5 posts/filler-mil-kidz
   8 ;; wait time before thomas
   ])

(def all-stories [["game-story (all)" global-story]])
