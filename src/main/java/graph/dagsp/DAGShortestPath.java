package graph.dagsp;

import java.util.*;


public class DAGShortestPath {

    public static class Result {
        public double[] dist;
        public int[] parent;
    }


    public static Result shortestPath(Map<Integer, List<int[]>> dag, int n, int source, List<Integer> topo) {
        Result res = new Result();
        res.dist = new double[n];
        res.parent = new int[n];
        Arrays.fill(res.dist, Double.POSITIVE_INFINITY);
        Arrays.fill(res.parent, -1);

        res.dist[source] = 0;

        for (int u : topo) {
            if (res.dist[u] != Double.POSITIVE_INFINITY) {
                for (int[] edge : dag.get(u)) {
                    int v = edge[0];
                    int w = edge[1];
                    if (res.dist[u] + w < res.dist[v]) {
                        res.dist[v] = res.dist[u] + w;
                        res.parent[v] = u;
                    }
                }
            }
        }
        return res;
    }


    public static Result longestPath(Map<Integer, List<int[]>> dag, int n, int source, List<Integer> topo) {
        Result res = new Result();
        res.dist = new double[n];
        res.parent = new int[n];
        Arrays.fill(res.dist, Double.NEGATIVE_INFINITY);
        Arrays.fill(res.parent, -1);

        res.dist[source] = 0;

        for (int u : topo) {
            if (res.dist[u] != Double.NEGATIVE_INFINITY) {
                for (int[] edge : dag.get(u)) {
                    int v = edge[0];
                    int w = edge[1];
                    if (res.dist[u] + w > res.dist[v]) {
                        res.dist[v] = res.dist[u] + w;
                        res.parent[v] = u;
                    }
                }
            }
        }
        return res;
    }

    public static List<Integer> reconstructPath(int target, int[] parent) {
        List<Integer> path = new ArrayList<>();
        for (int v = target; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}
