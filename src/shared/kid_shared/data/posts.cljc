(ns kid-shared.data.posts
  (:require [kid-shared.data.authors :as posters]
            [kid-shared.types.post :as posts]
            [kid-shared.data.activities :as activities]))


(def p1-climate-refugees-copenhagen
  {:type :post-text
   :id "p1-climate-refugees-copenhagen"
   :description "So-called “climate refugees” from Copenhagen are trashing Liguria. Here’s a picture from the suburbs of Genoa taken yesterday -- this is an outrage! "
   :time-limit 4000
   :fake-news? true
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p1-climate-refugees-copenhagen/p1-climate-refugees-copenhagen-main.jpg"
   :by posters/rainer-werner
   :explanation "The shown photograph was actually from Hurricaine Michael"
   :activities [activities/ris-hurricaine-michael]})

(def p1-climate-refugees-copenhagen-response
  {:type :re-post
   :id "p1-climate-refugees-copenhagen-response"
   :post p1-climate-refugees-copenhagen
   :comment "Can’t believe this is still happening: the picture of the devastated seafront doing the rounds in certain groups is absolutely fake. We’re tracking a small network of accounts pushing these news, stay tuned."
   :by posters/j_louis})

(def p2-financial-gdp
  {:type :post-text
   :id "p2-financial-gdp"
   :description "Pan-EU Q2 GDP release: -2.4% from Q1, (1.2 percentage point lower than forecast). Follow our liveblog for reactions from around the continent. https://on.ft.com/live/eX2GhH"
   :time-limit 900
   :fake-news? false
   :by posters/financial_times})

(def p2-financial-gdp-repost-1
  {:type :re-post
   :id "p2-financial-gdb-repost-1"
   :comment "There you have it: In spite of years of calamities and large-scale population displacement, we’re still seeing only a *relatively manageable* contraction of the economy."
   :post p2-financial-gdp
   :by posters/economy_economy})

(def p2-financial-gdp-repost-2
  {:type :re-post
   :id "p2-financial-gdb-repost-2"
   :comment "Indeed the “Miracle of the Mid-Century” as Mr John said. Things did NOT go to shit, and this is truly mind-boggling."
   :post p2-financial-gdp
   :by posters/european_liberal})

(def p3-financiel-gdp
  {:type :post-text
   :id "p3-financiel-gdp"
   :image "https://kid-game-resources.s3.eu-central-1.amazonaws.com/p3-financiel-gdp/chart_migrants.jpeg"
   :fake-news? true
   :time-limit 900
   :by posters/financiel_times
   :description "Central Europe faces severe economic problems due large-scale arrival of lazy and criminal climate refugees."})

(def p3-financiel-gdp-repost-1
  {:type :re-post
   :id "p3-financiel-gdp-repost-1"
   :comment "'Lazy and criminal refugees?' So the FT has joined the right-wingers and hate mongers now? Unsubscribing!"
   :post p3-financiel-gdp
   :by posters/alicia_ko})

(def p3-financiel-gdp-repost-2
  {:type :re-post
   :id "p3-financiel-gdp-repost-2"
   :comment " Yet another example of false narratives driven by those who’d rather leave our neighbours to their misery. Climate problems concern ALL OF US, and blatant racism isn’t helping!"
   :post p3-financiel-gdp
   :by posters/journo-jane})

(def examples [])

(def all-activity-posts [p1-climate-refugees-copenhagen
                         p1-climate-refugees-copenhagen-response
                         p2-financial-gdp
                         p3-financiel-gdp])
