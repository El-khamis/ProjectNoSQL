import javafx.beans.value.ObservableBooleanValue;
import org.openrdf.model.Statement;
import java.util.TreeMap;
import java.util.Set;
import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class dictionnaire {
    int cpt = 0;

  public  HashMap<String, Integer> hmap = new HashMap<String , Integer>();
  public  HashMap<Integer,String > hmap_inverse = new HashMap<Integer , String>();
  public  int tailleMax = 0;
  //public  ArrayList<String> ourElements = new ArrayList<>();
    public HashSet<String> ourElements = new HashSet();
    //index
    //predicat en 1er
  public  TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> pos =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    //Stats
  public  TreeMap<Integer,Double> statistique_predicat = new TreeMap<Integer,Double>();
  public  TreeMap<Integer,TreeMap<Integer,Double>> statistique_predicat_objet = new TreeMap<Integer,TreeMap<Integer,Double>>();

    //objet en 1er
  public  TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> ops =new TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>>();
    //stats//
  public  TreeMap<Integer,Double> statistique_objet = new TreeMap<Integer,Double>();
  public  TreeMap<Integer,TreeMap<Integer,Double>> statistique_objet_predicat = new TreeMap<Integer,TreeMap<Integer,Double>>();


    //ArrayList a remplir depuis RDF
  public  ArrayList<String> objet_string = new ArrayList();
  public  ArrayList<String> sujet_string = new ArrayList();
  public  ArrayList<String> predicat_string = new ArrayList();

    //ArrayList a remplir depuis Arraylist<String>
  public  ArrayList<Integer> objet_int = new ArrayList();
  public  ArrayList<Integer> sujet_int = new ArrayList();
  public  ArrayList<Integer> predicat_int = new ArrayList();

    public dictionnaire() throws FileNotFoundException, UnsupportedEncodingException {


    }


    //fonction qui remplis l'array liste pour avoir la première version du dico

    /**
     *
     * @param st
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     * Virer les 3 if
     * utiliser les hashset pour éviter les doublons
     * ou bien vier les doublons ailleurs
     *
     */
    public void addArrayList(Statement st) throws FileNotFoundException, UnsupportedEncodingException {

        ourElements.add(st.getSubject().toString());
        sujet_string.add(st.getSubject().toString());

        this.tailleMax++;

        ourElements.add(st.getPredicate().toString());
        predicat_string.add(st.getPredicate().toString());

        ourElements.add(st.getObject().toString());
        objet_string.add(st.getObject().toString());

    }

     public void Index_creation(ArrayList<Integer> list1,ArrayList<Integer>  list2,ArrayList<Integer>  liste3,
                                TreeMap<Integer,TreeMap<Integer,TreeSet<Integer>>> index
                                ,TreeMap<Integer,Double>stat_secondaire) throws IOException {

         for(int i=0;i<list1.size();i++) {
             //si la tree map ne contient pas le Sujet en index
             if(!index.containsKey(list1.get(i))){
                //on crée une treemap object predicat qui vas contenir le predicat
                //on crée Treeset object qui lui va juste être une liste d'objets

                TreeSet<Integer> Objects= new TreeSet<Integer>();
                Objects.add( liste3.get(i));


                TreeMap<Integer,TreeSet<Integer>> Objects_predicat= new TreeMap<Integer,TreeSet<Integer>>();
                Objects_predicat.put( list2.get(i),Objects);

                index.put(list1.get(i),Objects_predicat);
                //--------------stats---------------//
                /*ici rien exist initialisation des stats
                stats_secondaire corespond au nombre d'occurence S
                statp statistique correspond au stat sur S P
                */

                stat_secondaire.put(list1.get(i),1.0);


            }
            //si le sujet existe déjà on recupère la treemap objet_predicat
            else{

                 //--------stats----------//
                 /* on incrément la valeur pour S */

                 double valeurStats =stat_secondaire.get(list1.get(i));
                 valeurStats = valeurStats+1;
                 stat_secondaire.put(list1.get(i),valeurStats);

                 //si le prédicat n'existe pas
               //  if(!Objects_predicat.containsKey( list2.get(i)))
                 if(!index.get(list1.get(i)).containsKey(list2.get(i)))
                 {


                     TreeSet<Integer> Objects= new TreeSet<Integer>();
                     Objects.add( liste3.get(i));

                     index.get(list1.get((i))).put(list2.get(i),Objects);

                 }
                 else {
                     index.get(list1.get((i))).get(list2.get(i)).add(liste3.get(i));

                    //-------------------------stats----------------------//

                     //on recupère la valeur dans statistique qui correspond à S et P

                 }
             }
         }
     }

    public void makeDictionnary() throws IOException {
        long start = System.currentTimeMillis();
        float elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
         int compteur = 0;
        //ourElements se doit de contenir uniquement les valeurs unique sinon il y aurai 2 cpt différents qui pointents vers le meme element
        //Collections.sort(ourElements);
        //on remplie les dico de bases
        for (String s : ourElements) {
            //a voir pour treemap ?
            hmap.put(s, cpt);
            hmap_inverse.put(cpt,s);
            cpt++;
        }

        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        System.out.println("Fin première boucle "+ elapsedTime +"sec");

        for (String s: sujet_string) {
            sujet_int.add(hmap.get(s));
            //on récupère les key assossier aux objet en fonction de leur position dans l'array list
            //reconstruction triplet sous forme d'arraylist d'index
            objet_int.add(hmap.get(objet_string.get(compteur)));
            predicat_int.add(hmap.get(predicat_string.get(compteur)));
            compteur++;
        }


        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        System.out.println("Fin deuxieme boucle "+ elapsedTime +"sec");

        System.out.println("Le compteur fait "+compteur+" lignes ");

     }

}
