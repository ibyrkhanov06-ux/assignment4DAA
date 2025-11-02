import graph.Graph;

public class Main {
    public static void main(String[] args) {
        Graph g = Graph.loadFromJson("tasks.json");
        if (g != null) {
            g.printGraph();
        } else {
            System.out.println("Failed to load graph.");
        }
    }
}
