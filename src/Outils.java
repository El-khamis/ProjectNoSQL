import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Iterator;

import static com.sun.org.apache.xml.internal.security.keys.keyresolver.KeyResolver.iterator;

public class Outils {


    String maVariable;



    public void parse_requete(String s,dictionnaire dict){
        s = s.replaceAll("\t", "");
        String[] corps = s.split("\\{");
        String[] entete = corps[0].split(" ");
        String[] bodysplit;
        String body;
        ArrayList<String> predicats = new ArrayList<>();
        ArrayList<String> objets = new ArrayList<>();
        ArrayList<Integer> predicats_int = new ArrayList<Integer>();
        ArrayList<Integer> objets_int = new ArrayList<Integer>();

        for (String s1 : entete) {
            if (s1.contains("?")) {
                maVariable = s1;
            }
        }

        for (String s1 : corps[1].split(" ")) {
            if (s1.contains("?") && !s1.equals(maVariable)) {
                System.out.println("La requete n'est pas une requete étoile\n");
                return;
            }
        }
        body = corps[1];
        bodysplit = body.replace("}","").split(" \\. ");

        String temp[];
        for(String s1: bodysplit ) {
            temp = s1.split(" ");


            if(temp[0].isEmpty())
            {
                objets.add(temp[3]);
                temp[3] = temp[3].replace("<",""); temp[3] = temp[3].replace(">","");
                objets_int.add(dict.hmap.get(temp[3]));
                predicats.add(temp[2]);
                temp[2] = temp[2].replace("<","");temp[2] = temp[2].replace(">","");
                predicats_int.add(dict.hmap.get(temp[2]));

            }
            else {
                objets.add(temp[2]);
                temp[2] = temp[2].replace("<","");temp[2] = temp[2].replace(">","");
                objets_int.add(dict.hmap.get(temp[2]));
                predicats.add(temp[1]);
                temp[1]=temp[1].replace("<",""); temp[1]= temp[1].replace(">","");
                predicats_int.add(dict.hmap.get(temp[1]));
            }

        }
        //execute_requete(predicats_int.get(0),objets_int.get(0),predicats_int.get(1),objets_int.get(1),predicats_int.get(2),objets_int.get(2),dict);
        execute_requetev2(predicats_int,objets_int,dict);
        //      System.out.println("La requete est une requete étoile dont la variable est "+maVariable+" et dont les prédicats sont :"+ predicats.toString()+""+predicats_int.toString()+"\n");
        //     System.out.println("La requete est une requete étoile dont la variable est "+maVariable+" et dont les objets sont :"+ objets.toString()+""+objets_int.toString()+"\n");

//
//        predicats_int.clear();
//        objets_int.clear();
    }


    /**
     *
     * @param p1 predicat 1
     * @param o1 objet 1
     * @param p2 predicat 2
     * @param o2 objet 2
     * @param p3 predicat 3
     * @param o3 objet 3
     * @param dict instance du dico courant
     *
     * Cette fonction a pour but d'executer une requete préalablement parser
     */
    public void execute_requete(int p1, int o1, int p2, int o2, int p3, int o3, dictionnaire dict){
        int premierPartieStat, deuxiemePartieStat, troisiemePartieStat;
        TreeSet premierPartie, deuxiemePartie, troisiemePartie;
        ArrayList<Integer> valeur= new  ArrayList<Integer> () ;

        /**
         *  On compare les stats sur les indexes afin de récupérer le treeset le plus petit( le + selectif)
         **/

        /**
         * On doit check si les valeurs p1 o1 existent vraiment
         */
        if(dict.statistique_objet.containsKey(o1) && dict.statistique_objet.containsKey(o2) && dict.statistique_objet.containsKey(o3) &&
                dict.statistique_predicat.containsKey(p1) &&  dict.statistique_predicat.containsKey(p2) && dict.statistique_predicat.containsKey(p3))
        {



            if(dict.statistique_predicat.get(p1)> dict.statistique_objet.get(o1)){
                premierPartie = dict.ops.get(o1).get(p1);
                premierPartieStat = premierPartie.size();
            }
            else{
                premierPartie = dict.pos.get(p1).get(o1);
                premierPartieStat = premierPartie.size();
            }

            if(dict.statistique_predicat.get(p2)> dict.statistique_objet.get(o2)){
                deuxiemePartie = dict.ops.get(o2).get(p2);
                deuxiemePartieStat = deuxiemePartie.size();
            }
            else{
                deuxiemePartie = dict.pos.get(p2).get(o2);
                deuxiemePartieStat = deuxiemePartie.size();
            }

            if(dict.statistique_predicat.get(p3)> dict.statistique_objet.get(o3)){
                troisiemePartie = dict.ops.get(o3).get(p3);
                troisiemePartieStat = troisiemePartie.size();
            }
            else{
                troisiemePartie = dict.pos.get(p3).get(o3);
                troisiemePartieStat = troisiemePartie.size();
            }


            /**
             * Partie optimisation requêtage
             * On selectionne le treeset le + selectif
             * De plus jointure à partir du Treeset le + petit avec les autres Treeset
             */
            System.out.println("la valeur premierpartie est "+premierPartie.toString()+"\n la valeur deuxiemepartie est "+deuxiemePartie.toString()+"\nla valeur troisièmepartie"+troisiemePartie+"\n \n");

            if (premierPartieStat == Math.min(Math.min(premierPartieStat, deuxiemePartieStat), troisiemePartieStat)){
                Iterator iterator = premierPartie.iterator();
                while (iterator.hasNext()){

                    int i= (int) iterator.next();
                    if (troisiemePartie.contains(i)&&deuxiemePartie.contains(i)){

                        valeur.add(i);
                    }

                }
            }


            if (deuxiemePartieStat == Math.min(Math.min(premierPartieStat, deuxiemePartieStat), troisiemePartieStat)){
                Iterator iterator = deuxiemePartie.iterator();
                while (iterator.hasNext()){
                    iterator.next();
                    if (troisiemePartie.contains(iterator.next())&&premierPartie.contains(iterator.next())){
                        valeur.add((int)iterator.next());
                    }
                }
            }


            if (troisiemePartieStat == Math.min(Math.min(premierPartieStat, deuxiemePartieStat), troisiemePartieStat)){
                Iterator iterator = troisiemePartie.iterator();
                while (iterator.hasNext()){
                    iterator.next();
                    if (premierPartie.contains(iterator.next())&&deuxiemePartie.contains(iterator.next())){

                        valeur.add((int)iterator.next());
                    }
                }
            }
        }
        for (int i:valeur) {
            String s=dict.hmap_inverse.get(i);
            System.out.println("la valeur i est "+i+"la réponse est "+s+"");
        }
        if(valeur.isEmpty())
        {
            // System.out.println("pas de réponse à la requete");

        }
    }

    public void execute_requetev2(ArrayList<Integer> listP, ArrayList<Integer> listO, dictionnaire dict){
        Boolean repEx = false;
        ArrayList<Integer> stockRep = new ArrayList<>();
        ArrayList<Integer> reponse = new ArrayList<>();
        /*
         * Si le nombre de predicat n'est pas égal au nombre d'objet c'est impossible
         */
        listO.removeAll(Collections.singleton(null));
        listP.removeAll(Collections.singleton(null));
        if(listP.size()!=listO.size()){
            System.out.println("Impossible. ");
            return;
        }

        for(int i=0; i<listP.size();i++){
            /**
             * Si le prédicat ou l'objet n'existent pas
             */
            if(dict.statistique_objet.containsKey(listO.get(i)) && dict.statistique_predicat.containsKey(listP.get(i))){
                /**
                 * Si stat de predicat > stat de obejt ( c a d predicat moins selectif que objet
                 */
                if(dict.statistique_predicat.get(listP.get(i))> dict.statistique_objet.get(listO.get(i))) {
                    /**
                     * On choisit OPS
                     */

                    try{
                        Iterator iterator = dict.ops.get(listO.get(i)).get(listP.get(i)).iterator();
                        while (iterator.hasNext()) {
                            int maVar = (int) iterator.next();
                            if(dict.sujet_int.contains(maVar)){
                                reponse.add(maVar);
                            }
                            else{
                                System.out.println("Ce n'est pas un sujet "+dict.hmap_inverse.get(maVar));
                            }
                        }
                    }
                    catch (NullPointerException e){
                        System.out.println(e+"un triplet n'existe pas");
                        return;
                    }

                }
                else{
                    /**
                     * On choisit POS
                     */
                    try{
                        Iterator iterator = dict.pos.get(listP.get(i)).get(listO.get(i)).iterator();
                        while (iterator.hasNext()) {
                            int maVar = (int) iterator.next();
                            if(dict.sujet_int.contains(maVar)){
                                reponse.add(maVar);
                            }
                            else{
                                System.out.println("Ce n'est pas un sujet "+dict.hmap_inverse.get(maVar));
                            }

                        }
                    }
                    catch(NullPointerException e){
                        System.out.println(e+" Un triplet n'existe pas");
                        return;
                    }

                }
            }
            else{
                System.out.println("Erreur de requete, objet ou predicat non existant");
                return;
            }
        }

        if (reponse.size()==0){
            System.out.println("La requete n'a pas de réponse");
            return;
        }
        if (listP.size() == 1){
            for (Integer rep : reponse)
            {
                System.out.println("La réponse est "+rep+" ce qui correspond a "+dict.hmap_inverse.get(rep));
            }
            System.out.println("Il y a "+reponse.size()+" reponses");
        }
        else{
            for (Integer rep: reponse) {
                if(Collections.frequency(reponse,rep) == listP.size() && !stockRep.contains(rep)){
                    System.out.println("La réponse est "+rep+" ce qui correspond a "+dict.hmap_inverse.get(rep));
                    stockRep.add(rep);
                    //reponse.remove(rep);
                    repEx=true;
                }
            }
        }
        if (!repEx){
            System.out.println("Pas de réponse trouvé");
        }
    }
}


