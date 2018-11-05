import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.RDFHandlerBase;



public final class RDFRawParser {



	public static void runParser(String path) throws IOException, RDFParseException, RDFHandlerException {
		Reader reader = new FileReader(
				"./donnees/500K.rdfxml");
		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());
		rdfParser.parse(reader, "");
		try {
			reader.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}


	private static class RDFListener extends RDFHandlerBase {





		@Override
		public void handleStatement(Statement st) {
			try {
				Main.dict.addArrayList(st);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}





//	public static void main(String args[]) throws IOException {
//
//      Outils outil = new Outils();
//		long start = System.currentTimeMillis();
//
//
//
//		float elapsedTime = System.currentTimeMillis() - start;
//
//
//        try {
//
//			elapsedTime = System.currentTimeMillis() - start;
//			elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("Rdf.parse a pris" + elapsedTime);
//			dict.makeDictionnary();
//
//            elapsedTime = System.currentTimeMillis() - start;
//            elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("Makedico a pris "+ elapsedTime +"sec");
//
//            dict.Index_creation(dict.objet_int,dict.predicat_int,dict.sujet_int,dict.ops,dict.statistique_objet);
//            dict.Index_creation(dict.predicat_int,dict.objet_int,dict.sujet_int,dict.pos,dict.statistique_predicat);
//
//            elapsedTime = System.currentTimeMillis() - start;
//			elapsedTime =  (elapsedTime /  1000F);
//			System.out.println("execution termine en  "+ elapsedTime+" sec");
//
//        	System.out.println("Veuillez entrer une requête");
//			String requete = " ";
//			String line;
//			BufferedReader br;
//			br = new BufferedReader(new FileReader(new File("./donnees/requetes.txt")));
//			while ((line = br.readLine()) != null){
//				requete+=" "+ line;
//				if(line.contains("}")){
//					outil.parse_requete(requete,dict);
//					requete = "";
//				}
//			}
//        } catch (Exception e) {
//				System.out.println("Une erreur est survnue "+e);
//			}
//
//		try {
//			reader.close();
//		} catch (IOException e) {
//		    System.out.println(e);
//		}
//
//        }
	}