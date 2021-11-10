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
                                  :image images/dw-logo}))

(def reuters (user/create {:name "Reuters"
                           :handle "@reuters"
                           :role :media
                           :image images/reuters-logo}))

(def zeit (user/create {:name "Zeit.de"
                        :handle "@zeit.de"
                        :role :media}))

(def freiheit378 (user/create {:name "Freiheit378"
                               :handle "@freiheit378"
                               :role :manipulator}))

(def eu-innovation (user/create {:name "EUI consortium"
                                 :handle "@eu-innovation"
                                 :role :media}))

(def rita-moonberg (user/create {:name "Rita Moonberg"
                                 :handle "@rita"
                                 :role :media}))

(def rainer-werner (user/create {:name "Rainer Werner"
                                 :handle "@rainer_werner"
                                 :role :media
                                 :image images/freiheit-logo}))

(def tktktktktk (user/create {:name "Anonimus"
                              :handle "@tktktktktk"
                              :role :media
                              :image images/reuters-logo}))

(def j_louis (user/create {:name "John Louis"
                           :handle "@j_louis"
                           :image images/j_louis
                           :role :media}))

(def financial_times (user/create {:name "The Financial Times"
                                   :handle "@FinancialTimes"
                                   :role :media
                                   :image images/ft-logo}))

(def financiel_times (user/create {:name "The Financial Times"
                                   :handle "@FinancielTimes"
                                   :role :media
                                   :image images/ft-logo}))

(def economy_economy (user/create {:name "the ECONOMY Blog"
                                   :handle "@economy_economy"
                                   :role :media}))

(def european_liberal (user/create {:name "In dubio pro libertate"
                                    :handle "@european_liberal"
                                    :role :media}))

(def alicia_ko (user/create {:name "Alicia Kowalski"
                             :handle "@alicia_ko"
                             :role :media}))

(def journo-jane (user/create {:name "Janelle Mao"
                               :handle "@journo-jane"
                               :role :media}))

(def patriot88 (user/create {:name "I'm a patriot, what are you?"
                             :handle "@patriot88"
                             :role :media}))

(def groceries4you (user/create {:name "*Downtown Groceries*"
                                 :handle "@groceries4you"
                                 :role :media}))

(def socialist_inquirer (user/create {:name "Socialist Inquirer"
                                      :handle "@socialist_inquirer"
                                      :role :media}))

(def patriotic_news (user/create {:name "Patriotic News"
                                      :handle "@patriotic_news"
                                      :role :media}))

(def stop_the_corp (user/create {:name "STOP THE CORP!"
                                 :handle "@Stop_the_Corp"
                                 :role :media}))

(def concerned_citizen (user/create {:name "Concerned Citizen"
                                     :handle "@_concerned_citizen"
                                     :role :media}))

(def c_report (user/create {:name "Climate Report"
                                     :handle "@c_report"
                                     :role :media}))

(def saxony4ever (user/create {:name "Danilo Backman"
                                     :handle "@saxony4ever"
                                     :role :media}))

(def ddoll99 (user/create {:name "Dresden Doll"
                                     :handle "@ddoll99"
                                     :role :media}))

(def thismightbesatire (user/create {:name "TMBS"
                                     :handle "@thismightbesatire"
                                     :role :media}))

(def int_news (user/create {:name "The Internationalist"
                                     :handle "@int_news"
                                     :role :media}))

(def aynrandftw (user/create {:name "Joe Libeal"
                              :handle "@aynrandftw"
                              :role :media}))

(def warehouseworker (user/create {:name "Abdul Smith"
                              :handle "@warehouseworker"
                              :role :media}))

(def anonymous_worker (user/create {:name "Anon"
                              :handle "@anonymous_worker"
                              :role :media}))


(def bloomberg_bizmonth (user/create {:name "Bloomberg Business Month"
                              :handle "@Bloomberg_BizMonth"
                              :role :media}))

(def channel1live (user/create {:name "Channel One"
                              :handle "@channel1live"
                              :role :media}))

(def socialmarkets2050 (user/create {:name "SoMa"
                              :handle "@socialmarkets2050"
                              :role :media}))


(def radio_europe (user/create {:name "Radio Europe"
                              :handle "@radio_europe"
                              :role :media}))

(def factsandfigures (user/create {:name "Facts and Figures"
                              :handle "@FactsAndFigures"
                              :role :media}))

(def workingmom99 (user/create {:name "Paula Adamovivh"
                              :handle "@workingmom99"
                              :role :media}))

(def megacorp_fresh (user/create {:name "Megacorp Fresh!"
                              :handle "@megacorp_fresh"
                              :role :media}))


(def undergroundpegasus (user/create {:name "Underground Pegasus"
                              :handle "@undergroundpegasus"
                              :role :media}))

(def coffeecakeweasel (user/create {:name "CCW"
                              :handle "@coffeecakeweasel"
                              :role :media}))

(def posters [deutsche-welle
              reuters
              zeit
              freiheit378
              rita-moonberg
              eu-innovation])

(defn random-poster []
  (get posters (rand-int (count posters))))

(defn gen-random-post [] (-> (post/gen-random)
                             (assoc :by (random-poster))))
