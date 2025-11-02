import graph.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
import graph.dagsp.DAGShortestPath;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Graph g = Graph.loadFromJson("tasks.json");
        if (g == null) {
            System.out.println("Graph not loaded!");
            return;
        }
        g.printGraph();

        // === 1. Find SCCs
        SCCFinder sccFinder = new SCCFinder(g);
        List<List<Integer>> sccs = sccFinder.findSCCs();

        Map<Integer, Integer> compOf = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) compOf.put(v, i);
        }

        // === 2. Build DAG with weights
        Map<Integer, List<int[]>> dag = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) dag.put(i, new ArrayList<>());

        for (Graph.Edge e : g.getEdges()) {
            int a = compOf.get(e.u);
            int b = compOf.get(e.v);
            if (a != b) dag.get(a).add(new int[]{b, e.w});
        }

        // === 3. Topological sort
        Map<Integer, List<Integer>> dagSimple = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) dagSimple.put(i, new ArrayList<>());
        for (var entry : dag.entrySet()) {
            for (int[] edge : entry.getValue()) {
                dagSimple.get(entry.getKey()).add(edge[0]);
            }
        }
        List<Integer> topo = TopologicalSort.sort(dagSimple, sccs.size());

        System.out.println("\n=== Topological Order ===");
        System.out.println(topo);

        // === 4. Shortest and Longest paths
        int src = compOf.get(g.getSource());
        DAGShortestPath.Result shortest = DAGShortestPath.shortestPath(dag, sccs.size(), src, topo);
        DAGShortestPath.Result longest = DAGShortestPath.longestPath(dag, sccs.size(), src, topo);

        System.out.println("\n=== Shortest Distances ===");
        System.out.println(Arrays.toString(shortest.dist));

        System.out.println("\n=== Longest Distances (Critical Path) ===");
        System.out.println(Arrays.toString(longest.dist));

        int maxNode = 0;
        for (int i = 0; i < longest.dist.length; i++) {
            if (longest.dist[i] > longest.dist[maxNode]) maxNode = i;
        }

        List<Integer> criticalPath = DAGShortestPath.reconstructPath(maxNode, longest.parent);
        System.out.println("\nCritical Path: " + criticalPath);
    }
}
