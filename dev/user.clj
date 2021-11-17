(ns user
  (:require [kid-game.main]
            [kid-game.utils.log :as log]
            :reload-all))

(log/debug "starting application from dev/user.clj")

(kid-game.main/-main 3449)
