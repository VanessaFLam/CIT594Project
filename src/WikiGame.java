import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;

public class WikiGame implements IWikiGame {
    // =============================================================================
    // = INSTANCE VARIABLES
    // =============================================================================

    Graph g;
    int numOfNodes;
    Map<Integer, Integer> wikiIDtoNodeID;
    Map<String, Integer> articleNametoNodeID;
    
    public static final Integer DefualtEdgeWeight = 1;
    
    // =============================================================================
    // = CONSTRUCTOR
    // =============================================================================
    public WikiGame() {
        g = new GraphL();
        numOfNodes = 0;
        wikiIDtoNodeID = new HashMap<Integer, Integer>();
        articleNametoNodeID = new HashMap<String, Integer>();
    }

    // =============================================================================
    // = METHODS
    // =============================================================================
    
    public int mapWikiIDtoNodeID(String filePath) {
        
        final String logPath = "data/logs/map_ids_error_log.txt";
        
    	try {
        
    	    // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));

            // Read each line in the file - each line is of form "<node_id> <page_id>".
            // Add mapping to file if successful, log error if not.
            String line = br.readLine();
            while (line != null) {
                
                try {
                    String[] toks = line.split("\\s+");	            
                    int wikiID = Integer.parseInt(toks[1]);
                    int nodeID = Integer.parseInt(toks[0]);	            
                    wikiIDtoNodeID.put(wikiID, nodeID);                    
                } catch (Exception e) {
                    bw.write("File parse error - line: " + line + "\n");
                }	            
	            line = br.readLine();
            
            }
	        
            // Close reader/writer.
            br.close();
            bw.close();
            
            // Return value is size of hashmap = number of mappings.
            return wikiIDtoNodeID.size();
                
    	} catch (IOException e) {
    	    e.printStackTrace();
    	    return -1;        	
    	}
    
    }
    
    public int mapArticleNametoNodeID(String filePath) {

        final String logPath = "data/logs/map_names_error_log.txt";

        try {

            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));

            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));

            // Read each line in the file - each line is of form "<page_id> <title>".
            // Add mapping to file if successful, log error if not.
            String line = br.readLine(); // skip header
            line = br.readLine();
            while (line != null) {

                try {
                    // Get wiki_id and name.
                    String[] toks = line.split("\\s+");             
                    int page_id = Integer.parseInt(toks[0]);
                    String article_name = toks[1];             
                    // Use wiki_id to get node_id from wikiIDtoNodeID map, then add.
                    articleNametoNodeID.put(article_name, wikiIDtoNodeID.get(page_id));
                } catch (Exception e) {
                    bw.write("File parse error - line: " + line + "\n");
                    bw.write("Error type: " + e.getClass() + "\n");
                }
                
                line = br.readLine();

            }

            // Close reader/writer.
            br.close();
            bw.close();

            // Return value is size of hashmap = number of mappings.
            return articleNametoNodeID.size();

        } catch (IOException e) {
            e.printStackTrace();
            return -1;          
        }
        
    }            
    
    @Override
    public int loadGraphFromDataSet(String filePath) {
             
        final String logPath = "data/logs/load_graph_error_log.txt";
        final boolean print_progress = true;
        
        try {
            
            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));

            // Get number of nodes from header and use to initialize graph.

            String line = br.readLine();
            String[] toks = line.split("\\s+");
            g.init(Integer.parseInt(toks[0]));
            
            // Get number of edges for tracking progress.
            int total_edges = Integer.parseInt(toks[1]);
            int edges_processed = 0;
            
            // Read remaining lines and process as edges.
            line = br.readLine();
            while (line != null) {

                // Split into tokens and parse values.
                toks = line.split("\\s+");
                System.out.println(Arrays.toString(toks));

                try {

                    // Get nodes.
                    int source_node_id = wikiIDtoNodeID.get(Integer.parseInt(toks[0]));
                    int dest_node_id   = wikiIDtoNodeID.get(Integer.parseInt(toks[1]));
                    INode sourceNode = (INode) g.getNode(source_node_id);
                    INode destNode   = (INode) g.getNode(dest_node_id);
                    
                    // Add edge to graph with weight.
                    int edge_wght   = Integer.parseInt(toks[2]);
                    g.addEdge(source_node_id, dest_node_id, edge_wght);
                    
                    // Update nodes.
                    
                    
                    
                    sourceNode.incrementOutdegree();
                    destNode.incrementIndegree();
                    edges_processed++;
//                    if (print_progress && ((edges_processed % 10000) == 0)) {
                    if (print_progress) {
                        double pct = (float) edges_processed / (float) total_edges;
                        System.out.println("\t\t" + edges_processed + " of " + total_edges
                                + " processed (" + pct + "%)...");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("File parse error - line: " + line);
                    System.out.println("Error type: " + e.getClass());
                    bw.write("File parse error - line: " + line + "\n");
                    bw.write("Error type: " + e.getClass() + "\n");
                }

                // Go to next line.
                line = br.readLine();

            }
            
            // Close reader/writer.
            br.close();
            bw.close();
            
            // Return number of nodes.
            return g.nodeCount();
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1; 
        }
        
    }

    @Override
    public Collection<Integer> findPath(int source, int destination, String param) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Collection<Integer> shortestPathHops(int source, int destination){
        
        // ========== VALIDATE INPUT ===========
        
        // validate source and destination
        if (source > numOfNodes || source < 1 || destination > numOfNodes || destination < 1) {
            return null;
        }
                
        // ========== RUN BFS ===========
        
        // create pred array and initialize to -1
        // create visited array and initialize to false
        // create shortest array and initialize to infinity
        int[] predArray = new int[g.nodeCount()];
        boolean[] visitedArray = new boolean[g.nodeCount()];
        int[] shortestArray = new int[g.nodeCount()];
        for (int i = 0; i < predArray.length; i++) {
            predArray[i] = -1;
            visitedArray[i] = false;
            shortestArray[i] = INFINITY;
        }

        // create linked-list to be our queue
        LinkedList<Integer> q = new LinkedList<Integer>();

        // add source to queue and mark as visited
        q.offer(source);
        visitedArray[source] = true;
        
        // BFS while loop
        int u,v;
        while (q.size() > 0) {

            // remove node at front of queue
            v = q.poll();

            // call neighbors and iterate over them
            int[] neighbors = g.neighbors(v);               //TODO: decide what g.neighbors is returning - collection or array
            for (int i = 0; i < neighbors.length; i++) {
                u = neighbors[i];
                // if u was not already visited
                if (!visitedArray[u]) {
                    // enqueue it; set pred, visited, and shortest values
                    q.offer(u);
                    predArray[u] = v;
                    visitedArray[u] = true;
                    shortestArray[u] = shortestArray[v] + 1;
                }
            }
        }
        
        // ========== PREPARE COLLECTION TO RETURN ===========
        
        // create the collection to return
        LinkedList<Integer> pathNodes = new LinkedList<Integer>();

        // since we ran BFS starting at source, if dest has no pred, there is no path
        if (predArray[destination] != -1) {
            // otherwise trace back the preds one at a time
            int currNode = destination;
            while (currNode != -1) {
                pathNodes.offerFirst(currNode);
                currNode = predArray[currNode];
            }
        }
        
        // return the collection
        return pathNodes;

    }
    
    @Override
    public Collection<Integer> findPathHops(int source, int destination) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Integer> findPathIndegree(int source, int destination) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Integer> findPathOutdegree(int source, int destination) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Collection<Integer> findPathSection(int source, int destination) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int countConnectedComponents() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    //general dijkstras
    
    
//    public Collection<Integer> shortestPathIndegree(int source, int destination){
//        
//        // ========== VALIDATE INPUT ===========
//        
//        // validate source and destination
//        if (source > numOfNodes || source < 1 || destination > numOfNodes || destination < 1) {
//            return null;
//        }
//        
//        // ========== CREATE THE PRIORITY QUEUE FOR DIJKSTRA'S ===========
//        
//        // Create the appropriate comparator
//        Comparator<Object> comp = new Comparator<Object>() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                
//                // cast object to nodes
//                INode node1 = (INode) o1;
//                INode node2 = (INode) o2;
//
//                // find the difference in the indegree for each node
//                int indegreeDiff = node1.getIndegree() - node2.getIndegree();
//
//                // if they have the same indegree, sort by article id
//                if (indegreeDiff == 0) {
//                    return node1.getWikiId() - node2.getWikiId();
//                }
//                
//                // otherwise return the difference of the indegrees
//                return indegreeDiff;
//            }
//        };
//        
//        // create a priority Queue of INodes
//        PriorityQueue<INode> q = new PriorityQueue<INode>(numOfNodes, comp);
//        
//        // insert all of the vertices into Q
//        // TODO: determine best way to enter all nodes - can we have graph return collection of all node objects?
//        for (int i = 1; i < g.nodeCount(); i++) {
//            // get node from graph, cast to INode, and add to q
//            q.add((INode)g.getNode(i));
//        }
//                       
//        // ========== RUN DIJKSTRA'S ===========
//        
//        // create pred array and initialize to -1
//        // create visited array and initialize to false
//        // create shortest array and initialize to infinity
//        int[] predArray = new int[g.nodeCount()];
//        boolean[] visitedArray = new boolean[g.nodeCount()];
//        int[] shortestArray = new int[g.nodeCount()];
//        for (int i = 0; i < predArray.length; i++) {
//            predArray[i] = -1;
//            visitedArray[i] = false;
//            shortestArray[i] = INFINITY;
//        }
//        
//        // set shortest of source to 0
//        shortestArray[source] = 0;
//       
//        // Dijkstra while loop
//        int vInd,uInd;
//        INode vNode,uNode;
//        while (q.size() > 0) {
//
//            // remove node at front of queue
//            vNode = q.poll();
//            vInd = vNode.getIndex();
//
//            // call neighbors and iterate over them
//            int[] neighbors = g.neighbors(vInd);               //TODO: decide what g.neighbors is returning - collection or array
//            for (int i = 0; i < neighbors.length; i++) {
//                uInd = neighbors[i];
//                uNode = (INode) g.getNode(uInd);
//                // RELAX(v,u)
//                if (shortestArray[uInd] > shortestArray[vInd] + uNode.getIndegree() ) {
//                    shortestArray[uInd] = shortestArray[vInd] + uNode.getIndegree(); 
//                    predArray[uInd] = vInd;
//                }
//            }
//        }
//        
//        // ========== PREPARE COLLECTION TO RETURN ===========
//        
//        // create the collection to return
//        LinkedList<Integer> pathNodes = new LinkedList<Integer>();
//
//        // since we ran BFS starting at source, if dest has no pred, there is no path
//        if (predArray[destination] != -1) {
//            // otherwise trace back the preds one at a time
//            int currNode = destination;
//            while (currNode != -1) {
//                pathNodes.offerFirst(currNode);
//                currNode = predArray[currNode];
//            }
//        }
//        
//        // return the collection
//        return pathNodes;
//
//    }
    
    public Collection<Integer> shortestPathIndegree(int source, int destination){
        
        // ========== VALIDATE INPUT ===========
        
        // validate source and destination
        if (source > numOfNodes || source < 1 || destination > numOfNodes || destination < 1) {
            return null;
        }
                
        // ========== RUN DIJKSTRA'S ===========
        
        // create pred array and initialize to -1
        // create visited array and initialize to false
        // create shortest array and initialize to infinity
        int[] predArray = new int[g.nodeCount()];
        boolean[] visitedArray = new boolean[g.nodeCount()];
        int[] shortestArray = new int[g.nodeCount()];
        for (int i = 0; i < predArray.length; i++) {
            predArray[i] = -1;
            visitedArray[i] = false;
            shortestArray[i] = INFINITY;
        }
        
        // set shortest of source to 0
        shortestArray[source] = 0;
        
        // Dijkstra while loop
        int v,u;
        INode uNode;
        for (int i=0; i < g.nodeCount(); i++) {
            
            // get the vertex with the minimum "shortest"
            v = minVertex(shortestArray, visitedArray);
            visitedArray[v] = true;
            if(shortestArray[v] ==  INFINITY) {
                break;
            }
            
            // call neighbors and iterate over them
            int[] neighbors = g.neighbors(v);               //TODO: decide what g.neighbors is returning - collection or array
            for (int j = 0; j < neighbors.length; j++) {
                u = neighbors[j];
                uNode = (INode) g.getNode(u);
                // RELAX(v,u)
                if (shortestArray[u] > shortestArray[v] + uNode.getIndegree() ) {
                    shortestArray[u] = shortestArray[v] + uNode.getIndegree(); 
                    predArray[u] = v;
                }
            }
        }
        
        // ========== PREPARE COLLECTION TO RETURN ===========
        
        // create the collection to return
        LinkedList<Integer> pathNodes = new LinkedList<Integer>();
        
        // since we ran Dijkstra's starting at source, if dest has no pred, there is no path
        if (predArray[destination] != -1) {
            // otherwise trace back the preds one at a time
            int currNode = destination;
            while (currNode != -1) {
                pathNodes.offerFirst(currNode);
                currNode = predArray[currNode];
            }
        }
        
        // return the collection
        return pathNodes;
        
    }
    
    public Collection<Integer> shortestPathHumanTime(int source, int destination){
        
        // ========== VALIDATE INPUT ===========
        
        // validate source and destination
        if (source > numOfNodes || source < 1 || destination > numOfNodes || destination < 1) {
            return null;
        }
                
        // ========== RUN DIJKSTRA'S ===========
        
        // create pred array and initialize to -1
        // create visited array and initialize to false
        // create shortest array and initialize to infinity
        int[] predArray = new int[g.nodeCount()];
        boolean[] visitedArray = new boolean[g.nodeCount()];
        int[] shortestArray = new int[g.nodeCount()];
        for (int i = 0; i < predArray.length; i++) {
            predArray[i] = -1;
            visitedArray[i] = false;
            shortestArray[i] = INFINITY;
        }
        
        // set shortest of source to 0
        shortestArray[source] = 0;
        
        // Dijkstra while loop
        int v,u;
        INode uNode;
        for (int i=0; i < g.nodeCount(); i++) {
            
            // get the vertex with the minimum "shortest"
            v = minVertex(shortestArray, visitedArray);
            visitedArray[v] = true;
            if(shortestArray[v] ==  INFINITY) {
                break;
            }
            
            // call neighbors and iterate over them
            int[] neighbors = g.neighbors(v);               //TODO: decide what g.neighbors is returning - collection or array
            for (int j = 0; j < neighbors.length; j++) {
                u = neighbors[j];
                uNode = (INode) g.getNode(u);
                // RELAX(v,u)
                if (shortestArray[u] > shortestArray[v] + g.weight(v, u) ) {
                    shortestArray[u] = shortestArray[v] + g.weight(v, u); 
                    predArray[u] = v;
                }
            }
        }
        
        // ========== PREPARE COLLECTION TO RETURN ===========
        
        // create the collection to return
        LinkedList<Integer> pathNodes = new LinkedList<Integer>();
        
        // since we ran Dijkstra's starting at source, if dest has no pred, there is no path
        if (predArray[destination] != -1) {
            // otherwise trace back the preds one at a time
            int currNode = destination;
            while (currNode != -1) {
                pathNodes.offerFirst(currNode);
                currNode = predArray[currNode];
            }
        }
        
        // return the collection
        return pathNodes;
        
    }
    
    /**
     * method to run Dijkstra's algorithm to find the shortest path between the source and destination articles
     * using the specified weight-type as edge weights in the algorithm
     *  
     * @param source 
     * @param destination
     * @param weightType the type of weight to be considered:
     * <ul>
     * <li>0 - how far down the page the link is
     * <li>1 - the indegree of the destination article
     * <li>2 - the outdegree of the destination article
     * </ul>
     * @return (Collection&ltInteger&gt) collection of node indices in the path from source to destination
     */
    public Collection<Integer> runDijsktra(int source, int destination, int weightType){
        
        // ========== VALIDATE INPUT ===========
        
        // validate source and destination
        if (source > numOfNodes || source < 1 || destination > numOfNodes || destination < 1) {
            return null;
        }
                
        // ========== RUN DIJKSTRA'S ===========
        
        // create pred array and initialize to -1
        // create visited array and initialize to false
        // create shortest array and initialize to infinity
        int[] predArray = new int[g.nodeCount()];
        boolean[] visitedArray = new boolean[g.nodeCount()];
        int[] shortestArray = new int[g.nodeCount()];
        for (int i = 0; i < predArray.length; i++) {
            predArray[i] = -1;
            visitedArray[i] = false;
            shortestArray[i] = INFINITY;
        }
        
        // set shortest of source to 0
        shortestArray[source] = 0;
        
        // Dijkstra while loop
        int v,u;
        INode uNode;
        for (int i=0; i < g.nodeCount(); i++) {
            
            // get the vertex with the minimum "shortest"
            v = minVertex(shortestArray, visitedArray);
            visitedArray[v] = true;
            if(shortestArray[v] ==  INFINITY) {
                break;
            }
            
            // call neighbors and iterate over them
            int[] neighbors = g.neighbors(v);               //TODO: decide what g.neighbors is returning - collection or array
            for (int j = 0; j < neighbors.length; j++) {
                u = neighbors[j];
                uNode = (INode) g.getNode(u);
                
                // get weight - weightType is input parameter to runDijkstra function
                int weight = determineWeight(v, u, weightType);

                // RELAX(v,u) with weight determined above
                if (shortestArray[u] > shortestArray[v] + weight) {
                    shortestArray[u] = shortestArray[v] + weight; 
                    predArray[u] = v;
                }
            }
        }
        
        // ========== PREPARE COLLECTION TO RETURN ===========
        
        // create the collection to return
        LinkedList<Integer> pathNodes = new LinkedList<Integer>();
        
        // since we ran Dijkstra's starting at source, if dest has no pred, there is no path
        if (predArray[destination] != -1) {
            // otherwise trace back the preds one at a time
            int currNode = destination;
            while (currNode != -1) {
                pathNodes.offerFirst(currNode);
                currNode = predArray[currNode];
            }
        }
        
        // return the collection
        return pathNodes;
        
    }
    
    private int determineWeight(int v, int u, int weightType) {
        int weight = 0;
        INode uNode = (INode) g.getNode(u);
        
        switch(weightType) {
            case 0:     weight = g.weight(v, u);
                        break;
            case 1:     weight = uNode.getIndegree();
                        break;
            case 2:     weight = uNode.getOutdegree();
                        break;
            default:    weight = g.weight(v, u);
                        break;
        }
        return weight;
    }
    
    // =============================================================================
    // = HELPER METHODS
    // =============================================================================
    
    // find the unvisited vertex with the minimum "shortest"
    private int minVertex(int[] shortest, boolean[] visited) {
        
        // initialize v to 0
        int v = 0;  
        
        // set v to the first unvisited vertex;
        for (int i=0; i < g.nodeCount(); i++) {
            if (!visited[i]) { 
                v = i; 
                break; 
            }
        }
        
        // now find the smallest
        for (int i=0; i < g.nodeCount(); i++) {
            if ((!visited[i]) && (shortest[i] < shortest[v])) {
                v = i;
            }
        }
        
        // return v
        return v;
    }
    
    
    /**
     * Dijkstra Helper
     */
    
//    static void DijkstraPQ(Graph G, int s, int[] D) {
//    	  int v;                                 // The current vertex
//    	  KVPair[] E = new KVPair[G.edgeCount()];        // Heap for edges
//    	  E[0] = new KVPair(0, s);               // Initial vertex
//    	  MinHeap H = new MinHeap(E, 1, G.edgeCount());
//    	  for (int i=0; i<G.nodeCount(); i++)            // Initialize distance
//    	    D[i] = INFINITY;
//    	  D[s] = 0;
//    	  for (int i=0; i<G.nodeCount(); i++) {          // For each vertex
//    	    do { KVPair temp = (KVPair)(H.removemin());
//    	         if (temp == null) return;       // Unreachable nodes exist
//    	         v = (Integer)temp.value(); } // Get position
//    	      while (G.getNode(v) == VISITED);
//    	    G.setValue(v, VISITED);
//    	    if (D[v] == INFINITY) return;        // Unreachable
//    	    int[] nList = G.neighbors(v);
//    	    for (int j=0; j<nList.length; j++) {
//    	      int w = nList[j];
//    	      if (D[w] > (D[v] + G.weight(v, w))) { // Update D
//    	        D[w] = D[v] + G.weight(v, w);
//    	        H.insert(new KVPair(D[w], w));
//    	      }
//    	    }
//    	  }
//    	}


    
    

}
