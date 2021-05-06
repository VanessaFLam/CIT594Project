
public class WikiGameRunner {

    public static void main(String[] args) {
        
        // File paths all in one place.
        final String id_map_file   = "data/clean/full_idmap_file.txt";
        final String graph_file    = "data/clean/full_graph_file.mtx";
        
        // Toggle for print statements.
        final boolean print_progress = WikiGame.printProgress;
        long startTime = 0;
        long endTime = 0;
        long elapsedTime = 0;
        
        // Create new Wikigame. Graph and maps are empty, numOfNodes = 0.
        WikiGame wg = new WikiGame();
        System.out.println("Welcome to the Wikipedia Game!");
        if (print_progress) {
            System.out.println("Building graph ...");
        }
        
        // Load graph.
        if (print_progress) {
            System.out.println("\tLoading graph (~?-? seconds expected) ... ");
            startTime = System.nanoTime();
        }
        wg.loadGraph(id_map_file, graph_file);
        if (print_progress) {
            endTime   = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("\tFinished in " + elapsedTime / 1000000000. + " seconds!");
        }

    }
    
}
