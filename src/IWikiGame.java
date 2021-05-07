import java.util.Collection;

public interface IWikiGame {
    
    // for use in graph algorithms
    public static final int INFINITY = Integer.MAX_VALUE;    
    
    /**
     * Create a graph representation of the dataset based off of two files:
     * <ol>
     *   <li> A "graph file" (.mtx). This file has a header row of form <# articles> <# links> 
     *   (i.e., <# vertices> <# edges>) and data rows of the form 
     *   "< wiki_id_from > < wiki_id_to > < link_section >" (i.e., there is a link from the article given 
     *   by wiki_id_from to the article given by wiki_id_to in the link_section-th section of 
     *   the former).
     *   
     *   <li> An "id map file" (.txt). Each row in this file is of form 
     *   "< node_id > < page_id > < article_title >", so it can be used to translate between these forms
     *   of identifying an article. There is a header row giving the number of nodes.
     * </ol>
     * @param idMapFilePath Path to id mapping file.
     * @param graphFilePath Path to graph edges file.
     */
    public void loadGraph(String idMapFilePath, String graphFilePath);
    
    /**
     * Load nodes from an "id map file" (.txt). Each row in this file is of form 
     *   "< node_id > < page_id > < article_title >". There is a header row giving the number of nodes. This method
     *   adds each of the nodes to the graph and also updates the wikiIDtoNodeID and articleNametoNodeID maps.
     *   
     * @param filePath Path to id mapping file
     * @return (int) number of nodes in the graph
     */
    public int loadNodes(String filePath);
    
    /**
     * Load edges from a "graph file" (.mtx). This file has a header row of form <# articles> <# links> 
     *   (i.e., <# vertices> <# edges>) and data rows of the form 
     *   "< wiki_id_from > < wiki_id_to > < link_section >" (i.e., there is a link from the article given 
     *   by wiki_id_from to the article given by wiki_id_to in the link_section-th section of 
     *   the former).
     * @param filePath Path to graph edges file
     * @return (int) number of edges in the graph
     */
    public int loadEdges(String filePath);
    
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
     * @return (Collection< Integer >) collection of node indices in the path from source to destination
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
           
}
