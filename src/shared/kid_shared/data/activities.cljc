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

(def bulgarian-president-fake-image
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


(def all-activities [ris-hurricaine-michael])
