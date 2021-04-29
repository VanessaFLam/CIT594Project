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
     * @param inFile The path of the csv file to process. The "full" file can be found at
     * "/data/raw/page.csv".
     * @param outFile The path of the csv file to write to. This file should be stored in the
     * "/data/clean/" directory.
     * @return
     */
    public static int getPageIds(String inFile, String outFile) {
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
