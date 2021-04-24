import java.util.Collection;

/**
 * @author OpenDSA
 */

public interface Graph
{
    /**
     * Initialize the graph with some number of vertices
     * 
     * @param n the number of vertices
     */
    void init(int n);


    /**
     * @return the number of vertices
     */
    int nodeCount();


    /**
     * @return the current number of edges
     */
    int edgeCount();


    /**
     * Set the value of node with index v
     * 
     * @param v   the index of the node
     * @param val the value of the node
     */
    void setNode(int v, Object val);

    /**
     * Get the nodes with index v
     * @param id
     * @return
     */
    Object getNode(int v);
    
    /**
     * Adds a new edge from node v to node w with weight wgt
     * 
     * @param v   - the from node
     * @param w   - the to node
     * @param wgt - the weight of the edge
     */
    void addEdge(int v, int w, int wgt);


    /**
     * Get the weight value for an edge
     * 
     * @param v - the from node
     * @param w - the to node
     * @return the weight of the (v,w) edge
     */
    int weight(int v, int w);


    /**
     * Removes the edge from the graph
     * 
     * @param v - the from node
     * @param w - the to node
     */
    void removeEdge(int v, int w);


    /**
     * Returns true iff the graph has the edge
     * 
     * @param v - the from node
     * @param w - the to node
     * @return true if the graph has a (v,w) edge
     */
    boolean hasEdge(int v, int w);

//    /**
//     * Returns Edge between v and w
//     * Returns null if no edge
//     */
//    Edge getEdge(int v, int w);

    /**
     * Returns an array containing the indices of the neighbors of v
     * 
     * @param v - the node
     * @return the array of neighbors
     */
    Collection<Object> neighbors(int v);

}