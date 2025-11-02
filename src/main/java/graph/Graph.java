package graph;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Graph {
    public static class Edge {
        public int u, v;
        public int w;
    }

    private int n;
    private boolean directed;
    private List<Edge> edges = new ArrayList<>();
    private int source;
    private String weightModel;

    // Constructor
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
    }

    public int getN() {
        return n;
    }

    public List<Edge> getEdges() {
        return edges;
    }


    public static Graph loadFromJson(String filename) {
        Gson gson = new Gson();
        try (Reader reader = new InputStreamReader(
                Graph.class.getResourceAsStream("/data/" + filename))) {

            JsonObject obj = gson.fromJson(reader, JsonObject.class);

            int n = obj.get("n").getAsInt();
            boolean directed = obj.get("directed").getAsBoolean();
            Graph g = new Graph(n, directed);
            g.source = obj.get("source").getAsInt();
            g.weightModel = obj.get("weight_model").getAsString();

            Type edgeListType = new TypeToken<List<Edge>>() {}.getType();
            g.edges = gson.fromJson(obj.get("edges"), edgeListType);

            return g;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printGraph() {
        System.out.println("Graph: " + n + " vertices");
        for (Edge e : edges) {
            System.out.printf("%d -> %d (w=%d)%n", e.u, e.v, e.w);
        }
    }

    public int getSource() {
        return source;
    }
}
