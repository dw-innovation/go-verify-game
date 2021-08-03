
# Kid game

## Developing

### requirements

this application needs `java` and `clojure` installed.

### Development

This application relies on two separate parts, the `app` (a clojurescript project) that is the frontend of the website, and the `server`, which is a clojure project.

#### Frontend

to start the frontend for development, use `npm run watch`

The `app` is hosted and compiled by `shadow-cljs`, where you can see the configuration in `shadow-cljs.edn`.  Javascript libraries can be added in package.json, and then used in clojurescript as well.

We also have JS interop, along with JS React interop working.  Please see the files in `src/app/kid_game/react_components/raw` to see the js files.  They are converted with babel in the watch script.

This is how js files can be imported into ClojureScript:

``` javascript
 [lodash :as lodash]
 ["./react_components/compiled/intro.js" :as intro]
 [moment]
```

Imported react elements can be included in reagent hiccup as:

``` javascript
        [(reagent/adapt-react-class intro/default) {}]
```

you can *connect to the frontend repl* by using your code editor to connect to the repl on `nrepl://localhost:8777`


#### Backend

to start the backend as is, do `clj -M:run`.  to start it as an **interactive repl** do `clj -M:nrepl` -- you can then use your editor to connect to this nrepl on `nrepl://localhost:12345`

This repl is started by Clojure CLI, and loads `dev/user.clj` by default.  You should see the `async` logs in this terminal window you started.  If you use `cider-jack-in` (which I do not suggest) then you can see the `async` output in a buffer called ` *nrepl server*` (notice the space at the beginning.)



## Architecture
### message passing architecture

image representation:
https://miro.com/app/board/o9J_lPUUZk0=/

The server only maintains a list of connected clients, and which room they belong to.  It passes messages semi-blindly between the clients.  This means that it checks to make sure the sent message is _valid_, but does no assumptions about content.  The Clients themselves manage this semi shared state through the message passing system.
