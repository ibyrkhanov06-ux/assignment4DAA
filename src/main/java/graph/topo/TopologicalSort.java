package graph.topo;

import java.util.*;


public class TopologicalSort {

    public static List<Integer> sort(Map<Integer, List<Integer>> adj, int n) {
        int[] indeg = new int[n];
        for (int u : adj.keySet()) {
            for (int v : adj.get(u)) {
                indeg[v]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indeg[i] == 0) q.add(i);
        }

        List<Integer> topo = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            topo.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) q.add(v);
            }
        }

        if (topo.size() != n) {
            System.out.println("Warning: Graph is not a DAG (cycle detected).");
        }

        return topo;
    }
}
