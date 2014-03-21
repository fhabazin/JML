package java.JML.test;

import java.JML.fileIO.InputFileReader;
import java.JML.regression.LinearRegression;
import java.JML.regression.RegressionFeatures;
import java.JML.regression.RegressionParameters;
import java.util.ArrayList;


public class LinearRegressionTest {
	private static ArrayList<RegressionFeatures> trainingSet;
	private static ArrayList<RegressionFeatures> validationSet;
	private static ArrayList<RegressionFeatures> testSet;
	private static final String[] SET_NAMES = new String[]{"Training", "Test", "Validation"};
	private static RegressionParameters params;
	
	public static void main(String[] args) {
		InputFileReader file = new InputFileReader("file");
		trainingSet = file.getArrayFromMap(SET_NAMES[0]);
		testSet = file.getArrayFromMap(SET_NAMES[1]);
		validationSet = file.getArrayFromMap(SET_NAMES[2]);
		params = new RegressionParameters();
		LinearRegression lr = new LinearRegression(trainingSet, validationSet, testSet, params);
		lr.regressionErrorLimitedTraining(0.1);
	}

}
