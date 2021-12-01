(ns kid-shared.data.authors (:require [kid-shared.types.post :as post]
                                      [kid-shared.types.user :as user]
                                      [kid-shared.resources.images :as images]))

;;
;; A datatype meant to be able to post certain news
;;  periodically
;;
;;

(def deutsche-welle (user/create {:name "Deutsche Welle"
                                  :handle "@dw"
                                  :role :media
                                  :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40dw.jpg"
                                  }))

(def reuters (user/create {:name "Reuters"
                           :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40reuters.jpg"
                           :handle "@reuters"
                           :role :media}))

(def zeit (user/create {:name "Zeit.de"
                        :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40zeit.jpg"
                        :handle "@zeit.de"
                        :role :media}))

(def freiheit378 (user/create {:name "Freiheit378"
                               :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40freiheit378.jpg"
                               :handle "@freiheit378"
                               :role :manipulator}))

(def eu-innovation (user/create {:name "EUI consortium"
                                 :handle "@eu-innovation"
                                 :role :media}))

(def rita-teenberg (user/create {:name "Rita Teenberg"
                                 :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40rita.jpg"
                                 :handle "@rita_teenberg"
                                 :role :media}))

(def rainer-werner (user/create {:name "Rainer Werner"
                                 :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40rainer_werner.jpg"
                                 :handle "@rainer_werner"
                                 :role :media}))


(def tktktktktk (user/create {:name "Anonimus"
                              :handle "@tktktktktk"
                              :role :media
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40anonimus.jpg"}))

(def j_louis (user/create {:name "John Louis"
                           :handle "@j_louis"
                           :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40j_louis.jpg"
                           :role :media}))

(def financial_times (user/create {:name "The Financial Times"
                                   :handle "@FinancialTimes"
                                   :role :media
                                   :image images/ft-logo}))

(def financiel_times (user/create {:name "The Financial Times"
                                   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40FinancielTimes.jpg"
                                   :handle "@FinancielTimes"
                                   :role :media
                                   }))

(def economy_economy (user/create {:name "the ECONOMY Blog"
                                   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40economy_economy.jpg"
                                   :handle "@economy_economy"
                                   :role :media}))

(def european_liberal (user/create {:name "In dubio pro libertate"
                                    :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40european_liberal.jpg"
                                    :handle "@european_liberal"
                                    :role :media}))

(def alicia_ko (user/create {:name "Alicia Kowalski"
                             :handle "@alicia_ko"
                             :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40alicia_ko.jpg"
                             :role :media}))

(def journo-jane (user/create {:name "Janelle Mao"
                               :handle "@journo-jane"
                               :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40journo-jane.jpg"
                               :role :media}))

(def patriot88 (user/create {:name "I'm a patriot, what are you?"
                             :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40patriot88.jpg"
                             :handle "@patriot88"
                             :role :media}))

(def groceries4you (user/create {:name "*Downtown Groceries*"
                                 :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40groceries4you.jpg"
                                 :handle "@groceries4you"
                                 :role :media}))

(def socialist_inquirer (user/create {:name "Socialist Inquirer"
                                      :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40socialist_inquirer.jpg"
                                      :handle "@socialist_inquirer"
                                      :role :media}))

(def patriotic_news (user/create {:name "Patriotic News"
                                  :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40patriotic_news.jpg"
                                      :handle "@patriotic_news"
                                      :role :media}))

(def stop_the_corp (user/create {:name "STOP THE CORP!"
                                 :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40Stop_the_Corp.jpg"
                                 :handle "@Stop_the_Corp"
                                 :role :media}))

(def concerned_citizen (user/create {:name "Concerned Citizen"
                                     :handle "@_concerned_citizen"
                                     :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40_concerned_citizen.jpg"
                                     :role :media}))

(def c_report (user/create {:name "Climate Report"
                            :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40c_report.jpg"
                                     :handle "@c_report"
                                     :role :media}))

(def saxony4ever (user/create {:name "Danilo Backman"
                               :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40saxony4ever.jpg"
                                     :handle "@saxony4ever"
                                     :role :media}))

(def ddoll99 (user/create {:name "Dresden Doll"
                           :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40ddoll99.jpg"
                                     :handle "@ddoll99"
                                     :role :media}))

(def thismightbesatire (user/create {:name "TMBS"
                                     :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40Stop_the_Corp.jpg"
                                     :handle "@thismightbesatire"
                                     :role :media}))

(def int_news (user/create {:name "The Internationalist"
                            :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40int_news.jpg"
                                     :handle "@int_news"
                                     :role :media}))

(def aynrandftw (user/create {:name "Joe Liberal"
                              :handle "@aynrandftw"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40ayndrandftw.jpg"
                              :role :media}))

(def warehouseworker (user/create {:name "Abdul Smith"
                                   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40warehouseworker.jpg"
                              :handle "@warehouseworker"
                              :role :media}))

(def anonymous_worker (user/create {:name "Anon"
                              :handle "@anonymous_worker"
                                    :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40anonymous_worker.jpg"
                              :role :media}))


(def bloomberg_bizmonth (user/create {:name "Bloomberg Business Month"
                                      :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40Bloomberg_BizMonth.jpg"
                              :handle "@Bloomberg_BizMonth"
                              :role :media}))

(def channel1live (user/create {:name "Channel One"
                                :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40channel1live.jpg"
                              :handle "@channel1live"
                              :role :media}))

(def socialmarkets2050 (user/create {:name "SoMa"
                                     :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40socialmarkets2050.jpg"
                              :handle "@socialmarkets2050"
                              :role :media}))


(def radio_europe (user/create {:name "Radio Europe"
                                :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40radio_euopre.jpg"
                              :handle "@radio_europe"
                              :role :media}))

(def factsandfigures (user/create {:name "Facts and Figures"
                                   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40FactsAndFigures.jpg"
                              :handle "@FactsAndFigures"
                              :role :media}))

(def workingmom99 (user/create {:name "Paula Adamovich"
                                :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40workingmom99.jpg"
                                :handle "@workingmom99"
                                :role :media}))

(def megacorp_fresh (user/create {:name "Megacorp Fresh!"
                                  :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40megacorp_fresh.jpg"
                              :handle "@megacorp_fresh"
                              :role :media}))


(def undergroundpegasus (user/create {:name "Underground Pegasus"
                                      :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40undergroundpegasus.jpg"
                              :handle "@undergroundpegasus"
                              :role :media}))

(def coffeecakeweasel (user/create {:name "CCW"
                                    :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40coffecakeweasel.jpg"
                              :handle "@coffeecakeweasel"
                              :role :media}))

(def barbcamara (user/create {:name "Barb Camara (SkyNews)"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40BarbCamara.jpg"
                              :handle "@BarbCamara"}))

(def blueberrysagittarius (user/create {:name "Raffaela B."
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40blueberrysagittarius.jpg"
                              :handle "@blueberrysagittarius"}))

(def unitedneocons (user/create {:name "NeoCons"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40unitedneocons.jpg"
                              :handle "@unitedneocons"}))

(def miss_jenny_m (user/create {:name "Jenny Mecklenburg"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40miss_jenny_m.jpg"
                              :handle "@miss_jenny_m"}))

(def marsexpress (user/create {:name "Life on Mars?"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40marsexpress.jpg"
                              :handle "@marsexpress"}))


(def lightningtangerine (user/create {:name "Lightning Tangerine"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40lighningtangerine.jpg"
                              :handle "@lightningtangerine"}))

(def bloggers_united (user/create {:name "Bloggers United"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40bloggers_united.jpg"
                              :handle "@bloggers_united"}))

(def megacorp_press (user/create {:name "Megacorp Press Department"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40bloggers_united.jpg"
                              :handle "@megacorp_press"}))

(def emily_dupont (user/create {:name "Emily D."
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40emily_dupont.jpg"
                              :handle "@emily_dupont"}))

(def gardiannews (user/create {:name "Guardian"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40gardiannews.jpg"
                              :handle "@gardiannews"}))

(def livingbytheriver99 (user/create {:name "LBTR"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40livingbytheriver99.jpg"
                              :handle "@livingbytheriver99"}))

(def blackberrycurrentcat (user/create {:name "Blackberry "
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40blackberrycurrentcat.jpg"
                              :handle "@blackberrycurrentcat"}))

(def dbf_eu (user/create {:name "Don't be Fooled!"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40dbf_eu.jpg"
                              :handle "@dbf_eu"}))

(def krill (user/create {:name "Wink"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40krill.jpg"
                              :handle "@krill"}))

(def LDRE (user/create {:name "La Defense Real Estate"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40ldre.jpg"
                              :handle "@krill"}))

(def newBBCnews (user/create {:name "New BBC News"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40newBBCnews.jpg"
                              :handle "@newBBCnews"}))

(def eu_innovation (user/create {:name "EU Innovation"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40eu_innovation.jpg"
                              :handle "@eu_innovation"}))


(def good_quotes (user/create {:name "Good Quotes"
                              :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/avatars/%40good_quotes.jpg"
                              :handle "@good_quotes"}))

(def posters [deutsche-welle
              reuters
              zeit
              freiheit378
              eu-innovation])

(defn random-poster []
  (get posters (rand-int (count posters))))

(defn gen-random-post [] (-> (post/gen-random)
                             (assoc :by (random-poster))))
