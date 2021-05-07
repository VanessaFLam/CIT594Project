import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

public class DataCleanerTest {

    @Test
    public void testBuildWikiIDtoArticleNameMapExceptions() {

        Map<Long, String> m;
        
        // Bad file path.
        m = DataCleaner.buildWikiIDtoArticleNameMap("badpath");
        assertNull(m);
        
        /*
         * TEST FILE -- "bad" row should just be skipped
         * page_id,item_id,title,views 1
         * 2,6199,Anarchism,31335
         * apple,banana,Autism,kiwi
         * 39,101038,Albedo,14573
         * 290,9659,A,25859
         * 303,173,Alabama,52765
         * 305,41746,Achilles,35877
         * 307,91,Abraham Lincoln,151008
         * 308,868,Aristotle,74700
         * 309,853997,An American in Paris,2156
         * 316,277751,Academy Award for Best Production Design,3114
         */
        
        m = DataCleaner.buildWikiIDtoArticleNameMap("data/raw/page_first_10_bad.csv");
        assertEquals(9, m.size());
        assertTrue(m.containsKey((long) 308));
        assertEquals("Aristotle", m.get((long) 308));
        assertFalse(m.containsValue("Autism"));
        
    }
    
    
    
    
    @Test
    public void testBuildWikiIDtoArticleNameMap() {
        
        /*
         * TEST FILE
         * page_id,item_id,title,views 1
         * 2,6199,Anarchism,31335
         * 25,38404,Autism,49693
         * 39,101038,Albedo,14573
         * 290,9659,A,25859
         * 303,173,Alabama,52765
         * 305,41746,Achilles,35877
         * 307,91,Abraham Lincoln,151008
         * 308,868,Aristotle,74700
         * 309,853997,An American in Paris,2156
         * 316,277751,Academy Award for Best Production Design,3114
         */
        
        Map<Long, String> m = DataCleaner.buildWikiIDtoArticleNameMap("data/raw/page_first_10.csv");
        assertEquals(10, m.size());
        assertTrue(m.containsKey((long) 308));
        assertEquals("Aristotle", m.get((long) 308));
        
    }

}
