import java.util.*;
/**
*interface for AsjList and AdjMatrix to inherit from, so that DWGraph can make calls indiscriminantly
 */
public interface Digraph {
    boolean add(String key);
    boolean add(String src, String dest, Double weight);
    String delete(String key);
    Double delete(String src, String dest);
    ArrayList<String> nodes();
    ArrayList<String> edges(String key);
    Double weight(String src, String dest);
    double density();
    double density(String key);
    int size();
    String toString();
    String toJSON();
}
