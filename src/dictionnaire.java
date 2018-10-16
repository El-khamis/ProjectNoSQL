import org.openrdf.model.Statement;
import java.util.TreeMap;
import java.util.Set;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class dictionnaire {

    HashMap<String, Integer> hmap = new HashMap<String , Integer>();
    HashMap<Integer,String > hmap_inverse = new HashMap<Integer , String>();

    ArrayList<String> ourElements = new ArrayList<>();

    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> pos =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();




    PrintWriter writer = new PrintWriter("Order.txt", "UTF-8");

    int cpt = 0;
    public dictionnaire() throws FileNotFoundException, UnsupportedEncodingException {
        HashMap<String, Integer> hmap = new HashMap<String, Integer>();
        HashMap<Integer,String > hmap_inverse = new HashMap<Integer, String>();
    }

    public HashMap<String, Integer > getHmap() {
        return hmap;
    }

    //fonction qui remplis l'array liste pour avoir la première version du dico
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

    //algo création index pos
     public void Pos_creation() throws IOException {
         FileInputStream fstream = new FileInputStream("FinalHashMap.txt");
         BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

         String strLine;

         while ((strLine = br.readLine()) != null) {
             // Print the content on the console
             String[] lineSplit = strLine.split(",");

             //si la tree map ne contient pas le Sujet en index
             //integer parse int utilisé car les nombres sont vue comme des string et non des int
             if(!pos.containsKey(Integer.parseInt(lineSplit[0])))
            {
                //on crée une treemap object predicat qui vas contenir le predicat
                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                //on crée Treeset object qui lui va juste être une liste d'objets
                TreeSet<Integer> Objects= new TreeSet<Integer>();
                Objects.add( Integer.parseInt(lineSplit[2]));
                Objects_predicat.put( Integer.parseInt(lineSplit[1]),Objects);
            }






         }




     }





    public void makeDictionnary() throws IOException {
        PrintWriter writer2 = new PrintWriter("Dico.txt", "UTF-8");
        Collections.sort(ourElements);
        //on remplie les dico de bases
        for (String s : ourElements) {
            writer.println(s);
            hmap.put(s, cpt);
            hmap_inverse.put(cpt,s);
            cpt++;
        }
        ourElements.clear();
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

            writer3.println(""+ hmap.get(lineSplit[0]).toString()+","+hmap.get(lineSplit[1]).toString() +","+hmap.get(lineSplit[2]).toString()+"");
        }

        //Close the input stream
        br.close();
        writer3.close();
        writer.close();
    }



}
