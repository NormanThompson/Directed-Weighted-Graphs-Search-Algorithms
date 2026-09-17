import java.util.*;

public class AdjMatrix implements Digraph {
    private Double[][] weights; //[src][dest]
    private HashMap<String, Integer> keyMap;
    int capacity;

    public AdjMatrix() {
        this.capacity = 100;
        this.weights = new Double[this.capacity][this.capacity];
        this.keyMap = new HashMap<>();
    }
    /**
    *adds a vertex to the adj matrix by putting it in the keymap and resizing the matrix if needed
    *@param key  the vertex to be added
    *@return true if added, false if not
     */
    @Override
    public boolean add(String key) {
        if (keyMap.containsKey(key)) {
            return false;
        }
        if (size() == capacity) {
            resize();
        }
        keyMap.put(key, size());
        return true;
    }
    /**
    *resizes the matrix by doubling its effective capacity and reassigning weights
     */
    public void resize() {
        capacity *= 2;
        Double[][] newWeights = new Double[capacity][capacity];
        for (int i = 0; i < size(); i++) {
            for (int j = 0; j < size(); j++) {
                newWeights[i][j] = weights[i][j];
            }
        }
        weights = newWeights;
    }
    /**
    *adds a connection from node src to node dest with a weight
    *@param src  the vertex where the connection comes from
    *@param dest the vertex where the connection goes to
    *@param weight  the weight of the connection from src to dest
    *@return true if added, false if not
     */
    @Override
    public boolean add(String src, String dest, Double weight) {
        if (!keyMap.containsKey(src)) {
            add(src);
        }
        if (!keyMap.containsKey(dest)) {
            add(dest);
        }
        if (weights[keyMap.get(src)][keyMap.get(dest)] != null) {
            return false;
        }
        weights[keyMap.get(src)][keyMap.get(dest)] = weight;
        return true;

    }
    /**
    *@return an ArrayList of all nodes
     */
    @Override
    public ArrayList<String> nodes() {
        return new ArrayList<>(keyMap.keySet());
    }
    /**
    *gives a list of outgoing edges from a node
    *@param key  the node that the edges are coming from
    *@return ArrayList   the list of edges coming from the key
     */
    @Override
    public ArrayList<String> edges(String key) {
        if (!keyMap.containsKey(key)) {
            return new ArrayList<>();
        }
        int row = keyMap.get(key);
        ArrayList<String> result = new ArrayList<>();
        for (String node : keyMap.keySet()) {
            int column = keyMap.get(node);
            if (weights[row][column] != null) {
                result.add(node);
            }
        }
        return result;
    }
    /**
    *gives the weight of a connection
    *@param src  the source of the connection
    *@param dest the dest of the connection
    *@return Double  the weight of the conenction
     */
    @Override
    public Double weight(String src, String dest) {
        if (!keyMap.containsKey(src) || !keyMap.containsKey(dest)) {
            return null;
        }
        int row = keyMap.get(src);
        int column = keyMap.get(dest);
        return weights[row][column];
    }
    /**
    *deletes a connection if it exists
    *@param src  the source of the connection
    *@param dest the destination of the conenction
    *@return Double  the weight of the removed connection
     */
    @Override
    public Double delete(String src, String dest) {
        if (!keyMap.containsKey(src) || !keyMap.containsKey(dest)) {
            return null;
        }
        int row = keyMap.get(src);
        int column = keyMap.get(dest);
        Double removed = weights[row][column];
        weights[row][column] = null;
        return removed;
    }
    /**
    *@return int the number of nodes in the keyMap
     */
    @Override
    public int size() {
        return keyMap.size();
    }
    /**
    *calculates the density of the map as the total # of connections / the # of possible connections
    *@return Double  the density of the keymap
     */
    @Override
    public double density() {
        int total = 0;
        int nodes = keyMap.size();
        if (nodes <= 1) {
            return 0;
        }
        for (int row = 0; row < size(); row++) {
            for (int column = 0; column < size(); column++) {
                if (weights[row][column] != null) {
                    total += 1;
                }
            }
        }
        return (double) total / ((double) nodes * (double) (nodes - 1));
    }
    /**
    *calculates the density for a single node as the # of connections / the # of possible connections
    *@return double  the density of the node
     */
    @Override
    public double density(String key) {
        int total = 0;
        if (!keyMap.containsKey(key) || keyMap.size() <= 1) {
            return 0;
        }
        int row = keyMap.get(key);
        for (int i = 0; i < size(); i++) {
            if (weights[row][i] != null) {
                total++;
            }
        }
        return (double) (total) / (double) (keyMap.size() - 1);

    }
    /**
    *deletes an entire key from the hashMap by removing its entire row and column
    *from the matrix and removing it from the keymap and then sets the entire last row and
    *column to null since it gets copied.
    *@param key  the node to be deleted
    *@return String  the key that was deleted, or null in the case that it wasn't
     */
    @Override
    public String delete(String key) {
        if (!keyMap.containsKey(key)) {
            return null;
        }
        int index = keyMap.get(key);
        int oldSize = size();
        keyMap.remove(key);
        //shift down
        for (int i = index; i < oldSize - 1; i++) {
            for (int j = 0; j < oldSize; j++) {
                weights[i][j] = weights[i + 1][j];
            }
        }
        //shift left
        for (int j = index; j < oldSize - 1; j++) {
            for (int i = 0; i < oldSize; i++) {
                weights[i][j] = weights[i][j + 1];
            }
        }

        for (int i = 0; i < oldSize; i++) {
            weights[i][oldSize - 1] = null;
            weights[oldSize - 1][i] = null;
        }

        for (String node : keyMap.keySet()) {
            if (keyMap.get(node) > index) {
                keyMap.put(node, keyMap.get(node) - 1);
            }
        }

        return key;
    }
    /**
    *gives a list of source nodes, destination nodes, and their respective weights. I wrote this
    *before the toJSON part so i decided to leave it as is.
    *@return String  the list of edges
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String src : keyMap.keySet()) {
            boolean first = true;
            sb.append(src + ": ");
            int row = keyMap.get(src);
            for (String dest : keyMap.keySet()) {
                int column = keyMap.get(dest);
                if (weights[row][column] != null) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(dest + "(" + weights[row][column] + ")");
                    first = false;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    /**
    *Tried my best to get the exact JSON format that you guys wanted for the assignment
    *This method just takes the entire active list and puts it into JSON format
    *@return String  the entire completed JSON string
    */
    @Override
    public String toJSON() {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        boolean first = true;
        for (String src : keyMap.keySet()) {
            if (!first) {
                sb.append(",\n");
            }
            first = false;
            sb.append("\t\"" + src + "\" : {\n");
            int row = keyMap.get(src);
            boolean firstEdge = true;
            for (String dest : keyMap.keySet()) {
                int column = keyMap.get(dest);
                if (weights[row][column] != null) {
                    if (!firstEdge) {
                        sb.append(",\n");
                    }
                    firstEdge = false;
                    sb.append("\t\t\"" + dest + "\" : ");
                    sb.append(weights[row][column]);
                }

            }
            sb.append("\n\t}");
        }
        sb.append("\n}");
        return sb.toString();
    }


}

