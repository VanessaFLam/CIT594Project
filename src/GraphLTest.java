import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class GraphLTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testNodeCount() {
        GraphL g = new GraphL();
        g.init(5);
        
        assertEquals(5,g.nodeCount());
    }

    @Test
    public void testEdgeCount() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
           g.addEdge(0, i, i*2);      
        }
        
        assertEquals(6,g.edgeCount());
    }

    @Test
    public void testGetAndSetNode() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertNull(g.getNode(1));
        
        g.setNode(1, "test");
        assertEquals("test",g.getNode(1));
    }

    @Test
    public void testGetAddAndHasEdge() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
           g.addEdge(0, i, i*2);      
        }
        
        
        for (int i = 1; i < 7; i++) {
            assertTrue(g.hasEdge(0, i)); 
            assertTrue(g.getEdge(0, i) instanceof GraphL.Edge );
         }
        
    }
    @Test
    public void testGetAndHasEdgeNull() {
        GraphL g = new GraphL();
        g.init(10);
       
        for (int i = 1; i < 7; i++) {
            assertFalse(g.hasEdge(0, i)); 
            assertNull(g.getEdge(0, i));
        }
        
    }
    
    @Test
    public void testAddEdgeZeroWeight() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
            g.addEdge(0, i, 0);      
         }
        
        assertEquals(0,g.edgeCount());
        for (int i = 1; i < 7; i++) {
            assertFalse(g.hasEdge(0, i)); 
            assertNull(g.getEdge(0, i));
        }
        
    }
    
    @Test
    public void testAddEdgeExists() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
            g.addEdge(0, 7-i, i*2);      
        }
        for (int i = 1; i < 7; i++) {
            g.addEdge(0, 7-i, i*3);      
        }
//        for (int i = 1; i < 7; i++) {
//            g.addEdge(0, i, i*4);      
//        }
        
        assertEquals(12,g.edgeCount());
        for (int i = 1; i < 7; i++) {
            assertEquals(i*3,g.weight(0, 7-i));
        }
        
    }


    @Test
    public void testWeight() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
           g.addEdge(0, i, i*2);      
        }
        
        
        for (int i = 1; i < 7; i++) {
            assertEquals(i*2,g.weight(0, i));
         }
    }
    @Test
    public void testWeightNonExistant() {
        GraphL g = new GraphL();
        g.init(10);
        
        for (int i = 1; i < 7; i++) {
            assertEquals(0,g.weight(0, i));
        }
    }

    @Test
    public void testRemoveEdge() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
           g.addEdge(0, i, i*2);      
        }
        assertEquals(6 ,g.edgeCount());
        for (int i = 1; i < 7; i++) {
            g.removeEdge(0, i);      
        }
        assertEquals(0 ,g.edgeCount());
    }
    
    @Test
    public void testRemoveEdgeNonExistant() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
            g.addEdge(0, i, i*2);      
        }
        assertEquals(6 ,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
            g.removeEdge(1, i);      
        }
        assertEquals(6 ,g.edgeCount());
    }

    @Test
    public void testNeighbors() {
        GraphL g = new GraphL();
        g.init(10);
        
        assertEquals(0,g.edgeCount());
        
        for (int i = 1; i < 7; i++) {
           g.addEdge(0, i, i*2);      
        }
        int nbrs[] = {1,2,3,4,5,6};
        int[] result = g.neighbors(0);
        for (int i = 0; i < nbrs.length; i++) {
            assertEquals(nbrs[i], result[i]);
        }
    }

}
