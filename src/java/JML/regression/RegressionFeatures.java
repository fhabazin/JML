package java.JML.regression;

import java.JML.normalization.FeatureNormalizer;

/**
 * 
 * 	@author Filip Habazin
 *	
 */
public class RegressionFeatures {
	
	private double known;
	private double[] features;

	
	
	public RegressionFeatures(double[] features, double known){
		this.features = FeatureNormalizer.normalizer(features);
		this.known = known;
	}
	
	public double sumProduct(RegressionParameters theta){
		double mult = 0;
		for(int i = 0; i < theta.getTheta().length; i++){
			mult += features[i] * theta.getTheta()[i];
		}
		return mult;		
	}

	public double getKnown() {
		return known;
	}

	public void setKnown(double known) {
		this.known = known;
	}

	public double[] getFeatures() {
		return features;
	}

	public void setFeatures(double[] features) {
		this.features = features;
	}
	
	public double[] multiplyByScalar(double scalar){
		double[] scaledFeatures = new double[features.length];
		for(int i = 0; i < features.length; i++){
			scaledFeatures[i] = features[i] * scalar;
		}
		return scaledFeatures;		
	}
	
	public static double[] add(double[] gradient, double[] thetaRegularized){
		if(gradient.length == thetaRegularized.length){
			double[] sum = new double[gradient.length];
			for(int i = 0; i < gradient.length; i++){
				sum[i] = gradient[i] + thetaRegularized[i];
			}
			return sum;	
		}else 
			return null;
		
	}
	
}
