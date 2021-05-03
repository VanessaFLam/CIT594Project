
public interface INode {
    
    /**
     * Getter for node index. Node index is the vertex number
     * of this node in the graph
     * @return (int) the node index
     */
    public int getIndex();
    
    /**
     * Setter for node index. Node index is the vertex number
     * of this node in the graph
     * @param index the value to set for node the node index
     */
    public void setIndex(int index);
    
    /**
     * Getter for the wiki ID of the article represented by
     * this node
     * @return (int) the wiki ID
     */
    public int getWikiId();
    
    /**
     * Setter for the wiki ID of the article represented by
     * this node
     * @param wiki_id the value of to set for wiki ID
     */
    public void setWikiId(int wikiID);
    
    /**
     * Getter for the name of the wiki article represented by
     * this node
     * @return (String) the wiki article name
     */
    public String getName();
    
    /**
     * Setter for the name of the wiki article represented by
     * this node
     * @param name wiki article name
     */
    public void setName(String name);
    
    /**
     * Getter for the outdegree of this node
     * @return (int) node outdegree
     */
    public int getOutdegree();
    
    /**
     * Setter for the outdegree of this node
     * @param outdegree
     */
    public void setOutdegree(int outdegree);

    /**
     * Getter for the indegree of this node
     * @return (int) node indegree
     */
    public int getIndegree();
    
    /**
     * Setter for the indegree of this node
     * @param indegree
     */
    public void setIndegree(int indegree);
    
    /**
     * increments the indegree of this node by 1
     */
	void incrementIndegree();
	
	/**
     * increments the outdegree of this node by 1
     */
	void incrementOutdegree();
	
}
