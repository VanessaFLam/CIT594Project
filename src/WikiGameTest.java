import static org.junit.Assert.*;

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
//        ans.add(0);
//        ans.add(2);
//        ans.add(6);
//        ans.add(8);
        
        //NEED TO FIX.
        System.out.println(wg.findPath(0, 8, "indegree"));
	}

}
