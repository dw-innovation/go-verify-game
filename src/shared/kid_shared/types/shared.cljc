(ns kid-shared.types.shared
  (:require [clojure.spec.alpha :as s]))

(s/def pos-int? (s/and integer? pos?))

(s/def ::id string?)

(s/def ::created pos-int?) ; timestamp
