
public class WikiGameRunner {

    public static void main(String[] args) {
        
        // File paths all in one place.
        final String id_map_file   = "data/clean/idmap_file_first_10.txt";
        final String graph_file    = "data/clean/graph_file_first_10.mtx";
        
        // Toggle for print statements.
        final boolean print_progress = true;
        long startTime, endTime, elapsedTime;
        
        // Create new Wikigame. Graph and maps are empty, numOfNodes = 0.
        WikiGame wg = new WikiGame();
        System.out.println("Welcome to the Wikipedia Game!");
        if (print_progress) {
            System.out.println("Building graph ...");
        }
        
//        // Build wikiIDtoNodeID map from idmap file.
//        if (print_progress) {
//            System.out.print("\tBuilding id map (~5-10 seconds expected) ... ");
//            startTime = System.nanoTime();
//        }
//        wg.mapWikiIDtoNodeID(id_map_file);
//        if (print_progress) {
//            endTime   = System.nanoTime();
//            elapsedTime = endTime - startTime;
//            System.out.println("finished in " + elapsedTime / 1000000000. + " seconds!");
//        }
//        
//        // Build articleNametoNodeID map from page_ids file.
//        if (print_progress) {
//            System.out.print("\tBuilding article name map (~10-15 seconds expected) ... ");
//            startTime = System.nanoTime();
//        }
//        wg.mapArticleNametoNodeID(name_map_file);
//        if (print_progress) {
//            endTime   = System.nanoTime();
//            elapsedTime = endTime - startTime;
//            System.out.println("finished in " + elapsedTime / 1000000000. + " seconds!");
//        }
        
        // Load nodes.
        if (print_progress) {
            System.out.print("\tLoading node information from " + id_map_file
                                + " (~?-? seconds expected) ... ");
            startTime = System.nanoTime();
        }
        wg.loadNodes(id_map_file);
        if (print_progress) {
            endTime   = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("finished in " + elapsedTime / 1000000000. + " seconds!");
        }
        
        // Load edges.
        if (print_progress) {
            System.out.print("\tLoading edge information from " + graph_file
                                + " (~?-? seconds expected) ... ");
            startTime = System.nanoTime();
        }
        wg.loadEdges(graph_file);
        if (print_progress) {
            endTime   = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("finished in " + elapsedTime / 1000000000. + " seconds!");
        }        
        
        
    }
    
    
}
