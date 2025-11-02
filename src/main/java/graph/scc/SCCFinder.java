package graph.scc;

import graph.Graph;
import java.util.*;

/**
 * Tarjan's Algorithm for finding Strongly Connected Components (SCCs)
 */
public class SCCFinder {

    private Graph graph;
    private int time = 0;
    private int[] ids;
    private int[] low;
    private boolean[] onStack;
    private Deque<Integer> stack;
    private List<List<Integer>> sccList;

    public SCCFinder(Graph graph) {
        this.graph = graph;
    }

    public List<List<Integer>> findSCCs() {
        int n = graph.getN();
        ids = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new ArrayDeque<>();
        sccList = new ArrayList<>();

        Arrays.fill(ids, -1);

        // Строим смежный список
        Map<Integer, List<Integer>> adj = new HashMap<>();
        for (int i = 0; i < n; i++) adj.put(i, new ArrayList<>());
        for (Graph.Edge e : graph.getEdges()) {
            adj.get(e.u).add(e.v);
        }

        for (int i = 0; i < n; i++) {
            if (ids[i] == -1) dfs(i, adj);
        }
        return sccList;
    }

    private void dfs(int at, Map<Integer, List<Integer>> adj) {
        stack.push(at);
        onStack[at] = true;
        ids[at] = low[at] = time++;

        for (int to : adj.get(at)) {
            if (ids[to] == -1) dfs(to, adj);
            if (onStack[to]) low[at] = Math.min(low[at], low[to]);
        }

        // Если это корень компоненты
        if (ids[at] == low[at]) {
            List<Integer> scc = new ArrayList<>();
            while (true) {
                int node = stack.pop();
                onStack[node] = false;
                scc.add(node);
                low[node] = ids[at];
                if (node == at) break;
            }
            sccList.add(scc);
        }
    }
}
