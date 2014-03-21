package java.JML.regression;


public abstract class GradientDescent {
	protected static double cost;
	
	public abstract RegressionParameters regressionErrorLimitedTraining(double desiredError);
	
	public abstract RegressionParameters regressionIterationLimitedTraining(double iterations);
	
	public abstract double calculateError();
	
	
	public static double getCost() {
		return cost;
	}
	
	public static void setCost(double cost) {
		GradientDescent.cost = cost;
	}
	 /*not done
	 private static RegressionParameters sigmoidFunction(SimpleMatrix sth){
		SimpleMatrix sigmoid = new SimpleMatrix(sth.numCols(),sth.numRows());
		MatrixIterator iter = sth.negative().iterator(true, 0, 0, SimpleMatrix.END, SimpleMatrix.END);
		while(iter.hasNext()){
			
			sigmoid.set(iter.getIndex(), Math.pow(Math.E, iter.next()));
		}
		return sigmoid;
	}*/

	
}