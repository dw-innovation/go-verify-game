(ns kid-shared.posts.posts
  (:require [kid-shared.posts.posters :as posters]
            [kid-shared.types.post :as posts]))


(def p1-climate-refugees-copenhagen
  {:type :post-text
   :id "p1-climate-refugees-copenhagen"
   :description "So-called “climate refugees” from Copenhagen are trashing Liguria. Here’s a picture from the suburbs of Genoa taken yesterday -- this is an outrage! "
   :time-limit 4000
   :fake-news? true
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/p1-climate-refugees-copenhagen-main.jpg"
   :by posters/rainer-werner
   :explanation "The shown photograph was actually from Hurricaine Michael"
   :activities [{:type :reverse-image-simple
                 :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/p1-climate-refugees-copenhagen-main.jpg"
                        :result-search [{:url "www.cnn.com"
                                         :title "Hurricane Michael makes landfall, wrecks thousands of homes"
                                         :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/44873000045_6bfd1caaa5_w.jpg"
                                         :date "18 Feb 2019"
                                         :text "A coastal engineering consultant says Panama City Beach ... surveys the damage by Hurricane Michael over Panama City (...)"}
                                        {:url "www.reuters.com"
                                         :title "Florida latest – Hurricane Michael makes landfall at 9 a.m. ET"
                                         :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837095881_2c53650400_w.jpg"
                                         :date "15 Feb 2019"
                                         :text "Reuters – In light of Hurricane Michael, the Bay County Public Library is helping you salvaged damaged family treasures. ... A former Panama City Police officer was arrested Friday afternoon and has been charged with (...)"}
                                        ]
                        :result-images [{:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/31516192358_debc2e274d_w.jpg" }
                                        {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/44873000045_6bfd1caaa5_w.jpg"}
                                        {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/45322467302_313ceb4da3_w.jpg"}
                                        {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50836367178_c98fb56618_w.jpg"}
                                        {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837095881_2c53650400_w.jpg"}
                                        {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837182897_94bce499fa_w.jpg"}
                                        ]}}]})

(def migrant-1 {:type :post-text
                :id :migrant-1
                :time-limit 300
                :title ""
                :description "So-called 'climate refugees' from Copenhagen are trashing Liguria! Here's a picture from the suburbs of Genova taken yesterday. This is an outrage!"
                :subtext "Reverse image search reveals that picture was actually taken in Florida after Hurricane Michael."
                :image "img/migrant-1.jpeg"
                :fake-news? true
                :by {:name "@giovanni_smith"}})

(def climate-1 {:type :post-text
                :id :climate-1
                :time-limit 300
                :title ""
                :description "Vegan diet will be mandatory in Germany as of next month!"
                :subtext "Google search will reveal Svenja Schulze never said anything like this.
Simple image analytics/forensics will reveal the 'vegan' logo on her shirt is photoshopped. real account is @svenja_schulze "
                :image "img/climate-1.png"
                :fake-news? true
                :by {:name "@ministerin_schulze"}})

(def climate-3 {:type :post-text
                :id :climate-3
                :time-limit 300
                :title ""
                :description "Just got this satellite image from @ESA. This is what our continent looks like now. Thanks to climate change and rising sea levels. Hate to say I told you so!"
                :subtext "ESA account check reveals the image is real
Google search reveals the image is real
RIS reveals the image is real
 "
                :image "img/climate-3.jpg"
                :fake-news? false
                :by posters/rita-moonberg
                :activities [{:type :test
                              :data "hello"}]})

(def simple [{:type :post-text
              :description "Just got my delivery on rice grown in the New Baltic Sea, next to the New Rostock wind farms, yum! I <3 Megacorp eco grub!
"
              :by {:name "@jenny_mecklenburg"}}
             {:type :post-text
              :description "Bowie sightings in the Mediterranean patently false, say fact checkers."
              :by {:name "@newBBCnews"}}
             {:type :post-text
              :description "Megacorp now employs 51% of workers in the EU! [link]"
              :by {:name "@radio_europe"}}
             {:type :post-text
              :description "New owner of beachfront? Sell now for maximum interest.  10 markets to macro-profit on your micro home! [link]"
              :by {:name "@LaDefenseRealEstate"}}
             {:type :post-text
              :description "Here's how AI, blockchain tech, and quantum computing helps us fight climate change. [link]"
              :by {:name "@eu_innovation"}}
              {:type :post-text
              :description " Algae Deluxe Rolls – from our sea farms to your plate in less than 12 hours, guaranteed. Contains all the nutrients you need. Order now! [link]"
              :by {:name "@megacorp_ads"}}])


(def examples (concat [migrant-1 climate-1 climate-3] simple))

(def all-activity-posts [p1-climate-refugees-copenhagen migrant-1])
