/**
 * The way that i deal with negative edge weights in the search is that, in DWGraph, before selecting a search algorithm,
 * we scan for any negative edge weights. If we are searching all pairs shortest paths and a negative edge weight exists,
 * then we return null (since my all pairs shortest paths isn't built to handle negative loops. Then, if we are searching all pairs
 * shortest path. and haven't found negative edge weights, we can run Floyd Warshall. Then, if there are negative edge weights, we
 * use Bellman ford, and if we don't, we use Dijkstras. The benefits of this approach is that it is very quick and simple to select a search
 * algorithm, and it selects accurately to produce correct results in reasonable runtime. However, one downside is that with negative edge weights AND
 * searching for all pairs shortest paths, there isn't even an attempt at it. It could be that there is a graph that has negative edge weights but no negative edge weight
 * cycles, in which case this attempt at the search algorithmm would return null even if valid shortest paths DO exist. That's one improvement I could see myself doing in the future, to handle
 * those kinds of scenarios. Additionally, I will say that the way that I return the all pairs shortest paths is pretty weird, as I don't return the paths themselves
 * and instead return each path in the format a -> b: x, where a is src, b is dest, and x is shortest distance. I wish i found a better way to return multiple actual paths at the same time, but this
 * was the best I could come up with. More in-depth descriptions of how i implemented each algorithm are at the top of each of their respective classes.
 */
import java.util.*;
public interface Search {
    Path search(DWGraph graph, String src, String dest);
    record Path(String src, String dest, double cost, DWGraph graph, String[] path){}

}
