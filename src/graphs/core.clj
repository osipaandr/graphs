(ns graphs.core
  (:require [graphs.brute-force :as br]
            [graphs.random-gen :as gen]
            [graphs.dijkstra :as d]))

(defn compare-results
  []
  (let [graph (gen/make-graph 100 200)
        start (gen/kw (rand-int 21))
        end   (gen/kw (rand-int 21))]
    (=
     (:length (d/shortest-path graph start end))
     (br/shortest-path graph start end))))
