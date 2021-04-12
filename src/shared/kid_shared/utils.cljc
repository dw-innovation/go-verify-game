(ns kid-shared.utils
  (:require [kid-shared.types.shared :as shared]
            [kid-shared.types.user :as user]
            [clojure.spec.alpha :as s]))

(defn deep-merge [a b]
  (merge-with (fn [x y]
                (cond (map? y) (deep-merge x y)
                      (vector? y) (concat x y)
                      :else y))
                 a b))
