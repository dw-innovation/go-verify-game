
# KID - sharpen your disinformation skills

> KID: in German, “Künstliche Intelligenz gegen Desinformation” (Artificial Intelligence against Mis-/Disinformation) and is funded by the BKM (Beauftragte der Bundesregierung für Kultur und Medien = the German Government’s Commissioner for Culture and Media)

This repository contains the code to run multiple versions of the KID game – a social media simulation game encouraging the verification of odd-looking posts.

The game is a standalone ClojureScript frontend application, that _can_ (but doesn't have to) connect to a Clojure/Jar backend to enable chat and mutiplayer.

# For Content Editors:

Please checkout [the relevant directory](./src/shared/kid_shared/posts)

---

# For Developers

## Quickstart
This application requires `java`, `node` (v14.17, current LTS) and `npm`, as well as `clojure` installed and on the $PATH.

### Standalone app, including frontend and REPL
Start the frontend application with `$ npm run watch`, which uses shadow-cljs to watch the frontend directories, and serves them to `http://localhost:8080`.

You can connect to the running process with a REPL by running `$ npm run repl`, or connecting through CIDER in emacs with `cider-connect-cljs`.

### Backend-only start
To start with a backend, you will also want to start the frontend (see above). However, this time the server will serve the frontend package and you will access it at a different URL.

Start the backend, with a REPL:
```
clj -M:repl
```

The server will then serve the frontend package on `localhost:3449`, which is connected to the backend.

Note: in this case, the backend also _serves_ the bundled js frontend

### Deployment
The `master` branch, tags, and open pull requests get deployed through Github Actions to AWS S3.

Results are available at: https://kid-game.s3.eu-central-1.amazonaws.com/master/index.html

Note: only the frontend version of the game will be seen at this url.  to run a realtime server, you're on your own :)

## Development
This application relies on two separate parts, the `app` (a clojurescript project) that is the frontend of the website, and the `server`, which is a clojure project.  `cljc` files, which can be shared between the app and the server, are stored in the shared directory

#### Frontend
The `app` is hosted and compiled by `shadow-cljs`, where you can see the configuration in `shadow-cljs.edn`.  Javascript libraries can be added in package.json, and then used in clojurescript as well.

### Routes
- `?post-id=xxx` - loads an individual post
- `?dev=anything` - loads a dev environment, including a logged in user
- `/dev-cards` - loads frontend components for frontend work

##### JS Interop

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

where `intro/default` is your imported react element from above

you can *connect to the frontend repl* by using your code editor to connect to the repl on `nrepl://localhost:8777`

#### Backend

to start the backend as is, do `clj -M:run`.  to start it as an **interactive repl** do `clj -M:nrepl` -- you can then use your editor to connect to this nrepl on `nrepl://localhost:12345`

This repl is started by Clojure CLI, and loads `dev/user.clj` by default.  You should see the `async` logs in this terminal window you started.  If you use `cider-jack-in` (which I do not suggest) then you can see the `async` output in a buffer called ` *nrepl server*` (notice the space at the beginning.)

## Architecture
### Message-passing architecture

The server only maintains a list of connected clients, and which room they belong to.  It passes messages semi-blindly between the clients.  This means that it checks to make sure the sent message is _valid_, but does no assumptions about content.  The Clients themselves manage this semi-shared state through the message-passing system.
