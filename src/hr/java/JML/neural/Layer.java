package hr.java.JML.neural;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;



public abstract class Layer {
	 protected int numOfNeurons;
	 protected DoubleMatrix weights;
	 protected Layer previousLayer;
	 
	 public DoubleMatrix getWeights() {
		return weights;
	}
	public void setWeights(DoubleMatrix weights) {
		this.weights = weights;
	}
	protected int getNumOfNeurons() {
		return numOfNeurons;
	}
	public void setNumOfNeurons(int numOfNeurons) {
		this.numOfNeurons = numOfNeurons;
	}
	public Layer getPreviousLayer() {
		return previousLayer;
	}
	public void setPreviousLayer(Layer previousLayer) {
		this.previousLayer = previousLayer;
	}
	
	
	public abstract DoubleVector calculateOutput(DoubleVector input);
}
