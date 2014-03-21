package java.JML.regression;

import java.util.Random;

public class RegressionParameters {
	private static final double MIN_ALPHA_VALUE = 0.000001;
	private static final double MAX_ALPHA_VALUE = 0.01;
	private static final double MIN_LAMBDA_VALUE = 0.1;
	private static final double MAX_LAMBDA_VALUE = 1;
	private double theta[];
	private double alpha;
	private double lambda;
	private double [] thetaRegularized;
	public RegressionParameters(double[] theta, double alpha, double lambda) {
		this.theta = theta;
		this.alpha = alpha;
		this.lambda = lambda;
		thetaRegularized = theta;
		thetaRegularized[0] = 0;
	}
	
	public RegressionParameters(){
		Random rnd = new Random();
		for(int i = 0; i < this.theta.length; i++){
			theta[i] = rnd.nextDouble();
		}
		alpha = MIN_ALPHA_VALUE + (MAX_ALPHA_VALUE - MIN_ALPHA_VALUE) * rnd.nextDouble();
		lambda = MIN_LAMBDA_VALUE + (MAX_LAMBDA_VALUE - MIN_LAMBDA_VALUE) * rnd.nextDouble();
		thetaRegularized = this.theta;
		thetaRegularized[0] = 0;
	}

	public double[] getTheta() {
		return theta;
	}

	public void setTheta(double[] theta) {
		this.theta = theta;
	}

	public double getAlpha() {
		return alpha;
	}

	public void setAlpha(double alpha) {
		this.alpha = alpha;
	}

	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}
	
	public double regularizedThetaSumSquared(){
		double mult = 0;
		for(int i = 0; i < thetaRegularized.length; i++){
			mult += thetaRegularized[i] * thetaRegularized[i];
		}
		return mult;
	}
	
	public double[] lambdaThetaReg(){
		double[] lambdaThetaReg = new double[thetaRegularized.length];
		for(int i = 0; i < thetaRegularized.length; i++){
			lambdaThetaReg[i] = thetaRegularized[i] * lambda;
		}
		return lambdaThetaReg;		
	}

	public double[] sub(double[] gradient){
		if(gradient.length == thetaRegularized.length){
			double[] sub = new double[gradient.length];
			for(int i = 0; i < gradient.length; i++){
				sub[i] = theta[i] - gradient[i];
			}
			return sub;	
		}else 
			return null;
		
	}
	
	public double[] multiplyByScalar(double scalar){
		double[] scaledFeatures = new double[theta.length];
		for(int i = 0; i < theta.length; i++){
			scaledFeatures[i] = theta[i] * scalar;
		}
		return scaledFeatures;		
	}

	
	
}
