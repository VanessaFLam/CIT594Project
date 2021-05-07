import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Provides functionality to clean / parse raw data to files used to populate data structures.
 * @author Jackson Golden
 */
public class DataCleaner {

    // =============================================================================
    // = OUTPUT FILE CREATION
    // =============================================================================
    
    /**
     * Process a csv file with four columns (page_id, item_id, title, views) and builds a Map from
     * page_id to title.
     * 
     * @param filePath The path of the csv file to process. The "full" file can be found at
     * "/data/raw/page.csv".
     * @return A mapping from page_id to article title.
     */
    public static Map<Long, String> buildWikiIDtoArticleNameMap(String filePath) {
        
        // Set up reader / writer. 
        try {         

            // Initialize reader and Map.
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            Map<Long, String> wikiIDtoArticleName = new HashMap<Long, String>();

            // Read each line, add to map.
            String line;
            line = br.readLine(); // skip header row
            line = br.readLine();
            while (line != null) {                
                
                try {
                    String[] toks = line.split(",(?=([^\"]|\"[^\"]*\")*$)"); // allow commas in ""s
                    long wikiID = Long.parseLong(toks[0]);
                    String articleName = toks[2];
                    wikiIDtoArticleName.put(wikiID, articleName);
                    
                } catch (Exception e) {
                    System.out.println("Error in buildWikiIDtoArticleNameMap parsing: " + line);
                }
                line = br.readLine();
                
            }

            // Close and return.
            br.close();
            return wikiIDtoArticleName;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        return null; // return null on failure
    
    }
        
    // ---------------------------------------------------------------------------------------------
        
    /**
     * Parses a JSONL file of links as given by the Wikipedia data. Returns two files:
     *   
     *   (1) A "graph file" (.mtx). This file has a header row of form <# articles> <# links> 
     *   (i.e., <# vertices> <# edges>) and data rows of the form 
     *   <wiki_id_from> <wiki_id_to> <link_section> (i.e., there is a link from the article given 
     *   by wiki_id_from to the article given by wiki_id_to in the link_section-th section of 
     *   the former).
     *   
     *   (2) An "id map file" (.txt). Each row in this file is of form 
     *   "<node_id> <page_id> <article_title>", so it can be used to translate between these forms
     *   of identifying an article.
     * 
     * @param inFile The path of the JSONL file to process. The "full" file can be found at
     * "data/raw/link_annotated_text.jsonl".
     * @param wikiIDtoArticleNameMap A mapping built by buildWikiIDtoArticleNameMap.
     * @param graphFile The path of the .mtx file to write to. This file should be stored in the
     * "/data/clean/" directory or a subdirectory thereof.
     * @param idMapFile The path of the .txt file to write to. This file should be stored in the
     * "/data/clean/" directory or a subdirectory thereof.
     * @return The header row of the output file.
     */
    public static String parseLinkData(String inFile, Map<Long, String> wikiIDtoArticleNameMap,
                                        String graphFile, String idMapFile) {
        
        final String logPath = "data/logs/data_cleaner_error_log.txt";
        
        try {
            
            // Set up counters for progress logging.
            long inRows  = 0;
            long outRows = 0;            
            
            // Set up reader / writers. Use a temporary file for initial pass of graph file.
            BufferedReader graphBR = new BufferedReader(new FileReader(inFile));
            BufferedWriter graphBW = new BufferedWriter(new FileWriter("temp_graph.mtx"));
            BufferedReader idMapBR;
            BufferedWriter idMapBW = new BufferedWriter(new FileWriter("temp_idmap.txt"));
            BufferedWriter logBW   = new BufferedWriter(new FileWriter(logPath));
            
            // Initialize TreeSet for tracking page_ids seen and counters.
            Collection<Long> pageIDs = new TreeSet<Long>();                        
            long pagesCount = 0;
            long linksCount = 0;
        
            // Read file line-by-line to create temp file.
            String line;
            line = graphBR.readLine();
            while (line != null) {
                
                // Logging.
                inRows++;
                if ((inRows % 10000) == 0) {
                    System.out.println("Processing row " + inRows + "...");
                }
                
                // Follow documentation's parsing pattern.
                Object obj = new JSONParser().parse(line);             // Each line is a JSON.
                JSONObject jo = (JSONObject) obj;                      // Cast to JSONObject.
                
                // Process new JSON (represents a page).
                long wikiID = (long) jo.get("page_id");                // Get wiki id of source.

                if (pageIDs.add(wikiID)) {                             // If new, add to set.

                    String articleName = wikiIDtoArticleNameMap.get(wikiID); // Get article name.

                    if (articleName != null) {                         // Add to id map file if can.

                        idMapBW.write(pagesCount + "\t"
                                        + wikiID + "\t"
                                        + articleName + "\n");     
                        idMapBW.flush();
                        pagesCount++;                                  // Increment counter.
                        
                    } else {                                           // Otherwise log error.
                        
                        logBW.write("Failed to match wikiID: " + wikiID + "\n");
                        
                    }
                                        
                }
                
                JSONArray sections = (JSONArray) jo.get("sections");   // Get list of JSONObjects.
                                                                       // (One for each section.)
                TreeSet<Long> linksSeen = new TreeSet<Long>();         // To avoid duplicates.
                
                // Iterate over sections.
                int numSections = sections.size();
                for (int i = 0; i < numSections; i++) {
                    
                    // Get all links (list of page_ids linked to).
                    JSONArray sectionLinks = 
                            (JSONArray) ((JSONObject) sections.get(i)).get("target_page_ids");
                    
                    // Iterate over links.
                    int numSectionLinks = sectionLinks.size();
                    for (int j = 0; j < numSectionLinks; j++) {
                        
                        long linkWikiID = (long) sectionLinks.get(j); // Get page id of link.
                        if (pageIDs.add(linkWikiID)) {                // If new, add to set.

                            // Get article name.
                            String articleName = wikiIDtoArticleNameMap.get(linkWikiID);

                            if (articleName != null) {                   // Add to id map file.

                                idMapBW.write(pagesCount + "\t"
                                        + linkWikiID + "\t"
                                        + articleName + "\n");     
                                idMapBW.flush();
                                pagesCount++;                           // Increment counter.

                            } else {                                     // Otherwise log error.

                                logBW.write("Failed to match wikiID: " + linkWikiID + "\n");

                            }

                        }
                        if (!linksSeen.contains(linkWikiID)) {        // If new link for page...
                            linksCount++;                               // - Increment link count.
                            linksSeen.add(linkWikiID);                // - Update list seen.
                            graphBW.write(wikiID + "\t"                      // - Write to file.
                                    + linkWikiID + "\t" 
                                    + i + "\n");  
                        }
                        
                    }
                    graphBW.flush();
                
                }
                
                // Get next line.
                line = graphBR.readLine();
                
            }                                 
            graphBW.close();
            graphBR.close();
            idMapBW.close();

            // Set header.
            String header = pagesCount + "\t" + linksCount;
            
            // Logging.
            System.out.println("Initial processing complete. " + inRows + " processed.");
            System.out.println("Header: " + header);
            System.out.println("Number of nodes: " + pagesCount);
            
            // Write headers to new files and copy temp file contents.            
            graphBR = new BufferedReader(new FileReader("temp_graph.mtx"));
            graphBW = new BufferedWriter(new FileWriter(graphFile));
            idMapBR = new BufferedReader(new FileReader("temp_idmap.txt"));
            idMapBW = new BufferedWriter(new FileWriter(idMapFile));
            
            graphBW.write(header + "\n");
            graphBW.flush();
            idMapBW.write(pagesCount + "\n");
            idMapBW.flush();
            
            line = idMapBR.readLine();
            while (line != null) {
                idMapBW.write(line + "\n");
                idMapBW.flush();
                line = idMapBR.readLine();
            }
             
            line = graphBR.readLine();
            while (line != null) {
                outRows++;
                if ((outRows % 10000) == 0) {
                    System.out.println(outRows + " of " + linksCount + " rows copied...");
                }
                graphBW.write(line + "\n");
                graphBW.flush();
                line = graphBR.readLine();
            }
            graphBW.close();
            idMapBW.close();
            graphBR.close();
            idMapBR.close();
            logBW.close();
            
            // Delete temp files
            File temp = new File("temp_graph.mtx");
            temp.delete();
            temp = new File("temp_idmap.txt");
            temp.delete();
            
            // Logging.
            System.out.println("File generation complete.");
            
            return header;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
                        
        return null;
        
    }    

    // =============================================================================
    // = FILE SNIPPET CREATION
    // =============================================================================
    
    /**
     * Copies the first {@code n} lines of the "full" csv file "data/raw/page.csv"
     * to a file stored in the same directory with the suffix "_first_n" added.
     * 
     * @param n The number of lines to include
     */
    @SuppressWarnings("unused")
    private static void getCSVFileSnippet(int n) {
        
        try {
            
            // Set up reader / writer. 
            BufferedReader br = new BufferedReader(new FileReader("data/raw/page.csv"));
            BufferedWriter bw = new BufferedWriter(
                    new FileWriter("data/raw/page" + "_first_" + n + ".csv"));
            
            // Read / write first n lines to new file.
            String line;
            line = br.readLine();
            while (n > 0) {
                bw.write(line + "\n");
                n--;
                bw.flush();
                line = br.readLine();
            }
            
            // Close reader / writer.
            bw.close();
            br.close();
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    // ---------------------------------------------------------------------------------------------

    /**
     * Copies the first {@code n} lines of the "full" link file "data/raw/link_annotated_text.jsonl"
     * to a file stored in the same directory with the suffix "_first_n" added.
     * 
     * @param n The number of lines to include
     */
    private static void getLinkFileSnippet(int n) {
        
        try {
            
            // Set up reader / writer.
            FileReader     fr = new FileReader("data/raw/link_annotated_text.jsonl");
            BufferedReader br = new BufferedReader(fr);
            FileWriter     fw = new FileWriter(
                                        "data/raw/link_annotated_text" + "_first_" + n + ".jsonl");
            BufferedWriter bw = new BufferedWriter(fw);
        
            // Read / write first n lines to new file.
            String line;
            line = br.readLine();
            while (n > 0) {
                bw.write(line + "\n");
                n--;
                bw.flush();
                line = br.readLine();
            }
            
            // Close reader / writer.
            bw.close();
            br.close();
        
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    // #############################################################################
    // = MAIN FUNCTION - USE TO GENERATE DATA FILES
    // #############################################################################
//    
//    /**
//     * Toggle the relevant sections to generate files.
//     */
//    public static void main(String[] args) {
//        
//        long startTime = System.nanoTime();
//        
//        /* BUILD WIKI ID TO ARTICLE NAME MAP (TAKES ~30 SECONDS) */
//        Map<Long, String> wikiIDtoArticleNameMap = 
//                buildWikiIDtoArticleNameMap("data/raw/page.csv");
//            
//        /* CREATE SNIPPET GRAPH & MAP FILES OF THE FIRST 1/10/100 LINES OF THE LINKS FILE */
//        boolean createSnippets = false;   // << Toggle to "true" to generate.
//        if (createSnippets) {
//            getLinkFileSnippet(1);
//            getLinkFileSnippet(10);
//            getLinkFileSnippet(100);
//            parseLinkData("data/raw/link_annotated_text_first_1.jsonl", wikiIDtoArticleNameMap,
//                    "data/clean/graph_file_first_1.mtx", "data/clean/idmap_file_first_1.txt");
//            parseLinkData("data/raw/link_annotated_text_first_10.jsonl", wikiIDtoArticleNameMap,
//                    "data/clean/graph_file_first_10.mtx", "data/clean/idmap_file_first_10.txt");
//            parseLinkData("data/raw/link_annotated_text_first_100.jsonl", wikiIDtoArticleNameMap,
//                    "data/clean/graph_file_first_100.mtx", "data/clean/idmap_file_first_100.txt");
//        }
//        
//        /* CREATE FULL GRAPH & MAP FILES (TAKES ~15 MIN) */
//        boolean createFull = true;   // << Toggle to "true" to generate.
//        if (createFull) {
//            parseLinkData("data/raw/link_annotated_text.jsonl", wikiIDtoArticleNameMap,
//                    "data/clean/full_graph_file.mtx", "data/clean/full_idmap_file.txt");         
//        }
//        
//        long endTime   = System.nanoTime();
//        long totalTime = endTime - startTime;
//        System.out.println("Time to run: " + totalTime / 1000000000. + " seconds");
//        
//    }

}
