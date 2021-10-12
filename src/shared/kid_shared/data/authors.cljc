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
