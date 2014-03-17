package java.JML.regression;

import java.JML.normalization.FeatureNormalizer;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

/**
 * 
 * 	@author Filip Habazin
 *	
 */
public class RegressionInputData {
	private static final double MIN_ALPHA_VALUE = 0.000001;
	private static final double MAX_ALPHA_VALUE = 0.01;
	private static final double MIN_LAMBDA_VALUE = 0.1;
	private static final double MAX_LAMBDA_VALUE = 1;
	double tempTheta[];
	double alpha;
	double lambda;
	SimpleMatrix theta;
	SimpleMatrix known;
	SimpleMatrix features;
	GradientDescent gradDesc;
	private double[][] temp;
	
	
	public RegressionInputData(double[][] features, double[] known){
		
		for (int i = 0; i < features.length; i++) {
			this.temp[i] = FeatureNormalizer.normalizer(features[i]);
		}
		this.features = new SimpleMatrix(temp);
		this.known = new SimpleMatrix(1, known.length);
		this.known.setColumn(0, 0, known);
		tempTheta = new double[features[0].length];
		Random rnd = new Random();
		for (int i = 0; i < tempTheta.length; i++) {
			tempTheta[i]= rnd.nextDouble();
		}
		theta = new SimpleMatrix(1, tempTheta.length);
		this.theta.setColumn(0, 0, tempTheta);
		alpha = MIN_ALPHA_VALUE + (rnd.nextDouble() * (MIN_ALPHA_VALUE - MAX_ALPHA_VALUE));
		lambda = MIN_LAMBDA_VALUE + (rnd.nextDouble() * (MIN_LAMBDA_VALUE - MAX_LAMBDA_VALUE));
	}
	
	public SimpleMatrix linearRegressionErrorLimitedTraining(double desiredError){
		 theta = GradientDescent.linearRegressionErrorLimitedTraining(features, known, theta, alpha, lambda, desiredError);
		 return theta;
	}
	
	public void calculateThetaWithNormalEquation(){
		theta = NormalEquation.calculateTheta(features,known);
	}
	
	public SimpleMatrix linearRegressionIterationLimitedTraining(int desiredIterations){
		 theta = GradientDescent.linearRegressionIterationLimitedTraining(features, known, theta, alpha, lambda, desiredIterations);
		 return theta;
	}
}
