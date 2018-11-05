import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.util.Dictionary;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Scanner;

import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;



public final class RDFRawParser {
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

	private static class RDFListener extends RDFHandlerBase {

		@Override
		public void handleStatement(Statement st) {
			try {
				dict.addArrayList(st);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String args[]) throws IOException {

        Outils outil = new Outils();
		long start = System.currentTimeMillis();
		Reader reader = new FileReader(
				"./donnees/100K.rdfxml");

		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		float elapsedTime = System.currentTimeMillis() - start;


        try {
			rdfParser.parse(reader, "");
			elapsedTime = System.currentTimeMillis() - start;
			elapsedTime =  (elapsedTime /  1000F);
			System.out.println("Rdf.parse a pris" + elapsedTime);
			dict.makeDictionnary();

            elapsedTime = System.currentTimeMillis() - start;
            elapsedTime =  (elapsedTime /  1000F);
			System.out.println("Makedico a pris "+ elapsedTime +"sec");
            //index pos
        //    dict.Index_creation(dict.sujet_int,dict.predicat_int,dict.objet_int,dict.pos,dict.statistique_predicat_objet,dict.statistique_predicat);
			//index print


            dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet);

            dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos,dict.statistique_predicat);

            //dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet_predicat,dict.statistique_objet);

            elapsedTime = System.currentTimeMillis() - start;
			elapsedTime =  (elapsedTime /  1000F);
			System.out.println("execution termine en  "+ elapsedTime+" sec");



        System.out.println("Veuillez entrer une requête");
        String requete = " ";
        String line;
        BufferedReader br;
        br = new BufferedReader(new FileReader(new File("./donnees/requetes.txt")));
        while ((line = br.readLine()) != null){
            requete+=" "+ line;

            if(line.contains("}")){
                outil.parse_requete(requete,dict);
                requete = "";
            }

        }
            		} catch (Exception e) {

		}

		try {
			reader.close();
		} catch (IOException e) {
		    System.out.println(e);
		}

        }
	}