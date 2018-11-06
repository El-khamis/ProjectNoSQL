import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
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
            System.exit(-1);
        }

        if(!argument.contains("-data")){
            System.out.println("Chemin vers le dossier contenant les données manquant");
            System.exit(-1);
        }

        if(!argument.contains("-output")){
            System.out.println("Chemin vers le dossier ou stocker les fichiers de sortie manquant ");
            System.exit(-1);
        }

        String pathQ= argument.get(argument.indexOf("-queries")+1).replaceAll("\"","");
        String pathD = argument.get(argument.indexOf("-data")+1).replaceAll("\"","");
        String pathO = argument.get(argument.indexOf("-output")+1).replaceAll("\"","");

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathO+"/tempsDexecution.csv"), false));
        ArrayList<String> OutputTxt = new ArrayList<>();

        Outils outil = new Outils();
        long start = System.currentTimeMillis();
        RDFRawParser.runParser(pathQ,pathD);
        //try {
        float elapsedTime = System.currentTimeMillis() - start;
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        OutputTxt.add("Parser le fichier "+pathD+" a pris,"+elapsedTime+" secondes\n");
        //System.out.println("Rdf.parse a pris" + elapsedTime);
        start=System.currentTimeMillis();
        dict.makeDictionnary();

        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        OutputTxt.add("La génération du dico a pris,"+elapsedTime+"secondes\n");
        //System.out.println("Makedico a pris "+ elapsedTime +"sec");

        start=System.currentTimeMillis();
        dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet);
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        //System.out.println("execution termine en  "+ elapsedTime+" sec");
        OutputTxt.add("La creation de l'index OPS a pris,"+elapsedTime+"secondes\n");

        start=System.currentTimeMillis();
        dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos,dict.statistique_predicat);
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        //System.out.println("execution termine en  "+ elapsedTime+" sec");
        OutputTxt.add("La creation de l'index POS a pris,"+elapsedTime+"secondes\n");


//        System.out.println("Maintenant place aux requetes ");
        start=System.currentTimeMillis();
        outil.retrieve_requete(pathQ);
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        if(argument.contains("-workload_time")){
            OutputTxt.add("L'execution de l'ensemble des requetes a pris,"+elapsedTime+"secondes\n");
        }
        for (String txt : OutputTxt) {
            writer.write(txt);
            //writer.newLine();
        }
        writer.close();

        if(argument.contains("-verbose")){
            BufferedReader br = null;
            FileReader fr = null;
            fr = new FileReader(pathO+"/tempsDexecution.csv");
            br = new BufferedReader(fr);

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
            }
        }
    }
}
