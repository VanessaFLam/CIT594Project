import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DataExplorer {

    Map<Integer, String> m;
    Map<String, Integer> n;
    
    public DataExplorer() {
        m = new HashMap<Integer, String>();
        n = new HashMap<String, Integer>();        
    }
    
    public void buildMaps(String filePath) {

        try {

            // Create reader.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            
            // Header tells number of nodes.
            String line = br.readLine();
            int numOfNodes = Integer.parseInt(line);
            int counter = 0;

            System.out.println("Building wikiID <--> article name maps. " + numOfNodes + " nodes in graph ...");
            

            // Read each line in the file.
            // Each line is of form "<node_id> <page_id> <article_title>".
            while ((line = br.readLine()) != null) {

                // Try to parse to "<node_id> <page_id> <article_title>". Log error if not.
                try {

                    // Get tokens.
                    String[] toks = line.split("\t");         
                    int nodeID = Integer.parseInt(toks[0]);
                    int wikiID = Integer.parseInt(toks[1]);
                    String articleName = toks[2];
                    String lCaseName = articleName.toLowerCase();
                    if (lCaseName == null) {
                        System.out.println(line);
                    }

                    // Add to maps.
                    m.put(wikiID, lCaseName);
                    n.put(lCaseName, wikiID);
                    counter++;
                    if ((counter % (numOfNodes / 20)) == 0) {
                        System.out.println("\t" + counter + " nodes processed (" 
                                + Math.round((float) (100 * counter) / numOfNodes) + "%) ...");
                        }
                  
                } catch (Exception e) {

                    e.printStackTrace();
                    
                }               

            }

            // Close reader/writer.
            br.close();

            // Return map.
            return;

        } catch (IOException e) {
            e.printStackTrace();
            return;          
        }

    }

    public static void main(String[] args) {

        DataExplorer d = new DataExplorer();
        d.buildMaps("data/clean/full_idmap_file.txt");        
        Scanner s = new Scanner(System.in);
        
        while (true) {
            System.out.println("enter 1 to get ID from article name.");
            System.out.println("enter 2 to get article name from ID.");

            String inputLine = s.nextLine();
            int selection = Integer.parseInt(inputLine);
            
            if (selection == 1) {
                System.out.println("enter article name:");
                inputLine = s.nextLine();
                String lIn = inputLine.toLowerCase();
                System.out.println("wiki ID: " + d.n.get(lIn));   
            } else if (selection == 2) {
                System.out.println("enter wiki ID:");
                inputLine = s.nextLine();
                int inputID = Integer.parseInt(inputLine);
                System.out.println("article name: " + d.m.get(inputID));                            
            }
            
        }
        
    }

}
