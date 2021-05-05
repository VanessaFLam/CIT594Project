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
    
//    public void loadGraph(String id_map_file, String graph_file) {
//        
//        loadNodes(id_map_file);
//        loadEdges(graph_file);
//        
//    }
    
    
    public int loadEdges(String filePath) {
        
        final String logPath = "data/logs/load_edges_error_log.txt";

        try {
        
            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Create writer (for log file).
            BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));

            // Header tells number of nodes and edges expected. Can skip here.
            String line = br.readLine();

            
            // Read each line in the file.
            // Each line is of form "<wiki_id_from> <wiki_id_to> <link_section>".
            while ((line = br.readLine()) != null) {
                
                // Try to parse to "<wiki_id_from> <wiki_id_to> <link_section>". Log error if not.
                try {
                    
                    // Get tokens.
                    String[] toks = line.split("\\s+");         
                    int wikiID_from = Integer.parseInt(toks[0]);
                    int wikiID_to   = Integer.parseInt(toks[1]);
                    int edgeWeight  = Integer.parseInt(toks[2]);
                    
                    // Get node ids.
                    int nodeID_from = wikiIDtoNodeID.get(wikiID_from);
                    int nodeID_to   = wikiIDtoNodeID.get(wikiID_to);                    
                    
                    // Add edge.
                    g.addEdge(nodeID_from, nodeID_to, edgeWeight);                    
                
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
    
    // form is "<node_id> <page_id> <article_title>" will get n from other file
    public String loadNodes(String filePath) {
        
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
            
            // Read each line in the file.
            // Each line is of form "<node_id> <page_id> <article_title>".
            while ((line = br.readLine()) != null) {
                
                // Try to parse to "<node_id> <page_id> <article_title>". Log error if not.
                try {
                    
                    // Get tokens.
                    String[] toks = line.split("\\s+");         
                    int nodeID = Integer.parseInt(toks[0]);
                    int wikiID = Integer.parseInt(toks[1]);
                    String articleName = toks[2];
                    
                    // Initialize node in graph.
                    INode n = new Node(nodeID, wikiID, articleName);
                    g.setNode(nodeID, n);                    
                    
                    // Add to maps.
                    wikiIDtoNodeID.put(wikiID, nodeID);
                    if (!articleNametoNodeIDs.containsKey(articleName)) {
                        articleNametoNodeIDs.put(articleName, new ArrayList<Integer>());
                    } else {
                        articleNametoNodeIDs.get(articleName).add(nodeID);
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
            return result;
            
        } catch (IOException e) {
            e.printStackTrace();
            return null;          
        }
        
    }
    
    
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

  public static final Integer DefualtEdgeWeight = 1;

	// =============================================================================
	// = CONSTRUCTOR
	// =============================================================================
	public WikiGame() {
		g = new GraphL();
		wikiIDtoNodeID = new HashMap<Integer, Integer>();
		articleNametoNodeID = new HashMap<String, Integer>();
	}

	// =============================================================================
	// = METHODS
	// =============================================================================

	// TODO: Method to create the hashmap -Jackson
	public int mapWikiIDtoNodeID(String filePath) {
		try {
			// created buffered file reader
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();

			while (line != null) {
				String[] toks = line.split("\\s+");

				int wikiID = Integer.parseInt(toks[1]);
				int nodeID = Integer.parseInt(toks[0]);

				wikiIDtoNodeID.put(wikiID, nodeID);

				line = br.readLine();
			}
			br.close();
			return wikiIDtoNodeID.size();
		} catch (IOException e) {
			// print error message
			System.out.println("Error reading input file: " + filePath);
		}
		return -1;
	}

	// Jackson
	public int mapArticleNametoNodeID(String filePath) {
		try {
			// created buffered file reader
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			// skip the header line
			br.readLine();

			String line = br.readLine();

			while (line != null) {
				String[] toks = line.split("\\s+");

				String articleName = toks[1];
				int wikiID = Integer.parseInt(toks[0]);
				System.out.println("looking for " + wikiID + " for " + articleName);

				try {
					int nodeID = wikiIDtoNodeID.get(wikiID);
					System.out.println("adding " + articleName + " -> " + nodeID);
					articleNametoNodeID.put(articleName, nodeID);
					line = br.readLine();
				} catch (NullPointerException e) {
					line = br.readLine();
					continue;
				}

			}
			br.close();
			return articleNametoNodeID.size();
		} catch (IOException e) {
			// print error message
			System.out.println("Error reading input file: " + filePath);
		}
		return -1;
	}

	// Jackson
	@Override
	public int loadGraphFromDataSet(String filePath) {

		// TODO: do we need to clear the graph? should i initialize graph here instead
		// of in constructor?

		try {
			// created buffered file reader
			FileReader fr = new FileReader(filePath);
			BufferedReader br = new BufferedReader(fr);

			// get number of nodes (from first line of input file)
			String line = br.readLine();
			String[] toks = line.split(" ");
			g.init(Integer.parseInt(toks[0]) + 1);

			// get next line and continue reading until EOF
			line = br.readLine();
			while (line != null) {
				// split line into tokens, using space as delimiter
				toks = line.split(" ");

				// ignore line if one of the first two values is 0
				if (Integer.parseInt(toks[0]) == 0 || Integer.parseInt(toks[1]) == 0) {
					line = br.readLine();
					continue;
				}

				// add edge to the graph
				// TODO: decide which edge weight to use here
				// TODO: should we validate data - i.e. check that parseInt is successful?
				g.addEdge(Integer.parseInt(toks[0]), Integer.parseInt(toks[1]), DefualtEdgeWeight);
				g.addEdge(Integer.parseInt(toks[0]), Integer.parseInt(toks[1]), Integer.parseInt(toks[2]));
				// update indegrees and outdegrees

				// get next line
				line = br.readLine();
			}
		} catch (IOException e) {
			// print error message
			System.out.println("Error reading input file: " + filePath);
		}

		// save and return the number of nodes
		numOfNodes = g.nodeCount() - 1;
		return numOfNodes;
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
	       
	       if(shortestArray[v] == INFINITY) break;
	       int[] nList = g.neighbors(v);
	       
	       for (int j = 0; j < nList.length; j++) {
	    	   int w = nList[j];
	    	   int weight = determineWeight(v, w, weightType);
	    	   if (shortestArray[w] > (shortestArray[v] + weight)) {
	    		   shortestArray[w] = shortestArray[v] + weight;
	    		   AbstractMap.SimpleEntry<Integer, Integer> wPair = new AbstractMap.SimpleEntry<Integer, Integer>(shortestArray[w], w);
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

	// find the unvisited vertex with the minimum "shortest"
	private int minVertex(int[] shortest, boolean[] visited) {

		// initialize v to 0
		int v = 0;

		// set v to the first unvisited vertex;
		for (int i = 0; i < g.nodeCount(); i++) {
			if (!visited[i]) {
				v = i;
				break;
			}
		}

		// now find the smallest
		for (int i = 0; i < g.nodeCount(); i++) {
			if ((!visited[i]) && (shortest[i] < shortest[v])) {
				v = i;
			}
		}

		// return v
		return v;
	}
	
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


}
