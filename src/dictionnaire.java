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

    //index
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> pos =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> sop =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> osp =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();


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



    //algo création index sop
    //l'algo créeé toutes les index à 3 valeurs
    /*ATTENTION bien spécifier l'index et verifier l'ordre des split exemple pour le spo:

     sujet=split[0]=>0
     objet=split[1]=>1
     predicat=split[2]=>2
     index=spo

     */
     public void Index_creation(int sujet,int predicat,int objet,TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> index) throws IOException {


         FileInputStream fstream = new FileInputStream("FinalHashMap.txt");
         BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

         String strLine;

         while ((strLine = br.readLine()) != null) {
             // Print the content on the console
             String[] lineSplit = strLine.split(",");

             //si la tree map ne contient pas le Sujet en index
             //integer parse int utilisé car les nombres sont vue comme des string et non des int
             if(!index.containsKey(Integer.parseInt(lineSplit[sujet])))
            {
                //on crée une treemap object predicat qui vas contenir le predicat
                //on crée Treeset object qui lui va juste être une liste d'objets
                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                TreeSet<Integer> Objects= new TreeSet<Integer>();

                Objects.add( Integer.parseInt(lineSplit[objet]));
                Objects_predicat.put( Integer.parseInt(lineSplit[predicat]),Objects);

                index.put(Integer.parseInt(lineSplit[sujet]),Objects_predicat);
            }
            //si la le sujet existe déjà on recupère la treemap objet_predicat
            else{
                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                 Objects_predicat=index.get(Integer.parseInt(lineSplit[sujet]));

                 //si le prédicat n'existe pas
                 if(!Objects_predicat.containsKey(Integer.parseInt(lineSplit[predicat])))
                 {
                     //on créeé une nouvelle liste
                     //on ajoute le nouvelle objet qui correspond au dernier argument de l'index qu'on veut
                     //on remplace par la nouvelle liste d'object
                     //on remplace par le nouvelle arbre

                     TreeSet<Integer> Objects= new TreeSet<Integer>();
                     Objects.add(Integer.parseInt(lineSplit[objet]));
                     Objects_predicat.put(Integer.parseInt(lineSplit[predicat]),Objects);
                     index.replace(Integer.parseInt(lineSplit[sujet]),Objects_predicat);
                 }
                 else {

                     TreeSet<Integer> Objects= new TreeSet<Integer>();
                     Objects=Objects_predicat.get( Integer.parseInt(lineSplit[predicat]));
                     Objects.add(Integer.parseInt(lineSplit[objet]));

                     //on remplace par la nouvelle liste d'object
                     //on remplace par le nouvelle arbre
                     Objects_predicat.replace(Integer.parseInt(lineSplit[predicat]),Objects);
                     index.replace(Integer.parseInt(lineSplit[sujet]),Objects_predicat);

                 }

             }

             br.close();




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
