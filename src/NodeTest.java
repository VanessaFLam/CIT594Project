import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NodeTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testGetAndSetIndex() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(1,n.getIndex());
        
        n.setIndex(345);
        assertEquals(345,n.getIndex());
    }

    @Test
    public void testGetAndSetWikiId() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(123,n.getWikiId());
        
        n.setWikiId(42);
        assertEquals(42,n.getWikiId());
    }

    @Test
    public void testGetAndSetName() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals("Hobbes",n.getName());
        
        n.setName("Calvin");
        assertEquals("Calvin",n.getName());
    }

    @Test
    public void testGetAndSetOutdegree() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(0,n.getOutdegree());
        
        n.setOutdegree(32);
        assertEquals(32,n.getOutdegree());
    }

    @Test
    public void testGetAndSetIndegree() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(0,n.getIndegree());
        
        n.setIndegree(55);
        assertEquals(55,n.getIndegree());
    }

    @Test
    public void testIncrementIndegree() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(0,n.getIndegree());
        n.incrementIndegree();
        assertEquals(1,n.getIndegree());
        for(int i=0; i <10; i++) {
            n.incrementIndegree();
        }
        assertEquals(11,n.getIndegree());
        
        n.setIndegree(55);
        n.incrementIndegree();
        assertEquals(56,n.getIndegree());
    }

    @Test
    public void testIncrementOutdegree() {
        Node n = new Node(1, 123, "Hobbes");
        assertEquals(0,n.getOutdegree());
        n.incrementOutdegree();
        assertEquals(1,n.getOutdegree());
        for(int i=0; i <10; i++) {
            n.incrementOutdegree();
        }
        assertEquals(11,n.getOutdegree());
        
        n.setOutdegree(55);
        n.incrementOutdegree();
        assertEquals(56,n.getOutdegree());
    }

}
