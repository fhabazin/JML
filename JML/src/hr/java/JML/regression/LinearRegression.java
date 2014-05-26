package hr.java.JML.regression;

import hr.java.JML.cost.CostFunction;
import hr.java.JML.cost.CostGradientTuple;
import hr.java.JML.learning.AbstractMinimizer;

import java.io.Serializable;
import java.util.Random;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;
/**
 * 
 * @author Filip Habazin
 * 
 * Implementation of linear regression algorithm for modeling the relationship between a scalar dependent variable y
 * and one or more explanatory variables denoted X.
 * By minimizing the the cost of the function the algorithm "learns" the parameter theta that best describes the relation between y and X.
 * this class also implements the <code>Serializable</code> interface, and thus can be used multiple times after a single training session.
 */
public class LinearRegression implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5679588472409321776L;
	private DoubleMatrix trainingSet;
	private DoubleMatrix testSet;
	private DoubleVector theta;
	private DoubleVector trainingSetKnown;
	private DoubleVector testSetKnown;
	private int numOfFeatures;
	private AbstractMinimizer minimizer;
	private static final double LAMBDA = 0.5; 
	
	/**
	 * Initializes <code>LinearRegression</code> with the test and training sets, 
	 * known values of theta, and the chosen minimizer method.
	 * 
	 * Pass each data set as a single 2d double array, the constructor handles the conversion to the input matrix and the known values vector.
	 * 
	 * @param data set used for the initial training
	 * @param data set used for validating the training
	 * @param parameters modeling the relationship between X and y
	 * @param choosen method of minimizing the cost can be either <code>Fmicg</code>, or <code>GradientDescent</code>
	 */
	public LinearRegression(double[][] trainingSet,
			double[][] testSet, double[] theta, AbstractMinimizer minimizer) {
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length-1;
		this.theta = new DenseDoubleVector(theta);

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
	 * Initializes <code>LinearRegression</code> with the test and training sets, 
	 * and the chosen minimizer method.
	 * 
	 * Theta is initialized to random values.
	 * Pass each data set as a single 2d double array, the constructor handles the conversion to the input matrix and the known values vector.
	 * 
	 * @param data set used for the initial training
	 * @param data set used for validating the training
	 * @param choosen method of minimizing the cost can be either <code>Fmicg</code>, or <code>GradientDescent</code>
	 */
	public LinearRegression(double[][] trainingSet,	double[][] testSet,
			AbstractMinimizer minimizer) {
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length-1;
		double[] tht = new double[trainingSet[0].length];
		
		double[][] temp = new double[trainingSet.length][numOfFeatures];
		double[] tempKnown = new double[trainingSet.length];
		Random rnd = new Random();
		for (int i = 0; i < trainingSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = trainingSet[i][j];
				tht[j] = rnd.nextDouble();
			}
			tempKnown[i] = trainingSet[i][numOfFeatures];
		}
		this.trainingSet = new DenseDoubleMatrix(temp);
		this.trainingSetKnown = new DenseDoubleVector(tempKnown);
		this.theta = new DenseDoubleVector(tht);
		
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
	 * Starts training until the error rate drops beneath the chosen value.
	 * @param desired error value
	 */
	public void errorLimitedTraining(double desiredError) {
		double error = 0;
		CostFunction cf = getCostFunction();
		int i=1;
		do{
			this.theta = minimizer.minimize(cf, theta, 1000, true);
			error = calculateError(testSet, testSetKnown);
			System.out.println("current iteration: " + i++ +" and error: " + error);
		}while(error >= desiredError);
	}

	/**
	 * Trains for a set number of iterations.
	 * @param number of iterations
	 */
	public void iterationLimitedTraining(int iterations) {
		CostFunction cf = getCostFunction();
		this.theta = minimizer.minimize(cf, theta, iterations, true);		
	}

	/**
	 * Calculates the error of the predicting a given data set.
	 * @param input set
	 * @param known y values
	 * @return the error between 0 and 1
	 */
	public double calculateError(DoubleMatrix testSet, DoubleVector testSetKnown) {
		DoubleVector currRow;
		int wrongCount = 0;
		double currPredict;
		for(int i = 0; i< testSet.getRowCount(); i++){
			currRow = testSet.getRowVector(i);
			currPredict = currRow.dot(theta);
			System.out.println(currPredict + " " + testSetKnown.get(i));
			if (currPredict != testSetKnown.get(i)*1.05 || currPredict != testSetKnown.get(i)*0.95)
				wrongCount++;
		}
		return wrongCount/testSet.getRowCount();

	}
	
	/**
	 * 
	 * @return the cost function representation of linear regression
	 */
	private CostFunction getCostFunction(){
			CostFunction costFunction = new CostFunction() {
				
				
				@Override
				public CostGradientTuple evaluateCost(DoubleVector input) {
					double cost = 0;
					DoubleVector costVect = trainingSet.multiplyVectorRow(input);
					costVect = costVect.subtract(trainingSetKnown).pow(2).divide(2*trainingSet.getRowCount());
					cost = costVect.sum() + theta.dot(theta)*LAMBDA/2*trainingSet.getRowCount();
					DoubleVector gradient = trainingSet.transpose().multiplyVectorColumn(trainingSet.multiplyVectorRow(theta).subtract(trainingSetKnown));
					gradient = gradient.add(theta.multiply(LAMBDA));
					return new CostGradientTuple(cost, gradient);
				}
			};
			
			return costFunction;
		}
	
	
	/**
	 * Classifies the input as one of two values
	 *
	 * @return returns the predicted value for the input
	 */
	public double predict(double[] input){
		DoubleVector feat = new DenseDoubleVector(input);
		return feat.dot(theta);
	}
	
}
