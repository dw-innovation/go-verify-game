(ns kid-game.main
  (:require [org.httpkit.server :as hk]
            [kid-game.handler :as h])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& [port]]
  (println "xxxxxxxxxxxxx")
  (println "fffffffffffff")
  (println "xxxxxxxxxxxxx")
  (println "xxxxxxxxxxxxx")
  (println "xxxxxxxxxxxxx")
  (println "xxxxxxxxxxxxx")
  (println "xxxxxxxxxxxxx")
  (hk/run-server h/app {:port (or (Integer. port) 8080)}))

(println "hello")
