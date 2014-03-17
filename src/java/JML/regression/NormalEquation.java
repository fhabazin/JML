package java.JML.regression;

import org.ejml.simple.SimpleMatrix;

public class NormalEquation {
	
	public static SimpleMatrix calculateTheta(SimpleMatrix features, SimpleMatrix known){
		SimpleMatrix theta = (features.transpose().mult(features)).pseudoInverse().mult(features).mult(known);
		return theta;
	}
}

