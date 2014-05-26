package hr.JML.examples;




import hr.java.JML.clustering.KMeans;
import hr.java.JML.clustering.KMeansOutputData;
import hr.java.JML.clustering.VectorsNotEqualLengthException;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.ReverbType;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class KMeansSwing {
	private static JFrame mainFrame;
	private static JSplitPane splitPane, topBottom;
	private static JScrollPane pictureScrollPane;
	static JFileChooser chooser;
	static FileNameExtensionFilter filter = new FileNameExtensionFilter("Image files",
			"jpg", "gif", "png");
	private static JMenuBar mainMenu;
	private static JMenu fileMenu;
	private static JMenuItem openMenuItem;
	private static String image;
	private static ImagePanel inputImgPanel;
	private static ImagePanel kMeanedImagePanel;
	private static JScrollPane inputScrollPane;
	private static JScrollPane outputScrollPane;
	private static JLabel numIteration, numCentroids;
	private static JButton runKmeans;
	private static JTextField numberofiterations,numCentroidsInput;
	static Image img;
	static Image outputImage;
	static double[][] rgb;
	static String previter;
	static String prevcent = previter = "";
	 static KMeans kmeans=null;
		private static int imageType;
		
		
	public static void main(String[] args){
		
		mainFrame = new JFrame("Kmeans Image compression");
		
		mainMenu = new JMenuBar();
		fileMenu = new JMenu("File");

		openMenuItem = new JMenuItem("Otvori sliku");
		openMenuItem.addActionListener(listener);
		fileMenu.add(openMenuItem);

		mainMenu.add(fileMenu);
		mainFrame.setJMenuBar(mainMenu);
		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		mainFrame.setSize(new Dimension(800, 600));
		mainFrame.setVisible(true);
	}
	
	

	
	
	public static void setUpPanel() {
		inputImgPanel = new ImagePanel();
		inputImgPanel.setImage(img);
		inputScrollPane = new JScrollPane(inputImgPanel);
		
		kMeanedImagePanel  = new ImagePanel();
		kMeanedImagePanel.setImage(outputImage);
		outputScrollPane = new JScrollPane(kMeanedImagePanel);
		
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
				inputScrollPane, outputScrollPane);

		mainFrame.setSize(inputImgPanel.getPreferredSize().width,
				inputImgPanel.getPreferredSize().height + 50);
		splitPane.setDividerLocation(mainFrame.getWidth());
		
		
		numIteration = new JLabel("Number of iterations: ");
		numberofiterations = new JTextField(10);
		numberofiterations.setText(previter);
		runKmeans = new JButton("Run KMeans");
		runKmeans.addActionListener(listener);
		numCentroids = new JLabel("number of centoids:");
		numCentroidsInput = new JTextField(10);
		numCentroidsInput.setText(prevcent);
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panel.add(numIteration);
		panel.add(numberofiterations);
		panel.add(numCentroids);
		panel.add(numCentroidsInput);
		panel.add(runKmeans);
		
		topBottom = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panel,splitPane);
		mainFrame.setContentPane(topBottom);
		mainFrame.pack();
	}
	static ActionListener listener = new ActionListener() {
		
		

		@Override
		public void actionPerformed(ActionEvent e) {
			chooser = new JFileChooser(System.getProperty("user.dir"));
			chooser.setFileFilter(filter);
			if (e.getSource()==openMenuItem) {
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					image = chooser.getSelectedFile().getAbsolutePath();
					try {
						img = ImageIO.read(new File(image));
						outputImage = ImageIO.read(new File(image));
						setUpPanel();
						readImage();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
					
					}
			}
			else if(e.getSource()==runKmeans){
				System.err.println("hi");
				prevcent = numCentroidsInput.getText();
				previter = numberofiterations.getText();
				if(kmeans==null)
					kmeans = new KMeans(rgb, Integer.parseInt(numCentroidsInput.getText()));
				 KMeansOutputData kmod = null;
				try {
					kmod = kmeans.runKMeans(Integer.parseInt(numberofiterations.getText()));
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (VectorsNotEqualLengthException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			 	    double[] comressed = new double[3*rgb.length];
			 	   for(int i = 0; i < kmod.getIndices().getLength(); i++){
			 		   
			 	    	int idx = (int)kmod.getIndices().get(i);
			 	    	System.out.println(idx);
			 	    	for(int j = 0; j < kmod.getCentroids().getColumnCount();j++){
			 	    		comressed[3*i+j] =   kmod.getCentroids().getRowVector(idx).get(j);
			 	    	}
			 	    	
			 	    	
			 	    	
			 	    }
				
			   outputImage = getImageFromArray(comressed, inputImgPanel.getImgSizeX(),inputImgPanel.getImgSizeY());
			    setUpPanel();
			}
			
			
			
		}
	};

	
	private static void readImage() throws IOException{
		 img = ImageIO.read(new File(image));
		    int[] colors = new int[inputImgPanel.getImgSizeX() * inputImgPanel.getImgSizeY()];
		    ((BufferedImage) img).getRGB(0, 0, inputImgPanel.getImgSizeX(), inputImgPanel.getImgSizeY(), colors, 0, inputImgPanel.getImgSizeY());
		    imageType =  ((BufferedImage) img).getType();
		    rgb = new double[colors.length][3];

		    for (int i = 0; i < colors.length; i++) {
		      Color color = new Color(colors[i]);
		      rgb[i][0] = (double) color.getRed();
		      rgb[i][1] = (double) color.getGreen();
		      rgb[i][2] = (double) color.getBlue();
		      
		    }
	}
	
	public static Image getImageFromArray(double[] pixels, int width, int height) {
		System.out.println(pixels.length - width*height);
        BufferedImage image = new BufferedImage(width, height, imageType);
        WritableRaster raster = (WritableRaster) image.getData();
        raster.setPixels(raster.getMinX(),raster.getMinY(),raster.getWidth(),raster.getHeight(),pixels);
        image.setData(raster);
        return image;
    }
}