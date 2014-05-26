package hr.java.JML.neural;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;

/**
 * More readable variant of the before used Tuple<> in {@link CostFunction}.
 * 
 * @author thomas.jungblut
 * 
 */
public class NeuralCostGradientTuple {

  private final double cost;
  private final DoubleVector gradient;

  public NeuralCostGradientTuple(double cost, DoubleVector gradient) {
    super();
    this.cost = cost;
    this.gradient = gradient;
  }

  public double getCost() {
    return this.cost;
  }

  public DoubleVector getGradient() {
    return this.gradient;
  }

}
