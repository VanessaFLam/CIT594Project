import java.util.Collection;

public interface IWikiGame {
    
    // for use in graph algorithms
    public static final int INFINITY = Integer.MAX_VALUE;
    
    /**
     * This takes in a file that has wiki ID to Node ID mappings. 
     * It will update the instance variable (a Map) wikiIDtoNodeID.
     * 
     * @param filePath - id_map
     * @return number of IDs mapped
     */
    public int mapWikiIDtoNodeID(String filePath);
    
    /**
     * This takes in a file that has Article Name to Node ID mappings. 
     * It will update the instance variable (a Map) articleNametoNodeID.
     * 
     * @param filePath - page_ids
     * @return number of Articles mapped
     */
    public int mapArticleNametoNodeID(String filePath);
    
    /**
     * Create a graph representation of the dataset. The format for the
     * input file should be as follows: the first line contains two numbers
     * the first is the number of nodes in the graph, the second is the 
     * number of edges; each subsequent line represents an edge with the vertices
     * being the first two numbers and any relevant edge-weights following.
     * 
     * @param filePath - graph_file
     * @return (int) the number of entries (nodes) in the dataset (graph)
     */
    public int loadGraphFromDataSet(String filePath);
    
    /**
     * Returns the shortest path between two articles; the final path includes 
     * the source and destination articles. This will choose between different weight
     * types to find the shortest path 
     * 
     * @param source      - the id of the origin article
     * @param destination - the id of the destination article
     * @param type 		  - the type of weight used to find the shortest path, for example:
     * <ul>
     * <li> "hops" - assuming each link has the same weight
     * <li> "indegree" - the indegree of the destination article
     * <li> "outdegree" - the outdegree of the destination article
     * <li> "section" - how far down the page the link is
     * </ul>
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> findPath(int source, int destination, String type);
    
    /**
     * This is a weight type to find the shortest path that makes all weights = 1. 
     * This is implemented using BFS 
     * 
     * @param source
     * @param destination
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> findPathHops(int source, int destination);
    
    /**
     * This is a weight type to find the shortest path that weights edges
     * based on the indegree of the destination node. The idea is to make it
     * more expensive to go to nodes that are frequently linked.
     * This is implemented using Dijkstra 
     * 
     * @param source
     * @param destination
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> findPathIndegree(int source, int destination);
    
    /**
     * This is a weight type to find the shortest path that weights edges
     * based on the outdegree of the destination node. The idea is to make
     * it more expensive to go to nodes that have a lot of links on them.
     * This is implemented using Dijkstra 
     * 
     * @param source
     * @param destination
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> findPathOutdegree(int source, int destination);
    
    /**
     * This is a weight type to find the shortest path that weights edges
     * based on the distance of the link from the page. The idea is to simulate
     * human reading, where scrolling further down the page takes more time.
     * This is implemented using Dijkstra 
     * 
     * @param source
     * @param destination
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> findPathSection(int source, int destination);
    
    /**
     * Runs DFS on the graph to count the number of connected components and
     * whether or not Wikipedia is completely connected
     * @return (int) number of connected components
     */
    int countConnectedComponents();
           
}
