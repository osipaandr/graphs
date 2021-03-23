# graphs

A Clojure library designed to work with graphs.

## Usage

All graphs are supposed to be designed as follows:

```clojure
  {:1 {:2 4,
       :3 3}
   :2 {:1 2}
   :3 {}}
```
So, each vertice is associated with a map, representing outgoing edges.

### graphs.random-gen
Namespace `graphs.random-gen` provides function `make-graph [n, s]`, where `n` is size of a graph (amount of vertices) and `s` is sparsness (amount of edges). It generates directed weakly(at least) connected weighted graph:

```clojure
(make-graph 6 10)
;; => {:4 {},
;;     :3 {:4 10, :2 6, :5 4, :1 3, :6 2},
;;     :2 {:1 9, :6 5},
;;     :1 {},
;;     :6 {:4 3, :5 5},
;;     :5 {:2 4}}
```
Notice that `n` should be non-negative integer and `s` should be in range `[n-1..n*(n-1)]`.
Weight of an edge is generated randomly from the range `[1..10]`. Consider using `graphs.random-gen/set-max-weight!` to change the upper limit.

### graphs.dijkstra
The most important function in `graphs.dijkstra` is `shortest-path [graph, start, end]` that implements [Dijkstra's algorithm](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm) for path finding in graphs. If path is found returns a map with path and it's length:
```clojure
;; Assuming G is a graph from the previous call,
(shortest-path G :5 :1)
;; => {:path [:5 :2 :1], :length 13}
```

### graphs.graph-utils
Provides functions to calculate graph's **eccentricity**, **diameter** and **radius** (check [Distance(graph theory)](https://en.wikipedia.org/wiki/Distance_(graph_theory))):
```clojure
(eccentricity G :3) ;; => 6
(radius G) ;; => 6
(diameter G) ;; => 18
```
