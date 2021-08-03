(ns kid-game.main
  (:require [org.httpkit.server :as hk]
            [kid-game.handler :as h])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& [port]]
  (hk/run-server h/app {:port (or (Integer. port) 8080)}))
