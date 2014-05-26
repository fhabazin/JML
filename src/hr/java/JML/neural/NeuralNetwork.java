package hr.java.JML.neural;

import hr.java.JML.activationFunctions.ActivationFunction;
import hr.java.JML.cost.CostFunction;
import hr.java.JML.cost.CostGradientTuple;
import hr.java.JML.learning.AbstractMinimizer;

import java.util.ArrayList;
import java.util.Collections;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;
import hr.java.JML.util.DenseMatrixFolder;

public class NeuralNetwork {
	protected static final double LAMBDA = 0.5;
	private ArrayList<Layer> layers;
	private DoubleMatrix trainingSet;
	private DoubleMatrix testSet;
	private DoubleVector trainingSetKnown;
	private DoubleVector testSetKnown;
	private int outputSize;
	private DoubleVector foldedTheta;
	private ActivationFunction af;
	private DoubleMatrix identity;
	private ArrayList<Layer> reverseLayers;
	private AbstractMinimizer minimizer;
	protected int[][] sizeArray;

	public NeuralNetwork(double[][] trainingSet, 
			int numOfLabels, int numOfHidden, int[] sizeOfHidden,
			ActivationFunction af, AbstractMinimizer minimizer) {
		this.af = af;
		double[][] temp = new double[trainingSet.length + 1][trainingSet[0].length];
		double[] tempKnown = new double[trainingSet.length + 1];
		this.minimizer = minimizer;
		for (int i = 0; i < trainingSet.length; i++) {
			temp[i][0] = 1;
			for (int j = 1; j < trainingSet[0].length; j++) {
				temp[i][j] = trainingSet[i][j];

			}
			tempKnown[i] = trainingSet[i][trainingSet[0].length-1];
		}
		this.trainingSet = new DenseDoubleMatrix(temp);
		this.trainingSetKnown = new DenseDoubleVector(tempKnown);
		this.outputSize = numOfLabels;
		
		layers = new ArrayList<Layer>();
		Layer prev = new DummyLayer(this.trainingSet.getColumnCount());
		for (int i = 0; i < numOfHidden; i++) {
			layers.add(new HiddenLayer(sizeOfHidden[i] + 1, prev, af));
			prev = layers.get(i);
		}

		layers.add(new OutputLayer(numOfLabels, prev, af));
	//	Collections.copy(reverseLayers, layers);
		this.sizeArray = computeUnfoldParameters();
		identity = getIdentityMatrix(this.trainingSet, this.trainingSetKnown);
	}

	public void trainTillError(double desiredError) {
		double error = 0;
		CostFunction cf = getCostFunction();

		do {
			this.foldedTheta = minimizer.minimize(cf, getFoldedThetaVector(),
					5, true);
			setThetas(foldedTheta);
			error = calculateError(testSet, testSetKnown);
		} while (error >= desiredError);
	}

	public void iterationLimitedTraining(int iterations) {
		CostFunction cf = getCostFunction();
		this.foldedTheta = minimizer.minimize(cf, getFoldedThetaVector(),
				iterations, true);
		setThetas(foldedTheta);
		

	}

	private void setThetas(DoubleVector foldedTheta2) {
		DoubleMatrix[] unroledTheta = DenseMatrixFolder.unfoldMatrices(
				foldedTheta, sizeArray);
		for (int i = 0; i < unroledTheta.length; i++)
			layers.get(i).setWeights(unroledTheta[i]);
	}

	private double calculateError(DoubleMatrix testSet,
			DoubleVector testSetKnown) {
		double error = 0;
		for (int i = 0; i < testSet.getRowCount(); i++) {
			if (predict(testSet.getRowVector(i)) != testSetKnown.get(i))
				error++;
		}

		return error / testSet.getRowCount();
	}

	private int predict(DoubleVector rowVector) {
		DoubleVector prevOutput = rowVector;
		for (Layer layer : layers) {
			prevOutput = af.activationFunction(layer.getWeights().multiplyVectorColumn(prevOutput));
		}
		int max = 0;
		for (int i = 1; i < prevOutput.getLength(); i++) {
			if (prevOutput.get(i) > max)
				max = i;
		}
		return max;

	}

	private CostFunction getCostFunction() {
		CostFunction costFunction = new CostFunction() {

			@Override
			public CostGradientTuple evaluateCost(DoubleVector input) {
				DoubleMatrix[] unroledTheta = DenseMatrixFolder.unfoldMatrices(
						input, sizeArray);
				double cost = 0;
				double penalty = 0;
				ArrayList<DoubleMatrix> outputs = new ArrayList<>();
				ArrayList<DoubleMatrix> sigmoids = new ArrayList<>();
				DoubleMatrix delta = null;
				DoubleMatrix[] deltas = new DenseDoubleMatrix[unroledTheta.length];
				DoubleMatrix[] gradients = new DenseDoubleMatrix[unroledTheta.length];
				DoubleMatrix costMat;
				DoubleMatrix layerOutput;
				DoubleMatrix ones = new DenseDoubleMatrix(
						identity.getRowCount(), identity.getColumnCount(), 1);

				for (int i = 0; i < unroledTheta.length; i++) {
					if (i == 0)
						layerOutput = trainingSet.multiply(unroledTheta[i]
								.transpose());
					else
						layerOutput = sigmoids.get(i - 1).multiply(
								unroledTheta[i].transpose());
					outputs.add(layerOutput);
					sigmoids.add(af.activationFunction(layerOutput));
					penalty += unroledTheta[i].pow(2).sum();
				}
				// J = (1/m)*sum(sum((-Y).*log(H) - (1-Y).*log(1-H), 2));
				costMat = sigmoids.get(sigmoids.size() - 1);
				cost = identity.multiply(-1).multiplyElementWise(log(costMat))
						.sum();
				cost += ones.subtract(identity)
						.multiplyElementWise(log(ones.subtract(costMat))).sum();
				cost += penalty;

				for (int i = sigmoids.size() - 1; i > -1; i--) {
					if (i == sigmoids.size() - 1)
						delta = sigmoids.get(i).subtract(identity);
					else
						delta = sigmoids
								.get(i + 1)
								.multiply(unroledTheta[i])
								.multiplyElementWise(
										af.gradient(sigmoids.get(i)));
					deltas[i] = delta;
				}

				for (int i = 0; i < outputs.size(); i++) {
					if (i == 0)
						deltas[i] = deltas[i].multiply(trainingSet);
					else
						deltas[i] = deltas[i].multiply(outputs.get(i - 1));

					gradients[i] = unroledTheta[i].multiply(
							LAMBDA / trainingSet.getRowCount()).add(
							deltas[i].divide(trainingSet.getRowCount()));
				}
				return new CostGradientTuple(cost,
						DenseMatrixFolder.foldMatrices(gradients));
			}
		};
		return costFunction;

	}

	private DoubleMatrix getIdentityMatrix(DoubleMatrix featureSet,
			DoubleVector known) {
		double[][] mat = new double[outputSize][outputSize];
		double[][] Y = new double[featureSet.getRowCount()][outputSize];
		for (int i = 0; i < outputSize; i++)
			mat[i][i] = 1;

		for (int i = 0; i < featureSet.getRowCount(); i++)
			Y[i] = mat[(int) known.get(i)];
		
		return new DenseDoubleMatrix(Y);
	}



	private DoubleMatrix log(DoubleMatrix input) {
		ArrayList<DoubleVector> log = new ArrayList<>();
		for (int i = 0; i < input.getRowCount(); i++) {
			log.add(input.getRowVector(i).log());
		}
		return new DenseDoubleMatrix(log);
	}

	public DoubleVector getFoldedThetaVector() {
		// get our randomized weights into a foldable format
		DoubleMatrix[] weightMatrices = new DoubleMatrix[layers.size()];
		for (int i = 0; i < weightMatrices.length; i++) {
			weightMatrices[i] = layers.get(i).getWeights();
		}
		return DenseMatrixFolder.foldMatrices(weightMatrices);
	}

	public int[][] computeUnfoldParameters() {

		int[][] unfoldParameters = new int[layers.size()][];
		for (int i = 0; i < unfoldParameters.length; i++) {
			unfoldParameters[i] = new int[] {
					layers.get(i).getWeights().getRowCount(),
					layers.get(i).getWeights().getColumnCount() };
		}
		return unfoldParameters;
	}
}
