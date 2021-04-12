
# Kid game

## Developing

### requirements

this application needs `leiningen` and `java` and `clojure` installed.

### spinning up dev env

in terminal:

``` sh
lein figwgeel
```

in emacs: 

``` sh
M-x cider-jack-in-clj&cljs
```

both start a server accessible at `localhost:3449`

### build for production

``` sh
make build-prod
```

this generates an `uberjar` in `target/kid-game-standalone.jar`, that can be run with

``` sh
make run-prod
```


## Architecture
### message passing architecture

image representation:
https://miro.com/app/board/o9J_lPUUZk0=/

The server only maintains a list of connected clients, and which room they belong to.  It passes messages semi-blindly between the clients.  This means that it checks to make sure the sent message is _valid_, but does no assumptions about content.  The Clients themselves manage this semi shared state through the message passing system.
