import graph.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static void main(String[] args) {

        String[] files = {
                "tasks_small.json",
                "tasks_small_2.json",
                "tasks_small_3.json",
                "tasks_medium.json",
                "tasks_medium_2.json",
                "tasks_medium_3.json",
                "tasks_large.json",
                "tasks_large_2.json",
                "tasks_large_3.json"
        };

        String outputPath = "src/main/resources/data/results.csv";

        try (FileWriter writer = new FileWriter(outputPath)) {
            writer.write("File,Nodes,Edges,Type,SCC Count,Execution Time (ms)\n");

            for (String file : files) {
                System.out.println("Processing " + file + " ...");

                Graph g = Graph.loadFromJson(file);
                if (g == null) {
                    System.out.println("Failed to load " + file);
                    continue;
                }

                long start = System.nanoTime();

                // Step 1: Find SCCs
                SCCFinder sccFinder = new SCCFinder(g);
                List<List<Integer>> sccs = sccFinder.findSCCs();

                // Step 2: Determine type (cyclic or acyclic)
                String type = (sccs.size() < g.getN()) ? "Cyclic" : "Acyclic";

                // Step 3: Build DAG
                Map<Integer, List<Integer>> dag = new HashMap<>();
                for (int i = 0; i < sccs.size(); i++) dag.put(i, new ArrayList<>());
                Map<Integer, Integer> compOf = new HashMap<>();
                for (int i = 0; i < sccs.size(); i++) {
                    for (int v : sccs.get(i)) compOf.put(v, i);
                }

                for (Graph.Edge e : g.getEdges()) {
                    int a = compOf.get(e.u);
                    int b = compOf.get(e.v);
                    if (a != b && !dag.get(a).contains(b)) {
                        dag.get(a).add(b);
                    }
                }

                // Step 4: Topological Sort
                List<Integer> topo = TopologicalSort.sort(dag, sccs.size());

                // Step 5: Compute shortest and longest paths in DAG
                Map<Integer, List<int[]>> dagWeighted = new HashMap<>();
                for (int i = 0; i < sccs.size(); i++) dagWeighted.put(i, new ArrayList<>());
                for (Graph.Edge e : g.getEdges()) {
                    int a = compOf.get(e.u);
                    int b = compOf.get(e.v);
                    if (a != b) dagWeighted.get(a).add(new int[]{b, e.w});
                }

                int src = compOf.get(g.getSource());
                DAGShortestPath.Result shortest = DAGShortestPath.shortestPath(dagWeighted, sccs.size(), src, topo);
                DAGShortestPath.Result longest = DAGShortestPath.longestPath(dagWeighted, sccs.size(), src, topo);

                long end = System.nanoTime();
                double timeMs = (end - start) / 1_000_000.0;

                // Step 6: Write result line
                writer.write(String.format(Locale.US, "%s,%d,%d,%s,%d,%.3f\n",
                        file,
                        g.getN(),
                        g.getEdges().size(),
                        type,
                        sccs.size(),
                        timeMs));

                System.out.printf("Done %s -> Time: %.3f ms, SCCs: %d%n",
                        file, timeMs, sccs.size());
            }

            System.out.println("\nAll results saved to: " + outputPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
