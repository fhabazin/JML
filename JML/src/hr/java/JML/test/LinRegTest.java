package hr.java.JML.test;

import static org.junit.Assert.*;
import hr.java.JML.learning.Fmincg;
import hr.java.JML.learning.GradientDescent;
import hr.java.JML.regression.LinearRegression;
import hr.java.JML.learning.GradientDescent.GradientDescentBuilder;
import org.junit.Test;



public class LinRegTest {

	@SuppressWarnings("deprecation")
	@Test
	public void test() {
		double[][] tr = {{1.0000000, 0.8414710, 0.5403023, 0.8414710},
				{1.0000000, 0.5984721, -0.8011436, -0.7568025},
				{1.0000000, -0.7568025, -0.6536436, 0.6569866},
				{1.0000000, -0.7055403, 0.7086698, -0.5440211},
				{1.0000000, 0.6569866, 0.7539023, 0.4201670},
				{1.0000000, 0.7984871, -0.6020119, -0.2879033},
				{1.0000000, -0.5440211, -0.8390715, 0.1498772},
				{1.0000000, -0.8754522, 0.4833048, -0.0088513},
				{1.0000000, 0.4201670, 0.9074468, -0.1323518},
				{1.0000000, 0.9348951, -0.3549243, 0.2709058}};
		
		
		LinearRegression lr = new LinearRegression(tr, tr, new Fmincg());
		lr.iterationLimitedTraining(10000000);
		for(int i = 0; i< tr.length; i++){
			assertEquals(tr[i][3], lr.predict(tr[i]), 0.05);
		}
	}
	

}
