package hr.JML.examples;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import de.jungblut.math.DoubleVector;
import hr.java.JML.clustering.KMeans;
import hr.java.JML.clustering.KMeansOutputData;

public class KMeansExample {
	public static void main(String... args) throws Exception {
	    BufferedImage img = ImageIO.read(new File("C:\\Users\\Filip\\Desktop\\ml\\mlclass-ex7\\bird_small.png"));
	    int[] colors = new int[img.getWidth() * img.getHeight()];
	    img.getRGB(0, 0, img.getWidth(), img.getHeight(), colors, 0, img.getWidth());

	    double[][] rgb = new double[colors.length][3];

	    for (int i = 0; i < colors.length; i++) {
	      Color color = new Color(colors[i]);
	      rgb[i][0] = (double) color.getRed();
	      rgb[i][1] = (double) color.getGreen();
	      rgb[i][2] = (double) color.getBlue();
	      
	    }
	    KMeans km = new KMeans(rgb, 32);
	    for (int k = 0; k < 20; k++) {
	    	 KMeansOutputData kmod = km.runKMeans(1);
	 	    double[] comressed = new double[3*colors.length];
	 	    for(int i = 0; i < kmod.getIndices().getLength(); i++){
	 	    	int idx = (int)kmod.getIndices().get(i);
	 	   // 	System.out.println(idx);
	 	    	for(int j = 0; j < kmod.getCentroids().getColumnCount();j++){
	 	    		comressed[3*i+j] =   kmod.getCentroids().getRowVector(idx).get(j);
	 	    	}
	 	    	
	 	    	
	 	    	
	 	    }
	 	   System.out.println(kmod. getCentroids());
		    BufferedImage compressed = (BufferedImage) getImageFromArray(comressed, img.getWidth(), img.getHeight());
		    File saveFile = new File("C:\\Users\\Filip\\Desktop\\ml\\mlclass-ex7\\bird_small_"+k+".png");
		    ImageIO.write(compressed, "png", saveFile);
		}
	   
	    
	  }
	
	public static Image getImageFromArray(double[] pixels, int width, int height) {
		System.out.println(pixels.length - width*height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(raster.getMinX(),raster.getMinY(),raster.getWidth(),raster.getHeight(),pixels);
        image.setData(raster);
        return image;
    }
}
