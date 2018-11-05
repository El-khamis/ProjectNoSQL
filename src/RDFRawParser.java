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



	public static void runParser(String pathQ, String pathD) throws IOException, RDFParseException, RDFHandlerException {
		Reader reader = new FileReader(
				pathD);
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
	}