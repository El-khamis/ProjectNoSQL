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
    //sujet en 1er
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> sop =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> spo =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();

    //predicat en 1er
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> pos =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> pso =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();

    //objet en 1er
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> osp =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> ops =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();

    //ArrayList a remplir depuis RDF
    ArrayList<String> objet_string = new ArrayList();
    ArrayList<String> sujet_string = new ArrayList();
    ArrayList<String> predicat_string = new ArrayList();

    //ArrayList a remplir depuis Arraylist<String>
    ArrayList<Integer> objet_int = new ArrayList();
    ArrayList<Integer> sujet_int = new ArrayList();
    ArrayList<Integer> predicat_int = new ArrayList();




    int cpt = 0;
    public dictionnaire() throws FileNotFoundException, UnsupportedEncodingException {

    }


    //fonction qui remplis l'array liste pour avoir la première version du dico
    public void addArrayList(Statement st) throws FileNotFoundException, UnsupportedEncodingException {

        if (!ourElements.contains(st.getSubject().toString().toLowerCase())) {
            ourElements.add(st.getSubject().toString().toLowerCase());
        }
        sujet_string.add(st.getSubject().toString().toLowerCase());

        if (!ourElements.contains(st.getPredicate().toString().toLowerCase())) {
            ourElements.add(st.getPredicate().toString().toLowerCase());
        }
        predicat_string.add(st.getPredicate().toString().toLowerCase());


        if (!ourElements.contains(st.getObject().toString().toLowerCase())) {
            ourElements.add(st.getObject().toString().toLowerCase());
        }
        objet_string.add(st.getObject().toString().toLowerCase());


    }



    //algo création index sop
    //l'algo créeé toutes les index à 3 valeurs
    /*ATTENTION bien spécifier l'index et verifier l'ordre des split exemple pour le spo:

     sujet=split[0]=>0
     objet=split[1]=>1
     predicat=split[2]=>2
     index=spo
objet_int sujet_int predicat_int
     */
     public void Index_creation(ArrayList<Integer> sujet,ArrayList<Integer>  predicat,ArrayList<Integer>  objet,TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> index) throws IOException {



         for(int i=0;i<sujet_int.size();i++)

         {
             //si la tree map ne contient pas le Sujet en index
             //integer parse int utilisé car les nombres sont vue comme des string et non des int
             if(!index.containsKey(sujet.get(i)))
            {
                //on crée une treemap object predicat qui vas contenir le predicat
                //on crée Treeset object qui lui va juste être une liste d'objets
                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                TreeSet<Integer> Objects= new TreeSet<Integer>();

                Objects.add( objet.get(i));
                Objects_predicat.put( predicat.get(i),Objects);

                index.put(sujet.get(i),Objects_predicat);
            }
            //si la le sujet existe déjà on recupère la treemap objet_predicat
            else{
                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                 Objects_predicat=index.get(sujet.get(i));

                 //si le prédicat n'existe pas
                 if(!Objects_predicat.containsKey( predicat.get(i)))
                 {
                     //on créeé une nouvelle liste
                     //on ajoute le nouvelle objet qui correspond au dernier argument de l'index qu'on veut
                     //on remplace par la nouvelle liste d'object
                     //on remplace par le nouvelle arbre

                     TreeSet<Integer> Objects= new TreeSet<Integer>();
                     Objects.add(objet.get(i));
                     Objects_predicat.put(predicat.get(i),Objects);
                     index.replace(sujet.get(i),Objects_predicat);
                 }
                 else {

                     TreeSet<Integer> Objects= new TreeSet<Integer>();
                     Objects=Objects_predicat.get( predicat.get(i));
                     Objects.add(sujet.get(i));

                     //on remplace par la nouvelle liste d'object
                     //on remplace par le nouvelle arbre
                     Objects_predicat.replace(predicat.get(i),Objects);
                     index.replace(sujet.get(i),Objects_predicat);

                 }

             }

         }
     }

    public void makeDictionnary() throws IOException {

         int compteur = 0;

        Collections.sort(ourElements);
        //on remplie les dico de bases
        for (String s : ourElements) {
            hmap.put(s, cpt);
            hmap_inverse.put(cpt,s);
            cpt++;
        }

        for (String s: sujet_string) {
            sujet_int.add(hmap.get(s));
            //on récupère les key assossier aux objet en fonction de leur position dans l'array list
            //reconstruction triplet sous forme d'arraylist d'index
            objet_int.add(hmap.get(objet_string.get(compteur)));
            predicat_int.add(hmap.get(predicat_string.get(compteur)));
            compteur++;
        }
        System.out.println("Le compteur fait "+compteur+" lignes ");

     }


}
