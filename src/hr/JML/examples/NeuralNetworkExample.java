package hr.JML.examples;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.Files;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import de.jungblut.math.dense.DenseDoubleMatrix;
import hr.java.JML.activationFunctions.SigmoidActivationFunction;
import hr.java.JML.learning.Fmincg;
import hr.java.JML.neural.NeuralNetwork;

public class NeuralNetworkExample {
	private static JFileChooser chooser;

	private static ArrayList<File> inputFiles;
	private static File directory;
	private static double[][] features;

	private static int[] rgbArray;

	private static void setupInputFiles() throws IOException {
		File[] allFiles = directory.listFiles();
		String delimiter = ".";
		inputFiles = new ArrayList<>();
		for (int j = 0; j < allFiles.length; j++) {
			File path = allFiles[j];

			String fileName = path.getName();
			int idx = fileName.lastIndexOf(delimiter);

			System.out.println(fileName.substring(0, idx));
			// System.err.println(pathStr);
			if (fileName.substring(idx + 1).toLowerCase().equals("jpg")) {
				inputFiles.add(path);
				// System.out.println(path.getAbsolutePath());
			}
		}

	}

	public static void main(String[] args) {
		chooser = new JFileChooser(System.getProperty("user.dir"));
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			directory = chooser.getSelectedFile();
			System.out.println(directory.getAbsolutePath());
			try {
				setupInputFiles();
				ArrayList<double[]> images = new ArrayList<>();
				double[][] inputs = getInputArraySize();
				for(int i = 0; i <inputFiles.size(); i++){
					inputs[i] = readImage(inputFiles.get(i));
					int idx = inputFiles.get(i).getName().lastIndexOf(".");
					inputs[i][inputs[i].length-1] = Double.parseDouble(inputFiles.get(i).getName().substring(0, idx));
				}

				NeuralNetwork net = new NeuralNetwork(inputs,  inputs.length, 2, new int[]{40,40}, new SigmoidActivationFunction(), new Fmincg());
				net.trainTillError(0.4);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private static double[][] getInputArraySize() throws IOException {
		BufferedImage img = ImageIO.read(inputFiles.get(0));

		return new double[inputFiles.size()][img.getWidth() * img.getHeight()+1];
	}

	private static double[] readImage(File inputFile) throws IOException {
		BufferedImage img = ImageIO.read(inputFile);
		// System.out.println(inputFile.getName());
		double[] grayscale = new double[img.getHeight() * img.getWidth()];
		int idx = 0;
		for (int x = 0; x < img.getHeight(); x++) {
			for (int y = 0; y < img.getWidth(); y++) {
				int rgb = img.getRGB(y, x);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);
				int gray = (r + g + b) / 3;
				grayscale[idx++] = gray;
			}
		}

		return grayscale;

	}

}
