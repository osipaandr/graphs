(ns graphs.core)

;; TODO: rework to make it always connected 

(defonce max-weight (atom 10))
(defn set-max-weight!
  "Since there's no basic restrictions on the edge weight, use this function to set it's maximum"
  [x]
  (if (pos-int? x)
    (reset! max-weight x)
    (println "Max edge weight should be positive integer")))

(defn edge-to
  [end]
  {end (inc (rand-int @max-weight))})

(defn add-edge
  [graph start end]
  (assoc graph start
         (merge (graph start) (edge-to end))))

(defn filter-ends
  "Drops starting vertice itself and vertices that already have an edge from start"
  [graph start verts]
  (->> (start graph)
       (keys)
       (set)
       (reduce disj (disj verts start))))

(def peek-rand (comp rand-nth seq))

(defn add-random-edge
  [graph k verts]
  (->> (filter-ends graph k verts)
       (peek-rand)
       (add-edge graph k)))

(defn build-recur
  [graph all-v unused-v edges-left]
  (if (= 0 edges-left)
    graph
    (let [v-pull (if (empty? unused-v) all-v unused-v)
          start (peek-rand v-pull)]
      (build-recur (add-random-edge graph start all-v)
                   all-v
                   (disj unused-v start)
                   (dec edges-left)))))

; probably there is a better way to do this
(defn scratch
  "Generates a graph with all given vertices but without any edges"
  [vertices]
  (reduce #(assoc %1 %2 {}) {} vertices))

(defn kw [a]
  (keyword (str a)))

(defn random-good-graph
  [n sprs]
  (let [all-v (set (map kw (range 1 (inc n))))]
    (build-recur (scratch all-v) all-v all-v sprs)))

(defn random-graph-impl
  [n sprs]
  (cond
    (= n 1) {:1 {}}
    :else (random-good-graph n sprs)))

(defn valid-args?
  [n sprs]
  (and
   (pos-int? n)
   (<= (dec n) sprs (* (dec n) n))))

(defn make-graph
  "If arguments for building a graph are valid, builds random graph"
  [n sprs]
  (cond
    (= n 0) {}
    (= n 1) {:1 {}}
    :else (if (valid-args? n sprs)
                                        ;(random-graph-impl n sprs)
            (random-good-graph n sprs)
            (throw (IllegalArgumentException. "Invalid args")))))
