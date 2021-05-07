import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.AbstractMap;
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
    Map<String, Collection<Integer>> articleNametoNodeIDs;
   

	public static final Integer DefaultEdgeWeight = 1;
    public static boolean printProgress = true;
    
    // =============================================================================
    // = CONSTRUCTOR
    // =============================================================================
    
    public WikiGame() {
        g = new GraphL();
        numOfNodes = 0;
        wikiIDtoNodeID = new HashMap<Integer, Integer>();
        articleNametoNodeIDs = new HashMap<String, Collection<Integer>>();
    }

    // =============================================================================
    // = METHODS
    // =============================================================================
    
    @Override
    public void loadGraph(String id_map_file, String graph_file) {
        
        loadNodes(id_map_file);
        loadEdges(graph_file);
        
    }    
    
    public int loadNodes(String filePath) {
        
        final String logPath = "data/logs/load_nodes_error_log.txt";
        
        try {
        
            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));
    
            // Header tells number of nodes. Use this to call g.init.
            String line = br.readLine();
            numOfNodes = Integer.parseInt(line);
            g.init(numOfNodes);
            int counter = 0;
            
            if (printProgress) {
                System.out.println("\t\tLoading node data. " + numOfNodes + " nodes in graph ...");
            }
            
            // Read each line in the file.
            // Each line is of form "<node_id> <page_id> <article_title>".
            while ((line = br.readLine()) != null) {
                
                // Try to parse to "<node_id> <page_id> <article_title>". Log error if not.
                try {
                    
                    // Get tokens.
                    String[] toks = line.split("\t");         
                    int nodeID = Integer.parseInt(toks[0]);
                    int wikiID = Integer.parseInt(toks[1]);
                    String articleName = toks[2];
                    String lowerArticleName = articleName.toLowerCase();
                    
                    // Initialize node in graph.
                    INode n = new Node(nodeID, wikiID, lowerArticleName);
                    g.setNode(nodeID, n);                    
                    
                    // Add to maps.
                    wikiIDtoNodeID.put(wikiID, nodeID);
                    if (!articleNametoNodeIDs.containsKey(lowerArticleName)) {
                        articleNametoNodeIDs.put(lowerArticleName, new ArrayList<Integer>());
                    }
                    articleNametoNodeIDs.get(lowerArticleName).add(nodeID);

                    counter++;
                    if (printProgress) {
                        if ((counter % (numOfNodes / 20)) == 0) {
                            System.out.println("\t\t\t" + counter + " nodes processed (" 
                                    + Math.round((float) (100 * counter) / numOfNodes) + "%) ...");
                        }
                    }
                
                } catch (Exception e) {
                
                    bw.write("File parse error: " + line + "\n");
                
                }               
            
            }
            
            // Close reader/writer.
            br.close();
            bw.close();
            
            // Return summary of key values in string.
            String result = numOfNodes + " " + wikiIDtoNodeID.size() 
                                                        + " " + articleNametoNodeIDs.size();
            return g.nodeCount();
            
        } catch (IOException e) {
            e.printStackTrace();
            return -1;          
        }
        
    }

    public int loadEdges(String filePath) {
        
        final String logPath = "data/logs/load_edges_error_log.txt";

        try {
        
            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));

            // Header tells number of nodes and edges expected. Can skip here.
            String line = br.readLine();
            long numOfEdges = Long.parseLong(line.split("\t")[1]);
            long counter = 0;
            
            if (printProgress) {
                System.out.println("\t\tLoading edge data. " + numOfEdges + " edges in graph ...");
            }
            
            // Read each line in the file.
            // Each line is of form "<wiki_id_from> <wiki_id_to> <link_section>".
            while ((line = br.readLine()) != null) {
                
                // Try to parse to "<wiki_id_from> <wiki_id_to> <link_section>". Log error if not.
                try {
                    
                    // Get tokens.
                    String[] toks = line.split("\t");         
                    int wikiID_from = Integer.parseInt(toks[0]);
                    int wikiID_to   = Integer.parseInt(toks[1]);
                    int edgeWeight  = Integer.parseInt(toks[2]);
                    
                    // Get node ids.
                    int nodeID_from = wikiIDtoNodeID.get(wikiID_from);
                    int nodeID_to   = wikiIDtoNodeID.get(wikiID_to);                    
                    
                    // Add edge.
                    g.addEdge(nodeID_from, nodeID_to, edgeWeight);
                    counter++;
                    INode nodeIDFrom = (INode) g.getNode(nodeID_from);
                    INode nodeIDTo = (INode) g.getNode(nodeID_to);
                    nodeIDFrom.incrementOutdegree();
                    nodeIDTo.incrementIndegree();
                    if (printProgress) {
                        if ((counter % (numOfEdges / 20)) == 0) {
                            System.out.println("\t\t\t" + counter + " edges processed (" 
                                    + Math.round((double) (100 * counter) / numOfEdges) + "%) ...");
                        }
                    }
                
                } catch (Exception e) {
                
                    bw.write("File parse error: " + line + "\n");
                
                }               
            
            }
            
            // Close reader/writer.
            br.close();
            bw.close();
            
            // Return edge count.
            return g.edgeCount();
                
        } catch (IOException e) {
            e.printStackTrace();
            return -1;          
        }        
        
    }    
       
	@Override
	public Collection<Integer> findPath(int source, int destination, String param) {
		String lowerParam = param.toLowerCase(); 
		Collection<Integer> ret = new LinkedList<Integer>();
		switch (lowerParam) {
		case "hops": 
			ret = findPathHops(source, destination);
			break;
		case "indegree": 
			ret = findPathIndegree(source, destination);
			break;	
		case "outdegree": 
			ret = findPathOutdegree(source, destination);
			break;
		case "section": 
			ret = findPathSection(source, destination);
			break;
		default:
			ret = findPathHops(source, destination);
			break;
		}
		return ret;
	}

	@Override
	public Collection<Integer> findPathHops(int source, int destination) {

		// ========== VALIDATE INPUT ===========

		// validate source and destination
		if (source >= numOfNodes || source < 0 || destination >= numOfNodes || destination < 0) {
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
		int u, v;
		while (q.size() > 0) {

			// remove node at front of queue
			v = q.poll();

			
			// call neighbors and iterate over them
			int[] neighbors = g.neighbors(v); // TODO: decide what g.neighbors is returning - collection or array
			for (int i = 0; i < neighbors.length; i++) {
				u = neighbors[i];
				// if u was not already visited
				if (!visitedArray[u]) {
					// enqueue it; set pred, visited, and shortest values
					q.offer(u);
					predArray[u] = v;
					visitedArray[u] = true;
					shortestArray[u] = shortestArray[v] + 1;
					if (u == destination) break;
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
	public Collection<Integer> findPathIndegree(int source, int destination) {
		
		return dijkstra(source, destination, 1);
	}

	@Override
	public Collection<Integer> findPathOutdegree(int source, int destination) {
		return dijkstra(source, destination, 2);
	}

	@Override
	public Collection<Integer> findPathSection(int source, int destination) {
		return dijkstra(source, destination, 0);
	}

	@Override
	public int countConnectedComponents() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	/**
	 * method to run Dijkstra's algorithm to find the shortest path between the
	 * source and destination articles using the specified weight-type as edge
	 * weights in the algorithm
	 * 
	 * @param source
	 * @param destination
	 * @param weightType  the type of weight to be considered:
	 *                    <ul>
	 *                    <li>0 - how far down the page the link is
	 *                    <li>1 - the indegree of the destination article
	 *                    <li>2 - the outdegree of the destination article
	 *                    </ul>
	 * @return (Collection&ltInteger&gt) collection of node indices in the path from
	 *         source to destination
	 */

	public Collection<Integer> dijkstra(int source, int destination, int weightType) {
        
	 	// ========== COMPARATOR ===========
	 //shortest, nodeIndex
	   Comparator<Entry<Integer, Integer>> comp = new Comparator<Entry<Integer, Integer>>() {
	   @Override
	   public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
	
	       // find the difference in the indegree for each node
	       int shortestDiff = o1.getKey() - o2.getKey();
	
	       // if they have the same indegree, sort by article id
	       if (shortestDiff == 0) {
	           return o1.getValue() - o2.getValue();
	       }
	       
	       // otherwise return the difference of the indegrees
	       return shortestDiff;
		   }
		};
	 
        // ========== VALIDATE INPUT ===========
        
        // validate source and destination
        if (source >= numOfNodes || source < 0 || destination >= numOfNodes || destination < 0) {
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
        
        PriorityQueue<Entry<Integer, Integer>> q = new PriorityQueue<Entry<Integer, Integer>>(numOfNodes, comp);
        AbstractMap.SimpleEntry<Integer, Integer> s = new AbstractMap.SimpleEntry<Integer, Integer>(0, source);
        q.offer(s);
        
        int v = 0;
        for (int i = 0; i < g.nodeCount(); i++) {
        	
        	do {
        		Entry<Integer, Integer> t = (Map.Entry<Integer, Integer>) q.poll();
        		if (t == null) break;
        		v = t.getValue();
        	} while (visitedArray[v]); 	
	       visitedArray[v] = true;
	       
	       if (v == destination) break;
	       
	       if(shortestArray[v] == INFINITY) break;
	       int[] nList = g.neighbors(v);
	       
	       for (int j = 0; j < nList.length; j++) {
	    	   int w = nList[j];
	    	   int weight = determineWeight(v, w, weightType);
	    	   if (shortestArray[w] > (shortestArray[v] + weight)) {
	    		   shortestArray[w] = shortestArray[v] + weight;
	    		   AbstractMap.SimpleEntry<Integer, Integer> wPair = new AbstractMap.SimpleEntry<Integer, Integer>(shortestArray[w], w);
	    		   predArray[w] = v;
	    		   q.offer(wPair);
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

	// =============================================================================
	// = HELPER METHODS
	// =============================================================================


	
	private int determineWeight(int v, int u, int weightType) {
		int weight = 0;
		INode uNode = (INode) g.getNode(u);

		switch (weightType) {
		case 0: //section
			weight = g.weight(v, u);
			break;
		case 1: //indegree
			weight = uNode.getIndegree();
			break;
		case 2: //outdegree
			weight = uNode.getOutdegree();
			break;
		default: //section
			weight = g.weight(v, u);
			break;
		}
		return weight;
	}
	
	public Collection<String> translateToArticleNames(Collection<Integer> path) {
		Collection<String> namePath = new ArrayList<String>();
		LinkedList<Integer> intPath = (LinkedList<Integer>) path;
		for (int i = 0; i < path.size(); i++) {
			INode node = (INode) g.getNode(intPath.get(i));
			String articleName = node.getName();
			namePath.add(articleName);
		}
		return namePath;
	}
	
	// =============================================================================
	// = GETTERS AND SETTERS
	// =============================================================================

	
    public Graph getG() {
		return g;
	}
    public Map<Integer, Integer> getWikiIDtoNodeID() {
		return wikiIDtoNodeID;
	}

	public Map<String, Collection<Integer>> getArticleNametoNodeIDs() {
		return articleNametoNodeIDs;
	}


}
