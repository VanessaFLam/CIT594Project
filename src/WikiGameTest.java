import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class WikiGameTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testHopsSmallest() {
   	 final String id_map_file   = "data/test/smallest_map.txt";
   	 final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.loadGraph(id_map_file, graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(3);
        ans.add(8);
        assertEquals(ans,wg.findPath(0, 8, "hops"));
	}
	
	@Test
	public void testSectionSmallest() {
		final String id_map_file   = "data/test/smallest_map.txt";
	   	final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.loadGraph(id_map_file, graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(2);
        ans.add(6);
        ans.add(8);
        assertEquals(ans, wg.findPath(0, 8, "section"));
	}
	
	@Test
	public void testIndegreeSmallest() {
		final String id_map_file   = "data/test/smallest_map.txt";
	   	 final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.loadGraph(id_map_file, graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(2);
        ans.add(6);
        ans.add(8);
        
        assertEquals(ans, wg.findPath(0, 8, "indegree"));
	}
	
	@Test
	public void testOutdegreeSmallest() {
		final String id_map_file   = "data/test/smallest_map.txt";
	   	 final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.loadGraph(id_map_file, graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(3);
        ans.add(8);
        
        assertEquals(ans, wg.findPath(0, 8, "outdegree"));
	}
	
	   @Test
	    public void testLoadNodes() {
	     final String id_map_file   = "data/test/test_id_map_small_1.txt";
	        
	        WikiGame wg = new WikiGame();
	        assertEquals(63, wg.loadNodes(id_map_file));
	        
	        // check map sizes
	        assertEquals(63, wg.getWikiIDtoNodeID().size());
	        assertEquals(63, wg.getArticleNametoNodeIDs().size());

	    }
	   
       @Test
       public void testLoadEdges() {
          final String id_map_file   = "data/test/test_id_map_small_1.txt";
          final String graph_file    = "data/test/test_graph_small_1.mtx";
           
          WikiGame wg = new WikiGame();
          wg.loadNodes(id_map_file);
          int numOfEdges = wg.loadEdges(graph_file);
          
          assertEquals(127,numOfEdges);
       }
       @Test
       public void testLoadNodesBadFile() {
           final String id_map_file   = "nonsense.txt";
           
           WikiGame wg = new WikiGame();
           assertEquals(-1, wg.loadNodes(id_map_file));
           
       }
       
       @Test
       public void testLoadEdgesBadFile() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "gibberish.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadNodes(id_map_file);
           int numOfEdges = wg.loadEdges(graph_file);
           
           assertEquals(-1,numOfEdges);
       }
       
       @Test
       public void testGraph() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           Graph g = wg.getG();
           
           // test node and edge counts
           assertEquals(63, g.nodeCount());
           assertEquals(127, g.edgeCount());
           
           // test nodes
           
           // Musical Instrument
           INode n1 = (INode) g.getNode(0);
           assertEquals(0, n1.getIndex());
           assertEquals(27406894, n1.getWikiId());
           assertEquals("musical instrument", n1.getName());
           assertEquals(6, n1.getOutdegree());
           assertEquals(5, n1.getIndegree());
           
           // Drum
           INode n2 = (INode) g.getNode(2);
           assertEquals(2, n2.getIndex());
           assertEquals(7950, n2.getWikiId());
           assertEquals("drum", n2.getName());
           assertEquals(4, n2.getOutdegree());
           assertEquals(1, n2.getIndegree());
           
           // Mammal
           INode n3 = (INode) g.getNode(46);
           assertEquals(46, n3.getIndex());
           assertEquals(18838, n3.getWikiId());
           assertEquals("mammal", n3.getName());
           assertEquals(10, n3.getOutdegree());
           assertEquals(7, n3.getIndegree());
           
           // Pillow
           INode n4 = (INode) g.getNode(60);
           assertEquals(60, n4.getIndex());
           assertEquals(706020, n4.getWikiId());
           assertEquals("pillow", n4.getName());
           assertEquals(0, n4.getOutdegree());
           assertEquals(1, n4.getIndegree());
           
           // test edges
           
       }
       
       @Test
       public void testHops() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Musical Instrument to Indus River
           List<Integer> ans1 = new LinkedList<Integer>(Arrays.asList(0,5,15,40));
           assertEquals(ans1,wg.findPath(0, 40, "hops"));
           
           // Musical Instrument to Fungus
           List<Integer> ans2 = new LinkedList<Integer>(Arrays.asList(0,2,12,16,42,45,50));
           assertEquals(ans2 ,wg.findPath(0, 50, "hops"));
       }
       
       @Test
       public void testHopsOutOfBounds() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           assertNull(wg.findPath(-1, 40, "hops"));
           
           assertNull(wg.findPath(0, 140, "hops"));
       }
       
       @Test
       public void testPathsDefault() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Musical Instrument to Indus River
           List<Integer> ans1 = new LinkedList<Integer>(Arrays.asList(0,5,15,40));
           assertEquals(ans1,wg.findPath(0, 40, "nonsense"));
           
           // Musical Instrument to Fungus
           List<Integer> ans2 = new LinkedList<Integer>(Arrays.asList(0,2,12,16,42,45,50));
           assertEquals(ans2 ,wg.findPath(0, 50, "wrong type"));
       }
       
       @Test
       public void testHopsNoPath() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Musical Instrument to Indus River
           List<Integer> ans1 = new LinkedList<Integer>();
           assertEquals(ans1,wg.findPath(33, 37, "hops"));
       }
       
       @Test
       public void testDijkstraOutOfBounds() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           assertNull(wg.findPath(-1, 40, "section"));
           assertNull(wg.findPath(0, 140, "indegree"));
           assertNull(wg.findPath(-1, 140, "outdegree"));
       }
       
       @Test
       public void testSection() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Goat to Fungus
           List<Integer> ans1 = new LinkedList<Integer>(Arrays.asList(42,43,46,51,56,50));
           assertEquals(ans1,wg.findPath(42, 50, "section"));
           
       }
       
       @Test
       public void testOutdegree() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Musical Instrument to trumpet
           List<Integer> ans1 = new LinkedList<Integer>(Arrays.asList(0,6,25,61,26,9));
           assertEquals(ans1,wg.findPath(0, 9, "outdegree"));
       }
       
       @Test
       public void testIndegree() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           
           // Feather to Amphibean
           List<Integer> ans1 = new LinkedList<Integer>(Arrays.asList(53,57,62,43));
           assertEquals(ans1,wg.findPath(53, 43, "indegree"));
       }
       
       @Test
       public void testTranslateToArticleNames() {
           final String id_map_file   = "data/test/test_id_map_small_1.txt";
           final String graph_file    = "data/test/test_graph_small_1.mtx";
           
           WikiGame wg = new WikiGame();
           wg.loadGraph(id_map_file, graph_file);
           // Musical Instrument to trumpet
           List<Integer> path = new LinkedList<Integer>(Arrays.asList(0,6,25,61,26,9));
           Collection<String> namePath = new ArrayList<String>(Arrays.asList("musical instrument", "bagpipes", "reed", "saxophone", "brass", "trumpet"));
           assertEquals(namePath,wg.translateToArticleNames(path));
           
           
       }
       
       

}
