(ns graphs.dijkstra)

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
