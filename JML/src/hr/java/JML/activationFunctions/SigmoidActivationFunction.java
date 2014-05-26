package hr.java.JML.activationFunctions;

import java.util.ArrayList;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;
/**
 * 
 * @author Filip Habazin
 * 
 * Computes the activation potentials and gradients for the Sigmoid activation function.
 * The sigmoid is described as f(x)=1/e^(-x). 
 */
public class SigmoidActivationFunction extends ActivationFunction {
	/**
	 * Computes the activation function of a single <code>double</code> input variable.
	 * @param double input parameter
	 * @return sigmoid of the input
	 */
	@Override
	public double activationFunction(double input) {
		double sigmoid = 1 / Math.pow(Math.E,-input);
		return sigmoid;
	}

	/**
	 * Computes the activation function of a <code>DoubleVector</code> input variable.
	 * The activation function is computed for each element in the vector.
	 * The size of the input vector is the same as the size of the output vector. 
	 * @param DoubleVector input parameter
	 * @return sigmoid of the input vector
	 */
	@Override
	public DoubleVector activationFunction(DoubleVector input) {
		DoubleVector sig = new DenseDoubleVector(input.getLength(), 0);
		sig = sig.subtract(input);
		sig = sig.exp();
		sig = sig.divideFrom(1);
		return sig;
	}
	
	/**
	 * Computes the derivative of the input vector.
	 * The derivative of the sigmoid function is defined as f(x)'=sigmoid(x)*(1-sigmoid(x)).
	 * The size of the input vector is the same as the size of the output vector.
	 * @param input vector
	 * @return derivative of the input vector
	 */
	@Override
	public DoubleVector gradient(DoubleVector input) {
		DoubleVector gradient = activationFunction(input).multiply(activationFunction(input).subtractFrom(1));
		return gradient;
	}

	/**
	 * Computes the activation function of a <code>DoubleMatrix</code> input variable.
	 * The activation function is computed for each element in the matrix.
	 * The size of the input matrix is the same as the size of the output matrix.
	 * @param DoubleMatrix input parameter
	 * @return sigmoid of the input matrix
	 */
	@Override
	public DoubleMatrix activationFunction(DoubleMatrix input) {
		
		ArrayList<DoubleVector> sigmoids = new ArrayList<>();
		for(int i = 0; i< input.getRowCount();i++){
			sigmoids.add(activationFunction(input.getRowVector(i)));
		}
		return new DenseDoubleMatrix(sigmoids);
	}

	/**
	 * Computes the derivative of the input matrix.
	 * The derivative of the sigmoid function is defined as f(x)'=sigmoid(x)*(1-sigmoid(x)).
	 * The size of the input matrix is the same as the size of the output matrix.
	 * @param input matrix
	 * @return derivative of the input matrix
	 */
	@Override
	public DoubleMatrix gradient(DoubleMatrix input) {
		ArrayList<DoubleVector> sigmoids = new ArrayList<>();
		for(int i = 0; i< input.getRowCount();i++){
			sigmoids.add(gradient(input.getRowVector(i)));
		}
		return new DenseDoubleMatrix(sigmoids);
	}

}
