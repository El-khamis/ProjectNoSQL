import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    static dictionnaire dict;
    static {
        dict = new dictionnaire();
    }
    static String pathO="";

    public static void main(String args[]) throws Exception {
        ArrayList<String> argument = new ArrayList<>();
        argument.addAll(Arrays.asList(args));

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
        pathO = argument.get(argument.indexOf("-output")+1).replaceAll("\"","");

        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(pathO+"/tempsDexecution.csv"), false));
        ArrayList<String> OutputTxt = new ArrayList<>();

        Outils outil = new Outils();
        long start = System.currentTimeMillis();
        RDFRawParser.runParser(pathD);
        float elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        OutputTxt.add("Parsing du fichier "+pathD+","+elapsedTime+" secondes\n");
        //System.out.println("Rdf.parse a pris" + elapsedTime);
        start=System.currentTimeMillis();
        dict.makeDictionnary();

        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        OutputTxt.add("Génération du dico,"+elapsedTime+" secondes\n");
        //System.out.println("Makedico a pris "+ elapsedTime +"sec");

        start=System.currentTimeMillis();
        dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet);
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        //System.out.println("execution termine en  "+ elapsedTime+" sec");
        OutputTxt.add("Creation de l'index OPS,"+elapsedTime+" secondes\n");

        start=System.currentTimeMillis();
        dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos,dict.statistique_predicat);
        elapsedTime = System.currentTimeMillis() - start;
        elapsedTime =  (elapsedTime /  1000F);
        //System.out.println("execution termine en  "+ elapsedTime+" sec");
        OutputTxt.add("Creation de l'index POS,"+elapsedTime+" secondes\n");


//        System.out.println("Maintenant place aux requetes ");
        outil.retrieve_requete(pathQ);
        Collections.shuffle(outil.listQuery);
        for( int i=0;i<10;i++) {
            start = System.currentTimeMillis();
            for (String req : outil.listQuery) {
                outil.parse_requete(req, Main.dict);
            }
            elapsedTime = System.currentTimeMillis() - start;
            elapsedTime = (elapsedTime / 1000F);
            OutputTxt.add("Execution de l'ensemble des requetes,"+elapsedTime+"secondes\n");
        }
        if(argument.contains("-workload_time")){
            OutputTxt.add("Execution de l'ensemble des requetes,"+elapsedTime+"secondes\n");
        }
        for (String txt : OutputTxt) {
            writer.write(txt);
        }
        writer.close();
        if(argument.contains("-verbose")){
            FileReader fr = new FileReader(pathO+"/tempsDexecution.csv");
            BufferedReader br = new BufferedReader(fr);
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                System.out.println(sCurrentLine);
            }
        }
        if(argument.contains("--export_results")){
            BufferedWriter WriteRequete = new BufferedWriter(new FileWriter(new File(Main.pathO+"/reponsesRequetes.csv"), false));
            for (int j=0; j<outil.OutputRep.size();j++)
            {
                WriteRequete.write(outil.OutputRep.get(j));
                WriteRequete.write(outil.Outputrep2.get(j));
                WriteRequete.write(outil.Outputrep3.get(j));
            }
            WriteRequete.close();
        }
        else{
            File file = new File(pathO+"/reponsesRequete.csv");
            file.delete();
        }
    }
}
