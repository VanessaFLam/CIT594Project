import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author OpenDSA
 *
 */
public class GraphL implements Graph {

    public class Edge {
        
        int vertex;
        short weight; //how far down the page

        Edge(int v, short w) {
            vertex = v;
            weight = w;
        }
        
    }

    private List<Edge>[]        adjacencyLists;
    private Object[]            nodeValues;
    private int                 numEdge;
    
    /**
     * Empty no argument constructor
     */
    GraphL()
    {
     // No real constructor needed
    }


    // Initialize the graph with n vertices. Creates two "parallel" arrays - nodes and adj lists.
    public void init(int n)
    {
        
        // Create n empty adjacency lists.
        adjacencyLists = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjacencyLists[i] = new ArrayList<Edge>();
        }

        // Create n-object array to hold nodes.        
        nodeValues = new Object[n];
        
        // Initialize.
        numEdge = 0;
        
    }


    // Return the number of vertices
    public int nodeCount()
    {
        return adjacencyLists.length;
    }


    // Return the current number of edges
    public int edgeCount()
    {
        return numEdge;
    }


   
	@Override
	public Object getNode(int v) {
		return nodeValues[v];
	}

   
	@Override
	public void setNode(int v, Object val) {
		nodeValues[v] = val;
		
	}
    
	public Edge getEdge(int v, int w) {
	    int index = find(v, w);
        if (index >= 0) {
			return adjacencyLists[v].get(index);
		} else {
			return null;
		}
	}


    // Find the index of edge TO w in IN v's adjacency list, if it is in list. o/w -1.
    private int find(int v, int w)
    {
        List<Edge> vAdjL = adjacencyLists[v];
        for (int i = 0; i < vAdjL.size(); i++) {
            if (vAdjL.get(i).vertex == w) {
                return i;
            }
        }
        return -1;
    }


    // Adds a new edge from node v to node w with weight wgt
    public void addEdge(int v, int w, short wgt)
    {
        
        // No zero weight.
        if (wgt == 0)
            return; // Can't store weight of 0
        // Update weight if exists.
        if (getEdge(v, w) != null) {
            getEdge(v, w).weight = wgt;
            return;
        }
        // New edge!
        adjacencyLists[v].add(new Edge(w, wgt));
        numEdge++;
        return;
        
    }


    // Get the weight value for an edge
    public int weight(int v, int w)
    {
        Edge e = getEdge(v, w);
        if (e != null) {
            return e.weight;
        } else {
            return 0;
        }
    }


    // Removes the edge from the graph.
    public void removeEdge(int v, int w)
    {
        int index = find (v, w);
        if (index >= 0) {
            adjacencyLists[v].remove(index);
            numEdge--;
        }
    }


    // Returns true iff the graph has the edge
    public boolean hasEdge(int v, int w)
    {        
        return (find(v, w) >= 0);
    }

    // Returns an array containing the indicies of the neighbors of v
    public int[] neighbors(int v)
    {
        
        // Get the adjacency list.
        List<Edge> adjL = adjacencyLists[v];
        
        // Initialize neighbors array.
        int[] nbrs = new int[adjL.size()];
        
        // Loop.
        for (int i = 0; i < adjL.size(); i ++) {
            nbrs[i] = adjL.get(i).vertex;
        }
        
        return nbrs;
       
    }

}
