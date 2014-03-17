package java.JML.fileIO;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.ejml.simple.SimpleMatrix;



public class InputFileReader {
	private static final String COMMENT_START_STRING = "/*";
	private static final String DELIMITER = "#";
	private static final String INPUT_IDENTIFIER_STRING = "input";
	private static final String SETUP_IDENTIFIER_STRING = "setup";
	private static final String NUM_OF_FEATURES_STRING = "numOfFeatures";
	private static final String NUM_OF_ROWS_STRING = "numOfRows";
	private static final String TYPE_OF_REGRESSION_STRING = "typeOfRegression";
	private static final String LINEAR_REGRESSION_STRING = "lin";
	private static final String LOGISTIC_REGRESSION_STRING = "log";
	private static final Map<String, String> args = new HashMap<String, String>();
	private String filePath;
	private static String line;
	private static int numOfFeatures;
	private static String typeOfRegression;
	private static int numOfRows;
	private SimpleMatrix features;
	private SimpleMatrix identities;
	private static double [][] featuresData;
	private static double[] indentitesData;
	
	public InputFileReader(String filePath){
		this.filePath = filePath;
	}
	
	public void parseFile() throws IOException {
		final FileInputStream fstream = new FileInputStream(filePath);
		final DataInputStream in = new DataInputStream(fstream);
		final BufferedReader br = new BufferedReader(new InputStreamReader(in));

		while ((line = br.readLine()) != null) {
			parseLine();
			if(line.contains(COMMENT_START_STRING))
				continue;
		}
		in.close();
		features = new SimpleMatrix(featuresData);
	}

	

	private static void parseLine() throws IOException {
		final int index = line.indexOf(DELIMITER);
		if (index == -1) {
			throw new IOException("Invalid command: " + line);
		}

		final String command = line.substring(0, index).toLowerCase().trim();
		final String argsStr = line.substring(index + 1).trim();
		final StringTokenizer tok = new StringTokenizer(argsStr, ",");
		args.clear();
		while (tok.hasMoreTokens()) {
			final String arg = tok.nextToken();
			final int index2 = arg.indexOf(':');
			if (index2 == -1) {
				throw new IOException("Invalid command: " + line);
			}
			final String key = arg.substring(0, index2).toLowerCase().trim();
			final String value = arg.substring(index2 + 1).trim();
			args.put(key, value);
		}

		executeLine(command, args);
	}

	private static void executeLine(final String command,
			final Map<String, String> args) throws IOException {
		if (command.equals(SETUP_IDENTIFIER_STRING)) {
			processSetup();
		}else if (command.equals(INPUT_IDENTIFIER_STRING)) {
			processInput();
		}  
	}
	
	private static void processSetup() throws NumberFormatException, IOException {
		numOfFeatures = Integer.parseInt(getArg(NUM_OF_FEATURES_STRING));
		numOfRows = Integer.parseInt(getArg(NUM_OF_ROWS_STRING));
		typeOfRegression = getArg(TYPE_OF_REGRESSION_STRING);
		featuresData = new double[numOfRows][numOfFeatures];
		indentitesData = new double[numOfRows];
	}
	
	private static void processInput() {
		
	}

	

	/*private static void processRow() throws IOException {
		final int redBr = Integer.parseInt(getArg("nr"));
		final String strNaziv = getArg("naziv");
		final double kol = Double.parseDouble(getArg("kol"));
		final double cij = Double.parseDouble(getArg("cij"));
		final double rab = Double.parseDouble(getArg("rab"));

		if (loadBid == true) {
			for (int col = 0; col < 7; col++) {
				switch (col) {
				case 0:
					bidTbl.getModel().setValueAt(redBr, redBr - 1, col);
					break;
				case 1:
					bidTbl.getModel().setValueAt(strNaziv, redBr - 1, col);
					break;
				case 2:
					bidTbl.getModel().setValueAt(kol, redBr - 1, col);
					break;
				default:
					break;
				}
			}
		} else {
			for (int col = 0; col < 7; col++) {
				switch (col) {
				case 0:
					racTbl.getModel().setValueAt(redBr, redBr - 1, col);
					break;
				case 1:
					racTbl.getModel().setValueAt(strNaziv, redBr - 1, col);
					break;
				case 2:
					racTbl.getModel().setValueAt(kol, redBr - 1, col);
					break;
				default:
					break;
				}
			}
		}

	}

	private static void processCostumer() throws IOException {
		if (loadBid == true) {
			final String strNaziv = getArg("naziv");
			nazivText.setText(strNaziv);
			final String strOib = getArg("oib");
			oibText.setText(strOib);
			final String strGrad = getArg("grad");
			gradText.setText(strGrad);
			final String strUlica = getArg("ulica");
			ulicaText.setText(strUlica);
		} else {
			final String strNaziv = getArg("naziv");
			nameText.setText(strNaziv);
			final String strOib = getArg("oib");
			oibRacText.setText(strOib);
			final String strGrad = getArg("grad");
			cityText.setText(strGrad);
			final String strUlica = getArg("ulica");
			ulicaTextRac.setText(strUlica);
		}
	}*/

	private static String getArg(final String name) throws IOException {
		final String result = args.get(name);
		if (result == null) {
			throw new IOException("Missing argument " + name + " on line: "
					+ line);
		}
		return result;
	}
}
