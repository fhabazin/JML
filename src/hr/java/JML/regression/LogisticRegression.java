package hr.java.JML.regression;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Random;

import hr.java.JML.activationFunctions.ActivationFunction;
import hr.java.JML.activationFunctions.SigmoidActivationFunction;
import hr.java.JML.cost.CostFunction;
import hr.java.JML.cost.CostGradientTuple;
import hr.java.JML.learning.AbstractMinimizer;
import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.DoubleVector.DoubleVectorElement;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;

/**
 * 
 * @author Filip Habazin
 *
 * Implementation of logistic regression binary classifier. 
 * Logistic regression classifies the input, based on the trained theta, as one of two possible values. 
 * 
 * This class also implements the <code>Serializable</code> interface, and thus can be used multiple times after a single training session.
 */
public class LogisticRegression implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DoubleMatrix trainingSet;
	private DoubleMatrix testSet;
	private DoubleVector theta;
	private DoubleVector trainingSetKnown;
	private DoubleVector testSetKnown;
	private int numOfFeatures;
	private AbstractMinimizer minimizer;
	private static final double LAMBDA = 0.2;
	private static final double THRESHOLD = 0.5;
	private ActivationFunction af;
	
	/**
	 * Initializes <code>LogisticRegression</code> with the test and training sets, 
	 * known values of theta, and the chosen minimizer method.
	 * 
	 * Pass each data set as a single 2d double array, the constructor handles the conversion to the input matrix and the known values vector. 
	 * 
	 * @param data set used for the initial training
	 * @param data set used for validating the training
	 * @param parameters modeling the relationship between X and y
	 * @param choosen method of minimizing the cost can be either <code>Fmicg</code>, or <code>GradientDescent</code>
	 */
	public LogisticRegression(double[][] trainingSet,
			double[][] testSet, double[] theta, AbstractMinimizer minimizer) {
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length-1;
		this.theta = new DenseDoubleVector(theta);
		double[][] temp = new double[trainingSet.length][numOfFeatures];
		double[] tempKnown = new double[trainingSet.length];
		this.af = new SigmoidActivationFunction();
		
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
	public LogisticRegression(double[][] trainingSet, double[][] testSet, AbstractMinimizer minimizer) {
		this.minimizer = minimizer;
		numOfFeatures = trainingSet[0].length-1;
		double[] tht = new double[trainingSet[0].length-1];
		this.af = new SigmoidActivationFunction();
		
		double[][] temp = new double[trainingSet.length][numOfFeatures];
		double[] tempKnown = new double[trainingSet.length];
		Random rnd = new Random();
		for (int i = 0; i < trainingSet.length; i++) {
			for (int j = 0; j < numOfFeatures; j++) {
				temp[i][j] = trainingSet[i][j];
				tht[j] = rnd.nextDouble();
			}
			System.out.println(tht.length +" " + temp[i].length);
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
		do {
			this.theta = minimizer.minimize(cf, theta, 1000, true);
			error = calculateError(testSet, testSetKnown);
		} while (error >= desiredError);
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
	 * 
	 * @return the cost function representation of linear regression
	 */
	private CostFunction getCostFunction() {
		CostFunction costFunction = new CostFunction() {
			//(1/m)*(-y'* log(h) - (1 - y)'*log(1-h))+(lambda/(2*m))*theta_reg'*theta_reg;
			@Override
			public CostGradientTuple evaluateCost(DoubleVector input) {
				double cost = 0;
				DoubleVector h = af.activationFunction(trainingSet.multiplyVectorRow(input));
				DoubleVector costVect = trainingSetKnown.subtractFrom(0).multiply(h.log()).subtract(trainingSetKnown.subtractFrom(1).multiply(h.subtractFrom(1).log()));
				cost = costVect.sum() + theta.dot(theta) * LAMBDA / (2 * trainingSet.getRowCount());
				DoubleVector gradient = trainingSet.transpose()
						.multiplyVectorRow(h.subtract(trainingSetKnown)).add(theta.multiply(LAMBDA).divide(trainingSet.getRowCount()));
				return new CostGradientTuple(cost, gradient);
				
				//grad = (1/m)*(X'*(h-y)+lambda*theta_reg);
			}
		};

		return costFunction;

	}

	/**
	 * Calculates the error of the predicting a given data set.
	 * @param input set
	 * @param known y values
	 * @return the error between 0 and 1
	 */
	private double calculateError(DoubleMatrix testSet,
			DoubleVector testSetKnown) {
		DoubleVector predictions = af.activationFunction(testSet.multiplyVectorRow(theta));
		DoubleVector errors = new DenseDoubleVector(predictions.subtract(testSetKnown));
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
	public boolean predict(double[] features){
		
		
		DoubleVector feat = new DenseDoubleVector(features);
		double z = feat.dot(theta);
		double sigmoid = af.activationFunction(z);
		if(sigmoid >= THRESHOLD)
			return true;
		else
			return false;
	}
}
	
	
