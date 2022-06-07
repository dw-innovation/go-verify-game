
# Go Verify! Game (KID project)

This repository contains the code to run multiple versions of Go Verify!.

Go Verify! is a social media simulation game (demo) that tries to teach journalists, content creators, and media trainers the basics of verification. I.e.: Players will learn how to go about scrutinizing (user generated) content and claims found on the web.

The game has been created in the scope of the DW Innovation project Künstliche Intelligenz gegen Desinformation (KID), which translates to: Artificial Intelligence against Disinformation.

Concept & Development: DW Innovation 

Graphic design: Andreas Leibe 

Funding: Bundesbeauftage für Kultur und Medien

Go Verify! is a standalone ClojureScript frontend application, that _can_ (but doesn't have to) connect to a Clojure/Jar backend to enable chat and mutiplayer.

# Quickstart

`
npm run watch
`

# For Content Editors:

please checkout [the relevant directory](./src/shared/kid_shared/data)

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

The `main` branch, tags, and open pull requests get deployed through Github Actions to AWS S3.

Results are available at: https://kid-game.s3.eu-central-1.amazonaws.com/main/index.html

Note: only the frontend version of the game will be seen at this url.  to run a realtime server, you're on your own :)

## Development

This application relies on two separate parts, the `app` (a clojurescript project) that is the frontend of the website, and the `server`, which is a clojure project.  `cljc` files, which can be shared between the app and the server, are stored in the shared directory

#### Frontend
The `app` is hosted and compiled by `shadow-cljs`, where you can see the configuration in `shadow-cljs.edn`.  Javascript libraries can be added in package.json, and then used in clojurescript as well.

### Routes
- `?post=id` - loads an individual post
- `?dev=anything` - loads a dev environment, including a logged in user
- `?uikit=anything` - loads frontend components for frontend work

---

KID is a project led by [Deutsche Welle Innovation](https://innovation.dw.com/)
