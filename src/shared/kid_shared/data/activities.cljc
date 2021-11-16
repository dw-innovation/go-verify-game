(ns kid-shared.data.activities)

(def ris-hurricaine-michael
  {:type :reverse-image-simple
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
          ;; :result-images [{:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/31516192358_debc2e274d_w.jpg" }
          ;;                 {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/44873000045_6bfd1caaa5_w.jpg"}
          ;;                 {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/45322467302_313ceb4da3_w.jpg"}
          ;;                 {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50836367178_c98fb56618_w.jpg"}
          ;;                 {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837095881_2c53650400_w.jpg"}
          ;;                 {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837182897_94bce499fa_w.jpg"}
          ;;                 ]
          }})

(def financiel-web-search
  {:type :web-search
   :data {:terms ["Financial Times" "UNHCR" "Climate Refugees"]
          :loading-time 4000
          :results [{:url "fuzzbeat.com"
                     :title "How European imposter accounts like `Financiel times` agitate against climate refugees."
                     :date "3 days ago"}
                    {:url "unhcr.org"
                     :title "High Commissioner Van de Mens confirms North America is currently taking in more climate refugees than Central Europe."
                     :date "5 days ago"}]}})

(def ris-minister-in-city
  {:type :reverse-image-crop
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p4-minister-in-the-city/Frankfurt_BusinessMan_2+1.jpg"
          :dimensions [1112 625]
          :result-search []
          :crop-hit-box [:path {:d "M9 600V9H609V600H9Z"
                                 :transform "translate(497,18)"}]
          :result-search-after-crop [{:url "gizmodo.com"
                                      :title "Article about Frankfurt, Germany"
                                      :text "Subtext about Frankfurt, Germany which is some random article, but shows the s"
                                      :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p4-minister-in-the-city/main2+2.jpg"
                                      :date "5 days ago"}
                                     ]}})

(def luxembourgian-president-fake-image
  {:type :polygon-search
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p5-bulgarian-president/ai-president.jpg"
          :dimensions [1024 1024]
          :polygons [{:shape [:path {:d "M94 50.5L51.5 96.5L3 50.5L22 3.5L75 11.5L94 50.5Z"
                                     :transform "translate(436,150.5)"}]
                      :message "A weird spot indeed."}
                     {:shape [:path {:d "M127.5 98L68.5 160.5L3.5 90L46 3H114.5L127.5 98Z"
                                     :transform "translate(579,140)"}]
                      :message "This strange artifact suggests the picture has been created by an AI."}
                     {:shape [:path {:d "M87 4.5L95.5 87L6 44.5L87 4.5Z"
                                     :transform "translate(112,680)"}]
                      :message "A weird spot indeed."}
                     {:shape [:path {:d "M290 223.5H4L28.5 114L100.5 26.5L179.5 4L197 105.5L290 223.5Z"
                                     :transform "translate(26,797)"}]
                      :message "This strange artifact suggests the picture has been created by an AI."}]}})

(def luxembourgian-president-web-search
  {:type :web-search
   :data {:terms ["Luxembourgian President" "EU" "Beret"]
          :loading-time 4000
          :results [{:url "factful.eu"
                     :title "\"Luxembourgian president\" viral post is a deepfake."
                     :date "2 days ago"}
                    {:url "thejournal.com"
                     :title "EU separatists: Right-wing tabloid \"@patriotic_news\" creates fake Luxembourgian president."
                     :date "2 days ago"}
                    {:url "fact-checkers-alliance.org"
                     :title "\"That's not even the traditional Luxembourgian beret\""
                     :date "1 day ago"}]}})

(def urine-bottles-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p6-urine-bottles/main.jpg"
          :result-search [
                          {:url "nnyt.com"
                           :title "Megacorp, class struggle and exaggerated stories"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p6-urine-bottles/main.jpg"
                           :date "3 hours ago"
                           :text "As Europe's most powerful conglomerate continues to exploit its blue collars, activists resort to spreading fabricated news. \"That's a terrible idea that will hurt the movement of precarious workers\", says..."}
                          {:url "inquiringminds.com"
                           :title "Yes, Megacorp is lying, but so are some activists"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p6-urine-bottles/main.jpg"
                           :date "5 hours ago"
                           :text "Online research reveals that Megacorp indeed made employees pee in bottles. However, there is no proof they have been recycling and selling..."}
                          ]
          }})

(def danes-crossing-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p7-danes-crossing/main.jpg"
          :result-search [
                          {:url "guardian.co.uk"
                           :title "Syrian Civil War: Refugees try to reach Greece via Turkey and the Mediterranean"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p7-danes-crossing/26523152511_421e9eb13a_w.jpg"
                           :date "16 years ago"}
                          {:url "independentnews.com"
                           :title "Greek-Turkish border crisis: Desparate Syrian refugees are crossing..."
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p7-danes-crossing/5308747015_54acba7c80_w.jpg"
                           :date "16 years ago"}
                          ]
          }})

(def megacorp-traffic-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/main.jpg"
          :result-search [
                          {:url "thehippocrit.com"
                           :title "The 13 REAL Reasons ATL Traffic Sucks"
                           :text "Atlanta's traffic supposedly sucks because: there just isn't enough space to move so many people through a metropolitan …"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/1c.jpg"
                           :date "3 weeks ago"}
                          {:url "us-lefty.com"
                           :title "Suburbs and the New American Poverty"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/1c.jpg"
                           :text "In the last 10 years alone, Atlanta's poor population in the suburbs grew by 159 percent. The promise the suburbs of Atlanta once held is no longer ..."
                           :date "2 months ago"}
                          {:url "hackaway.eu"
                           :title "Phone Tracking Heatmap"
                           :text "In my latest project I went all deep on localisation data. I tracked my own phone while traveling Germany using a new algorithm and ..."
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/2c.jpg"
                           :date "1 year ago"}
                          {:url "techranch.com"
                           :title "What your phone reveals about you"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/2c.jpg"
                           :text "99% of EU citizens own a smartphone with GPS function. In this post, the hackaway collective shows how easy it is to exploit..."
                           :date "1 year ago"}
                          ]
          }})

(def flooding-dams-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p9-flooding-dams/main.jpg"
          :result-search [
                          {:url "trekadvisor.com"
                           :title "The most beautiful waterfalls in Patagonia"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p9-flooding-dams/main.jpg"
                           :date "16 years ago"}
                          {:url "travelbloggers.org"
                           :title "Discover Salto Grande and Torres del Paine National Park"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p9-flooding-dams/main.jpg"
                           :date "16 years ago"}
                          ]
          }})

(def snow-cannons-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p10-snow-cannons/main.jpg"
          :result-search [
                          {:url "oslo-tourism.com"
                           :title "From Akershus Fortress to Holmenkollen Ski Jump: 12 top-rated tourist attractions in Norway's Capital"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p10-snow-cannons/main.jpg"
                           :date "16 years ago"}
                          {:url "holmenkollen.org"
                           :title "Discover Oslo's iconic Ski Jump"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p10-snow-cannons/main.jpg"
                           :date "16 years ago"}
                          ]
          }})

(def megacorp-prisons-ris
  {:type :reverse-image-simple
   :data {:main-image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p11-megacorp-prisons/main.jpg"
          :result-search [
                          {:url "thejournal.com"
                           :title "Heated debate over surveillance and intimidation of workers at Megacorp factories after investigative report by The Internationalist"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p11-megacorp-prisons/main.jpg"
                           :date "2 hours ago"}
                          {:url "legalmatters.com"
                           :title "Megacorp's European Distro Centers: Drastic Breaches of Labor Law"
                           :img-src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p11-megacorp-prisons/main.jpg"
                           :date "2 days ago"}
                          ]
          }})


(def all-activities [ris-hurricaine-michael])
