package hr.java.JML.neural;

import de.jungblut.math.DoubleVector;

public class DummyLayer extends Layer {

	@Override
	public DoubleVector calculateOutput(DoubleVector input) {
		// TODO Auto-generated method stub
		return null;
	}
	public DummyLayer(int size){
		setNumOfNeurons(size);
	}

}
