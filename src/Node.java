
public class Node implements INode {

    int index;
    int wikiID;
    String name;
    int indegree;
    int outdegree;

    public Node(int index, int wikiID, String name) {
        this.index = index;
        this.wikiID = wikiID;
        this.name = name;
        this.indegree = 0;
        this.outdegree = 0;
    }
    
    @Override
    public int getIndex() {
        return index;
    }
    
    @Override
    public void setIndex(int index) {
        this.index = index;
    }
    
    @Override
    public int getWikiId() {
        return wikiID;
    }
    
    @Override
    public void setWikiId(int wikiID) {
        this.wikiID = wikiID;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int getOutdegree() {
        return outdegree;
    }
    
    @Override
    public void setOutdegree(int outdegree) {
        this.outdegree = outdegree;
    }
    
    @Override
    public int getIndegree() {
        return indegree;
    }
    
    @Override
    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }
    
    @Override
    public void incrementIndegree() {
        this.indegree++;
    }
    
    @Override
    public void incrementOutdegree() {
        this.outdegree++;
    }
}
