package hr.java.JML.neural;

import hr.java.JML.activationFunctions.ActivationFunction;

import java.util.Random;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;

public class HiddenLayer extends Layer {
	private ActivationFunction af;
	
	
	public HiddenLayer(int size, Layer prev, ActivationFunction af) {
		super.setNumOfNeurons(size);
		super.setPreviousLayer(prev);
		weights = new DenseDoubleMatrix(getNumOfNeurons(), getPreviousLayer().getNumOfNeurons());
		Random rnd = new Random();
		for(int i =0; i < getNumOfNeurons(); i++){
			for (int j = 0; j < getPreviousLayer().getNumOfNeurons(); j++) {
				weights.set(j, j, rnd.nextDouble());
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
