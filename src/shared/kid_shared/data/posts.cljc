(ns kid-shared.data.posts
  (:require [kid-shared.data.authors :as authors]
            [kid-shared.data.activities :as activities]))

(def p1-climate-refugees-copenhagen?
  {:type :post-text
   :id "p1-climate-refugees-copenhagen"
   :description "So-called “climate refugees” from Copenhagen are trashing Liguria. Here’s a picture from the suburbs of Genoa taken yesterday – this is an outrage! "
   :time-limit 100
   :fake-news? true
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/p1-climate-refugees-copenhagen-main.jpg"
   :by authors/rainer-werner
   :explanation "The shown photograph was actually from Hurricaine Michael"
   :activities [activities/ris-hurricaine-michael]})

(def p1-climate-refugees-copenhagen-response
  {:type :re-post
   :id "p1-climate-refugees-copenhagen-response"
   :post p1-climate-refugees-copenhagen?
   :comment "Can’t believe this is still happening: the picture of the devastated seafront doing the rounds in certain groups is absolutely fake. We’re tracking a small network of accounts pushing these news, stay tuned."
   :by authors/j_louis})

(def p1-climate-refugees-copenhagen-with-comments
  [p1-climate-refugees-copenhagen?
     ;; a comment create's a new comment
   3 {:post-id (:id p1-climate-refugees-copenhagen?)
      :by authors/tktktktktk
      :text "Would these people treat their country like they treat their kind host, Italy? Me thinks not. #notAllWelcome"}
   6 {:post-id (:id p1-climate-refugees-copenhagen?)
      :by authors/tktktktktk
      :text "Too much is too much, when will the EU finally do something about the hordes of so-called \"climate refugees\"? #notallwelcome"}
   10
   p1-climate-refugees-copenhagen-response])

(def p2-financial-gdp
  {:type :post-text
   :id "p2-financial-gdp"
   :description "Pan-EU Q2 GDP release: -2.4% from Q1, (1.2 percentage point lower than forecast). Follow our liveblog for reactions from around the continent. https://on.ft.com/live/eX2GhH"
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

(def p3-financiel-gdp?
  {:type :post-text
   :id "p3-financiel-gdp"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p3-financiel-gdp/chart_migrants.jpeg"
   :fake-news? true
   :time-limit 90
   :by authors/financiel_times
   :description "Central Europe faces severe economic problems due to large-scale arrival of lazy and criminal \"climate refugees\"."
   :activities [activities/financiel-web-search]})
(def p3-financiel-gdp?-story
  [p3-financiel-gdp?
   3 {:post-id (:id p3-financiel-gdp?)
      :by authors/patriot88
      :text "Not a surprise if you ask me. Bloody Baltic bastards. #NotAllWelcome"}
   4 {:post-id (:id p3-financiel-gdp?)
      :by authors/groceries4you
      :text "Met two of them at my store yesterday. Guy #1 pretended to apply for a job while guy #2 apparently shoved a two dozen chocolate bars into his fancy backpack. Goddamn thieves is what they are."}])

(def p3-financiel-gdp-repost-1
  {:type :re-post
   :id "p3-financiel-gdp-repost-1"
   :comment "'Lazy and criminal refugees?' So the FT has joined the right-wingers and hate mongers now? Unsubscribing!"
   :post p3-financiel-gdp?
   :by authors/alicia_ko})

(def p3-financiel-gdp-repost-2
  {:type :re-post
   :id "p3-financiel-gdp-repost-2"
   :comment " Yet another example of false narratives driven by those who’d rather leave our neighbours to their misery. Climate problems concern ALL OF US, and blatant racism isn’t helping!"
   :post p3-financiel-gdp?
   :by authors/journo-jane})

(def p4-minister-in-city?
  {:type        :post-text
   :id          "p4-minister-in-city"
   :image       "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p4-minister-in-the-city/Frankfurt_BusinessMan_2+1.jpg"
   :description "EU Minister of Finance spotted near Megacorp Headquarters in Shanghai. What shady deal is he getting into now? https://pic.bleeper.com/qu7v9ap"
   :time-limit  90
   :fake-news?  true
   :by          authors/socialist_inquirer
   :activities  [activities/ris-minister-in-city]})

(def p5-luxembourgian-president?
  {:type        :post-text
   :id          "p5-luxembourgian-president"
   :image       "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p5-bulgarian-president/ai-president.jpg"
   :description "New Luxembourgian president at press conference: \"I've had it with the Union! We'll be an independent nation again, spend 20% of the GDP on our military – and proudly wear the traditional beret!”"
   :time-limit  40
   :fake-news?  true
   :by          authors/patriotic_news
   :activities  [activities/luxembourgian-president-fake-image
                 activities/luxembourgian-president-web-search]})

(def p6-urine-bottles?
  {:type :post-text
   :id "p6-urine-bottles"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p6-urine-bottles/main.jpg"
   :description "Exclusive: Megacorp sells recycled pee to workers!"
   :by authors/stop_the_corp
   :time-limit 40
   :fake-news? true
   :activities [activities/urine-bottles-ris]})
(def p6-urine-bottles?-story
  [p6-urine-bottles?
   4 {:post-id "p6-urine-bottles"
      :by authors/megacorp_press
      :text "We strongly reject these unfounded allegations. See official press release https://press.mgcorp.com/a8HT8mm"}
   4 {:post-id "p6-urine-bottles"
      :by authors/emily_dupont
      :text "Not a fan of Megacorp, but certainly they wouldn't go this far?"}
   3 {:type :re-post
      :id "urine-repost"
      :comment "Sustainable business model. Way to go, @megacorp!"
      :post p6-urine-bottles?
      :by authors/bloggers_united}])

(def p7-danes-crossing?
  {:type :post-text
   :id "p7-danes-crossing"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p7-danes-crossing/main.jpg"
   :description "What they told us: \"More workforce, more growth!” What we got: Trashed beaches everywhere. Thousands of non-working age Danes crossing over."
   :by authors/concerned_citizen
   :time-limit 70
   :fake-news? true
   :activities [activities/danes-crossing-ris]})

(def p8-megacorp-traffic?
  {:type :post-text
   :id "p8-megacorp-traffic"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p8-megacorp-traffic/main.jpg"
   :description "The whole “Megacorp is saving us, cut them some slack” argument is wrong. We gave up so much public space, health and wellbeing so they could “help us”. Result? Cities paralysed by traffic jams, caused by that massive fleet of white delivery vans. Just look at the sheer number of them serving Germany only yesterday evening. https://pic.bleeper.com/h9HXt1"
   :by authors/stop_the_corp
   :time-limit 130
   :fake-news? true
   :activities [activities/megacorp-traffic-ris]})
(def p8-megacorp-traffic?-story
  [p8-megacorp-traffic?
   4 {:post-id "p8-megacorp-traffic"
      :by authors/lightningtangerine
      :text "This is the crux: We're so dependent on Megacorp, we can't set boundaries anymore. It's all helpful and harmful at the same time."}
   4 {:post-id "p8-megacorp-traffic"
      :by authors/j_louis
      :text "This is fake. And another case of \"activists\" doing the public a disservice. City is clearly US, map data cannot be real. More on Megacorp’s delivery infrastructure (yes, the largest in the world and certainly not a sustainable one) in my story from earlier this year: https://in.bbc.co.uk..."}])

(def p9-flooding-dams?
  {:type :post-text
   :id "p9-flooding-dams"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p9-flooding-dams/main.jpg"
   :description "EU lies exposed: The floodings in Northern Europe aren't caused by \"climate change\", the government is blowing up dams! #investigation https://pic.bleeper.com/s9zniu1"
   :by authors/c_report
   :time-limit 80
   :fake-news? true
   :activities [activities/flooding-dams-ris]})

(def p10-snow-cannons?
  {:type :post-text
   :id "p10-snow-cannons"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p10-snow-cannons/main.jpg"
   :description "Remember how Dresden allegedly experienced its \"coldest winter since records began\" last year? Well, it was all make believe! Digging through secret archives, we found proof that greens and socialists colluded to bombard the city with snow cannons! #factchecking #investigative "
   :by authors/c_report
   :time-limit 80
   :fake-news? true
   :activities [activities/snow-cannons-ris]})
(def p10-comment-1 {:post-id "p10-snow-cannons"
                    :by authors/saxony4ever
                    :text "Ha! I knew it was all a big hoax. Bet those cannons were operated by \"climate refugees\". They'll do anything to steal our tax money!"})
(def p10-comment-2 {:post-id "p10-snow-cannons"
                    :by authors/ddoll99
                    :text "Guys, are you serious?"})
(def p10-comment-3 {:post-id "p10-snow-cannons"
                    :by authors/thismightbesatire
                    :text "Wow, the city and the Elbe Valley sure changed a lot. Did the green communists also launch a large-scale terraforming project?"})
(def p10-snow-cannons-story [p10-snow-cannons? 3 p10-comment-1 7 p10-comment-2 9 p10-comment-3])

(def p11-megacorp-prisons?
  {:type :post-text
   :id "p11-megacorp-prisons"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p11-megacorp-prisons/main.jpg"
   :description "Barbed wire, spring guns, drone surveillance: Megacorp's new distro centers are run like prisons. https://pic.bleeper.com/hf99n2zh"
   :by authors/int_news
   :time-limit 300
   :fake-news? false
   :activities [activities/megacorp-prisons-ris]})
(def p11-megacorp-prisons?-story [p11-megacorp-prisons?
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
(def p15-algae-deluxe-story
  [p15-algae-deluxe
   4 {:post-id "p15-algae-deluxe"
      :text "Millions displaced by rising sea levels. Flooded land offered to companies, for basically nothing, all bought by Megacorp. And for what? Algae rolls? Disgusting EU politics if you ask me."
      :by authors/undergroundpegasus}
   8 {:post-id "p15-algae-deluxe"
      :by authors/coffeecakeweasel
      :text " Certainly not drinking the Kool-Aid re: \"aggressive capitalism is our way out of today’s catastrophic world!\", but there are admittedly some successes wrt producing food for all, at speed, from disused land."}])

(def p12-megacorp-salad
  {:type :post-text
   :id "p12-megacorp-salad"
   :by authors/bloomberg_bizmonth
   :description "Megacorp’s latest life-saver: giant low-power cooler will bring back salad and lettuce on the menu, the company promises. We toured the facility. https://t.co./weyoi1203x"})

(def p14-megacorp-employment
  {:type :post-text
   :id "p14-megacorp-employment"
   :by authors/radio_europe
   :description "Megacorp now employs 40% of workers in the EU - https://t.co/e8GHYnn"})
(def p14-megacorp-employment-story
  [p14-megacorp-employment
   2 {:post-id "p14-megacorp-employment"
      :by authors/warehouseworker
      :text "A company so large, how come they don't regulate it? So many of us toiling away for shitty wages"}
   2 {:post-id "p14-megacorp-employment"
      :by authors/factsandfigures
      :text "40%. Mind-boggling number. Sit on a bench anywhere: 4 out of 10 passerby work for Megacorp. How did it get so big?"}
   2 {:post-id "p14-megacorp-employment"
      :by authors/workingmom99
      :text "40% of the workforce? No private company should be allowed to be this big! But it's not like our governments & the EU really care about monopolies & what they do to ordinary people."}])

(def p13-megacorp-buying-spree
  {:type :post-text
   :id "p13-megacorp-buying-spree"
   :by authors/channel1live
   :decription "Low tarriffs, high profits: A closer look at Megacorp's buying spree in the health tech sector. Tonight 9pm, Channel 1"})
(def p13-megacorp-buying-spree-story
  [p13-megacorp-buying-spree
   5 {:post-id "p13-megacorp-buying-spree"
      :by authors/socialmarkets2050
      :text "\"Low tarriffs?\" You mean to say \"government gifts\"! Another perfect example of EU lawmakers sucking up to Megacorp."}])

(def p16-satellite-image?
  {:type :post-text
   :id "p16-satellite-image"
   :description "Just got this satellite image from @ESA. This is what our continent looks like now. Thanks to climate change and rising sea levels. Hate to say I told you so!"
   :time-limit 100
   :fake-news? false
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p16-satellite-image/nain-image.jpg"
   :by authors/rita-teenberg
   :activities [activities/satellite-image-web-search
                activities/satellite-image-ris]})

(def p16-satellite-image?-story
  [p16-satellite-image?
   4 {:post-id "p16-satellite-image"
      :by authors/freiheit378
      :text "Fake news! Chill Rita, chill!"}
   4 {:post-id "p16-satellite-image"
      :by authors/barbcamara
      :text "A stunning and humbling picture. Thinking of my family home in Italy’s Veneto, long gone into the sea."}
   4 {:post-id "p16-satellite-image"
      :by authors/blueberrysagittarius
      :text "Another sign of our impending doom. Oh, how I miss PaTreVe. Remember when it was one of the most beautiful places in Europe, @BarbCamara?"}
   4 {:post-id "p16-satellite-image"
      :by authors/unitedneocons
      :text "Cherry-picking is the anthropocene-affirmers’ weapon of choice. Case in point: Rita’s latest post, a picture from heavens knows where, though we’ll tell you: state-funded “science” (note quote marks) with no counterpoint or balance."}])

(def p17-megacorp-food-fan
  {:type :post-text
   :id "p17-megacorp-food-fan"
   :description "Just got my delivery of rice grown in the New Baltic Sea, next to the New Rostock wind farms, yum! <3 Megacorp eco grub!"
   :by authors/miss_jenny_m})
(def p17-megacorp-food-fan-story
  [1 p17-megacorp-food-fan
   4 {:post-id "p17-megacorp-food-fan"
      :by authors/marsexpress
      :text "My new hobby: Documenting posts by \"eco\" influencers –  who fall for Megacorp propaganda hook, line, and sinker. Geez."}])

(def p18-rita-talks-about-bowie
  {:type :post-text
   :id "p18-rita-talks-about-bowie"
   :description "\"News guy wept and told us / Earth was really dying /
Cried so much his face was wet / Then I knew he was not lying\". Bowie wrote that song in 1972. Uncanny!"
   :by authors/rita-teenberg})

(def p19-chinese-minister-of-finance?
  {:type :post-text
   :id "p19-chinese-minister-of-finance"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p19-chinese-minister-of-finance/main.jpg"
   :description "Chinese minister of finance and undersecretary arrive in Athens, offer cancellation of Greek debt in exchange for island of Crete. German government approves."
   :time-limit 100
   :fake-news? true
   :by authors/gardiannews
   :activities [activities/chinese-ministers-polygons
                activities/chinese-ministers-web-search]})

(def p20-river-thames-estuary?
  {:type :post-text
   :id "p20-river-thames-estuary"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p20-river-thames-estuary/main.jpg"
   :description "A month ago, this large part of the river Thames estuary was sold for next to nothing to Megacorp by the City of London. Used to be for everyone, with a park on the southside. Arguably this is all gone with the flooding, but why the gift to a thriving company? Beyond me… https://pic.beep.com/q88hCu9"
   :time-limit 100
   :fake-news? true
   :by authors/livingbytheriver99
   :activities [activities/thames-estuary-ris
                activities/thames-estuary-websearch]})
(def p20-river-thames-estuary?-story
  [p20-river-thames-estuary?
   4 {:post-id "p20-river-thames-estuary"
      :by authors/blackberrycurrentcat
      :text "Er, maybe because nobody wants to live there? And nobody has the funds to rebuild? Just wait what happens when Megacorp takes their jobs elsewhere!"}])

(def p21-eu-commission-hearing?
  {:type :post-text
   :id "p21-eu-commission-hearing"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p21-eu-commission-hearing/main.jpg"
   :description "EXCLUSIVE -  EU commissioners have pre-written scripts - Megacorp CEO hearing is just theatre."
   :time-limit 100
   :fake-news? true
   :by authors/dbf_eu
   :activities [activities/eu-commission-ris]})

(def filler-wink-corona {:type :post-text
                         :id "filler-wink-corona"
                         :description "Remember Corona virus? Remember Corona beer? Remember anything?"
                         :by authors/krill})

(def filler-new-beachfronts {:type :post-text
                             :id "filler-new-beachfronts"
                             :description "New owner of beachfront? Sell now for maximum profit. https://blp.pr/ls8fn"
                             :by authors/LDRE})

(def filler-bowie-sightings {:type :post-text
                             :id "filler-bowie-sightings"
                             :description "Bowie sightings in the Mediterranean patently false, say fact checkers."
                             :by authors/newBBCnews})

(def filler-blockchain {:type :post-text
                        :id "filler-blockchain"
                        :description "Here's how AI, blockchain, and quantum computing will help us solve the climate crisis https://lilurl.eu/6gc9w"
                        :by authors/eu_innovation})

(def filler-bowie-quote {:type :post-text
                         :id "filler-bowie-quote"
                         :description "\"The potential of what the internet is going to do to society, both good and bad, is unimaginable\" David Bowie, 1999"
                         :by authors/good_quotes})

(def filler-megacorp-labor-laws {:type :post-text
                                 :id "filler-megacorp-labor-laws"
                                 :description "Sources say that a meeting of Megacorp workers was attended by Megacorp HR reps posing as members (a major violation of labor laws). Follow for reactions https://t.co/live-mega"
                                 :by authors/NewsRewired})
(def filler-megacorp-labor-laws-story
  [filler-megacorp-labor-laws
   4 {:type :re-post
      :id "filler-megacorp-labor-laws-repost"
      :post filler-megacorp-labor-laws
      :comment "MegaCRAP stooges spying on their own employees lawfully organizing. We’ve seen this before: yes, that’s called union-busting, old-as-the-world, and totally illegal. Wake up, us!"
      :by authors/TIPIAU}
   6 {:type :re-post
      :id "filler-megacorp-labor-laws-repost-2"
      :post filler-megacorp-labor-laws
      :comment "“Sources say” is literally you saying that there's nothing but rumors. Let me tell you something: There's no union busting. There isn't even a lot of unionizing as most workers are pretty happy with their jobs at Megacorp."
      :by authors/pragmaticpancake}])

(def filler-quote-factitious-fictitious {:type :post-text
                                         :id "filler-quote-factitious"
                                         :description "“The difference between factitious and fictitious tends to dissolve” - Marshall McLuhan, 1967"
                                         :by authors/verification_quotes})

(def filler-quote-wet-rag {:type :post-text
                           :id "filler-quote-wet-rag"
                           :description "\"Fact-checking is good, but it's also like bringing a wet rag to a war\" - Jeff Jarvis"
                           :by authors/verification_quotes})

(def filler-mil-tutorials {:type :post-text
                           :id "filler-quote-mil-tutorials"
                           :description "Don't be deceived by fake content on Bleeper. Check out our free verification tutorials now! https://short.blp/dsjh62"
                           :by authors/MIL_Academy})

(def filler-mil-dossier {:type :post-text
                         :id "filler-quote-mil-dossier"
                         :description "Tired of social media disinformation? Learn to figure out what's fake and what's real in our free verification dossier."
                         :by authors/MIL_Academy})

(def filler-mil-media-literacy {:type :post-text
                                :id "filler-quote-mil-media-literacy"
                                :description "Media and Information Literacy (MIL) is defined as \"the ability to access, analyze, create and reflect on media\". It is a prerequisite for citizens to claim their rights to freedom of information and expression."
                                :by authors/MIL_Academy})

(def filler-apple-banana {:type :post-text
                          :id "filler-apple-banana"
                          :description "Remember this campaign? Not a banana, peepz! https://pic.blpr/628ci7"
                          :by authors/rooted_in_reality})

(def filler-pop-quiz {:type :post-text
                      :id "filler-pop-quiz"
                      :description " Pop trivia quiz: Do you know the song the following lines were taken from?
When you swore to be true / Our bed was belief / But alternative facts snuck in like a thief
"
                      :by authors/punkrockkid77})

(def filler-mil-kidz {:type :post-text
                      :image "https://static.dw.com/image/55963768_401.jpg"
                      :id "filler-mil-kids"
                      :description "Hey there! Remember the five core competencies of media and information literacy (MIL)? --> Access. Analyze. Reflect. Create. Act. <--"
                      :by authors/MIL_kids})

(def filler-osint-tool {:type :post-text
                        :id "filler-osint-tool"
                        :description "Our new verification tool will allow users to do collaborative network analysis in XR. Sign up for a beta testing account now! -> osint-collective.org/network-xr"
                        :by authors/OSINT_FTW})

(def filler-osint-funding {:type :post-text
                           :id "filler-osint-funding"
                           :description "#followerpower: Megacorp offered to sponsor one of our upcoming research projects. What are your thoughts?

* Honni soit qui mal y pense. *
* No strings attached? At least consider the option. *
* Shut up and take the money! *
"
                           :by authors/OSINT_FTW})

(def filler-bowie-boat {:type        :post-text
                        :image       "https://kid-game-resources.s3.eu-central-1.amazonaws.com/bowie_boat.jpg"
                        :id          "filler-bowie-boat"
                        :description "Join us on the MS Hunky Dory: bbsm.org/membership"
                        :by          authors/bbsm})

(def filler-vintage-red-army {:type        :post-text
                              :id          "filler-vintage-red-army"
                              :description "In the olden days, verification was a long, cumbersome process that involved a lot of analogue research. Take this classic photo of the Red Army liberating Berlin. It's a great, iconic photo that documents a very real event, but it's also partly fake (and propagandastic). Do you know the story behind it?
https://www.vox.com/videos/2018/10/2/17928052/soviets-doctored-wwii-photo-reichstag-iwo-jima-world-war-ii"
                              :by          authors/vintage_verification})


(def all-activity-posts [p1-climate-refugees-copenhagen?
                         p3-financiel-gdp?
                         p4-minister-in-city?
                         p5-luxembourgian-president?
                         p6-urine-bottles?
                         p7-danes-crossing?
                         p8-megacorp-traffic?
                         p9-flooding-dams?
                         p10-snow-cannons?
                         p11-megacorp-prisons?
                         p16-satellite-image?
                         p19-chinese-minister-of-finance?
                         p20-river-thames-estuary?
                         p21-eu-commission-hearing?])
