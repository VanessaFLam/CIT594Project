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
		final String id_map_file   = "data/test/smallest_nodeid_wikiid.txt";
        final String name_map_file = "data/test/smallest_wikiid_articlename.txt";
        final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.mapWikiIDtoNodeID(id_map_file);
        wg.mapArticleNametoNodeID(name_map_file);
        wg.loadGraphFromDataSet(graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(3);
        ans.add(8);
        assertEquals(ans,wg.findPath(0, 8, "hops"));
	}
	
	@Test
	public void testSectionSmallest() {
		final String id_map_file   = "data/test/smallest_nodeid_wikiid.txt";
        final String name_map_file = "data/test/smallest_wikiid_articlename.txt";
        final String graph_file    = "data/test/smallest_test.mtx";
        
        WikiGame wg = new WikiGame();
        wg.mapWikiIDtoNodeID(id_map_file);
        wg.mapArticleNametoNodeID(name_map_file);
        wg.loadGraphFromDataSet(graph_file);
        
        List<Integer> ans = new LinkedList<Integer>();
        ans.add(0);
        ans.add(2);
        ans.add(6);
        ans.add(8);
        assertEquals(ans, wg.findPath(0, 8, "section"));
	}

}
