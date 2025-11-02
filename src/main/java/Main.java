import graph.Graph;
import graph.scc.SCCFinder;
import graph.topo.TopologicalSort;
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

        System.out.println("\n=== Strongly Connected Components ===");
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("SCC " + (i + 1) + ": " + sccs.get(i));
        }

        // === 2. Build condensation graph (DAG)
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

        // === 3. Topological Sort
        List<Integer> topo = TopologicalSort.sort(dag, sccs.size());

        System.out.println("\n=== Condensation DAG (SCC connections) ===");
        for (var entry : dag.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        System.out.println("\n=== Topological Order of SCCs ===");
        System.out.println(topo);
    }
}
