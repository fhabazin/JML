package hr.java.JML.neural;

import de.jungblut.math.DoubleMatrix;
import de.jungblut.math.DoubleVector;

/**
 * Cost function interface to be implemented when using with a optimizer like
 * conjugate gradient for example.
 * 
 * @author thomas.jungblut
 * 
 */
public interface NeuralCostFunction  {

  /**
   * Evaluation for the cost function to retrieve cost and gradient.
   * 
   * @param input a given input vector
   * @return a tuple consists of J (cost) and a vector X which is the gradient
   *         of the input.
   */
  public NeuralCostGradientTuple evaluateCost(DoubleMatrix input, DoubleVector expected);

}
