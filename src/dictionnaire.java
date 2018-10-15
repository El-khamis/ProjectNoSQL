import org.openrdf.model.Statement;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class dictionnaire {

    HashMap<String, Integer> hmap = new HashMap<String , Integer>();
    ArrayList<String> ourElements = new ArrayList<>();
    PrintWriter writer = new PrintWriter("Order.txt", "UTF-8");

    int cpt = 0;
    public dictionnaire() throws FileNotFoundException, UnsupportedEncodingException {
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
    }

    public HashMap<String, Integer > getHmap() {
        return hmap;
    }


    public void addArrayList(Statement st) throws FileNotFoundException, UnsupportedEncodingException {

        if (!ourElements.contains(st.getSubject().toString().split("/")[st.getSubject().toString().split("/").length - 1])) {
            ourElements.add(st.getSubject().toString().split("/")[st.getSubject().toString().split("/").length - 1].toLowerCase());
        }

        if (!ourElements.contains(st.getPredicate().toString().split("/")[st.getPredicate().toString().split("/").length - 1])) {
            ourElements.add(st.getPredicate().toString().split("/")[st.getPredicate().toString().split("/").length - 1].toLowerCase());
        }

        if (!ourElements.contains(st.getObject().toString().split("/")[st.getObject().toString().split("/").length - 1])) {
            ourElements.add(st.getObject().toString().split("/")[st.getObject().toString().split("/").length - 1].toLowerCase());
        }

        writer.println(st.getSubject().toString().split("/")[st.getSubject().toString().split("/").length - 1].toLowerCase()+","
                +st.getPredicate().toString().split("/")[st.getPredicate().toString().split("/").length - 1].toLowerCase() +","
                +st.getObject().toString().split("/")[st.getObject().toString().split("/").length - 1].toLowerCase());
    }


    public void makeDictionnary() throws IOException {
        PrintWriter writer2 = new PrintWriter("Dico.txt", "UTF-8");
        Collections.sort(ourElements);
        for (String s : ourElements) {
            writer.println(s);
            hmap.put(s, cpt);
            cpt++;
        }
        writer2.close();

        PrintWriter writer3 = new PrintWriter("FinalHashMap.txt", "UTF-8");


        // Open the file
        FileInputStream fstream = new FileInputStream("Order.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        while ((strLine = br.readLine()) != null) {
            // Print the content on the console
            String[] lineSplit = strLine.split(",");
            writer3.println("<"+ hmap.get(lineSplit[0]).toString()+","+hmap.get(lineSplit[1]).toString() +","+hmap.get(lineSplit[2]).toString()+">");
        }

        //Close the input stream
        br.close();
        writer3.close();
        writer.close();
    }



}
