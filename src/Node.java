
public class Node implements INode {
	
	int index;
	int wiki_id;
	String name;
	int indegree;
	int outdegree;
	
	public Node(int index, int wiki_id, String name) {
		this.index = index;
		this.wiki_id = wiki_id;
		this.name = name;
		this.indegree = 0;
		this.outdegree = 0;
	}
	
	
	public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
    }


    public int getWikiId() {
        return wiki_id;
    }


    public void setWikiId(int wiki_id) {
        this.wiki_id = wiki_id;
    }


    public String getName() {
        return name;
    }


    public void setName(String name) {
        this.name = name;
    }


    public int getOutdegree() {
        return outdegree;
    }


    public void setOutdegree(int outdegree) {
        this.outdegree = outdegree;
    }

    public int getIndegree() {
        return indegree;
    }

    public void setIndegree(int indegree) {
        this.indegree = indegree;
    }

	
	public void incrementIndegree() {
		this.indegree++;
	}
	
	public void incrementOutdegree() {
		this.outdegree++;
	}
}
