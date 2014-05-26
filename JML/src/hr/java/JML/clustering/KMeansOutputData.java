package hr.java.JML.clustering;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;

public class KMeansOutputData {
	private DoubleMatrix centroids;
	private DoubleVector indices;
	
	public KMeansOutputData(DoubleMatrix centroids, DoubleVector indices) {
		this.centroids = centroids;
		this.indices = indices;
	}
	public DoubleMatrix getCentroids() {
		return centroids;
	}
	public void setCentroids(DoubleMatrix centroids) {
		this.centroids = centroids;
	}
	public DoubleVector getIndices() {
		return indices;
	}
	public void setIndices(DoubleVector indices) {
		this.indices = indices;
	}
	
	
}
