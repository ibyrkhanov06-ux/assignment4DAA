import graph.Graph;
import graph.scc.SCCFinder;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Graph g = Graph.loadFromJson("tasks.json");
        if (g == null) {
            System.out.println("Graph not loaded!");
            return;
        }
        g.printGraph();

        System.out.println("\n=== Strongly Connected Components ===");
        SCCFinder scc = new SCCFinder(g);
        List<List<Integer>> comps = scc.findSCCs();

        int idx = 1;
        for (List<Integer> c : comps) {
            System.out.println("SCC " + idx++ + ": " + c);
        }
    }
}
