import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;

import java.io.*;
import java.util.ArrayList;

public class Main {
    static dictionnaire dict;

    static {
        try {
            dict = new dictionnaire();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws IOException, RDFParseException, RDFHandlerException {
        ArrayList<String> argument = new ArrayList<>();
        for (String arg : args){
            argument.add(arg);
        }

        if(!argument.contains("-queries")){
            System.out.println("Chemin vers le dossier contenant les requetes manquant");
            System.exit(0);
        }

        if(!argument.contains("-data")){
            System.out.println("Chemin vers le dossier contenant les données manquant");
            System.exit(0);
        }
        String path= argument.get(argument.indexOf("-queries")+1).replaceAll("\"","");

        String donnees = argument.get(argument.indexOf("-data")+1).replaceAll("\"","");

        Outils outil = new Outils();
        long start = System.currentTimeMillis();
        RDFRawParser.runParser("ok");
        //try {
        float elapsedTime = System.currentTimeMillis() - start;
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        System.out.println("Rdf.parse a pris" + elapsedTime);
        dict.makeDictionnary();

        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        System.out.println("Makedico a pris "+ elapsedTime +"sec");

        dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet);
        dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos,dict.statistique_predicat);

        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        System.out.println("execution termine en  "+ elapsedTime+" sec");
        System.out.println("Maintenant place aux requetes ");
        outil.retrieve_requete(path);
    }
}
