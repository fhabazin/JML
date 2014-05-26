package hr.java.JML.activationFunctions;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
/**
 * 
 * @author Filip Habazin
 * Computes the activation potentials and gradients of the input.
 *
 */
public abstract class ActivationFunction {
	
	public abstract double activationFunction(double input);
	
	public abstract DoubleVector activationFunction(DoubleVector input);
	
	public abstract DoubleMatrix activationFunction(DoubleMatrix input);
	
	public abstract DoubleVector gradient(DoubleVector input);
	
	public abstract DoubleMatrix gradient(DoubleMatrix input);
}
