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
          :result-images [{:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/31516192358_debc2e274d_w.jpg" }
                          {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/44873000045_6bfd1caaa5_w.jpg"}
                          {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/45322467302_313ceb4da3_w.jpg"}
                          {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50836367178_c98fb56618_w.jpg"}
                          {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837095881_2c53650400_w.jpg"}
                          {:src "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/ris-results/50837182897_94bce499fa_w.jpg"}
                          ]}})

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


(def all-activities [ris-hurricaine-michael])
