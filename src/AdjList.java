import java.util.*;

public class AdjList implements Digraph {
    private HashMap<String, HashMap<String, Double>> connections;

    public AdjList() {
        this.connections = new HashMap<>();
    }
    /**
    *adds a key to the hashMap with no connections
    *@param key  the node to be added
    *@return     true if added, false if not (already exists)
     */
    @Override
    public boolean add(String key) {
        if (connections.containsKey(key)) {
            return false;
        } else {
            connections.put(key, new HashMap<>());
            return true;
        }
    }
    /**
    *adds a single edge if it doesn't already exist
    *@param src  the source of the edge
    *@param dest the destination of the edge
    *@param weight   the weight of the edge
    *@return     true if added, false if not
     */
    @Override
    public boolean add(String src, String dest, Double weight) {
        if (connections.containsKey(src) && connections.get(src).containsKey(dest)) {
            return false;
        }
        if (!connections.containsKey(src)) {
            connections.put(src, new HashMap<>());
        }
        if (!connections.containsKey(dest)) {
            connections.put(dest, new HashMap<>());
        }
        connections.get(src).put(dest, weight);
        return true;
    }
    /**
    *@return ArrayList   a list of all of the nodes in the map
     */
    @Override
    public ArrayList<String> nodes() {
        return new ArrayList<>(connections.keySet());
    }
    /**
    *gives a list of all the edges from a single node if it exists
    *@param key  the node we are getting edges for
    *@return ArrayList   the list of all edges in the map for the given key
     */
    @Override
    public ArrayList<String> edges(String key) {
        if (!connections.containsKey(key)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(connections.get(key).keySet());
    }
    /**
    *finds the weight of a single edge
    *@param src  the source of the edge
    *@param dest the destination of the edge
    *@return Double  the weight of the edge
     */
    @Override
    public Double weight(String src, String dest) {
        if (!connections.containsKey(src)) {
            return null;
        }
        return connections.get(src).get(dest);
    }
    /**
    *deletes a key from the  list, starting by removing the key itself and then each
    *connection for it
    *@param key  the key to be removed
    *@return String  the key that was removed, or null if it wasn't
     */
    @Override
    public String delete(String key) {
        if (!connections.containsKey(key)) {
            return null;
        }
        connections.remove(key);
        for (String node : connections.keySet()) {
            connections.get(node).remove(key);
        }
        return key;
    }
    /**
    *deletes a single connection from the list if it exists
    *@param src  the source of the connection
    *@param dest the destination of the connection
    *@return Double  the weight of the deleted connection if it was removed, or null if it wasn't
     */
    @Override
    public Double delete(String src, String dest) {
        if (!connections.containsKey(src)) {
            return null;
        }
        Double weight = connections.get(src).get(dest);
        connections.get(src).remove(dest);
        return weight;
    }
    /**
    *@return int the size of the list
     */
    @Override
    public int size() {
        return connections.size();
    }
    /**
    *calculates the density of the list by total # of edges / total # of possible edges
    *@return double  the density of the list
     */
    @Override
    public double density() {
        int size = connections.size();
        if (size <= 1) {
            return 0;
        } else {
            int edges = 0;
            for (String node : connections.keySet()) {
                edges += connections.get(node).size();
            }
            return (double) edges / ((double) size * ((double) size - 1.0));
        }
    }
    /**
    *calculates the density for a single node as # of connections from it / # of possible connections
    *@param key  the node we are calculating density for
    *@return double  the density of the returned node
     */
    @Override
    public double density(String key) {
        if (!connections.containsKey(key)) {
            return 0;
        }
        if (connections.size() <= 1) {
            return 0;
        }
        return (double) connections.get(key).size() / ((double) connections.size() - 1.0);
    }
    /**
    *wrote this before the toJSON so i don't know what
    *use it has, but it basically just makes a list with
    *weights and connections
    *@return String  the list of conenctions
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String node : connections.keySet()) {
            boolean first = true;
            sb.append(node + ": ");
            for (String dest : connections.get(node).keySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(dest + " (" + connections.get(node).get(dest) + ")");
                first = false;
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    /**
    *converts to the given JSON format, hopefully it works well. In my testing it seems to work
    *@return String  the JSON string
     */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        boolean first = true;
        for (String src : connections.keySet()) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            sb.append("\t\"");
            sb.append(src);
            sb.append("\" : {\n");
            boolean firstEdge = true;
            for (String dest : connections.get(src).keySet()) {
                if (!firstEdge) {
                    sb.append(",\n");
                }
                firstEdge = false;
                sb.append("\t\t\"");
                sb.append(dest);
                sb.append("\" : ");
                sb.append(connections.get(src).get(dest));
            }
            sb.append("\n\t}");
        }
        sb.append("\n}");
        return sb.toString();

    }
}