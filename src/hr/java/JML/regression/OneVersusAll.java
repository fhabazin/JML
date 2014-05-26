package hr.java.JML.regression;

import hr.java.JML.activationFunctions.ActivationFunction;
import hr.java.JML.activationFunctions.SigmoidActivationFunction;
import hr.java.JML.cost.CostFunction;
import hr.java.JML.cost.CostGradientTuple;
import hr.java.JML.learning.AbstractMinimizer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.DoubleVector.DoubleVectorElement;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;

/**
 * 
 * @author Filip Habazin
 *
 * Implementation of logistic regression used to classify between more than two values.
 * Trains n logistic regression classifiers where n is the number of needed classification values.
 * This class also implements the <code>Serializable</code> interface, and thus can be used multiple times after a single training session.
 */
public class OneVersusAll implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1270336982506955947L;
	private DoubleMatrix trainingSet;
	private DoubleMatrix testSet;
	private DenseDoubleMatrix theta;
	private DoubleVector trainingSetKnown;
	private DoubleVector testSetKnown;
	private int numOfFeatures;
	private AbstractMinimizer minimizer;
	private static final double LAMBDA = 0.5;
	private static final double THRESHOLD = 0.5;
	private int[] classifiers;
	private ActivationFunction af;
	/**
	 * Initializes <code>OneVersusAll</code> with the test and training sets, 
	 * known values of theta, and the chosen minimizer method.
	 * 
	 * Pass each data set as a single 2d double array, the constructor handles the conversion to the input matrix and the known values vector. 
	 * 
	 * @param data set used for the initial training
	 * @param data set used for validating the training
	 * @param parameters modeling the relationship between X and y
	 * @param array containing the values for each classifier, the length of this array is the number of classifiers
	 * @param choosen method of minimizing the cost can be either <code>Fmicg</code>, or <code>GradientDescent</code>
	 */
	 
	public OneVersusAll(double[][] trainingSet,	double[][] testSet, double[][] theta,
			int[] classifiers, AbstractMinimizer minimizer) {
		this.classifiers = classifiers;
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length;
		this.theta = new DenseDoubleMatrix(theta);
		af = new SigmoidActivationFunction();
		double[][] temp = new double[trainingSet.length][numOfFeatures];
		double[] tempKnown = new double[trainingSet.length];

		for (int i = 0; i < trainingSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = trainingSet[i][j];
			}
			tempKnown[i] = trainingSet[i][numOfFeatures];
		}
		this.trainingSet = new DenseDoubleMatrix(temp);
		this.trainingSetKnown = new DenseDoubleVector(tempKnown);
		

		temp = new double[testSet.length][numOfFeatures];
		tempKnown = new double[testSet.length];

		for (int i = 0; i < testSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = testSet[i][j];
			}
			tempKnown[i] = testSet[i][numOfFeatures];
		}
		this.testSet = new DenseDoubleMatrix(temp);
		this.testSetKnown = new DenseDoubleVector(tempKnown);
	}
	
	/**
	 * Initializes <code>OneVersusAll</code> with the test and training sets, 
	 * and the chosen minimizer method.
	 * 
	 * Theta is initialized to random values.
	 * Pass each data set as a single 2d double array, the constructor handles the conversion to the input matrix and the known values vector. 
	 * 
	 * @param data set used for the initial training
	 * @param data set used for validating the training
	 * @param parameters modeling the relationship between X and y
	 * @param array containing the values for each classifier, the length of this array is the number of classifiers
	 * @param choosen method of minimizing the cost can be either <code>Fmicg</code>, or <code>GradientDescent</code>
	 */
	public OneVersusAll(double[][] trainingSet, double[][] testSet, ArrayList<String> classifiers,
			AbstractMinimizer minimizer) {
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length;
		double[][] tht = new double[numOfFeatures][classifiers.size()];
		af = new SigmoidActivationFunction();
		double[][] temp = new double[trainingSet.length][numOfFeatures];
		double[] tempKnown = new double[trainingSet.length];
		Random rnd = new Random();
		for (int i = 0; i < trainingSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = trainingSet[i][j];
				tht[i][j] = rnd.nextDouble();
			}
			tempKnown[i] = trainingSet[i][numOfFeatures];
		}
		this.trainingSet = new DenseDoubleMatrix(temp);
		this.trainingSetKnown = new DenseDoubleVector(tempKnown);
		this.theta = new DenseDoubleMatrix(tht);

		

		temp = new double[testSet.length][numOfFeatures];
		tempKnown = new double[testSet.length];

		for (int i = 0; i < testSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = testSet[i][j];
			}
			tempKnown[i] = testSet[i][numOfFeatures];
		}
		this.testSet = new DenseDoubleMatrix(temp);
		this.testSetKnown = new DenseDoubleVector(tempKnown);
	}
	
	private CostFunction getCostFunction() {
		CostFunction costFunction = new CostFunction() {
			//(1/m)*(-y'* log(h) - (1 - y)'*log(1-h))+(lambda/(2*m))*theta_reg'*theta_reg;
			@Override
			public CostGradientTuple evaluateCost(DoubleVector input) {
				double cost = 0;
				DoubleVector h = af.activationFunction(trainingSet.multiplyVectorRow(input));
				DoubleVector costVect = trainingSetKnown.subtract(trainingSetKnown.multiply(2)).multiply(h).subtract(trainingSetKnown.subtractFrom(1).multiply(h));
				cost = costVect.sum() + input.dot(input) * LAMBDA / (2 * trainingSet.getRowCount());
				DoubleVector gradient = trainingSet.transpose()
						.multiplyVectorRow(h.subtract(trainingSetKnown)).add(input.multiply(LAMBDA)).divide(trainingSet.getRowCount());
				return new CostGradientTuple(cost, gradient);
			}
		};

		return costFunction;
	}
	
	
	/**
	 * Starts training until the error rate drops beneath the chosen value.
	 * @param desired error value
	 */
	public void errorLimitedTraining(double desiredError) {
		double error = 0;
		CostFunction cf = getCostFunction();
		do {
			for(int i = 0; i < classifiers.length; i++){
				this.theta.setColumnVector(i, minimizer.minimize(cf, theta.getColumnVector(i), 1000, true));
			}
			error = calculateError(testSet, testSetKnown);
		} while (error >= desiredError);
	}

	/**
	 * Trains for a set number of iterations.
	 * @param number of iterations
	 */
	public void iterationLimitedTraining(int iterations) {
		CostFunction cf = getCostFunction();
		for(int i = 0; i < classifiers.length; i++){
			this.theta.setColumnVector(i, minimizer.minimize(cf, theta.getColumnVector(i), 1000, true));
		}
	}
	
	/**
	 * Calculates the error of the predicting a given data set.
	 * @param input set
	 * @param known y values
	 * @return the error between 0 and 1
	 */
	private double calculateError(DoubleMatrix set, DoubleVector known){
		DoubleVector predictions  = predict(set);
		DoubleVector errors = predictions.subtract(known);
		Iterator<DoubleVectorElement> iter = errors.iterate();
		DoubleVectorElement elem;
		int numWrong = 0;
		while(iter.hasNext()){
			elem = iter.next();
			if (elem.getValue() != 0)
				numWrong++;
		}
		
		return numWrong / predictions.getLength();
		
	}
	
	/**
	 * Classifies the input as one of two values
	 * @param features
	 * @return returns <code>true</code> if the prediction is above the threshold probability, 
	 * <code>false</code> otherwise.
	 */
	public DoubleVector predict(DoubleMatrix set) {
		DoubleMatrix predictions = af.activationFunction(set.multiply(theta.transpose()));
		DoubleVector max = new DenseDoubleVector(predictions.getRowCount());
		for(int i = 0; i < predictions.getRowCount(); i++){
			int idx = 0;
			for(int j = 0; j < predictions.getColumnCount(); j++){
				if(predictions.get(i,idx) < predictions.get(i, j))
					idx = j;
			}
			max.set(idx, classifiers[idx]);
		}
		return max;
	}
	
	
	
}
