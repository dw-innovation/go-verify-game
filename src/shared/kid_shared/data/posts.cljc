(ns kid-shared.data.posts
  (:require [kid-shared.data.authors :as authors]
            [kid-shared.types.post :as posts]
            [kid-shared.data.activities :as activities]))


(def p1-climate-refugees-copenhagen
  {:type :post-text
   :id "p1-climate-refugees-copenhagen"
   :description "So-called “climate refugees” from Copenhagen are trashing Liguria. Here’s a picture from the suburbs of Genoa taken yesterday -- this is an outrage! "
   :time-limit 4000
   :fake-news? true
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/p1-climate-refugees-copenhagen-main.jpg"
   :by authors/rainer-werner
   :explanation "The shown photograph was actually from Hurricaine Michael"
   :activities [activities/ris-hurricaine-michael]})

(def p1-climate-refugees-copenhagen-response
  {:type :re-post
   :id "p1-climate-refugees-copenhagen-response"
   :post p1-climate-refugees-copenhagen
   :comment "Can’t believe this is still happening: the picture of the devastated seafront doing the rounds in certain groups is absolutely fake. We’re tracking a small network of accounts pushing these news, stay tuned."
   :by authors/j_louis})

(def p2-financial-gdp
  {:type :post-text
   :id "p2-financial-gdp"
   :description "Pan-EU Q2 GDP release: -2.4% from Q1, (1.2 percentage point lower than forecast). Follow our liveblog for reactions from around the continent. https://on.ft.com/live/eX2GhH"
   :time-limit 900
   :by authors/financial_times})

(def p2-financial-gdp-repost-1
  {:type :re-post
   :id "p2-financial-gdb-repost-1"
   :comment "There you have it: In spite of years of calamities and large-scale population displacement, we’re still seeing only a *relatively manageable* contraction of the economy."
   :post p2-financial-gdp
   :by authors/economy_economy})

(def p2-financial-gdp-repost-2
  {:type :re-post
   :id "p2-financial-gdb-repost-2"
   :comment "Indeed the “Miracle of the Mid-Century” as Mr John said. Things did NOT go to shit, and this is truly mind-boggling."
   :post p2-financial-gdp
   :by authors/european_liberal})

(def p3-financiel-gdp
  {:type :post-text
   :id "p3-financiel-gdp"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p3-financiel-gdp/chart_migrants.jpeg"
   :fake-news? true
   :time-limit 900
   :by authors/financiel_times
   :description "Central Europe faces severe economic problems due large-scale arrival of lazy and criminal climate refugees."
   :activities [activities/financiel-web-search]})

(def p3-financiel-gdp-repost-1
  {:type :re-post
   :id "p3-financiel-gdp-repost-1"
   :comment "'Lazy and criminal refugees?' So the FT has joined the right-wingers and hate mongers now? Unsubscribing!"
   :post p3-financiel-gdp
   :by authors/alicia_ko})

(def p3-financiel-gdp-repost-2
  {:type :re-post
   :id "p3-financiel-gdp-repost-2"
   :comment " Yet another example of false narratives driven by those who’d rather leave our neighbours to their misery. Climate problems concern ALL OF US, and blatant racism isn’t helping!"
   :post p3-financiel-gdp
   :by authors/journo-jane})

(def p4-minister-in-city
  {:type :post-text
   :id "p4-minister-in-city"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p4-minister-in-the-city/Frankfurt_BusinessMan_2+1.jpg"
   :description "EU Minister of Finance spotted near Megacorp Headquarters in Shanghai. What shady deal is he getting into now?"
   :time-limit 2000
   :fake-news? true
   :by authors/socialist_inquirer
   :activities [activities/ris-minister-in-city]})

(def p5-luxembourgian-president
  {:type :post-text
   :id "p5-luxembourgian-president"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p5-bulgarian-president/ai-president.jpg"
   :description "New Luxembourgian president at press conference: \"I've had it with the Union! We'll be an independent nation again, spend 20% of the GDP on our military – and proudly wear the traditional beret!”"
   :time-limit 1000
   :fake-news? true
   :by authors/patriotic_news
   :activities [activities/luxembourgian-president-fake-image
                activities/luxembourgian-president-web-search]})

(def p6-urine-bottles
  {:type :post-text
   :id "p6-urine-bottles"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p6-urine-bottles/main.jpg"
   :description "Exclusive: Megacorp forces workers to pee in bottles!"
   :by authors/stop_the_corp
   :time-limit 500
   :fake-news? true
   :activities [activities/urine-bottles-ris]})

(def p7-danes-crossing
  {:type :post-text
   :id "p7-danes-crossing"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p7-danes-crossing/main.jpg"
   :description "What they told us: \"More workforce, more growth!” What we got: Trashed beaches everywhere. Thousands of non-working age Danes crossing over."
   :by authors/concerned_citizen
   :time-limit 700
   :fake-news? true
   :activities [activities/danes-crossing-ris]
   })

(def p8-megacorp-traffic
  {:type :post-text
   :id "p8-megacorp-traffic"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/main.jpg"
   :description "The whole “Megacorp is saving us, cut them some slack” argument is wrong. We gave up so much public space, health and wellbeing so they could “help us”. Result? Cities paralysed by traffic jams, caused by that massive fleet of white delivery vans. Just look at the sheer number of them serving Germany only yesterday evening. https://pic.beep.com/h9HXt1"
   :by authors/stop_the_corp
   :time-limit 1300
   :fake-news? true
   :activities [activities/megacorp-traffic-ris]
   })

(def p9-flooding-dams
  {:type :post-text
   :id "p9-flooding-dams"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p9-flooding-dams/main.jpg"
   :description "EU lies exposed: The floddings in Northern Europe aren't caused by \"climate change\", the government is blowing up dams! #investigative"
   :by authors/c_report
   :time-limit 800
   :fake-news? true
   :activities [activities/flooding-dams-ris]
   })

(def p10-snow-cannons
  {:type :post-text
   :id "p10-snow-cannons"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p10-snow-cannons/main.jpg"
   :description "Remember how Dresden allegedly experienced its \"coldest winter since records began\" last year? Well, it was all make believe! Digging through secret archives, we found proof that greens and socialists colluded to bombard the city with snow cannons! #factchecking #investigative "
   :by authors/c_report
   :time-limit 800
   :fake-news? true
   :activities [activities/snow-cannons-ris]
   })
(def p10-comment-1 {:post-id "p10-snow-cannons"
                    :by authors/saxony4ever
                    :text "Ha! I knew it was all a big hoax. Bet those cannons were operated by \"climate refugees\". They'll do anything to steal our tax money!"} )
(def p10-comment-2 {:post-id "p10-snow-cannons"
                    :by authors/ddoll99
                    :text "Guys, are you serious?"})
(def p10-comment-3 {:post-id "p10-snow-cannons"
                    :by authors/thismightbesatire
                    :text "Wow, the city and the Elbe Valley sure changed a lot. Did the green communists also launch a large-scale terraforming project?"
                    })
(def p10-snow-cannons-story [p10-snow-cannons 3 p10-comment-1 7 p10-comment-2 9 p10-comment-3])


(def p11-megacorp-prisons
  {:type :post-text
   :id "p11-megacorp-prisons"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p11-megacorp-prisons/main.jpg"
   :description "Barbed wire, spring guns, drone surveillance: Megacorp's new distro centers are run like prisons"
   :by authors/int_news
   :time-limit 3000
   :fake-news? false
   :activities [activities/megacorp-prisons-ris]})
(def p11-megacorp-prisons-story [p11-megacorp-prisons
                                 5 {:post-id "p11-megacorp-prisons"
                                    :by authors/aynrandftw
                                    :text "That's ridiculous, and you know it. Sure, they have security to stop the bloody unionists from wrecking the place, but MC workers are treated well, earn decent wages and enjoy a lot of benefits!"}
                                 3 {:post-id "p11-megacorp-prisons"
                                    :by authors/warehouseworker
                                    :text "A colleague of mine was injured by an MC spring gun last week. His 'rule violation': Leaving 10 minutes before the shift ended, so he could pick up his kid from hospital."}
                                 3 {:post-id "p11-megacorp-prisons"
                                    :by authors/anonymous_worker
                                    :text "Corporate bastards! They'll get what they deserve soon."}])

(def p15-algae-deluxe {:type :post-text
                       :id "p15-algae-deluxe"
                       :by authors/megacorp_fresh
                       :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p15-algea-deluxe/alga.jpeg"
                       :description "Algae Deluxe Rolls – from our sea farms to your plate in less than 12 hours, guaranteed. Contains all the nutrients you need. Order now: https://buy.mgcorp.com/at57HJam"})
(def megacorp-filler-story [1  p15-algae-deluxe
                            10 {:type :re-post
                               :id "p15-algae-deluxe-re-post"
                               :post p15-algae-deluxe
                               :comment "Millions displaced by rising sea levels. Flooded land offered to companies, for bascially nothing, all bought by Megacorp. And for what? Algae rolls? Disgusting EU politics if you ask me."
                               :by authors/undergroundpegasus}
                            4 {:post-id "p15-algae-deluxe-re-post"
                               :by authors/coffeecakeweasel
                               :text " Certainly not drinking the Kool-Aid re: \"aggressive capitalism is our way out of today’s catastrophic world!\", but there are admittedly some successes wrt producing food for all, at speed, from disused land."}
                            6 {:type :post-text
                               :id "p12-megacorp-salad"
                               :by authors/bloomberg_bizmonth
                               :description "Megacorp’s latest life-saver: giant low-power cooler will bring back salad and lettuce on the menu, the company promises. We toured the facility. https://t.co./weyoi1203x"}
                            13 {:type :post-text
                               :id "p13-megacorp-buying-spree"
                               :by authors/channel1live
                               :decription "Low tarriffs, high profits: A closer look at Megacorp's buying spree in the health tech sector. Tonight 9pm, Channel 1"}
                            13 {:post-id "p13-megacorp-buying-spree"
                               :by authors/socialmarkets2050
                               :text "\"Low tarriffs?\" You mean to say \"government gifts\"! Another perfect example of EU lawmakers sucking up to Megacorp."}
                            4 {:type :post-text
                               :id "p14-megacorp-employment"
                               :by authors/radio_europe
                               :description "Megacorp now employs 40% of workers in the EU - https://t.co/e8GHYnn"}
                            2 {:post-id "p14-megacorp-employment"
                               :by authors/warehouseworker
                               :text "A company so large, how come they don't regulate it? So many of us toiling away for shitty wages"}
                            2 {:post-id "p14-megacorp-employment"
                               :by authors/factsandfigures
                               :text "40%. Mind-boggling number. Sit on a bench anywhere: 4 out of 10 passerby work for Megacorp. How did it get so big?"}
                            2 {:post-id "p14-megacorp-employment"
                               :by authors/workingmom99
                               :text "40% of the workforce? No private company should be allowed to be this big! But it's not like our governments & the EU really care about monopolies & what they do to ordinary people."}

                            ])

(def all-activity-posts [p1-climate-refugees-copenhagen
                         p1-climate-refugees-copenhagen-response
                         p2-financial-gdp
                         p3-financiel-gdp
                         p4-minister-in-city
                         p5-luxembourgian-president
                         p6-urine-bottles
                         p7-danes-crossing
                         p8-megacorp-traffic
                         p9-flooding-dams
                         p10-snow-cannons
                         p11-megacorp-prisons])
