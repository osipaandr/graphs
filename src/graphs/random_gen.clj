(ns graphs.random-gen
  (:require [clojure.set :refer :all]))

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
  (let [graph' (if (contains? graph end)
                 graph
                 (assoc graph end {}))]
    (assoc graph' start
           (merge (start graph) (edge-to end)))))

(def peek-rand (comp rand-nth seq))

(defn rand2-diff
  [coll]
  (let [first  (peek-rand coll)
        second (peek-rand (disj coll first))]
    [first, second]))

(defn select-verts
  [graph all-v]
  (let [used-v (keys graph)
        unused-v (difference all-v used-v)]
    (if (empty? unused-v)
      (rand2-diff all-v)
      (-> (map peek-rand [used-v unused-v])
          (shuffle)))))

(defn build-recur
  [graph all-v edges-left]
  (if (= 0 edges-left)
    graph
    (let [[start, end] (select-verts graph all-v)]
      (build-recur (add-edge graph start end)
                   all-v
                   (dec edges-left)))))

(defn kw [a]
  (keyword (str a)))

(defn random-graph
  [n sprs]
  (let [all-v (set (map kw (range 1 (inc n))))
        origin (peek-rand all-v)]
    (build-recur {origin {}} all-v sprs)))

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
            (random-graph n sprs)
            (throw (IllegalArgumentException. "Invalid args")))))
