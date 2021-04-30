
public interface INode {
    public int getIndex();
    public void setIndex(int index);

    public int getWikiId();
    public void setWikiId(int wiki_id);

    public String getName();
    public void setName(String name);

    public int getOutdegree();
    public void setOutdegree(int outdegree);

    public int getIndegree();
    public void setIndegree(int indegree);
    
	void incrementIndegree();
	
	void incrementOutdegree();
	
}
