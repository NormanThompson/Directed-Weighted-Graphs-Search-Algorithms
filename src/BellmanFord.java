import java.util.*;

public class BellmanFord implements Search {
    /**
     * this runs very similar to dijkstras, but with a little bit of an addition for negative edge weights.
     * Initially, the setup is the same as Dijkstra's. We still check that the src and dest are in the graph,
     * as if they are not, we can immediately return null. If they are, we set up the same maps as last time
     * for distances, which contains the shortest possible known path from the source to a node, and bestKnown,
     * which stores the node one must travel through to follow the shortest possible path from the src to that node.
     * Initially, we set each node's distance to infinity and the path to null, just to clean up things for the actual working loops.
     * Then, we start examining each node. Look at each node size times, and each time we look at a node, we check its best distance.
     * If it has a distance that isn't infinity, we look at all its neighbors and check if any of those connections improve the
     * distance to that node. If they do, then going through that current node is the best new distance, and we update
     * the HashMaps accordingly. This is also where we check for negative edge cycles, with the if statement for i == size - 1. The reason
     * that this works, is that if we have x nodes, the longest possible path from src to dest that could still be the shortest path would be
     * through v - 1 edges. So if you have 3 nodes, you would have src a to dst c through b (2 edges). If it was ideal to go from a to b and then back to a,
     * then you would have a negative edge weight cycle. Therefore, if you are still updating a path that should be finalized AFTER going through the maximum
     * allowable number of edges, we know for sure that we have a negative edge weight cycle and can return null
     * @param graph the graph we are searching in
     * @param src   the source of the search, where we are trying to find the shortest path
     * @param dest  the destination of the search
     * @return Path record of the shortest known path, if it exists
     */
    @Override
    public Path search(DWGraph graph, String src, String dest) {
        if (!graph.nodes().contains(src) || !graph.nodes().contains(dest)) {
            return null;
        }
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> bestKnown = new HashMap<>();
        for (String node : graph.nodes()) {
            distances.put(node, Double.POSITIVE_INFINITY);
            bestKnown.put(node, null);
        }
        distances.put(src, 0.0);
        int size = graph.nodes().size();
        for (int i = 0; i < size; i++) {
            for (String currentNode : graph.nodes()) {
                if (!(distances.get(currentNode) == Double.POSITIVE_INFINITY)) {
                    for (String neighbor : graph.edges(currentNode)) {
                        double newDistance = distances.get(currentNode) + graph.weight(currentNode, neighbor);
                        if (newDistance < distances.get(neighbor)) {
                            if(i == size - 1) {
                                return null;
                            }
                            distances.put(neighbor, newDistance);
                            bestKnown.put(neighbor, currentNode);
                        }
                    }
                }
            }
        }
        if (distances.get(dest) == Double.POSITIVE_INFINITY) {
            return new Path(src, dest, Double.POSITIVE_INFINITY, graph, new String[0]);
        }
        LinkedList<String> path = new LinkedList<>();
        for (String step = dest; step != null; step = bestKnown.get(step)) {
            path.addFirst(step);
        }
        return new Path(src, dest, distances.get(dest), graph, path.toArray(new String[0]));
    }

}
