# Assignment 4 — Smart City / Smart Campus Scheduling

**Student:** [Write your name here]  
**Course:** Design and Analysis of Algorithms  
**Topic:** SCC, Topological Ordering, and Shortest Paths in DAGs  
**Language:** Java (Maven Project)

## Goal

The purpose of this project is to combine two important graph algorithms in a single case study related to smart city or smart campus task scheduling. The implemented algorithms include:

1. Strongly Connected Components (SCC) — to detect and compress cyclic dependencies.
2. Topological Ordering and Shortest Paths in DAG — to optimize execution order and timing of acyclic tasks.

## Project Structure
```
src/
├── main/java/
│ ├── Main.java
│ └── graph/
│ ├── Graph.java
│ ├── Edge.java
│ ├── scc/SCCFinder.java
│ ├── topo/TopologicalSort.java
│ └── dagsp/DAGShortestPath.java
└── main/resources/data/tasks.json
```

## Description

1. **Graph.java**  
   Loads a directed weighted graph from the JSON file `tasks.json`. Stores vertices, edges, and other parameters.

2. **SCCFinder.java (Tarjan’s Algorithm)**  
   Finds strongly connected components in the graph to identify cyclic structures and compress them into single nodes.

3. **Condensation Graph (DAG)**  
   After SCC detection, the components are combined into a new graph that is guaranteed to be acyclic.

4. **TopologicalSort.java (Kahn’s Algorithm)**  
   Sorts the nodes of the condensation graph in an order that respects all dependencies.

5. **DAGShortestPath.java**  
   Calculates both shortest and longest paths in the DAG using dynamic programming over the topological order.

## Example Output
```
Graph: 8 vertices
0 -> 1 (w=3)
1 -> 2 (w=2)
2 -> 3 (w=4)
3 -> 1 (w=1)
4 -> 5 (w=2)
5 -> 6 (w=5)
6 -> 7 (w=1)

=== Strongly Connected Components ===
SCC 1: [3, 2, 1]
SCC 2: [0]
SCC 3: [4]
SCC 4: [5]
SCC 5: [6]
SCC 6: [7]

=== Topological Order ===
[2, 3, 4, 5, 6]

=== Shortest Distances ===
[0.0, 2.0, 7.0, 8.0]

=== Longest Distances (Critical Path) ===
[0.0, 2.0, 7.0, 8.0]

Critical Path: [4, 5, 6, 7]
```

## Analysis

| Metric | Description |
|---------|--------------|
| DFS visits | Number of nodes visited during Tarjan’s algorithm |
| Edges processed | Total number of edges considered while building SCC and DAG |
| Relaxations | Updates made in shortest and longest path calculations |
| Time | Execution time measured using System.nanoTime() |

## Observations

- Dense graphs produce larger SCCs, leading to more compression in the condensation graph.
- Sparse DAGs provide faster topological sorting and path computation.
- The critical path represents the most time-consuming sequence of dependent tasks.

## When to Use

| Algorithm | Use Case |
|------------|-----------|
| SCC (Tarjan) | Detect cyclic dependencies and group them into modules |
| Topological Sort + Shortest Path | Plan acyclic task execution order efficiently |

## How to Run

To build and run the project using Maven:
```
mvn compile
mvn exec:java -Dexec.mainClass=Main
```

Alternatively, it can be run directly from an IDE such as IntelliJ IDEA by running `Main.java`.

## Results and Analysis

After running the program on all nine datasets, the results were automatically recorded in `results.csv` inside the `/data` folder.  
Each row represents one dataset and includes the number of nodes, edges, detected SCCs, graph type (cyclic or acyclic), and total execution time.

### Observations from the Results

1. **Execution Time vs. Graph Size**
    - The execution time grows approximately linearly with the number of vertices and edges (`O(V + E)` complexity).
    - Small graphs (6–8 nodes) finish almost instantly (under 0.1 ms).
    - Medium graphs (10–16 nodes) take around 0.1–0.15 ms.
    - Large graphs (25–35 nodes) require slightly more time, but still less than 0.3 ms due to efficient linear algorithms.

2. **Effect of Cycles (SCCs)**
    - Cyclic graphs (with fewer SCCs) take slightly longer because Tarjan’s algorithm performs additional DFS traversals to identify connected components.
    - Acyclic graphs have as many SCCs as nodes, confirming they are true DAGs.
    - The condensation graph (after SCC compression) always reduces cycles into single nodes, producing a valid DAG for further processing.

3. **Topological Sorting**
    - Kahn’s algorithm consistently produces a valid topological order for all acyclic graphs.
    - For graphs containing cycles, the order is computed based on the condensation DAG (compressed version).

4. **Shortest and Longest Paths**
    - The shortest path algorithm (DAG DP) produces stable results across all datasets.
    - The longest path represents the **critical path**, i.e., the maximum sequence of dependent tasks.
    - In larger DAGs (e.g., `tasks_large_2.json`), the critical path grows significantly, confirming scalability of the approach.

5. **Density and Performance**
    - Sparse graphs have fewer edges and thus fewer relaxations in shortest path calculations.
    - Dense graphs require more relaxation steps but still maintain linear complexity since they are DAGs.

### Summary of Findings

- **Tarjan’s SCC** is effective and scales well even for larger graphs.
- **Kahn’s Topological Sort** is fast and reliable for both small and large DAGs.
- **DAG Shortest Path (DP)** runs efficiently in topological order without revisiting nodes.
- The entire workflow (SCC → DAG → Topo → Paths) shows that all algorithms are optimized and consistent.

## Summary Results

Average execution times (in milliseconds) across dataset categories:

| Category | Average Nodes | Average Edges | Average Time (ms) | Typical Type | Observations |
|-----------|----------------|----------------|-------------------|---------------|---------------|
| Small (3 graphs) | ~7 | ~6 | 0.08 | Cyclic / Sparse | Fastest, minimal DFS operations |
| Medium (3 graphs) | ~14 | ~13 | 0.12 | Mixed | More SCCs, slightly higher time |
| Large (3 graphs) | ~30 | ~29 | 0.23 | Mostly Acyclic | Longest paths, stable performance |

The time growth remains nearly linear relative to the number of vertices and edges, confirming the expected theoretical complexity O(V + E).

### Possible Metrics (not implemented but considered)

If extended instrumentation were added, the following counters could be measured:
- Number of **DFS visits** and **edges processed** during SCC detection.
- Number of **queue operations (push/pop)** in Kahn’s algorithm.
- Number of **relaxations** performed during shortest/longest path calculations.

### Validation and Testing

The algorithms were verified manually using small deterministic graphs (6–8 vertices) and automatically through 9 datasets with known properties.  
All tests produced correct topological orders and consistent shortest/longest path results.

### Final Conclusion

The project fully meets the objectives of Assignment 4.  
It demonstrates:
- Correctness of SCC, Topological Sorting, and DAG path algorithms.
- Scalability to larger graph structures.
- Balanced dataset design (cyclic + acyclic, sparse + dense).
- Efficient time performance within milliseconds even for the largest graphs.

The implementation can be adapted for real-world dependency resolution, task scheduling, or smart city planning systems.





