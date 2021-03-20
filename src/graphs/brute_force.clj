(ns graphs.brute-force
  (:require [graphs.random-gen :refer [make-graph]]
            [graphs.aggr-utils :refer :all]))

(defn brute-force-shortest
  [graph start end appendix]
  (cond
    (empty? graph) nil
    (= start end) appendix
    :else (->> (start graph)
               (map #(brute-force-shortest
                      (dissoc graph start)
                      (key %)
                      end
                      (if (nil? (val %))
                        nil
                        (+ appendix (val %)))))
               nil-safe-min)))

(defn shortest-path
  [graph start end]
  (brute-force-shortest graph start end 0))
