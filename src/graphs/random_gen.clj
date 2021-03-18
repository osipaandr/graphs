(ns graphs.random-gen
  (:require [clojure.set :refer :all]))

(defonce ^:private max-weight (atom 10))
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

(defn available-verts
  ([entry all]
   (let [start (key entry)
         ends (keys (val entry))]
     (available-verts start ends all)))

  ([start ends all]
   (let [used (cons start ends)]
     (->> (map set [all used])
          (apply difference)))))

(defn full?
  [entry all]
  (empty? (available-verts entry all)))

(defn rand-non-full-vertice
  [graph]
  (let [all (keys graph)]
    (-> (remove #(full? % all) graph)
        ((comp rand-nth keys)))))

(defn rand2-diff
  [graph]
  (let [all (keys graph)
        start (rand-non-full-vertice graph)
        valid-ends (available-verts start (keys (start graph)) all)]
    [start, (peek-rand valid-ends)]))

(defn select-verts
  [graph all]
  (let [used (keys graph)
        unused (difference all used)]
    (if (empty? unused)
      (rand2-diff graph)
      (-> (map peek-rand [used unused])
          (shuffle)))))

(defn build-recur
  [graph all-v edges-left]
  #_(println graph)
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
