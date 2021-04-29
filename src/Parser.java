import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Provides functionality to parse raw Wikipedia data to files used to populate data structures.
 * @author Jackson Golden
 */
public class Parser {

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
    public static int cleanPageCSV(String inFile, String outFile) {
        return 0;
    }
    
    /**
     * Parses a JSONL file of links as given by the Wikipedia data and translates to a .mtx file.
     * This file has a header row of form <# articles> <# links> (i.e., <# vertices> <# edges>) and
     * data rows of the form <page_id_from> <page_id_to> (i.e., there is a link from the article
     * given by page_id_from to the article given by page_id_to).
     * 
     * @param inFile The path of the JSONL file to process. The "full" file can be found at
     * "data/raw/link_annotated_text.jsonl".
     * @param outFile The path of the .mtx file to write to. This file should be stored in the
     * "/data/clean/" directory or a subdirectory thereof.
     * @return The number of links successfully parsed. This will be equivalent to the number of
     * rows in the output file (not including header).
     */
    public static int parseLinkData(String inFile, String outFile) {
        return 0;
    }
    
    
    
    public static void main(String[] args) {
//        
        // ALSO INCLUDE HOW FAR DOWN?
        
        
//        try {
//            BufferedReader br = new BufferedReader(new FileReader("data/raw/link_annotated_text.jsonl"));
//            BufferedWriter bw = new BufferedWriter(new FileWriter("data/jsonTest.txt"));            
//            String line;
//            line = br.readLine();
//            while (line != null) {
//              Object obj = new JSONParser().parse(line); // each line is a JSON (bc .jsonl file)
//              JSONObject jo = (JSONObject) obj;          // turn into JSONObject
//              long page_id = (long) jo.get("page_id");   // get the page id
//              JSONArray sections = (JSONArray) jo.get("sections"); // get list of JSONObjects for sections
//              for (int i = 0; i < sections.size(); i++) {          // go through each section
//                  JSONArray section_links = (JSONArray) ((JSONObject) sections.get(i)).get("target_page_ids"); // list of links is a JSONArray
//                  for (int j = 0; j < section_links.size(); j++) {
//                      bw.write(page_id + "\t" + section_links.get(j) + "\n");
//                      System.out.println(page_id + "\t" + section_links.get(j));
//                  }
//                  bw.flush();
//              }                
//              line = br.readLine(); 
//            }
//            bw.close();
//            br.close();
//            
//            
////            Object obj = new JSONParser().parse(br);
//        
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } 
//        catch (ParseException e) {
//            e.printStackTrace();
//        }
//        
//     
        System.out.println("test");
    }
}
