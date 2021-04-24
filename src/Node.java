
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
	
	public void incrementIndegree() {
		this.indegree++;
	}
	
	public void incrementOutdegree() {
		this.outdegree++;
	}
}
