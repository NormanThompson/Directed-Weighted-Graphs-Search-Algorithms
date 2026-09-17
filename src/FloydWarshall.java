import java.util.*;

public class FloydWarshall implements Search {
    /**
     * The way that this search works, is that it takes in a graph and ALL as src and dest.
     * For each node, it initially sets the distance to themselves as 0 and any initial connections between
     * nodes as the shortest distance. We also map each node to an integer index, since we need a consistent ordering for later.
     * Then, we get to the actual bulk of the work itself, the triply nested for loop that makes this algorithm run in O(n^3) time.
     * In this for loop, k acts as the main node we are examining in each pairing of src and dest, to see if going
     * through the k node improves the travel distance from i to j. If it does, then we replace the distance with that path,
     * and if it doesn't, we leave it. This repeats for each node and each src/dest pair.
     * After the for loops are done, i store the results in a new ArrayList. and convert that to an array for the final return.
     * The shortest paths of each source/destination pair are contained within that array.
     * @param graph we are finding all pairs shortest paths for
     * @param src this is just going to be all here, since we are looking for all paris shortest paths
     * @param dest this is just going to be all also, for the same reason as src
     * @return Path this is the path that is REPURPOSED to contain an array field that no longer contains a specific
     * path, but rather contains all of the shortest paths in string form.
     */
    @Override
    public Path search(DWGraph graph, String src, String dest) {
        List<String> nodes = graph.nodes();
        int size = nodes.size();
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 0; i < size; i++) {
            indices.put(nodes.get(i), i);
        }

        double[][] distances = new double[size][size];
        for (double[] row : distances) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }
        for (int i = 0; i < size; i++) {
            distances[i][i] = 0.0;
        }
        for (String source : nodes) {
            for (String destination : graph.edges(source)) {
                distances[indices.get(source)][indices.get(destination)] = graph.weight(source, destination);
            }
        }

        for (int k = 0; k < size; k++) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (distances[i][k] != Double.POSITIVE_INFINITY && distances[k][j] != Double.POSITIVE_INFINITY) {
                        distances[i][j] = Math.min(distances[i][j], distances[i][k] + distances[k][j]);
                    }
                }
            }
        }
        List<String> results = new ArrayList<>();
        for (String source : nodes) {
            for (String destination : nodes) {
                if (!source.equals(destination)) {
                    results.add(source + " to " + destination + ": " + distances[indices.get(source)][indices.get(destination)]);
                }
            }
        }
        return new Path("<ALL>", "<ALL>", 0.0, graph, results.toArray(new String[0]));
    }
}