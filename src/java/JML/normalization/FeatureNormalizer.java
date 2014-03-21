package java.JML.normalization;
public class FeatureNormalizer {
	
	private static double mean(double[] m) {
	    double sum = 0;
	    
	    for (int i = 0; i < m.length; i++) {
	        sum += m[i];
	    }
	    
	    return sum / m.length;
	}
	
	private static double standardDeviation(double[] m) {
	    double mean = mean(m);
	    double stdDev = 0;
	    
	    for(int i = 0; i < m.length; i++){
	    	stdDev += Math.pow((m[i] - mean),2); 
	    }
	    
		return Math.sqrt(stdDev);
	}
	
	public static double[] normalizer(double[] features){
		double mean = mean(features);
		double stdDev = standardDeviation(features);
		double normalizedArray [] = new double[features.length];
		
		for(int i = 0; i < features.length; i++){
			normalizedArray[i] = (features[i] - mean) / stdDev;
		}
		
		return normalizedArray;
	}
}
