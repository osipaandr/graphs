(ns graphs.core
  (:require [graphs.brute-force :as br]
            [graphs.random-gen :refer [make-graph] :as gen]
            [graphs.dijkstra :refer [shortest-path] :as d]
            [graphs.graph-utils :refer [eccentricity, radius, diameter]]))

;; 1. Weighted graph is represented as follows:
;;    {:4 {:1 4, :3 3},
;;     :1 {:5 3},
;;     :5 {},
;;     :3 {:5 4, :2 4},
;;     :2 {:1 1}}
;;    So each vertice is associated with a map
;;    which entries are vertices associated with weights
;;
;; 2. Try make-graph
;;    
;; 3. Try shortest-path.
;;    Notice that result is a map, not a vector of vertices:
;;    {:path [:2 :8 :4], :length 8}
;;
;; 4. Try eccentricity, radius, diameter

(defn compare-results
  []
  (let [graph (make-graph 100 200)
        start (gen/kw (inc (rand-int 100)))
        end   (gen/kw (inc (rand-int 100)))]
    (=
     (:length (shortest-path graph start end))
     (br/shortest-path graph start end))))
