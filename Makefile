build-prod:
	lein uberjar
run-prod:
	java ${JVM_OPTS} -cp target/kid-game-standalone.jar clojure.main -m kid-game.main 14157
# important is to run jave with -Xms128m -Xmx350m -Xss512k (before -cp)
# 14157 is current port on opalstack server
# java -cp kid-game-standalone.jar clojure.main -m kid-game.main 14157
