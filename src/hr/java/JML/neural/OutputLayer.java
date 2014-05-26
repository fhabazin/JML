package hr.java.JML.neural;

import java.util.Random;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;
import hr.java.JML.activationFunctions.ActivationFunction;
import hr.java.JML.neural.Layer;;

public class OutputLayer extends Layer {
	
	private DoubleMatrix weights;
	private ActivationFunction af;
	
	public OutputLayer(int size, Layer prev, ActivationFunction af) {
		setNumOfNeurons(size);
		setPreviousLayer(prev);
		weights = new DenseDoubleMatrix(getNumOfNeurons(), getPreviousLayer().getNumOfNeurons());
		Random rnd = new Random();
		for(int i =0; i < weights.getRowCount(); i++){
			for (int j = 0; j < weights.getColumnCount(); j++) {
				weights.set(i, j, rnd.nextDouble());
			}
		}
		this.af = af;
	}

	

	public DoubleMatrix getWeights() {
		return weights;
	}

	public void setWeights(DoubleMatrix weights) {
		this.weights = weights;
	}
	
	public void setWeightsByNeuron(DoubleMatrix weights) {
		this.weights = weights;
	}



	@Override
	public DoubleVector calculateOutput(DoubleVector input) {
		DoubleVector output = new DenseDoubleVector(getNumOfNeurons(),1);
		for(int i = 1; i < getNumOfNeurons(); i++){
			output.set(i, input.dot(weights.getColumnVector(i)));
		}
		
		return af.activationFunction(output);
	}
	
}
