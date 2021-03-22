(ns graphs.graph-utils
  (:require
;            [graphs.brute-force :as brf]
;            [graphs.random-gen :refer [make-graph]]
            [graphs.aggr-utils :refer :all]
            [graphs.dijkstra :refer [shortest-path]]))

(defn eccentricity
  [graph vertice]
  (nil-safe-max
   (map
    #(:length (shortest-path graph vertice %))
    (remove #(= vertice %) (keys graph)))))

(defn aggregate-eccentricity
  [f]
  (fn [graph]
    (f
     (map (partial eccentricity graph)
          (keys graph)))))

(def radius (aggregate-eccentricity nil-safe-min))
(def diameter (aggregate-eccentricity nil-safe-max))
