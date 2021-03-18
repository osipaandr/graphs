(ns graphs.dijkstra)

(def routes-map
  {:2 nil
   :3 nil
   :4 13})

(defn scratch-routes
  [graph]
  (zipmap (keys graph) (repeat nil)))

(defn shortest-path
  [graph start end]
  nil)
