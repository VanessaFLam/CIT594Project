import java.util.*;

public class WikiGameRunner { 

    private Scanner s;

    private int getNodeIDFromArticle(String type, WikiGame wg) {
        s = new Scanner(System.in);
        System.out.println("Please input the name of the " + type + " Wikipedia page:");
        String articleName = s.nextLine();
        String lowerArticleName = articleName.toLowerCase();
        Map<String, Collection<Integer>> map = wg.getArticleNametoNodeIDs();
        ArrayList<Integer> nodeIDs = (ArrayList<Integer>) map.get(lowerArticleName);
        int nodeID;
        while (true) {
            if (nodeIDs == null) {
                System.out.println("This Wikipedia page does not exist, "
                        + "please input a different " + type + ":");
                articleName = s.nextLine();
                lowerArticleName = articleName.toLowerCase();
                nodeIDs = (ArrayList<Integer>) map.get(lowerArticleName);
            } else if (nodeIDs.size() > 1) {
                System.out.println("There are several articles with that name. "
                        + "We will choose one at random for you");
                System.out.println(nodeIDs);
                Random r = new Random();
                int num = r.nextInt(nodeIDs.size()) - 1;
                nodeID = nodeIDs.get(num);
                System.out.println(nodeID);
                break;
            } else if (nodeIDs.size() == 1) {
                nodeID = nodeIDs.get(0);
                break;
            } else {
                System.out.println("There are no Wikipedia pages with this name, "
                        + "please input a different " + type + ":");
                articleName = s.nextLine();
                nodeIDs = (ArrayList<Integer>) map.get(lowerArticleName);
            }
        }
        return nodeID;
    }

    private Collection<String> userInteraction(WikiGame wg) {
        int source = getNodeIDFromArticle("source", wg);
        int destination = getNodeIDFromArticle("destination", wg);
        ArrayList<String> typeOptions = new ArrayList<String>();
        typeOptions.add("hops");
        typeOptions.add("indegree");
        typeOptions.add("outdegree");
        typeOptions.add("section");
        System.out.println("Please choose from the following distance types:");
        System.out.println("Hops");
        System.out.println("Indegree");
        System.out.println("Outdegree");
        System.out.println("Section");
        String lowerType;
        while (true) {
            String type = s.nextLine();
            lowerType = type.toLowerCase();
            if (!typeOptions.contains(lowerType)) {
                System.out.println("Please choose one of the above options");
            } else {
                break;
            }
        }
        Collection<Integer> intPath = wg.findPath(source, destination, lowerType);
        return wg.translateToArticleNames(intPath);
    }

    public void runUntilQuit(WikiGame wg) {
        boolean quit = false;
        while (!quit) {
            System.out.println(userInteraction(wg));
            System.out.println("Do you want to play again? (yes/no)");
            String quitString = s.next();
            String quitStringLower = quitString.toLowerCase().trim();
            while (true) {
                if (quitStringLower.compareTo("no") == 0) {
                    quit = true;
                    System.out.println("Thanks for playing!");
                    break;
                } else if (quitStringLower.compareTo("yes") == 0) {
                    break;
                } else {
                    System.out.println("Please type yes or no");
                    quitString = s.next();
                    quitStringLower = quitString.toLowerCase();
                }
            }
        }

    }

    public static void main(String[] args) {
        WikiGameRunner wgr = new WikiGameRunner();
        // File paths all in one place.
        final String id_map_file   = "data/clean/full_idmap_file.txt";
        final String graph_file    = "data/clean/full_graph_file.mtx";

//        final String id_map_file = "data/test/smallest_map.txt";
//        final String graph_file = "data/test/smallest_test.mtx";

        // Toggle for print statements.
        final boolean print_progress = WikiGame.isPrintProgress();
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
            System.out.println("\tLoading graph (90 seconds expected) ... ");
            startTime = System.nanoTime();
        }
        wg.loadGraph(id_map_file, graph_file);
        if (print_progress) {
            endTime = System.nanoTime();
            elapsedTime = endTime - startTime;
            System.out.println("\tFinished in " + elapsedTime / 1000000000. + " seconds!");
        }

        wgr.runUntilQuit(wg);

    }

}
