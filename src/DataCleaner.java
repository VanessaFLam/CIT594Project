import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.TreeMap;
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

    /**
     * Takes in a csv file with four columns (page_id, item_id, title, views) and copies just
     * page_id and title to a new csv file.
     * 
     * @param inFile The path of the csv file to process. The "full" file can be found at
     * "/data/raw/page.csv".
     * @param outFile The path of the csv file to write to. This file should be stored in the
     * "/data/clean/" directory or a subdirectory thereof.
     * @return The number of rows successfully parsed (not including header).
     */
    public static long cleanPageCSV(String inFile, String outFile) {
        
        // Set up reader / writer. 
        try {         
            
            // Initialize reader, writer, and counter.
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            long counter = 0;
            
            // Read each line, remove unneeded fields, write to output file.
            String line;
            line = br.readLine();
            while (line != null) {                
            
                counter++;
                String[] split = line.split(",(?=([^\"]|\"[^\"]*\")*$)"); // allow commas in ""s
                bw.write(split[0] + "\t" + split[2] + "\n");                
                line = br.readLine();
            
            }
            
            // Close and return.
            bw.close();
            br.close();
            return counter;
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    
    /**
     * Copies the first {@code n} lines of the "full" csv file "data/raw/page.csv"
     * to a file stored in the same directory with the suffix "_first_n" added.
     * 
     * @param n The number of lines to include
     */
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
    
    
    /**
     * Parses a JSONL file of links as given by the Wikipedia data and translates to a .mtx file.
     * This file has a header row of form <# articles> <# links> (i.e., <# vertices> <# edges>) and
     * data rows of the form <page_id_from> <page_id_to> <link_section> (i.e., there is a link from 
     * the article given by page_id_from to the article given by page_id_to in the link_section-th
     * section of the article).
     * 
     * @param inFile The path of the JSONL file to process. The "full" file can be found at
     * "data/raw/link_annotated_text.jsonl".
     * @param outFile The path of the .mtx file to write to. This file should be stored in the
     * "/data/clean/" directory or a subdirectory thereof.
     * @return The header row of the output file.
     */
    public static String parseLinkData(String inFile, String outFile) {
        
        try {
            
            // Set up counters for progress logging.
            long inRows  = 0;
            long outRows = 0;            
            
            // Set up reader / writer. Use a temporary file for initial pass.
            BufferedReader br = new BufferedReader(new FileReader(inFile));
            BufferedWriter bw = new BufferedWriter(new FileWriter("temp.mtx"));
            
            // Initialize TreeSet for tracking page_ids seen and counters.
            Collection<Long> page_ids = new TreeSet<Long>();                        
            long pages_count = 0;
            long links_count = 0;
        
            // Read file line-by-line to create temp file.
            String line;
            line = br.readLine();
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
                long page_id = (long) jo.get("page_id");               // Get page id.
                if (page_ids.add(page_id)) {                           // If new page id, increment.
                    pages_count++;
                }
                JSONArray sections = (JSONArray) jo.get("sections");   // Get list of JSONObjects.
                                                                       // (One for each section.)
                TreeSet<Long> links_seen = new TreeSet<Long>();        // To avoid duplicates.
                
                // Iterate over sections.
                int num_sections = sections.size();
                for (int i = 0; i < num_sections; i++) {
                    
                    // Get all links (list of page_ids linked to).
                    JSONArray section_links = 
                            (JSONArray) ((JSONObject) sections.get(i)).get("target_page_ids");
                    
                    // Iterate over links.
                    int num_section_links = section_links.size();
                    for (int j = 0; j < num_section_links; j++) {
                        
                        long link_page_id = (long) section_links.get(j); // Get page id of link.
                        if (page_ids.add(link_page_id)) {                // Update if new page.
                            pages_count++;
                        }
                        if (!links_seen.contains(link_page_id)) {        // If new link for page...
                            links_count++;                               // - Increment link count.
                            links_seen.add(link_page_id);                // - Update list seen.
                            bw.write(page_id + "\t"                      // - Write to file.
                                    + link_page_id + "\t" 
                                    + i + "\n");  
                        }
                        
                    }
                    bw.flush();                    
                
                }
                
                // Get next line.
                line = br.readLine();
                
            }                                 
            bw.close();
            br.close();

            // Set header.
            String header = pages_count + "\t" + links_count;
            
            // Logging.
            System.out.println("Initial processing complete. " + inRows + " processed.");
            System.out.println("Header: " + header);
            
            // Write header to new file and copy temp file contents.            
            br = new BufferedReader(new FileReader("temp.mtx"));
            bw = new BufferedWriter(new FileWriter(outFile));
            
            bw.write(header + "\n");
            bw.flush();
            
            line = br.readLine();
            while (line != null) {
                outRows++;
                if ((outRows % 10000) == 0) {
                    System.out.println(outRows + " of " + links_count + " rows copied...");
                }
                bw.write(line + "\n");
                bw.flush();
                line = br.readLine();
            }
            bw.close();
            br.close();
            
            // Delete temp file.
            File temp = new File("temp.mtx");
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
    

    /**
     * Uncomment the relevant sections to generate files.
     * 
     * @param args Unused.
     */
    public static void main(String[] args) {
        
        long startTime = System.nanoTime();

//        /* CREATE SNIPPET PAGE_IDS FILES OF THE FIRST 1/10/100 LINES OF THE CSV FILE */
//        getCSVFileSnippet(1);
//        getCSVFileSnippet(10);
//        getCSVFileSnippet(100);
//        System.out.println(
//                cleanPageCSV("data/raw/page_first_1.csv", "data/clean/page_ids_first_1.csv"));
//        System.out.println(
//                cleanPageCSV("data/raw/page_first_10.csv", "data/clean/page_ids_first_10.csv"));
//        System.out.println(
//                cleanPageCSV("data/raw/page_first_100.csv", "data/clean/page_ids_first_100.csv"));
        
        
        /* CREATE FULL PAGE_IDS FILE (TAKES ~15 SECONDS) */
//        System.out.println(
//              cleanPageCSV("data/raw/page.csv", "data/clean/page_ids.csv"));
       
        
        /* CREATE SNIPPET GRAPH FILES OF THE FIRST 1/10/100 LINES OF THE LINKS FILE */
//        getLinkFileSnippet(1);
//        getLinkFileSnippet(10);
//        getLinkFileSnippet(100);
//        System.out.println(parseLinkData("data/raw/link_annotated_text_first_1.jsonl",
//                "data/clean/graph_file_first_1.mtx"));
//        System.out.println(parseLinkData("data/raw/link_annotated_text_first_10.jsonl",
//                "data/clean/graph_file_first_10.mtx"));
//        System.out.println(parseLinkData("data/raw/link_annotated_text_first_100.jsonl",
//                                            "data/clean/graph_file_first_100.mtx"));

        
        /* CREATE FULL GRAPH FILE (TAKES ~15 MIN) */
        System.out.println(parseLinkData("data/raw/link_annotated_text.jsonl",
                                              "data/clean/full_graph_file.mtx"));
        
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("Time to run: " + totalTime / 1000000000. + " seconds");
        
    }

}
