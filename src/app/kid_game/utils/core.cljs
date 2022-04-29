(ns kid-game.utils.core
  (:require [kid-shared.types.shared :as shared]
            [cljs.core :refer [random-uuid]]
            [clojure.string :as str]
            [clojure.spec.alpha :as s]))

;; mirrored utils
;;   utils that also exist in cljs server
;;
(defn timestamp-now [] (.getTime (js/Date.)))

(defn zp
  "Zero Pad numbers - takes a number and the length to pad to as arguments"
  [n c]
  (.padStart
   (js/String n)
   c
   "0"))

(defn date->string [date]
  (let [months ["January" "February" "March" "April" "May" "June" "July" "August" "September" "October" "November" "December"]]
    (str (months (.getUTCMonth date))
         " "
         (.getUTCDate date)
         " "
         (.getUTCFullYear date)
         ", "
         (.getUTCHours date)
         ":"
         (zp (.getUTCMinutes date) 2)
         )))

(defn new-uuid [] (str (random-uuid)))

;; app only utils

(defn map-values [f m]
  (into {} (for [[k v] m] [k (f v)])))

(defn indexes [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))

(defn in?
  "true if coll contains elm"
  [coll elm]
  (some #(= elm %) coll))

(defn find-first [f coll]
  (first (filter f coll)))

;(def url-pattern #"(?i)^(?:(?:https?|ftp)://)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,}))\.?)(?::\d{2,5})?(?:[/?#]\S*)?$")
(def url-in-whitespace-pattern #"[\s]\S+\.\S+(?:$|\s)")
(def url-pattern #"^\S+\.\S+$")
(defn url? [s] (or (re-matches url-in-whitespace-pattern s)
                   (re-matches url-pattern s)))
(url? " a.n ") ; -> " a.n "
(url? " https://google.com ") ; -> " https://google.com"
(url? "google.com") ; -> google.com
(url? "hello") ; -> nil
(url? "hello.") ; -> nil
(url? "this is a sentence with url.in it") ; -> nil
(url? "https://t.co/e8GHYnn") ; -> "https://t.co/e8GHYnn"
(url? " https://t.co/e8GHYnn") ; " https://t.co/e8GHYnn"

;; takes a string, returns a styled hiccup form
;;   the hackiest way possible, because i don't want to write a parser or lexer right now
(defn highlight-text [^string text]
  (-> text
      ;; Step 1: insert our delimiting characters around the urls
      (str/replace url-in-whitespace-pattern (fn [match] (str " LLL" (str/trim match) "LLL ")))
      (str/replace url-pattern               (fn [match] (str "LLL" (str/trim match) "LLL")))
      ;; Step 2: split the string by our delimiter, which will give us alternating
      ;;   URL and non-url text strings
      (str/split #"LLL")
      ;; ;; Step 3: convert this to a hiccup form
      ((fn [matches]
         [:span.parsed-string
          (map (fn [a]
                 (if (url? a)
                   ^{:key a} [:span.link a]
                   a))
               matches)]))))
