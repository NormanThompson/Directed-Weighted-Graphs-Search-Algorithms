import java.io.File;
import java.util.*;

public class DWGraph {
    private Digraph graph;
    private int size;
    private double mtxThreshold;
    private double lstThreshold;
    private Search strategy;

    public DWGraph() {
        this.graph = new AdjList();
        this.size = 0;
        this.mtxThreshold = 0.66;
        this.lstThreshold = 0.33;
    }

    /**
     *constructor for building the DWGraph from a file using the filepath. Reads using
     *the JSON format provided.
     *@param filepath the path to the JSON file
     */
    public DWGraph(String filepath) {
        this();
        try {
            String src = null;
            Scanner scan = new Scanner(new File(filepath));
            while (scan.hasNextLine()) {
                String line = scan.nextLine().trim();
                if (line.isEmpty() || line.equals("{") || line.equals("}")) {
                    continue;
                }
                if (line.endsWith("{")) {
                    line = line.replace("\"", "");
                    line = line.replace(":", "");
                    line = line.replace("{", "");
                    src = line.trim();
                    add(src);
                } else if (src != null && line.contains(":")) {
                    String[] parts = line.split(":");

                    parts[0] = parts[0].replace("\"", "");
                    parts[0] = parts[0].trim();

                    parts[1] = parts[1].replace(",", "");
                    parts[1] = parts[1].trim();

                    double weight = Double.parseDouble(parts[1]);

                    add(src, parts[0], weight);
                }

            }
            scan.close();
        } catch (Exception e) {
            System.out.println("Error reading file" + e);
        }

    }

    /**
     *adds a node to the graph
     *@param key  the key to be added
     *@return true if the key was added, false if not
     */
    public boolean add(String key) {
        boolean result = graph.add(key);
        if (result) {
            size++;
        }
        convert();
        return result;
    }

    /**
     *checks if the graph needs to be converted from an AdjList to an AdjMatrix or vice versa
     */
    private void convert() {
        double density = graph.density();
        if (graph instanceof AdjList && density > mtxThreshold) {
            AdjMatrix newMatrix = new AdjMatrix();
            transferTo(newMatrix);

        } else if (graph instanceof AdjMatrix && density < lstThreshold) {
            AdjList newList = new AdjList();
            transferTo(newList);
        }
    }

    /**
     *converts one graph type into another by adding each node and then each edge from that node
     *@param newGraph     the graph we are converting to (sets the current graph equal to this at the end
     */
    private void transferTo(Digraph newGraph) {
        for (String node : graph.nodes()) {
            newGraph.add(node);
        }
        for (String src : graph.nodes()) {
            for (String dest : graph.edges(src)) {
                newGraph.add(src, dest, graph.weight(src, dest));
            }
        }
        this.graph = newGraph;
    }

    /**
     *deletes a node from the graph
     *@param key  the node to be removed
     *@return String  the key that was removed (or null if not)
     */
    public String delete(String key) {
        String result = graph.delete(key);
        if (result != null) {
            size--;
        }
        convert();
        return result;
    }

    /**
     *finds the graph's density by calling density() in graph
     *@return double  the density of the graph
     */
    public double density() {
        return graph.density();
    }

    /**
     *finds a node's density by calling density(key) in graph
     *@param key  the node we want the density for
     *@return double  the density of the given node
     */
    public double density(String key) {
        return graph.density(key);
    }

    /**
     *@return int the size of the graph
     */
    public int size() {
        return this.size;
    }

    /**
     *deletes an edge from the graph
     *@param src  the source of the edge
     *@param dest the destination of the edge
     *@return double  the weight of the deleted edge
     */
    public Double delete(String src, String dest) {
        Double result = graph.delete(src, dest);
        convert();
        return result;

    }

    /**
     *@return ArrayList   the list of graph nodes
     */
    public ArrayList<String> nodes() {
        return graph.nodes();
    }

    /**
     *@return ArrayList   the list of graph edges
     */
    public ArrayList<String> edges(String key) {
        return graph.edges(key);
    }

    /**
     *gives the weight of a single edge
     *@param src  the source of the edge
     *@param dest the destination of the edge
     *@return double  the weight of the requested edge
     */
    public Double weight(String src, String dest) {
        return graph.weight(src, dest);
    }

    /**
     *adds an edge if it doesn't already exist
     *@param src  the source of the edge
     *@param dest the destination of the edge
     *@param weight   the weight of the edge
     *@return true if the edge was added, false if not
     */
    public boolean add(String src, String dest, Double weight) {
        int preSize = graph.size();
        boolean result = graph.add(src, dest, weight);
        if (result) {
            size += graph.size() - preSize;
        }
        convert();
        return result;
    }

    /**
     *calls toString from graph
     */
    @Override
    public String toString() {
        return graph.toString();
    }

    /**
     *calls toJSON from graph
     */
    public String toJSON() {
        return graph.toJSON();
    }

    /**
     *@param filepath the filepath for the graph to be loaded
     *@return the graph loaded from the filepath using the constructor(filepath)
     */
    public static DWGraph load(String filepath) {
        return new DWGraph(filepath);
    }

    /**
     * This method returns a Path from some src to dest, deciding which of the three algorithms
     * to use based on the inpout. if the input is for all pairs shortest paths, we run floyd warshall.
     * if the input is from a single src to dest, then we run bellman ford
     * if there are negative edge weights, and dijkstras if not.
     * @param src the source of the search
     * @param dest the destination of the path
     * @return path record containing shortest path, total cost, source, dest
     */
    public Search.Path search(String src, String dest) {
        if (src.equals("<ALL>") && dest.equals("<ALL>")) {
            strategy = new FloydWarshall();
        } else if (hasNegativeEdgeWeights()) {
            strategy = new BellmanFord();
        } else {
            strategy = new Dijkstras();
        }
        return strategy.search(this, src, dest);
    }

    /**
     * This just goes through each edge and checks if any of them are negative
     * @return true if the graph has negative edge weights, false if not
     */
    public boolean hasNegativeEdgeWeights() {
        for (String src : graph.nodes()) {
            for (String dest : graph.edges(src)) {
                if (graph.weight(src, dest) < 0) {
                    return true;
                }
            }
        }
        return false;
    }


}
