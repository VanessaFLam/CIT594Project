
public class WikiGameRunner {

    public static void main(String[] args) {
        
        WikiGame wg = new WikiGame();
        wg.mapWikiIDtoNodeID("data/clean/full_idmap_file.txt");
        
        wg.mapArticleNametoNodeID("data/clean/page_ids.csv");
        
    }
    
    
}
