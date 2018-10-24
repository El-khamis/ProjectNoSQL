import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.util.Dictionary;
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

//		long start = System.currentTimeMillis();
//
//
//		Reader reader = new FileReader(
//				"./donnees/100K.rdfxml");
//
//		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
//		rdfParser.setRDFHandler(new RDFListener());
//		float elapsedTime = System.currentTimeMillis() - start;
//		elapsedTime = (long) (elapsedTime /  1000F);
//		System.out.println("Rio parse a pris "+ elapsedTime);
//
//
//
//		try {
//			rdfParser.parse(reader, "");
//
//			elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("Rdf.parse"+ elapsedTime);
//
//			dict.makeDictionnary();
//
//            elapsedTime = System.currentTimeMillis() - start;
//            elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("Makedico a pris "+ elapsedTime);
//            //index pos
//            dict.Index_creation(dict.sujet_int,dict.predicat_int,dict.objet_int,dict.spo);
//			dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos);
//			dict.Index_creation(dict.sujet_int,dict.objet_int,dict.predicat_int,dict.sop);
//			dict.Index_creation(dict.predicat_int,dict.sujet_int,dict.objet_int,dict.pso);
//
//			//index print
//
//			elapsedTime = System.currentTimeMillis() - start;
//			elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("execution terminé en  "+ elapsedTime);
			Outils outil = new Outils();

			System.out.println("Veuillez entrer une requête");
			String requete = " ";
			String line;
			BufferedReader br;
			br = new BufferedReader(new FileReader(new File("./donnees/requetes.txt")));
			while ((line = br.readLine()) != null){
                requete+=" "+ line;

                if(line.contains("}")){
                    outil.parse_requete(requete);
                    requete = "";
                }
                else if(requete.contains("}")){

                }
			}

//		} catch (Exception e) {
//
//		}
//
//
//
//		try {
//			reader.close();
//		} catch (IOException e) {
//		}


	}
}