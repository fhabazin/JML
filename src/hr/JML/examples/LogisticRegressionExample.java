package hr.JML.examples;

import hr.java.JML.learning.GradientDescent.GradientDescentBuilder;
import hr.java.JML.learning.Fmincg;
import hr.java.JML.learning.GradientDescent;
import hr.java.JML.regression.LogisticRegression;


public class LogisticRegressionExample {

	public static void main(String[] args) {
		double[][] tr = {{1.0000000, 2.2873553, 0.8908079, 0},
				{1.0000000, 2.4717267, -0.6861101, 0},
				{1.0000000, 0.3836040, -1.6322217, 1},
				{1.0000000, -2.0572025, -1.0776761, 0},	
				{1.0000000, -2.6066264, 0.4676799, 0},
				{1.0000000, -0.7595301, 1.5830532, 1},
				{1.0000000, 1.7858747, 1.2429747, 1},
				{1.0000000, 2.6893545, -0.2398890, 0},
				{1.0000000, 1.1202542, -1.5021998, 1},
				{1.0000000, -1.4788027, -1.3833951, 0},
				{1.0000000, -2.7182552, 0.0072967, 0},
				{1.0000000, -1.4585564, 1.3912800, 0},
				{1.0000000, 1.1421324, 1.4961268, 1},
				{1.0000000, 2.6927500, 0.2254416, 0},
				{1.0000000, 1.7676656, -1.2525136, 1},
				{1.0000000, -0.7826024, -1.5789136, 1},
				{1.0000000, -2.6133493, -0.4536676, 0},
				{1.0000000, -2.0413950, 1.0886782, 0},
				{1.0000000, 0.4074085, 1.6300983, 1},
				{1.0000000, 2.4816425, 0.6728136, 0}};
		
		double[][] translated = new double[tr.length][3];
		for (int i = 0; i < tr.length; i++) {
			for(int j = 0; j < tr[0].length-1; j++){
				translated[i][j] = tr[i][j];
			}
		}
		 GradientDescent gd = GradientDescentBuilder.create(0.8d).momentum(0.9d)
			        .breakOnDifference(1e-20).build();
		LogisticRegression lr = new LogisticRegression(tr, tr, new Fmincg());
		lr.iterationLimitedTraining(100000);
		
		for(int i = 0; i< tr.length; i++){
			System.out.println("for known values: " + arrayToString(tr[i]) + " the prediction was: "+ lr.predict(translated[i]) + " and expected value was: " + tr[i][3]);
		}
	}

	private static String arrayToString(double[] ds) {
		String array = "";
		for(int i = 0; i < ds.length-1; i++){
			array+=ds[i]+", ";
		}
		return array;
	}

}
