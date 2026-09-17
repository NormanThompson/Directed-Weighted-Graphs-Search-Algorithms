import java.util.*;
public class Dijkstras implements Search {
    /**
     * First, it's worth doing a check to make sure that the src and dest are actually in the graph.
     * If they're not, then we just return null. otherwise, we construct the hashmaps to use for searching.
     * The distances HashMap basically stores the shortest known distance from the source to a query node. So
     * distances.get(node) will be the shortest distance in double form from our src to that node.
     * The bestKnown HashMap stores where the shortest distance from a node came from. so if src -> a -> b is
     * the shortest path, then bestKnown.get("b") would be a, and bestKnown.get("a") would be src.
     * We also use a priority queue to make sure that the shortest known path to any node currently can be polled
     * from the top of the queue. Then, as long as the queue has nodes in it, we poll from the top (solidifying that
     * node). If the solidified node is the destination node, then we know that is the shortest path. Otherwise,
     * we check if going through the solidified node decreases the distance of any connections, and if it does,
     * we can re-add it to the queue with the new value, set the distance accordingly, and update the bestKnown path
     * for that node to the solidified node. Finally, once our queue is empty, we check if the distance to the destination
     * is still infinity, as if it is, that means that the dest has no connection route possible to src. If it isn't,
     * then we can return the proper path.
     * @param graph the graph we are searching from
     * @param src the source of the path we are looking to find the minimum of
     * @param dest the destination of the path we are looking to find the minimum of
     * @return Path with the source, destination, the graph we searched from, and the stored path
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
        PriorityQueue<String> queue = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        queue.add(src);
        while (!queue.isEmpty()) {
            String solidifiedNode = queue.poll();
            if (solidifiedNode.equals(dest)) {
                break;
            }
            for (String neighbor : graph.edges(solidifiedNode)) {
                double newDistance = graph.weight(solidifiedNode, neighbor) + distances.get(solidifiedNode);
                if (newDistance < distances.get(neighbor)) {
                    queue.remove(neighbor);
                    distances.put(neighbor, newDistance);
                    bestKnown.put(neighbor, solidifiedNode);
                    queue.add(neighbor);
                }
            }
        }
        if(distances.get(dest) == Double.POSITIVE_INFINITY) {
            return new Path(src, dest, Double.POSITIVE_INFINITY, graph, new String[0]);
        }
        LinkedList<String> path = new LinkedList<>();
        for(String step = dest; step != null; step = bestKnown.get(step)) {
            path.addFirst(step);
        }
        return new Path(src, dest, distances.get(dest), graph, path.toArray(new String[0]));

    }
}
