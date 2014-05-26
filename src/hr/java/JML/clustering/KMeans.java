package hr.java.JML.clustering;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import javax.management.MXBean;

import org.apache.commons.math3.analysis.solvers.NewtonSolver;
import org.apache.commons.math3.stat.descriptive.rank.Median;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;
import de.jungblut.math.dense.DenseDoubleMatrix;
import de.jungblut.math.dense.DenseDoubleVector;

public class KMeans implements Serializable{
	private DoubleMatrix featureSet;
	private int numOfCentroids;
	private DoubleMatrix centroids;
	private DoubleVector idx;
	
	public KMeans(double[][] featureSet, int numOfCentroids){
		this.featureSet = new DenseDoubleMatrix(featureSet);
		this.numOfCentroids = numOfCentroids;
		this.centroids = initializeCentroids();
		
	}
	
	public KMeansOutputData runKMeans(int maxIterations) throws VectorsNotEqualLengthException{
		
		DoubleMatrix prevCentroids;
		
		for(int i = 0; i < maxIterations; i++){
			
			idx = findClosestCentroids();
			
			prevCentroids = centroids;
			centroids = computeCentroids();
			System.out.println( centroids);
		}
		return new KMeansOutputData(centroids, idx);
				
	}

	private DoubleMatrix initializeCentroids() {
		Random rnd = new Random();
		int random;
		DoubleMatrix centroids = new DenseDoubleMatrix(numOfCentroids, featureSet.getColumnCount());
		for(int i = 0; i< numOfCentroids;i++){
			random = rnd.nextInt(featureSet.getRowCount());
			centroids.setRowVector(i, featureSet.getRowVector(random));
		}
		System.out.println(centroids);
		return centroids;
	}
	
	private DoubleVector findClosestCentroids(){
		DoubleVector closest = new DenseDoubleVector(featureSet.getRowCount(), 0);
		double[] distances = new double[numOfCentroids];
		DoubleVector distance;
		for(int i = 0; i < closest.getLength();i++){
			for(int j = 0; j < numOfCentroids; j++){
				distance = featureSet.getRowVector(i).subtract(centroids.getRowVector(j));
				distances[j] = distance.dot(distance);
			}
			closest.set(i, findMinimum(distances));
		}
		return closest;
	}

	private double findMinimum(double[] distances) {
		double min = Double.MAX_VALUE;
		int idx = 0;
		for(int i = 0; i < distances.length; i++){
			if(distances[i] < min){
				min = distances[i];
				idx = i;
			}
		}
		return idx;
	}

	private DoubleMatrix computeCentroids() throws VectorsNotEqualLengthException{
		ArrayList<DoubleVector> pts = new ArrayList<>();
		DoubleMatrix mat;
		for(int i = 0; i < numOfCentroids; i++){
			pts.clear();
			for(int j = 0; j < idx.getLength(); j++){
				if(i==idx.get(j))
					pts.add(featureSet.getRowVector(j));
			}
			if(pts.size()!=0){
				mat = new DenseDoubleMatrix(pts);
				System.err.println(pts.size());
				centroids.setRowVector(i, meanColumns(mat));
			}
		}
		return centroids;
		
	}

	private DoubleVector meanColumns(DoubleMatrix mat) {
		DoubleVector vect = new DenseDoubleVector(mat.getColumnCount(),0);
		for(int i = 0; i < vect.getLength(); i++){
			vect.set(i, mat.getColumnVector(i).sum()/mat.getRowCount());
		}
		
		return vect;
	}

	private boolean vectorsEqual(DoubleVector first, DoubleVector second) throws VectorsNotEqualLengthException {
		int equalCount = 0;
		if(first.getLength()==second.getLength()){
			for(int i = 0; i < first.getLength(); i++){
				if(first.get(i)==second.get(i))
					equalCount++;
			}
			
			
		}
		else
			throw new VectorsNotEqualLengthException();
		if(equalCount==first.getLength())
			return true;
		else
			return false;
		
	}
	
	 public static DoubleVector median(DoubleMatrix mat)
	  {
		 Median median = new Median();
		 double[] medians = new double[mat.getColumnCount()];
		 for(int i = 0; i < mat.getColumnCount();i++){
			 medians[i] = median.evaluate(mat.getRowVector(i).toArray());
		 }
		 return new DenseDoubleVector(medians);
	  }

	

	public void setCentroids(int numOfCentroids) {
		this.numOfCentroids = numOfCentroids;
		
	}
}
