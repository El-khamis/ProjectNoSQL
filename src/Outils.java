import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;
import java.util.Iterator;

public class Outils {


    String maVariable;


    public void retrieve_requete(String path){
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        String requete;
        String line;
        BufferedReader br;
        //Parcours des fichiers dans le dossier
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                try{
                    //je l'ouvre, je le parcours et pour chaque requete j'appelle parse_requeteString requete = " ";
                        requete = " ";
                        br = new BufferedReader(new FileReader(new File(path+"/"+listOfFiles[i].getName())));
                        while ((line = br.readLine()) != null){
                            requete+=" "+ line;
                            if(line.contains("}")){
                                parse_requete(requete, Main.dict);
                                requete = "";
                            }
                        }
                    } catch (Exception e) {
                        // System.out.println("Une erreur est survenue "+e);
                }
            }
        }
    }

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
                // System.out.println("La requete n'est pas une requete étoile\n");
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
        execute_requete(predicats_int,objets_int,dict);

    }

    public void execute_requete(ArrayList<Integer> listP, ArrayList<Integer> listO, dictionnaire dict){
        Boolean repEx = false;
        ArrayList<Integer> stockRep = new ArrayList<>();
        ArrayList<Integer> reponse = new ArrayList<>();
        /*
         * Si le nombre de predicat n'est pas égal au nombre d'objet c'est impossible
         */
        listO.removeAll(Collections.singleton(null));
        listP.removeAll(Collections.singleton(null));
        if(listP.size()!=listO.size()){
            // System.out.println("Impossible. ");
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
                                // System.out.println("Ce n'est pas un sujet "+dict.hmap_inverse.get(maVar));
                            }
                        }
                    }
                    catch (NullPointerException e){
                        // System.out.println(e+"un triplet n'existe pas");
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
                                // System.out.println("Ce n'est pas un sujet "+dict.hmap_inverse.get(maVar));
                            }

                        }
                    }
                    catch(NullPointerException e){
                        // System.out.println(e+" Un triplet n'existe pas");
                        return;
                    }

                }
            }
            else{
                // System.out.println("Erreur de requete, objet ou predicat non existant");
                return;
            }
        }

        if (reponse.size()==0){
            // System.out.println("La requete n'a pas de réponse");
            return;
        }
        if (listP.size() == 1){
            for (Integer rep : reponse)
            {
                // System.out.println("La réponse est "+rep+" ce qui correspond a "+dict.hmap_inverse.get(rep));
            }
            // System.out.println("Il y a "+reponse.size()+" reponses");
        }
        else{
            for (Integer rep: reponse) {
                if(Collections.frequency(reponse,rep) == listP.size() && !stockRep.contains(rep)){
                    // System.out.println("La réponse est "+rep+" ce qui correspond a "+dict.hmap_inverse.get(rep));
                    stockRep.add(rep);
                    //reponse.remove(rep);
                    repEx=true;
                }
            }
        }
        if (!repEx){
            // System.out.println("Pas de réponse trouvé");
        }
    }
}


