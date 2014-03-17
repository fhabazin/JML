package java.JML.regression;

import org.ejml.simple.SimpleMatrix;


public class GradientDescent {
	private static double cost;
	
	public static SimpleMatrix linearRegressionErrorLimitedTraining(SimpleMatrix features, SimpleMatrix known, SimpleMatrix theta, double alpha, double lambda, double desiredError){
		
		do{
			cost = 0;
			theta = theta.minus(((((theta.transpose().mult(features)).minus(known)).transpose()).mult(features)).transpose().divide((double) features.numRows()).divide(1/alpha));
			cost = Math.pow(( (theta.transpose().mult(features)).minus(known)).elementSum(), 2) / 2 * features.numRows();
		}while(cost > desiredError);
		return theta;
	}
	
	public static SimpleMatrix linearRegressionIterationLimitedTraining(SimpleMatrix features, SimpleMatrix known, SimpleMatrix theta, double alpha, double lambda, int iterations){
		
		for(int i = 0; i < iterations; i++ ){
			cost = 0;
			theta = theta.minus(((((theta.transpose().mult(features)).minus(known)).transpose()).mult(features)).transpose().divide((double) features.numRows()));
			cost = Math.pow(( (theta.transpose().mult(features)).minus(known)).elementSum(), 2) / 2 * features.numRows();
		}
		return theta;
	}
	
	public static double getCost() {
		return cost;
	}
	
	public static void setCost(double cost) {
		GradientDescent.cost = cost;
	}
	// not done
	private static SimpleMatrix sigmoidFunction(SimpleMatrix sth){
		SimpleMatrix sigmoid = null;
		sigmoid = (sth.negative());
		return sigmoid;
	}



	
	
	
	
}
