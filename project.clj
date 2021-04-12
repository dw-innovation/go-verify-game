(defproject kid-game "0.1.0-SNAPSHOT"
  :description "FIXME: write this!"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :min-lein-version "2.7.1"

  :dependencies [[org.clojure/clojure "1.10.0"]
                 [org.clojure/clojurescript "1.10.439"]
                 [org.clojure/core.async  "0.4.490"]
                 [org.clojure/test.check "0.9.0"]
                 [compojure "1.6.1"]
                 [jarohen/chord "0.8.1"]
                 [reagent "0.8.1"]
                 [medley "1.1.0"]
                 [http-kit "2.4.0-alpha2"]]

  :plugins [[lein-figwheel "0.5.18" :exclusions [[http-kit]]]
            [lein-ancient "0.6.15"]
            [cider/cider-nrepl "0.25.6"]
            [lein-cljsbuild "1.1.7" :exclusions [[org.clojure/clojure]]]]

  :source-paths ["src/server" "src/shared"]

  :uberjar-name "kid-game-standalone.jar"

  :jvm-opts ["-Xmx200m"]                ; this seems to not work
                                        ; try the profile setting down below

  :resource-paths ["resources"]

  :figwheel {:server-port 3449 ;; default
             ;; :server-ip "127.0.0.1"
             :server-logfile "log"
             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this

             ;; doesn't work for you just run your own server :) (see lein-ring)

             :ring-handler kid-game.handler/app

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you are using emacsclient you can just use
             ;; :open-file-command "emacsclient"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log"

             ;; to pipe all the output to the repl
             ;; :server-logfile false
             }


  ;; Setting up nREPL for Figwheel and ClojureScript dev
  ;; Please see:
  ;; https://github.com/bhauman/lein-figwheel/wiki/Using-the-Figwheel-REPL-within-NRepl
  :profiles {:dev {:dependencies [[figwheel-sidecar "0.5.18" :exclusions [[http-kit]]]
                                  [cider/piggieback "0.3.10"]]
                   ;; need to add dev source path here to get user.clj loaded
                   :cljsbuild {:builds {:dev {:source-paths ["src/app" "src/shared"]
                                              :figwheel true
                                              :compiler {:main kid-game.core
                                                         :asset-path "js/compiled/out"
                                                         :output-to "resources/public/js/compiled/main.js"
                                                         :output-dir "resources/public/js/compiled/out"
                                                         :source-map-timestamp true}}}}

                   :repl-options {:nrepl-middleware [cider.piggieback/wrap-cljs-repl]}
                   ;; need to add the compliled assets to the :clean-targets
                   :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                                     :target-path]}
             :uberjar {:prep-tasks ["compile" ["cljsbuild" "once" "min"]]
                       :aot :all
                       :main kid-game.main
                       :jvm-opts ^:replace ["-Xms128m" "-Xmx350m" "-Xss512k" "-XX:MaxMetaspaceSize=150m"] ; also seems to not work
                       ; it looks like the above ^^^ needs to be put directly into the java process
                       :cljsbuild {:builds {:min {:source-paths ["src/app"]
                                                  :compiler {:output-to "resources/public/js/compiled/main.js"
                                                             :asset-path "js/compiled/out"
                                                             :output-dir "resources/public/js/compiled/out"
                                                             :main kid-game.core
                                                             :optimizations :advanced
                                                             :pretty-print false
                                                             ; this isn't needed any more because currently the frontent
                                                             ; just connects to wherever it got served from
                                                             ; :closure-defines {kid-game.core/ws-url "wss://kid-game.herokuapp.com/ws"}
                                                             }}}}}
             })
