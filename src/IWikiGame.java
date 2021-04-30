import java.util.Collection;

public interface IWikiGame {
    
    public static final int INFINITY = Integer.MAX_VALUE;
    
    /**
     * Create a graph representation of the dataset. The format for the
     * input file should be as follows: the first line contains two numbers
     * the first is the number of nodes in the graph, the second is the 
     * number of edges; each subsequent line represents an edge with the vertices
     * being the first two numbers and any relavent edge-weights following.
     * 
     * @param filePath the path of the data
     * @return (int) the number of entries (nodes) in the dataset (graph)
     */
    int loadGraphFromDataSet(String filePath);
    
    /**
     * Returns the shortest path between two articles; the final path includes 
     * the source and destination articles
     * 
     * @param source      - the id of the origin article
     * @param destination - the id of the destination article
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    Collection<Integer> findPath(int source, int destination);
}
