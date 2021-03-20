(ns graphs.dijkstra
  (:require [clojure.zip :as zip]
            [graphs.random-gen :as gen]))

(defn scratch-routes
  [graph]
  (zipmap (keys graph) (repeat {:path nil
                                :length nil
                                :visited nil})))

(defn update-routes
  ([routes k v]
   (if (or
        (nil? (k routes))
        (> v (k routes)))
     (assoc routes k v)))

  ([routes entry]
   (update-routes routes (key entry) (val entry))))



(defn shortest-path
  [graph start end]
  (let [neighbors (start graph)
        step-info {:graph graph
                   :start start
                   :end end
                   :routes (scratch-routes graph)}]
    
    (println (reduce update-routes (:routes step-info) neighbors)))
  #_(print @routes-map))

(defn any-outgoing-visited?
  [graph ]
  )

(defn end?
  [graph-info vertice]
  #_(let [{:keys [graph start end]} graph-info]
      (and
       (not= vertice end)     
       (any-outgoing-visited? graph vertice)))
  (= vertice (:end graph-info)))

(defn graph-zipper
  [graph start end]
  (zip/zipper #((complement end?) {:graph graph
                                   :start start
                                   :end end} %)
          #(-> % graph keys)
          nil
          start))
