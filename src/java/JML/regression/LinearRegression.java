package java.JML.regression;

import java.JML.training.GradientDescent;
import java.util.ArrayList;


public class LinearRegression extends GradientDescent {
	private ArrayList<RegressionFeatures> trainingSet;
	private ArrayList<RegressionFeatures> validationSet;
	private ArrayList<RegressionFeatures> testSet;
	private RegressionParameters params;
	
	
	public LinearRegression(ArrayList<RegressionFeatures> trainingSet,
			ArrayList<RegressionFeatures> validationSet,
			ArrayList<RegressionFeatures> testSet, RegressionParameters params) {
		this.params = params;
		this.trainingSet = trainingSet;
		this.validationSet = validationSet;
		this.testSet = testSet;
		
	}


	@Override
	public RegressionParameters regressionErrorLimitedTraining(
			double desiredError) {
		double error = 0;
		double cost = 0;
		double[][] gradient;
		do{
			cost = 0;
			for(RegressionFeatures features : trainingSet){
				cost += Math.pow(features.sumProduct(params) - features.getKnown(),2) + params.getLambda() * params.regularizedThetaSumSquared();
				cost = cost/(2*trainingSet.size());
			}
			
			gradient = new double[trainingSet.size()][trainingSet.get(0).getFeatures().length];
			for(int i = 0; i < trainingSet.size(); i++){
				gradient[i] = trainingSet.get(i).multiplyByScalar(trainingSet.get(i).sumProduct(params) - trainingSet.get(i).getKnown()); 
				gradient[i] = RegressionFeatures.add(gradient[i], params.lambdaThetaReg());
				params.setTheta(params.sub(params.multiplyByScalar(params.getAlpha())));
			}
			
			error = calculateError();
		}while(error  > desiredError);
		
		
		return params;
	}


	@Override
	public RegressionParameters regressionIterationLimitedTraining(
			double iterations) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public double calculateError() {
		double error = 0;
		for(RegressionFeatures test : testSet){
			if(test.sumProduct(params) != test.getKnown())
				error++;
		}
		return error/testSet.size();
	}


	
	
	
	
}
