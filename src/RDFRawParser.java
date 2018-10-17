import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.*;
import java.util.Dictionary;

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

	public static void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException {




		Reader reader = new FileReader(
				"./donnees/100K.rdfxml");

		org.openrdf.rio.RDFParser rdfParser = Rio.createParser(RDFFormat.RDFXML);
		rdfParser.setRDFHandler(new RDFListener());




		try {
			rdfParser.parse(reader, "");

            dict.makeDictionnary();
            //index pos
           dict.Index_creation(1,2,0,dict.pos);
			dict.writer3.close();
			dict.writer.close();
			dict.writer2.close();

			dict.pos.get(99382).get(6715).toString();
			//index print
			System.out.println("execution terminé");
		} catch (Exception e) {

		}

		try {
			reader.close();
		} catch (IOException e) {
		}
	}
}