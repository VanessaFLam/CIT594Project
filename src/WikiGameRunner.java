
public class WikiGameRunner {

    public static void main(String[] args) {
        
        // File paths all in one place.

//        final String id_map_file   = "data/clean/full_idmap_file.txt";
//        final String name_map_file = "data/clean/page_ids.csv";
//        final String graph_file    = "data/clean/full_graph_file.mtx";
    	
    	 final String id_map_file   = "data/test/smallest_map.txt";
//         final String name_map_file = "data/test/smallest_wikiid_articlename.txt";
         final String graph_file    = "data/test/smallest_test.mtx";

        
        // Toggle for print statements.
        final boolean print_progress = true;
        long startTime, endTime, elapsedTime;
        
        // Create new Wikigame. Graph and maps are empty, numOfNodes = 0.
        WikiGame wg = new WikiGame();
        System.out.println("Welcome to the Wikipedia Game!");
        if (print_progress) {
            System.out.println("Building graph ...");
        }
        
        // Load nodes.
        if (print_progress) {
            System.out.print("\tLoading node information from " + id_map_file
                                + " (~?-? seconds expected) ... ");
            startTime = System.nanoTime();
        }
        wg.loadGraph(id_map_file, graph_file);
        if (print_progress) {
            endTime   = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("finished in " + elapsedTime / 1000000000. + " seconds!");
        }

    }
    
}
