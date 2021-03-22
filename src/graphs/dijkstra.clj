(ns graphs.dijkstra
  (:require [clojure.zip :as zip]
            [graphs.random-gen :as gen]))

(defn has-incoming-edges?
  [graph vertice]
  (->> graph
       vals
       (some #(contains? % vertice))))

(defn should-be-removed?
  [graph vertice special]
  (if (= vertice special)
    false
    ((complement has-incoming-edges?) graph vertice)))

(defn remove-edges-to
  ([graph v k]
   (as-> (k graph) x
       (dissoc x v)
       (assoc graph k x)))
  
  ([graph v]
   (reduce #(remove-edges-to %1 v %2) graph (keys graph))))

(defn remove-vertice
  [graph vertice]
  (-> (dissoc graph vertice)
      (remove-edges-to vertice)))

(defn simplify
  [graph special]
  (let [rem-pred #(should-be-removed? graph % special)
        verts-to-remove (filter rem-pred (keys graph))]
    (if (empty? verts-to-remove)
      graph
      (simplify
       (reduce #(remove-vertice %1 %2) graph verts-to-remove)
       special))))

(defn graph-zipper
  [graph start]  
  (zip/zipper #(not-empty (% graph))
              #(-> % graph keys)
              nil
              start))

(defn scratch-routes
  [graph, start]
  (-> (zipmap (keys graph) (repeat {:path nil
                                    :length nil
                                    :visited false}))
      (assoc start {:path []
                    :length 0
                    :visited true})))

(defn split-by-2
  [coll]
  (if (<= (count coll) 2)
    [(vec coll)]
    (concat [[(coll 0) (coll 1)]]
            (split-by-2 (vec (rest coll))))))

(defn calc-path
  [graph [a, b]]
  (let [edge (a graph)]
    (if (nil? edge) nil (b edge))))

(defn path-len
  [graph verts]
  (let [split (split-by-2 verts)
        +' (fn [a b] (cond
                        (nil? a) nil
                        (nil? b) nil
                        :else (+ a b)))]
    (reduce (fn [sum [a, b]]
              (+' (calc-path graph [a b]) sum))
            0 split)))

(defn update-routes
  [graph routes]
  (fn [loc]
    (let [path (zip/path loc)
          vert (zip/node loc)
          cur-len (:length (vert @routes))
          new-len (path-len graph (conj path vert))]
      (if (or
           (nil? cur-len)
           (< cur-len new-len))
        (swap! routes #(assoc % vert {:path path
                                      :length new-len
                                      :visited false}))))))

(defn no-visited-left?
  [routes]
  (some #(not (:visited (val %))) @routes))

(defn loc-children
  [loc]
  (println loc)
  (when-let [loc-child (zip/down loc)]
    (->> loc-child
         (iterate zip/right)
         (take-while some?))))

(defn visit-marker
  [routes]
  (fn [vertice]
    (swap! routes
           (fn [m]
             (update m vertice #(assoc % :visited true))))))

; todo: test
(defn shortest-path
  [graph start end]
  (let [graph' (remove-edges-to (simplify graph start) start)
        g-zipper (graph-zipper graph' start)
        routes (atom (scratch-routes graph' start))
        locs-to-iterate (atom [g-zipper])
        upd (update-routes graph' routes)
        visit (visit-marker routes)
        visited? (fn [loc]
                   (as-> loc x
                     (zip/node x)
                     (x routes)
                     (:visited x)))
        find-children (fn []
                        (->> @locs-to-iterate
                             (mapcat loc-children)
                             (remove visited?)))]
    (while (and
            (seq @locs-to-iterate)
            (some :visited (map val @routes)))
      (println "LOCS: "@locs-to-iterate)
      (let [children (mapcat loc-children @locs-to-iterate)]
        (mapv upd children)
        (mapv visit @locs-to-iterate)
        (reset! locs-to-iterate children)))
    (dissoc (end @routes) :visited)))

; routes map sample
(comment {:1 {:path [:3]
              :length 14
              :visited false}
          :2 {:path [:1 :3 :4]
              :length 23
              :visited true}
          :3 {:path nil
              :length nil
              :visited false}})
