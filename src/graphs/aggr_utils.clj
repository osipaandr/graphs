(ns graphs.aggr-utils)

(defn nil-safe-aggr
  [fun coll]
  (if-let [numbers (seq (remove nil? coll))]
    (apply fun numbers)
    nil))

(def nil-safe-max (partial nil-safe-aggr max))
(def nil-safe-min (partial nil-safe-aggr min))
