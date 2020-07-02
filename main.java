import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.scene.shape.Circle;

@SuppressWarnings("unused")
public class Main {	
	
	public static void main(String[] args) {
		
		//Defining inputImages
		BufferedImage imageDeepLabV3PlusOutput = null;
		BufferedImage imageGroundTruth = null;
		
		//Reading inputImages
        try {
			imageDeepLabV3PlusOutput = ImageIO.read(new File("C://Users/Josh/Desktop/karim/DeepLab.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			imageGroundTruth = ImageIO.read(new File("C://Users/Josh/Desktop/karim/GroundTruth.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Checking inputImages dimensions compatibility
		if ((imageDeepLabV3PlusOutput.getWidth() != imageGroundTruth.getWidth()) || (imageDeepLabV3PlusOutput.getHeight() != imageGroundTruth.getHeight())) {
			System.out.println("Error: inputImages dimensions compatibility check failed !");
			System.exit(0);
		}
		
		//Building matrixColorProbability
		double [] [] matrixColorProbability = new double [imageDeepLabV3PlusOutput.getWidth()] [imageDeepLabV3PlusOutput.getHeight()]; 
		for(int i = 0; i < imageDeepLabV3PlusOutput.getWidth(); i++){
			for(int j = 0; j < imageDeepLabV3PlusOutput.getHeight(); j++){
				matrixColorProbability [i] [j] = (double) (imageDeepLabV3PlusOutput.getRGB(i, j) & 0xff) / 255;
			}
		}
		
		//Creating resultImages
		BufferedImage resultImage1 = new BufferedImage(imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage resultImage2 = new BufferedImage(imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight(), BufferedImage.TYPE_INT_RGB);
		BufferedImage resultImage3 = imageDeepLabV3PlusOutput;
		BufferedImage resultImage4 = new BufferedImage(imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		//Creating visualization frames
		JFrame frame1 = new JFrame();
		frame1.setSize(819, 645);
		frame1.setTitle("resultImage1");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel1 = new JPanel();
		frame1.add(panel1);
		JLabel picLabel1 = new JLabel(new ImageIcon(resultImage1));
		panel1.add(picLabel1);
		panel1.repaint();
		frame1.setVisible(true);
		JFrame frame2 = new JFrame();
		frame2.setSize(819, 645);
		frame2.setTitle("resultImage2");
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel2 = new JPanel();
		frame2.add(panel2);
		JLabel picLabel2 = new JLabel(new ImageIcon(resultImage2));
		panel2.add(picLabel2);
		panel2.repaint();
		frame2.setVisible(true);
		JFrame frame3 = new JFrame();
		frame3.setSize(819, 645);
		frame3.setTitle("resultImage3");
		frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel3 = new JPanel();
		frame3.add(panel3);
		JLabel picLabel3 = new JLabel(new ImageIcon(resultImage3));
		panel3.add(picLabel3);
		panel3.repaint();
		frame3.setVisible(true);
		JFrame frame4 = new JFrame();
		frame4.setSize(819, 645);
		frame4.setTitle("resultImage4");
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel panel4 = new JPanel();
		frame4.add(panel4);
		JLabel picLabel4 = new JLabel(new ImageIcon(resultImage4));
		panel4.add(picLabel4);
		panel4.repaint();
		frame4.setVisible(true);
		
		//Defining statisticalData XSSF variables
		/*XSSFWorkbook workbook = new XSSFWorkbook();
		Sheet sheet1 = workbook.createSheet("Energy Kept");
		Sheet sheet2 = workbook.createSheet("Undiscovered Pixels");
		Row row1 = null;
		Row row2 = null;
		int excelColumnCounter = 0;
		int excelRowCounter = 0;*/
        
		//Defining RJMCMC parameters
		boolean loop1, loop2;
		//int numberIterationsWTemperature = Integer. parseInt(args[0]);
		//int numberIterationsOverall = Integer. parseInt(args[1]);
		//int heatMapeConsideration = Integer. parseInt(args[2]);
		//int numberCircles = Integer. parseInt(args[3]);
	    //int rmin = Integer. parseInt(args[4]);
		//int rmax = Integer. parseInt(args[5]);
		//double thresholdOverlap = Double. parseDouble(args[6]);
		//double probabilityCoefficient = Double. parseDouble(args[7]);
		//double sizeCoefficient = Double. parseDouble(args[8]);
		//double connectionCoefficient = Double. parseDouble(args[9]);
		//double distanceToConnectionCoefficient = Double. parseDouble(args[10]);
		//double distanceToNearbyCoefficient = Double. parseDouble(args[11]);
		//double radiusSimilarityCoefficient = Double. parseDouble(args[12]);
		//double verticalAngleCoefficient = Double. parseDouble(args[13]);
		//NormalDistribution sizeDistribution = new NormalDistribution(rmax, rmax / 2);
		//NormalDistribution distanceToConnectionDistribution = new NormalDistribution(0, 1);
		//NormalDistribution distanceToNearbyDistribution = new NormalDistribution(0, 1);
		//NormalDistribution radiusSimilarityDistribution = new NormalDistribution(0, rmax / 2);
		NormalDistribution verticalAngleDistribution = new NormalDistribution(0, 1);
		Circle [] circlesMCMC = new Circle [9999];
		boolean [] statesMCMC = new boolean [9999];
		int [] connectionsMCMCKept = new int [9999];
		int [] connectionsMCMCProposed = new int [9999];
		int [] nearbyMCMCKept = new int [9999];
		int [] nearbyMCMCProposed = new int [9999];
		List<Integer> connectionsMCMCChangeList = new ArrayList<Integer>();
		List<Integer> nearbyMCMCChangeList = new ArrayList<Integer>();
		double [] [] distanceMatrix = new double [9999] [9999];
		double [][] dataBuffer =  new double [9999][12];
		boolean [][] matrixMCMCContours = new boolean [imageDeepLabV3PlusOutput.getWidth()][imageDeepLabV3PlusOutput.getHeight()];
		boolean [][] matrixMCMCFull = new boolean [imageDeepLabV3PlusOutput.getWidth()][imageDeepLabV3PlusOutput.getHeight()];
		boolean [][] matrixMCMCFullHeated = new boolean [imageDeepLabV3PlusOutput.getWidth()][imageDeepLabV3PlusOutput.getHeight()];
		boolean [][] matrixMCMCFullProhibited = new boolean [imageDeepLabV3PlusOutput.getWidth()][imageDeepLabV3PlusOutput.getHeight()];
		int numberSets;
		int [] setsRepartition = new int [9999];
		List<Integer> additionalConnectionsN1 = new ArrayList<Integer>();
		List<Integer> additionalConnectionsN2 = new ArrayList<Integer>();
		int indexCircleDelete = -1;
		Circle circleDelete = new Circle ();
		Random random = new Random();
		UniformIntegerDistribution xDistribution = new UniformIntegerDistribution(0,imageDeepLabV3PlusOutput.getWidth()-1);
		UniformIntegerDistribution yDistribution = new UniformIntegerDistribution(0,imageDeepLabV3PlusOutput.getHeight()-1);
		//UniformIntegerDistribution rDistribution = new UniformIntegerDistribution(rmin,rmax);
		//UniformIntegerDistribution xTRDistribution = new UniformIntegerDistribution(-rmax,rmax);
		//UniformIntegerDistribution yTRDistribution = new UniformIntegerDistribution(-rmax,rmax);
		//UniformIntegerDistribution rDIDistribution = new UniformIntegerDistribution(-rmax,rmax);
		//UniformIntegerDistribution xTRsHDistribution = new UniformIntegerDistribution(-3,3);
		//UniformIntegerDistribution yTRsHDistribution = new UniformIntegerDistribution(-3,3);
		//UniformIntegerDistribution rDIsHDistribution = new UniformIntegerDistribution(-3,3);
		double energyKept = Double.POSITIVE_INFINITY;
		double energyProposed = 0;
		double gain = 0;
		double acceptanceMCMC = 1;
		double temperature = 0;
		int mcmcMove;
		UniformIntegerDistribution mcmcMoveDistribution = new UniformIntegerDistribution(1,3);
		
		//Executing RJMCMC
		/*if (numberIterationsOverall < numberCircles) {
			System.out.println("Error: conflicting RJMCMC parameters !");
			System.exit(0);
		}
		else {
			int k2 = 1;
			loop2 = true;
			while (loop2) {
				int k1 = 1;
				loop1 = true;
				while (loop1) {
					int i = 0;
					row2 = sheet2.createRow(excelRowCounter);
					excelColumnCounter = 0;
					if ((k2 != 1) || (k1 != 1)) {
						for(int m = 0; m < imageDeepLabV3PlusOutput.getWidth(); m++){
							for(int n = 0; n < imageDeepLabV3PlusOutput.getHeight(); n++){
								matrixMCMCContours [m] [n] = false; 
								matrixMCMCFull [m] [n] = false; 
								matrixMCMCFullHeated [m] [n] = false;
							}
						}
					}
					while (i < numberCircles) {
						if (!statesMCMC [i]) {
							do {
								birth (circlesMCMC, i, xDistribution, yDistribution, rDistribution);
								smartShaking (circlesMCMC, i, matrixColorProbability, rmin, rmax, xTRsHDistribution, yTRsHDistribution, rDIsHDistribution);
								for (int d = i-1; d >= 0; d--)
									distanceMatrix [i] [d] = distanceMatrix [d] [i] = distancePointToPoint (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [d].getCenterX(), circlesMCMC [d].getCenterY());
							} while (Double.isInfinite(overlapEnergy (thresholdOverlap, numberCircles, circlesMCMC, statesMCMC, distanceMatrix, false, i, imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight())) || (intersectionCircleToProhibitionMape (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCFullProhibited)));
						}
						else {
							for (int d = i-1; d >= 0; d--)
								distanceMatrix [i] [d] = distanceMatrix [d] [i] = distancePointToPoint (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [d].getCenterX(), circlesMCMC [d].getCenterY());
						}
						matrixMCMCContoursAddDelete (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCContours, true);
						matrixMCMCFullAdd (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCFull, false);
						matrixMCMCFullAdd (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCFullHeated, true);
						resultImage1 = matrixToBufferedImage (matrixMCMCContours);
				    	resultImage2 = matrixToBufferedImage (matrixMCMCFull);
				    	resultImage3 = circlesToImage (imageDeepLabV3PlusOutput, numberCircles, circlesMCMC, i);
				    	resultImage4 = matrixToBufferedImage (matrixMCMCFullHeated);
				    	picLabel1.setIcon(new ImageIcon(resultImage1));
						picLabel2.setIcon(new ImageIcon(resultImage2));
						picLabel3.setIcon(new ImageIcon(resultImage3));
						picLabel4.setIcon(new ImageIcon(resultImage4));
						picLabel1.repaint();
						picLabel2.repaint();
						picLabel3.repaint();
						picLabel4.repaint();
						System.out.println("RJMCMC iteration: " + Integer.toString(i+1) + " out of: " + numberIterationsOverall + " - Loop1 iteration: " + Integer.toString(k1) + " out of: undefined" + " - Loop2 iteration: " + Integer.toString(k2) + " out of: undefined");
						i++;
					}
					energyKept = 0;
					for (int j = 0; j < numberCircles; j++) {
						energyKept += circleEnergy (numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, connectionsMCMCProposed, nearbyMCMCKept, nearbyMCMCProposed, connectionsMCMCChangeList, nearbyMCMCChangeList, distanceMatrix, dataBuffer, matrixColorProbability, false, j, -1, probabilityCoefficient, sizeCoefficient, connectionCoefficient, distanceToConnectionCoefficient, distanceToNearbyCoefficient, radiusSimilarityCoefficient, verticalAngleCoefficient, rmax, sizeDistribution, distanceToConnectionDistribution, distanceToNearbyDistribution, radiusSimilarityDistribution, verticalAngleDistribution);
					}
					connectionsToImage (resultImage1, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					connectionsToImage (resultImage2, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					connectionsToImage (resultImage3, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					picLabel1.setIcon(new ImageIcon(resultImage1));
					picLabel2.setIcon(new ImageIcon(resultImage2));
					picLabel3.setIcon(new ImageIcon(resultImage3));
					picLabel4.setIcon(new ImageIcon(resultImage4));
					picLabel1.repaint();
					picLabel2.repaint();
					picLabel3.repaint();
					picLabel4.repaint();
					try {
						ImageIO.write(resultImage1, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage1_" + Integer.toString(i) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						ImageIO.write(resultImage2, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage2_" + Integer.toString(i) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						ImageIO.write(resultImage3, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage3_" + Integer.toString(i) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						ImageIO.write(resultImage4, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage4_" + Integer.toString(i) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while (i < numberIterationsOverall) {
						if (i < numberIterationsWTemperature)
							temperature = 1;
						else
							temperature = 1;
						mcmcMove = mcmcMoveDistribution.sample();
						do {
							indexCircleDelete = random.nextInt(numberCircles);
						} while (statesMCMC [indexCircleDelete]);
						circleDelete.setCenterX(circlesMCMC [indexCircleDelete].getCenterX());
						circleDelete.setCenterY(circlesMCMC [indexCircleDelete].getCenterY());
						circleDelete.setRadius(circlesMCMC [indexCircleDelete].getRadius());
						if (mcmcMove == 1)
						   birth (circlesMCMC, indexCircleDelete, xDistribution, yDistribution, rDistribution);
						else if (mcmcMove == 2)
							translation (circlesMCMC, indexCircleDelete, xTRDistribution, yTRDistribution, imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight());
						else
							dilatation (circlesMCMC, indexCircleDelete, rDIDistribution, rmin, rmax);
						smartShaking (circlesMCMC, indexCircleDelete, matrixColorProbability, rmin, rmax, xTRsHDistribution, yTRsHDistribution, rDIsHDistribution);
						if ((!intersectionCircleToProhibitionMape (circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY(), circlesMCMC [indexCircleDelete].getRadius(), matrixMCMCFullProhibited)) && ((i < heatMapeConsideration - 1) || (matrixMCMCFullHeated [(int) circlesMCMC [indexCircleDelete].getCenterX()][(int) circlesMCMC [indexCircleDelete].getCenterY()]))) {
							for (int j = 0; j < numberCircles; j++) {
								if (j != indexCircleDelete) {
									distanceMatrix [j] [indexCircleDelete] = distanceMatrix [indexCircleDelete] [j] = distancePointToPoint (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY());
								}
							}
							if (overlapEnergy (thresholdOverlap, numberCircles, circlesMCMC, statesMCMC, distanceMatrix, true, indexCircleDelete, imageDeepLabV3PlusOutput.getWidth(), imageDeepLabV3PlusOutput.getHeight()) == 0) {
								energyProposed = 0;
								for (int j = 0; j < numberCircles; j++) {
									 energyProposed += circleEnergy (numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, connectionsMCMCProposed, nearbyMCMCKept, nearbyMCMCProposed, connectionsMCMCChangeList, nearbyMCMCChangeList, distanceMatrix, dataBuffer, matrixColorProbability, true, j, indexCircleDelete, probabilityCoefficient, sizeCoefficient, connectionCoefficient, distanceToConnectionCoefficient, distanceToNearbyCoefficient, radiusSimilarityCoefficient, verticalAngleCoefficient, rmax, sizeDistribution, distanceToConnectionDistribution, distanceToNearbyDistribution, radiusSimilarityDistribution, verticalAngleDistribution);
								}
								gain = energyKept / energyProposed;
								if (Math.pow(gain, temperature) >= Math.min(1, acceptanceMCMC)) {
									matrixMCMCContoursAddDelete (circleDelete.getCenterX(), circleDelete.getCenterY(), circleDelete.getRadius(), matrixMCMCContours, false);
									matrixMCMCFullDelete (circleDelete.getCenterX(), circleDelete.getCenterY(), circleDelete.getRadius(), numberCircles, circlesMCMC, indexCircleDelete, matrixMCMCFull, false);
									matrixMCMCFullDelete (circleDelete.getCenterX(), circleDelete.getCenterY(), circleDelete.getRadius(), numberCircles, circlesMCMC, indexCircleDelete, matrixMCMCFullHeated, true);
									for (int j = 0; j < numberCircles; j++) {
										connectionsMCMCKept [j] = connectionsMCMCProposed [j];
										nearbyMCMCKept [j] = nearbyMCMCProposed [j];
									}
									dataBuffer [indexCircleDelete][0] = dataBuffer [9998][0];
									dataBuffer [indexCircleDelete][1] = dataBuffer [9998][1];
									for (int j = 0; j < connectionsMCMCChangeList.size(); j++) {
										dataBuffer [connectionsMCMCChangeList.get(j)][2] = dataBuffer [connectionsMCMCChangeList.get(j)][3];
										dataBuffer [connectionsMCMCChangeList.get(j)][4] = dataBuffer [connectionsMCMCChangeList.get(j)][5];
										dataBuffer [connectionsMCMCChangeList.get(j)][8] = dataBuffer [connectionsMCMCChangeList.get(j)][9];
										dataBuffer [connectionsMCMCChangeList.get(j)][10] = dataBuffer [connectionsMCMCChangeList.get(j)][11];
									}
									for (int j = 0; j < nearbyMCMCChangeList.size(); j++) {
										dataBuffer [nearbyMCMCChangeList.get(j)][6] = dataBuffer [nearbyMCMCChangeList.get(j)][7];
									}
									connectionsMCMCChangeList.clear();
									nearbyMCMCChangeList.clear();
									matrixMCMCContoursAddDelete (circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY(), circlesMCMC [indexCircleDelete].getRadius(), matrixMCMCContours, true);
									matrixMCMCFullAdd (circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY(), circlesMCMC [indexCircleDelete].getRadius(), matrixMCMCFull, false);
									matrixMCMCFullAdd (circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY(), circlesMCMC [indexCircleDelete].getRadius(), matrixMCMCFullHeated, true);
									energyKept = energyProposed;
								}
								else {
									circlesMCMC [indexCircleDelete].setCenterX(circleDelete.getCenterX());
									circlesMCMC [indexCircleDelete].setCenterY(circleDelete.getCenterY());
									circlesMCMC [indexCircleDelete].setRadius(circleDelete.getRadius());
									connectionsMCMCChangeList.clear();
									nearbyMCMCChangeList.clear();
									for (int j = 0; j < numberCircles; j++) {
										if (j != indexCircleDelete) {
											distanceMatrix [j] [indexCircleDelete] = distanceMatrix [indexCircleDelete] [j] = distancePointToPoint (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY());
										}
									}
								}
							}
							else {
								circlesMCMC [indexCircleDelete].setCenterX(circleDelete.getCenterX());
								circlesMCMC [indexCircleDelete].setCenterY(circleDelete.getCenterY());
								circlesMCMC [indexCircleDelete].setRadius(circleDelete.getRadius());
								for (int j = 0; j < numberCircles; j++) {
									if (j != indexCircleDelete) {
										distanceMatrix [j] [indexCircleDelete] = distanceMatrix [indexCircleDelete] [j] = distancePointToPoint (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [indexCircleDelete].getCenterX(), circlesMCMC [indexCircleDelete].getCenterY());
									}
								}
							}
						}
						else {
							circlesMCMC [indexCircleDelete].setCenterX(circleDelete.getCenterX());
							circlesMCMC [indexCircleDelete].setCenterY(circleDelete.getCenterY());
							circlesMCMC [indexCircleDelete].setRadius(circleDelete.getRadius());
						}	
						if (((i+1) % 1000 == 0) || ((i+1) == numberIterationsOverall)) {
							resultImage1 = matrixToBufferedImage (matrixMCMCContours);
					    	resultImage2 = matrixToBufferedImage (matrixMCMCFull);
					    	resultImage3 = circlesToImage (imageDeepLabV3PlusOutput, numberCircles, circlesMCMC, i);
					    	resultImage4 = matrixToBufferedImage (matrixMCMCFullHeated);
					    	connectionsToImage (resultImage1, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
							connectionsToImage (resultImage2, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
							connectionsToImage (resultImage3, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
							picLabel1.setIcon(new ImageIcon(resultImage1));
							picLabel2.setIcon(new ImageIcon(resultImage2));
							picLabel3.setIcon(new ImageIcon(resultImage3));
							picLabel4.setIcon(new ImageIcon(resultImage4));
							picLabel1.repaint();
							picLabel2.repaint();
							picLabel3.repaint();
							picLabel4.repaint();
							System.out.println("RJMCMC iteration: " + Integer.toString(i+1) + " out of: " + numberIterationsOverall + " - Loop1 iteration: " + Integer.toString(k1) + " out of: undefined" + " - Loop2 iteration: " + Integer.toString(k2) + " out of: undefined");
							try {
								ImageIO.write(resultImage1, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage1_" + Integer.toString(i+1) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								ImageIO.write(resultImage2, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage2_" + Integer.toString(i+1) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								ImageIO.write(resultImage3, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage3_" + Integer.toString(i+1) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								ImageIO.write(resultImage4, "png", new File("C://Users/KEPLAB-04/Desktop/test/" + args[15]+ "/resultImage4_" + Integer.toString(i+1) + "_" + Integer.toString(k1) + "_" + Integer.toString(k2) + ".png"));
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							row2.createCell(excelColumnCounter).setCellValue(undiscoveredParts (resultImage2, imageGroundTruth, numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2));
							excelColumnCounter++;
						}
						i++;
					}
					//loop1 = structureOptimizer (numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, distanceMatrix, dataBuffer, matrixColorProbability, connectionCoefficient, distanceToConnectionCoefficient, radiusSimilarityCoefficient, verticalAngleCoefficient, distanceToConnectionDistribution, radiusSimilarityDistribution, verticalAngleDistribution);
					loop1 = false;
					if ((k2 != 1) || (k1 != 1)) {
						for(int m = 0; m < imageDeepLabV3PlusOutput.getWidth(); m++){
							for(int n = 0; n < imageDeepLabV3PlusOutput.getHeight(); n++){
								matrixMCMCFullProhibited [m] [n] = false; 
							}
						}
					}
					for (int j = 0; j < numberCircles; j++) {
						if (statesMCMC [j]) {
							matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFullProhibited, false);
							expandConnectionInMatrix (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), circlesMCMC [connectionsMCMCKept [j]].getCenterX(), circlesMCMC [connectionsMCMCKept [j]].getCenterY(), circlesMCMC [connectionsMCMCKept [j]].getRadius(), matrixMCMCFullProhibited);
						}
					}
					k1++;
					excelRowCounter++;
				}
				k2++;
				excelRowCounter++;
				int h = 0;
				boolean intersects = false;
				while (h < numberCircles) {
					if (!statesMCMC [h]) {
						intersects =  false;
						int iC = 0;
						while ((iC < numberCircles) && (!intersects)) {
							if ((iC != h) && (iC != connectionsMCMCKept [h])) {
								if (intersectionCircleToSegment (iC, h, connectionsMCMCKept [h], circlesMCMC, distanceMatrix))
									intersects = true;
								else
									iC++;
							}
							else
								iC++;
						}
						if (((dataBuffer [h][0] / probabilityCoefficient) >= 0.3) || (connectionProbability (circlesMCMC [h].getCenterX(), circlesMCMC [h].getCenterY(), circlesMCMC [h].getRadius(), circlesMCMC [connectionsMCMCKept [h]].getCenterX(), circlesMCMC [connectionsMCMCKept [h]].getCenterY(), circlesMCMC [connectionsMCMCKept [h]].getRadius(), matrixColorProbability) <= 0.5) || (intersects)) {
							loop2 = false;
							for (int j = h+1; j < numberCircles; j++) {
								circlesMCMC [j-1].setCenterX(circlesMCMC [j].getCenterX());
								circlesMCMC [j-1].setCenterY(circlesMCMC [j].getCenterY());
								circlesMCMC [j-1].setRadius(circlesMCMC [j].getRadius());
								connectionsMCMCKept [j-1] = connectionsMCMCKept [j];
								statesMCMC [j-1] = statesMCMC [j];
								dataBuffer [j-1][0] = dataBuffer [j][0];
							}
							numberCircles--;
							for (int j = 0; j < numberCircles; j++) {
								if (connectionsMCMCKept [j] > h)
									connectionsMCMCKept [j]--;
							}
							for (int v1 = 0; v1 < numberCircles; v1++) {
								for (int v2 = 0; v2 < numberCircles; v2++) {
									if (v1 < v2)
										distanceMatrix [v1] [v2] = distanceMatrix [v2] [v1] = distancePointToPoint (circlesMCMC [v1].getCenterX(), circlesMCMC [v1].getCenterY(), circlesMCMC [v2].getCenterX(), circlesMCMC [v2].getCenterY());
								}
							}
							h = 0;
						}
						else
							h++;
					}
					else
						h++;
				}
				//loop2 = false;
				if (loop2 == true) {
					for(int m = 0; m < imageDeepLabV3PlusOutput.getWidth(); m++){
						for(int n = 0; n < imageDeepLabV3PlusOutput.getHeight(); n++){
							matrixMCMCFullProhibited [m] [n] = false; 
						}
					}
					for (int j = 0; j < numberCircles; j++) {
						statesMCMC [j] = true;
						matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFullProhibited, false);
						expandConnectionInMatrix (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), circlesMCMC [connectionsMCMCKept [j]].getCenterX(), circlesMCMC [connectionsMCMCKept [j]].getCenterY(), circlesMCMC [connectionsMCMCKept [j]].getRadius(), matrixMCMCFullProhibited);
					}
					numberCircles += 50;
				}
				else {
					for(int m = 0; m < imageDeepLabV3PlusOutput.getWidth(); m++){
						for(int n = 0; n < imageDeepLabV3PlusOutput.getHeight(); n++){
							matrixMCMCContours [m] [n] = false; 
							matrixMCMCFull [m] [n] = false; 
							matrixMCMCFullHeated [m] [n] = false;
						}
					}
					for (int j = 0; j < numberCircles; j++) {
						matrixMCMCContoursAddDelete (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCContours, true);
						matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFull, false);
						matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFullHeated, true);
					}
					resultImage1 = matrixToBufferedImage (matrixMCMCContours);
			    	resultImage2 = matrixToBufferedImage (matrixMCMCFull);
			    	resultImage3 = circlesToImage (imageDeepLabV3PlusOutput, numberCircles, circlesMCMC, 9998);
			    	resultImage4 = matrixToBufferedImage (matrixMCMCFullHeated);
			    	connectionsToImage (resultImage1, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					connectionsToImage (resultImage2, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					connectionsToImage (resultImage3, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
					picLabel1.setIcon(new ImageIcon(resultImage1));
					picLabel2.setIcon(new ImageIcon(resultImage2));
					picLabel3.setIcon(new ImageIcon(resultImage3));
					picLabel4.setIcon(new ImageIcon(resultImage4));
					picLabel1.repaint();
					picLabel2.repaint();
					picLabel3.repaint();
					picLabel4.repaint();
				}
			}
		}*/
		
		//Writing solverData
		/*try(FileWriter fw = new FileWriter("C://Users/KEPLAB-04/Desktop/test/" + args[15] + "/solverData.txt", false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw))
		{
			for (int h = 0; h < numberCircles; h++) {
			     out.println(Double.toString(circlesMCMC [h].getCenterX()));
			     out.println(Double.toString(circlesMCMC [h].getCenterY()));
			     out.println(Double.toString(circlesMCMC [h].getRadius()));
			     out.println(Integer.toString(connectionsMCMCKept [h]));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Loading Data !!!
		Path path = Paths.get("C://Users/Josh/Desktop/karim/solverData.txt");
		int lineCount = -1;
		try {
			lineCount = (int) Files.lines(path).count();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		int numberCircles = lineCount / 4;
		for (int v = 0; v < numberCircles; v++) {
		     circlesMCMC [v] = new Circle (0);
		}
		int var1 = 0;
		int var2 = 0;
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("C://Users/Josh/Desktop/karim/solverData.txt"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}  
		while (scanner.hasNextLine()) {  
		   String line = scanner.nextLine();
		   if (var1 == 0)
				circlesMCMC [var2].setCenterX(Double.valueOf(line));
			else if (var1 == 1)
				circlesMCMC [var2].setCenterY(Double.valueOf(line));
			else if (var1 == 2)
				circlesMCMC [var2].setRadius(Double.valueOf(line));
			else
				connectionsMCMCKept [var2] = Integer.valueOf(line);
			var1++;
			if (var1 == 4) {
				var1 = 0;
				var2++;
			}
		}
		for (int v = 0; v < numberCircles; v++) {
			matrixMCMCContoursAddDelete (circlesMCMC [v].getCenterX(), circlesMCMC [v].getCenterY(), circlesMCMC [v].getRadius(), matrixMCMCContours, true);
			matrixMCMCFullAdd (circlesMCMC [v].getCenterX(), circlesMCMC [v].getCenterY(), circlesMCMC [v].getRadius(), matrixMCMCFull, false);
			matrixMCMCFullAdd (circlesMCMC [v].getCenterX(), circlesMCMC [v].getCenterY(), circlesMCMC [v].getRadius(), matrixMCMCFullHeated, true);
		}
		for (int d1 = 0; d1< numberCircles; d1++) {
			for (int d2 = 0; d2< numberCircles; d2++) {
				if (d1 != d2) {
					distanceMatrix [d1] [d2] = distancePointToPoint (circlesMCMC [d1].getCenterX(), circlesMCMC [d1].getCenterY(), circlesMCMC [d2].getCenterX(), circlesMCMC [d2].getCenterY());
				}
			}
		}
		
		//Updating resultImage !!!
		for (int j = 0; j < numberCircles; j++) {
			matrixMCMCContoursAddDelete (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCContours, true);
			matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFull, false);
			matrixMCMCFullAdd (circlesMCMC [j].getCenterX(), circlesMCMC [j].getCenterY(), circlesMCMC [j].getRadius(), matrixMCMCFullHeated, true);
		}
		resultImage1 = matrixToBufferedImage (matrixMCMCContours);
    	resultImage2 = matrixToBufferedImage (matrixMCMCFull);
    	resultImage3 = circlesToImage (imageDeepLabV3PlusOutput, numberCircles, circlesMCMC, 9998);
    	resultImage4 = matrixToBufferedImage (matrixMCMCFullHeated);
    	connectionsToImage (resultImage1, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
		connectionsToImage (resultImage2, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
		connectionsToImage (resultImage3, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept);
		picLabel1.setIcon(new ImageIcon(resultImage1));
		picLabel2.setIcon(new ImageIcon(resultImage2));
		picLabel3.setIcon(new ImageIcon(resultImage3));
		picLabel4.setIcon(new ImageIcon(resultImage4));
		picLabel1.repaint();
		picLabel2.repaint();
		picLabel3.repaint();
		picLabel4.repaint();
		
		
		//Checking additional connections
		intoSets (numberCircles, connectionsMCMCKept, setsRepartition);
		System.out.println("Initialized");
		additionalConnections00 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 1.01);
		System.out.println("00 over!");
		additionalConnections0 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 1.5);
		System.out.println("0 over!");
		System.out.println("Step 1 over!");
		additionalConnections1 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 1);
		System.out.println("1-1 over!");
		additionalConnections1 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 0.9);
		System.out.println("1-0.9 over!");
		additionalConnections1 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 0.8);
		System.out.println("1-0.8 over!");
		additionalConnections1 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 0.7);
		System.out.println("1-0.7 over!");
		System.out.println("Step 2 over!");
		additionalConnections2 (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, distanceMatrix, matrixColorProbability, 37);
		System.out.println("Step 3 over!");
		numberSets = 0;
    	for(int i = 0; i < numberCircles; i++){
    		if (numberSets < setsRepartition [i])
    			numberSets = setsRepartition [i];
    	}
		
		//Updating visualization frames
		additionalConnectionsToImage (resultImage1, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2);
		additionalConnectionsToImage (resultImage2, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2);
		additionalConnectionsToImage (resultImage3, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2);
		picLabel1.setIcon(new ImageIcon(resultImage1));
		picLabel2.setIcon(new ImageIcon(resultImage2));
		picLabel3.setIcon(new ImageIcon(resultImage3));
		picLabel1.repaint();
		picLabel2.repaint();
		picLabel3.repaint();
		
		//Writing resultImages
		try {
			ImageIO.write(resultImage1, "png", new File("C://Users/Josh/Desktop/karim/resultImage1.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage2, "png", new File("C://Users/Josh/Desktop/karim/resultImage2.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage3, "png", new File("C://Users/Josh/Desktop/karim/resultImage3.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage4, "png", new File("C://Users/Josh/Desktop/karim/resultImage4.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//No small blobs part
		for(int m = 0; m < imageDeepLabV3PlusOutput.getWidth(); m++){
			for(int n = 0; n < imageDeepLabV3PlusOutput.getHeight(); n++){
				matrixMCMCContours [m] [n] = false; 
				matrixMCMCFull [m] [n] = false; 
				matrixMCMCFullHeated [m] [n] = false;
			}
		}
		
	    boolean [] treeOrBlobs = new boolean [numberSets];
		for (int i = 1; i <= numberSets; i++) {
			if (areaSet (numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, i, imageGroundTruth.getWidth(), imageGroundTruth.getHeight()) >= (0 * imageGroundTruth.getWidth() * imageGroundTruth.getHeight()))
				treeOrBlobs [i-1] = true;
		}
		for (int i = 0; i < numberCircles; i++) {
			if (treeOrBlobs [setsRepartition [i] - 1]) {
				matrixMCMCContoursAddDelete (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCContours, true);
				matrixMCMCFullAdd (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCFull, false);
				matrixMCMCFullAdd (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), matrixMCMCFullHeated, true);
			}
		}
		resultImage1 = matrixToBufferedImage (matrixMCMCContours);
    	resultImage2 = matrixToBufferedImage (matrixMCMCFull);
    	resultImage3 = circlesToImageNoBlobs (imageDeepLabV3PlusOutput, numberCircles, circlesMCMC, setsRepartition, treeOrBlobs);
    	resultImage4 = matrixToBufferedImage (matrixMCMCFullHeated);
    	connectionsToImageNoBlobs (resultImage1, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, setsRepartition, treeOrBlobs);
		connectionsToImageNoBlobs (resultImage2, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, setsRepartition, treeOrBlobs);
		connectionsToImageNoBlobs (resultImage3, numberCircles, circlesMCMC, statesMCMC, connectionsMCMCKept, setsRepartition, treeOrBlobs);
		additionalConnectionsToImageNoBlobs (resultImage1, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, treeOrBlobs);
		additionalConnectionsToImageNoBlobs (resultImage2, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, treeOrBlobs);
		additionalConnectionsToImageNoBlobs (resultImage3, circlesMCMC, additionalConnectionsN1, additionalConnectionsN2, setsRepartition, treeOrBlobs);
		picLabel1.setIcon(new ImageIcon(resultImage1));
		picLabel2.setIcon(new ImageIcon(resultImage2));
		picLabel3.setIcon(new ImageIcon(resultImage3));
		picLabel4.setIcon(new ImageIcon(resultImage4));
		picLabel1.repaint();
		picLabel2.repaint();
		picLabel3.repaint();
		picLabel4.repaint();
		/*try {
			ImageIO.write(resultImage1, "png", new File("C://Users/Josh/Desktop/LST/" + num + "z/" + num2 + "/resultImage1_NoBlobs.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage2, "png", new File("C://Users/Josh/Desktop/LST/" + num + "z/" + num2 + "/resultImage2_NoBlobs.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage3, "png", new File("C://Users/Josh/Desktop/LST/" + num + "z/" + num2 + "/resultImage3_NoBlobs.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			ImageIO.write(resultImage4, "png", new File("C://Users/Josh/Desktop/LST/" + num + "z/" + num2 + "/resultImage4_NoBlobs.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
				
		//Computing last statisticalData
		//row2.createCell(excelColumnCounter).setCellValue(undiscoveredParts (resultImage2, imageGroundTruth, numberCircles, circlesMCMC, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2));
		
		//Writing statisticalData
		/*FileOutputStream output = null;
		try {
			output = new FileOutputStream(new File("C://Users/KEPLAB-04/Desktop/test/" + args[15] + "/statisticalData.xlsx"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			workbook.write(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			workbook.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Creating finalImage
		BufferedImage finalImage = BufferedImageDeepCopy (resultImage2);
		for(int i = 0; i < finalImage.getWidth(); i++){
			for(int j = 0; j < finalImage.getHeight(); j++){
				if ((finalImage.getRGB(i, j) & 0x00FFFFFF) != 0)
					finalImage.setRGB(i, j, Color.WHITE.getRGB());
			}
		}
		for(int i = 0; i < numberCircles; i++){
			if (treeOrBlobs [setsRepartition [i] - 1])
				expandConnectionInImage (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), circlesMCMC [connectionsMCMCKept [i]].getCenterX(), circlesMCMC [connectionsMCMCKept [i]].getCenterY(), circlesMCMC [connectionsMCMCKept [i]].getRadius(), finalImage);
		}
		for(int i = 0; i < additionalConnectionsN1.size(); i++){
			if (treeOrBlobs [setsRepartition [additionalConnectionsN1.get(i)] - 1])
			    expandConnectionInImage (circlesMCMC [additionalConnectionsN1.get(i)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(i)].getCenterY(), circlesMCMC [additionalConnectionsN1.get(i)].getRadius(), circlesMCMC [additionalConnectionsN2.get(i)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(i)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(i)].getRadius(), finalImage);
		}
		
		//Writing finalImage
		try {
			ImageIO.write(finalImage, "png", new File("C://Users/Josh/Desktop/karim/finalImage.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Computing general statistics
		double treeTree = 0;
		double treeBackground = 0;
		double backgroundTree = 0;
		double backgroundBackground = 0;
		for(int i = 0; i < imageGroundTruth.getWidth(); i++){
			for(int j = 0; j < imageGroundTruth.getHeight(); j++){
				if ((imageGroundTruth.getRGB(i, j) & 0xff) == 0) {
					if ((finalImage.getRGB(i, j) & 0xff) == 0)
						backgroundBackground++;
					else
						backgroundTree++;
				}
				else {
					if ((finalImage.getRGB(i, j) & 0xff) == 0)
						treeBackground++;
					else
						treeTree++;
				}
			}
		}
		double accuracy = ((treeTree + backgroundBackground) / (finalImage.getWidth() * finalImage.getHeight())) * 100;
		double pO = (treeTree + backgroundBackground) / (treeTree + treeBackground + backgroundTree + backgroundBackground);
		double pE = (((treeTree + treeBackground) * (treeTree + backgroundTree)) / ((treeTree + treeBackground + backgroundTree + backgroundBackground) * (treeTree + treeBackground + backgroundTree + backgroundBackground))) + (((backgroundTree + backgroundBackground) * (treeBackground + backgroundBackground)) / ((treeTree + treeBackground + backgroundTree + backgroundBackground) * (treeTree + treeBackground + backgroundTree + backgroundBackground)));
		double kappa = (pO - pE) / (1 - pE);
		double precision = treeTree / (treeTree + backgroundTree);
		double recall = treeTree / (treeTree + treeBackground);
		double f1 = 2 * ((precision * recall) / (precision + recall));
		System.out.println("treeTree account: " + ((int) treeTree));
		System.out.println("treeBackground account: " + ((int) treeBackground));
		System.out.println("backgroundTree account: " + ((int) backgroundTree));
		System.out.println("backgroundBackground account: " + ((int) backgroundBackground));
		System.out.println("Pixel-wise accuracy: " + accuracy + " %");
		System.out.println("Observed proportionate agreement: pO: " + pO);
		System.out.println("Overall random agreement: pE: " + pE);
		System.out.println("Cohen's kappa: " + kappa);
		System.out.println("Precision: " + precision);
		System.out.println("Recall: " + recall);
		System.out.println("F1 score: " + f1);
		
		//Writing general statistics
		try(FileWriter fw = new FileWriter("C://Users/Josh/Desktop/karim/generalStatistics.txt", false);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter out = new PrintWriter(bw))
		{
			     out.println("treeTree account: " + ((int) treeTree));
			     out.println("treeBackground account: " + ((int) treeBackground));
			     out.println("backgroundTree account: " + ((int) backgroundTree));
			     out.println("backgroundBackground account: " + ((int) backgroundBackground));
			     out.println("Pixel-wise accuracy: " + accuracy + " %");
			     out.println("Observed proportionate agreement: pO: " + pO);
			     out.println("Overall random agreement: pE: " + pE);
			     out.println("Cohen's kappa: " + kappa);
			     out.println("Precision: " + precision);
			     out.println("Recall: " + recall);
			     out.println("F1 score: " + f1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

	public static double distancePointToPoint (double x1, double y1, double x2, double y2) {
		
		//Returning result
		return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
		
	}
	
    public static double distancePointToLine (double x, double y, double x1, double y1, double x2, double y2) {
    	
    	//Initialization
		double A = x - x1;
		double B = y - y1;
		double C = x2 - x1;
		double D = y2 - y1;
		double E = -D;
		double F = C;

		//Processing
		double dot = A * E + B * F;
		double len_sq = E * E + F * F;

		//Returning result
	    return Math.abs(dot) / Math.sqrt(len_sq);
	    
	}
    
    public static double distancePointToCurve (double x, double y, double a, double b, double c, double x1, double x2) {
    	
    	//Initialization
    	double distance = Double.POSITIVE_INFINITY;
    	int numberPoints = (int) (100 * (x2-x1));
    	double ratio, xCurve, yCurve, distanceCurrent;
    	
    	//Processing
    	for(int k = 1; k <= numberPoints; k++){
    		ratio = (double) k / numberPoints;
    		xCurve = (ratio * x1) + ((1 - ratio) * x2);
    		yCurve = (a * Math.pow(xCurve, 2)) + (b * xCurve) + c;
    		distanceCurrent = distancePointToPoint (x, y, xCurve, yCurve);
    		if (distanceCurrent < distance)
    			distance = distanceCurrent;
    	}
    	
    	//Returning result
    	return distance;
    	
    }
    
    public static boolean intersectionLineToLine (double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
    	
    	//Processing
    	Line2D line1 = new Line2D.Double(x1, y1, x2, y2);
		Line2D line2 = new Line2D.Double(x3, y3, x4, y4);
		
		//Returning result
    	return line2.intersectsLine(line1);
    	
    }
    
    public static double angleLineToLine (double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
		
    	//Initialization
    	double angle;
    	
		//Processing
    	double angle1 = Math.atan2(y1 - y2,
                                   x1 - x2);
        double angle2 = Math.atan2(y3 - y4,
                                   x3 - x4);
        angle = Math.abs(angle1 - angle2);
        if (angle > 3.141592653589793) {
            angle2 = Math.atan2(y3 - y4,
                                x3 + x4);
            angle = Math.abs(angle1 - angle2);
        }
	    
	    //Returning result
	    return angle;
	    
	}
    
    public static boolean intersectionCircleToSegment (int a, int b, int c, Circle [] circlesMCMC, double [] [] distanceMatrix) {
    	
    	//Initialization
    	boolean intersects = false;
    	
    	//Processing
    	if (distancePointToLine (circlesMCMC [a].getCenterX(), circlesMCMC [a].getCenterY(), circlesMCMC [b].getCenterX(), circlesMCMC [b].getCenterY(), circlesMCMC [c].getCenterX(), circlesMCMC [c].getCenterY()) <= circlesMCMC [a].getRadius()) {
    		if ((distanceMatrix [a] [b] <= distanceMatrix [b] [c]) && (distanceMatrix [a] [c] <= distanceMatrix [b] [c]))
    			intersects = true;
    	}
    	
    	//Returning result
    	return intersects;
    	
    }
    
    public static boolean intersectionCircleToProhibitionMape (double x, double y, double r, boolean [][] matrixMCMCFullProhibited) {
    	
    	//Initialization
    	boolean intersects = false;
    	int [] boundaries = searchBoundaries (x, y, r, matrixMCMCFullProhibited.length, matrixMCMCFullProhibited [0].length);
    	
    	//Processing
    	int i = boundaries [2];
    	while ((i <= boundaries [3]) && (!intersects)) {
    		int j = boundaries [0];
			while ((j <= boundaries [1]) && (!intersects)) {
				if (matrixMCMCFullProhibited [i] [j]) {
				   if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r) {
					   intersects = true;
				   }
				}
				j++;
			}
			i++;
    	}
    	
    	//Returning result
    	return intersects;
    	
    }
 
    public static int [] searchBoundaries (double x, double y, double r, int width, int height) {
    	
    	//Initialization
    	int result [] = new int [4];
    	result [0] = (int) (y - r);
    	result [1] = (int) (y + r);
    	result [2] = (int) (x - r);
    	result [3] = (int) (x + r);
    	
    	//Processing
    	result [0]--; 
    	result [1]++;
    	result [2]--;
    	result [3]++;
    	if (result [0] < 0)
    		result [0] = 0;
    	if (result [1] > height - 1)
    		result [1] = height - 1;
    	if (result [2] < 0)
    		result [2] = 0;
    	if (result [3] > width - 1)
    		result [3] = width - 1;
    	
    	//Returning result
    	return result;
    	
    }
    
    public static boolean pixelBelongingToCirclesWithException (double x, double y, int numberCircles, Circle [] circlesMCMC, int indexExclusion, boolean heat) {
    	
    	//Initialization
    	boolean belongs = false;
    	int i = 0;
    	
    	//Processing
    	while ((i < numberCircles) && (!belongs)) {
    		if (i != indexExclusion) {
    			if (!heat) {
    				if (distancePointToPoint (x, y, circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY()) <= circlesMCMC [i].getRadius())
        				belongs = true;
    			}
    			else {
    				if (distancePointToPoint (x, y, circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY()) <= (circlesMCMC [i].getRadius() + 50))
        				belongs = true;
    			}
    		}
    		i++;
    	}
    	
    	//Returning result
    	return belongs;
    	
    }

    public static void matrixMCMCFullAdd (double x, double y, double r, boolean [][] matrixMCMCFull, boolean heat) {
    	
    	//Initialization
    	int [] boundaries;
    	if (!heat)
    		boundaries = searchBoundaries (x, y, r, matrixMCMCFull.length, matrixMCMCFull [0].length);
    	else
    		boundaries = searchBoundaries (x, y, r + 50, matrixMCMCFull.length, matrixMCMCFull [0].length);
    		   	
    	//Processing
    	for(int i = boundaries [2]; i <= boundaries [3]; i++){
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (!heat) {
					if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r)
						matrixMCMCFull [i][j] = true;
				}
				else {
					if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= (r + 50))
						matrixMCMCFull [i][j] = true;
				}
			}
    	}
    	
    }

    public static void matrixMCMCFullDelete (double x, double y, double r, int numberCircles, Circle [] circlesMCMC, int indexExclusion, boolean [][] matrixMCMCFull, boolean heat) {
    	
    	//Initialization
    	int [] boundaries;
    	if (!heat)
    		boundaries = searchBoundaries (x, y, r, matrixMCMCFull.length, matrixMCMCFull [0].length);
    	else
    		boundaries = searchBoundaries (x, y, r + 50, matrixMCMCFull.length, matrixMCMCFull [0].length);
    	
    	//Processing
    	for(int i = boundaries [2]; i <= boundaries [3]; i++){
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (!heat) {
					if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r) {
						if (!pixelBelongingToCirclesWithException (i + 0.5, j + 0.5, numberCircles, circlesMCMC, indexExclusion, heat))
						    matrixMCMCFull [i][j] = false;
					}
				}
				else {
					if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= (r + 50)) {
						if (!pixelBelongingToCirclesWithException (i + 0.5, j + 0.5, numberCircles, circlesMCMC, indexExclusion, heat))
						    matrixMCMCFull [i][j] = false;
					}
				}
			}
    	}
    	
    }
    
    public static void matrixMCMCFullDelete (double x, double y, double r, int numberCircles, boolean [][] matrixMCMCFull) {
    	
    	//Initialization
    	int [] boundaries;
    	boundaries = searchBoundaries (x, y, r, matrixMCMCFull.length, matrixMCMCFull [0].length);
    	
    	//Processing
    	for(int i = boundaries [2]; i <= boundaries [3]; i++){
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r)
				    matrixMCMCFull [i][j] = false;
			}
    	}
    	
    }
    
    public static void matrixMCMCContoursAddDelete (double x, double y, double r, boolean [][] matrixMCMCContours, boolean addDelete) {
    	
    	//Initialization
    	int [] boundaries = searchBoundaries (x, y, r, matrixMCMCContours.length, matrixMCMCContours [0].length);
    	boolean k;
    	
    	//Processing
    	for(int i = boundaries [2]; i <= boundaries [3]; i++){
    		k = false;
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (!k) {
					if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r) {
						if (addDelete)
						    matrixMCMCContours [i][j] = true;
						else
							matrixMCMCContours [i][j] = false;
						k = true;
					}
				}
				else {
					if ((distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r) && (distancePointToPoint (x, y, i + 0.5, j + 1.5) > r)) {
						if (addDelete)
						    matrixMCMCContours [i][j] = true;
						else
							matrixMCMCContours [i][j] = false;
					}
				}
			}
    	}
    	for(int i = boundaries [0]; i <= boundaries [1]; i++){
    		k = false;
			for(int j = boundaries [2]; j <= boundaries [3]; j++){
				if (!k) {
					if (distancePointToPoint (x, y, j + 0.5, i + 0.5) <= r) {
						if (addDelete)
						    matrixMCMCContours [j][i] = true;
						else
							matrixMCMCContours [j][i] = false;
						k = true;
					}
				}
				else {
					if ((distancePointToPoint (x, y, j + 0.5, i + 0.5) <= r) && (distancePointToPoint (x, y, j + 1.5, i + 0.5) > r)) {
						if (addDelete)
						    matrixMCMCContours [j][i] = true;
						else
							matrixMCMCContours [j][i] = false;
					}
				}
			}
    	}	
    	
    }
    
    public static boolean circleOverlappingCircle (double thresholdOverlap, Circle [] circlesMCMC, int a, int b, int width, int height) {
    	
    	//Initialization
    	boolean overlap = false;
    	int taken = 0, free = 0;
    	int [] boundaries;
    	
    	//Processing
    	boundaries = searchBoundaries (circlesMCMC [a].getCenterX(), circlesMCMC [a].getCenterY(), circlesMCMC [a].getRadius(), width, height);
		for(int i = boundaries [2]; i <= boundaries [3]; i++){
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (distancePointToPoint (circlesMCMC [a].getCenterX(), circlesMCMC [a].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [a].getRadius()) {
					if (distancePointToPoint (circlesMCMC [b].getCenterX(), circlesMCMC [b].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [b].getRadius())
						taken++;
					else
						free++;
				    
				}
			}
    	}
		if (((double) taken / (free + taken)) > thresholdOverlap)
			overlap = true;
    	
    	//Returning result
    	return overlap;
    	
    }
 
    public static double overlapEnergy (double thresholdOverlap, int numberCircles, Circle [] circlesMCMC, boolean [] statesMCMC, double [] [] distanceMatrix, boolean phase, int indexCurrentCircle, int width, int height) {
    	
    	//Initialization
    	double energy = 0;
    	int k = indexCurrentCircle - 1;
    	
    	//Processing
    	if (!phase) {
    		while ((k >= 0) && (energy == 0)) {
        		if (distanceMatrix [indexCurrentCircle][k] < (circlesMCMC [indexCurrentCircle].getRadius() + circlesMCMC [k].getRadius())) {
        			if (circleOverlappingCircle (thresholdOverlap, circlesMCMC, indexCurrentCircle, k, width, height) || circleOverlappingCircle (thresholdOverlap, circlesMCMC, k, indexCurrentCircle, width, height))
        				energy = Double.POSITIVE_INFINITY;
        		}
        		k--;
    		}
    		if (energy == 0) {
    			for(int i = indexCurrentCircle+1; i < numberCircles; i++){
    				if (statesMCMC [i]) {
    					if (distancePointToPoint (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY()) < (circlesMCMC [indexCurrentCircle].getRadius() + circlesMCMC [i].getRadius())) {
    						if (circleOverlappingCircle (thresholdOverlap, circlesMCMC, indexCurrentCircle, i, width, height) || circleOverlappingCircle (thresholdOverlap, circlesMCMC, i, indexCurrentCircle, width, height))
    	        				energy = Double.POSITIVE_INFINITY;
    					}
    				}
    			}
    		}
    	}
    	else {
    		k = 0;
    		while ((k != numberCircles) && (energy == 0)) {
    			if (k != indexCurrentCircle) {
    				if (distanceMatrix [indexCurrentCircle][k] < (circlesMCMC [indexCurrentCircle].getRadius() + circlesMCMC [k].getRadius())) {
            			if (circleOverlappingCircle (thresholdOverlap, circlesMCMC, indexCurrentCircle, k, width, height) || circleOverlappingCircle (thresholdOverlap, circlesMCMC, k, indexCurrentCircle, width, height))
            				energy = Double.POSITIVE_INFINITY;
            		}
    			}
    			k++;
    		}
    	}
    	
    	//Returning result
    	return energy;
    	
    }
    
    public static double colorProbability (double x, double y, double r, double [] [] matrixColorProbability) {
    	
    	//Initialization
    	int [] boundaries = searchBoundaries (x, y, r, matrixColorProbability.length, matrixColorProbability [0].length);
    	double probability = 0;
    	int k = 0;
    	
        //Processing
    	for(int i = boundaries [2]; i <= boundaries [3]; i++){
			for(int j = boundaries [0]; j <= boundaries [1]; j++){
				if (distancePointToPoint (x, y, i + 0.5, j + 0.5) <= r) {
					probability += matrixColorProbability [i][j];
				    k++;
				}
			}
    	}
    	probability = probability / k;
    	if (probability < 0.01)
    		probability = 0.01;
    		
    	//Returning result
    	return probability;
    	
    }
    
    public static double probabilityToEnergy (double probability) {
    	
       	//Returning result
    	return (-1) * Math.log(probability);
    	
    }
    
    public static double sizeEnergy (double r, int rmax, NormalDistribution sizeDistribution) {
    	
    	//Initialization
    	double probability;
    	
    	//Processing
    	probability = sizeDistribution.density(r) / sizeDistribution.density(rmax);
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return (-1) * Math.log(probability);
    	
    }
    
    public static double connectionProbability (double x1, double y1, double r1, double x2, double y2, double r2, double [] [] matrixColorProbability) {
    	
    	//Initialization
    	double probability = 0;
    	int k = 0;
    	double dx = x1 - x2;
    	double dy = y1 - y2;
    	double distance = Math.sqrt(dx*dx + dy*dy);
    	dx /= distance;
    	dy /= distance;
    	double x3 = x1 + (r1)*dy;
    	double y3 = y1 - (r1)*dx;
    	double x4 = x1 - (r1)*dy;
    	double y4 = y1 + (r1)*dx;
    	double x5 = x2 + (r2)*dy;
    	double y5 = y2 - (r2)*dx;
    	double x6 = x2 - (r2)*dy;
    	double y6 = y2 + (r2)*dx;
    	int ptN1, ptN2;
    	if (r1 >= r2)
    	    ptN1 = (int) (2 * (2 * r1));
    	else
    		ptN1 = (int) (2 * (2 * r2));
    	double x11, y11, x12, y12, ratio1, distance2, ratio2;
    	int x2D, y2D;
    	
    	//Processing
    	for(int i = 1; i <= ptN1; i++){
    		ratio1 = (double) i / ptN1;
    		x11 = (ratio1 * x4) + ((1 - ratio1) * x3);
        	y11 = (ratio1 * y4) + ((1 - ratio1) * y3);
        	x12 = (ratio1 * x6) + ((1 - ratio1) * x5);
        	y12 = (ratio1 * y6) + ((1 - ratio1) * y5);
        	distance2 = distancePointToPoint (x11, y11, x12, y12);
        	ptN2 = (int) (2 * distance2);
        	for(int j = 1; j <= ptN2; j++){
        		ratio2 = (double) j / ptN2;
        		x2D = (int) ((ratio2 * x12) + ((1 - ratio2) * x11));
            	y2D = (int) ((ratio2 * y12) + ((1 - ratio2) * y11));
            	if ((x2D >= 0) && (y2D >= 0) && (x2D < matrixColorProbability.length) && (y2D < matrixColorProbability [0].length)) {
            		probability += matrixColorProbability [x2D] [y2D];
            		k++;
            	}   
        	}
    	}
    	probability = probability / k;
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return probability;
    	
    }
    
    public static double distanceToConnectionEnergy (int a, int b, double r1, double r2, double [] [] distanceMatrix, NormalDistribution distanceToConnectionDistribution) {
    	
    	//Initialization
    	double probability = 1 - (distanceToConnectionDistribution.density(distanceMatrix [a][b] / (r1 + r2)) / distanceToConnectionDistribution.density(0)); 
    	
    	//Processing
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return (-1) * Math.log(probability);
    	
    }
    
    public static double distanceToNearbyEnergy (int a, int b, double r1, double r2, double [] [] distanceMatrix, NormalDistribution distanceToNearbyDistribution) {
    	
    	//Initialization
    	double probability = 1 - (distanceToNearbyDistribution.density(distanceMatrix [a][b] / (r1 + r2)) / distanceToNearbyDistribution.density(0));
    	
    	//Processing
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return (-1) * Math.log(probability);
    	
    }
    
    public static double radiusSimilarityEnergy (double r1, double r2, NormalDistribution radiusSimilarityDistribution) {
    	
    	//Initialization
    	double probability = radiusSimilarityDistribution.density(Math.abs(r1 - r2) / ((r1 + r2) / 2)) / radiusSimilarityDistribution.density(0);
    	
    	//Processing
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return (-1) * Math.log(probability);
    			
    }
	
    public static double verticalAngleEnergy (double x1, double y1, double x2, double y2, NormalDistribution verticalAngleDistribution) {
    	
    	//Initialization
    	double angle;
    	double probability;
    	
    	//Processing
    	if (y1 < y2)
    	    angle = angleLineToLine (x2, y2, x1, y1, x1, y1, x1, y1 - 50);
    	else
    	    angle = angleLineToLine (x1, y1, x2, y2, x2, y2, x2, y2 - 50);
    	probability = verticalAngleDistribution.density(angle) / verticalAngleDistribution.density(0);
    	if (probability < 0.01)
    		probability = 0.01;
    	
    	//Returning result
    	return (-1) * Math.log(probability);
    			
    }
    
    public static int [] circlePossibleConnections (int numberCircles, Circle [] circlesMCMC, int indexCurrentCircle, double [] [] distanceMatrix) {
    	
    	//Initialization
    	int [] connections = new int [4];
    	connections [0] = -1;
    	connections [1] = -1;
    	connections [2] = -1;
    	connections [3] = -1;
    	double currentDistance;
    	double shortestDistance;
    	double minimalDistance;
    	boolean intersectsCircle;
        int connectionToConsider = -1;
    	int k;
    	
    	//Processing
    	minimalDistance = 0;
    	for(int i = 0; i < 4; i++) {
        	shortestDistance = Double.POSITIVE_INFINITY;
        	for(int j = 0; j < numberCircles; j++){
        		currentDistance = distanceMatrix [indexCurrentCircle][j];
        		if ((currentDistance < shortestDistance) && (currentDistance > minimalDistance)) {
        			shortestDistance = currentDistance;
        			connectionToConsider = j;
        		}
        	}
        	connections [i] = connectionToConsider;
        	minimalDistance = shortestDistance;
    	}
    	for(int i = 0; i < 4; i++) {
    		intersectsCircle = false;
    	    k = 0;
			while ((k < numberCircles) && (!intersectsCircle)){
				if ((k != indexCurrentCircle) && (k != connections [i])) {
					if (intersectionCircleToSegment (k, indexCurrentCircle, connections [i], circlesMCMC, distanceMatrix))
						intersectsCircle = true;
				}
				k++;
			}
			if (intersectsCircle)
				connections [i] = -1;
    	}
    	
    	//Returning result
    	return connections;
    	
    }
    
    public static double circleEnergy (int numberCircles, Circle [] circlesMCMC, boolean [] statesMCMC, int [] connectionsMCMCKept, int [] connectionsMCMCProposed, int [] nearbyMCMCKept, int [] nearbyMCMCProposed, List<Integer> connectionsMCMCChangeList, List<Integer> nearbyMCMCChangeList, double [] [] distanceMatrix, double [][] dataBuffer, double [] [] matrixColorProbability, boolean phase, int indexCurrentCircle, int indexCircleDelete, double probabilityCoefficient, double sizeCoefficient, double connectionCoefficient, double distanceToConnectionCoefficient, double distanceToNearbyCoefficient, double radiusSimilarityCoefficient, double verticalAngleCoefficient, int rmax, NormalDistribution sizeDistribution, NormalDistribution distanceToConnectionDistribution, NormalDistribution distanceToNearbyDistribution, NormalDistribution radiusSimilarityDistribution, NormalDistribution verticalAngleDistribution) {
    	
    	//Initialization
    	double energy = 0;
    	int [] possibleConnections;
    	int k = 0;
    	int bestConnection = -1;
    	double currentConnectionEnergy;
    	double bestConnectionEnergy = Double.POSITIVE_INFINITY;
    	double currentDistance = 0;
    	double shortestDistance = Double.POSITIVE_INFINITY;
    	int shortestDistanceIndex = -1;
    	
    	//Processing
    	if (!phase) {
            if (!statesMCMC [indexCurrentCircle]) {
            	dataBuffer [indexCurrentCircle][0] = probabilityCoefficient * probabilityToEnergy (colorProbability (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [indexCurrentCircle].getRadius(), matrixColorProbability));
        		dataBuffer [indexCurrentCircle][1] = sizeCoefficient * sizeEnergy (circlesMCMC [indexCurrentCircle].getRadius(), rmax, sizeDistribution);
        		possibleConnections = circlePossibleConnections (numberCircles, circlesMCMC, indexCurrentCircle, distanceMatrix);
            	while (k < 4) {
            		if (possibleConnections [k] != -1) {
            			currentConnectionEnergy = probabilityToEnergy (connectionProbability (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [possibleConnections [k]].getCenterX(), circlesMCMC [possibleConnections [k]].getCenterY(), circlesMCMC [possibleConnections [k]].getRadius(), matrixColorProbability));
            			if (currentConnectionEnergy < bestConnectionEnergy) {
            				bestConnectionEnergy = currentConnectionEnergy;
            				bestConnection = possibleConnections [k];
            			}
            		}
            		k++;
            	}
            	dataBuffer [indexCurrentCircle][2] = connectionCoefficient * bestConnectionEnergy;
            	dataBuffer [indexCurrentCircle][4] = distanceToConnectionCoefficient * distanceToConnectionEnergy (indexCurrentCircle, bestConnection, circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [bestConnection].getRadius(), distanceMatrix, distanceToConnectionDistribution);
            	dataBuffer [indexCurrentCircle][6] = distanceToNearbyCoefficient * distanceToNearbyEnergy (indexCurrentCircle, possibleConnections [0], circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [possibleConnections [0]].getRadius(), distanceMatrix, distanceToNearbyDistribution);
            	dataBuffer [indexCurrentCircle][8] = radiusSimilarityCoefficient * radiusSimilarityEnergy (circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [bestConnection].getRadius(), radiusSimilarityDistribution);
            	dataBuffer [indexCurrentCircle][10] = verticalAngleCoefficient * verticalAngleEnergy (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [bestConnection].getCenterX(), circlesMCMC [bestConnection].getCenterY(), verticalAngleDistribution);
            	connectionsMCMCKept [indexCurrentCircle] = bestConnection;
            	nearbyMCMCKept [indexCurrentCircle] = possibleConnections [0];
    		}
            else {
            	for(int i = 0; i < numberCircles; i++){
            		currentDistance = distanceMatrix [indexCurrentCircle][i];
            		if ((currentDistance < shortestDistance) && (indexCurrentCircle != i)) {
            			shortestDistance = currentDistance;
            			shortestDistanceIndex = i;
            		}
            	}
            	dataBuffer [indexCurrentCircle][6] = distanceToNearbyCoefficient * distanceToNearbyEnergy (indexCurrentCircle, shortestDistanceIndex, circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [shortestDistanceIndex].getRadius(), distanceMatrix, distanceToNearbyDistribution);
            	nearbyMCMCKept [indexCurrentCircle] = shortestDistanceIndex;
            }
            energy = dataBuffer [indexCurrentCircle][0] + dataBuffer [indexCurrentCircle][1] + dataBuffer [indexCurrentCircle][2] + dataBuffer [indexCurrentCircle][4] + dataBuffer [indexCurrentCircle][6] + dataBuffer [indexCurrentCircle][8] + dataBuffer [indexCurrentCircle][10];
    	}
    	else {
    		if (!statesMCMC [indexCurrentCircle]) {
    			if (indexCurrentCircle == indexCircleDelete) {
        			dataBuffer [9998][0] = probabilityCoefficient * probabilityToEnergy (colorProbability (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [indexCurrentCircle].getRadius(), matrixColorProbability));
        			dataBuffer [9998][1] = sizeCoefficient * sizeEnergy (circlesMCMC [indexCurrentCircle].getRadius(), rmax, sizeDistribution);
        			energy = dataBuffer [9998][0] + dataBuffer [9998][1];
        		}
        		else
        			energy = dataBuffer [indexCurrentCircle][0] + dataBuffer [indexCurrentCircle][1];
        		possibleConnections = circlePossibleConnections (numberCircles, circlesMCMC, indexCurrentCircle, distanceMatrix);
            	while (k < 4) {
            		if (possibleConnections [k] != -1) {
            			currentConnectionEnergy =  probabilityToEnergy (connectionProbability (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [possibleConnections [k]].getCenterX(), circlesMCMC [possibleConnections [k]].getCenterY(), circlesMCMC [possibleConnections [k]].getRadius(), matrixColorProbability));
            			if (currentConnectionEnergy < bestConnectionEnergy) {
            				bestConnectionEnergy = currentConnectionEnergy;
            				bestConnection = possibleConnections [k];
            			}
            		}
            		k++;
            	}
            	if ((indexCurrentCircle == indexCircleDelete) || (connectionsMCMCKept [indexCurrentCircle] == indexCircleDelete) || (connectionsMCMCKept [indexCurrentCircle] != bestConnection)) {
            		dataBuffer [indexCurrentCircle][3] = connectionCoefficient * bestConnectionEnergy;
            		dataBuffer [indexCurrentCircle][5] = distanceToConnectionCoefficient * distanceToConnectionEnergy (indexCurrentCircle, bestConnection, circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [bestConnection].getRadius(), distanceMatrix, distanceToConnectionDistribution);
            		dataBuffer [indexCurrentCircle][9] = radiusSimilarityCoefficient * radiusSimilarityEnergy (circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [bestConnection].getRadius(), radiusSimilarityDistribution);
                	dataBuffer [indexCurrentCircle][11] = verticalAngleCoefficient * verticalAngleEnergy (circlesMCMC [indexCurrentCircle].getCenterX(), circlesMCMC [indexCurrentCircle].getCenterY(), circlesMCMC [bestConnection].getCenterX(), circlesMCMC [bestConnection].getCenterY(), verticalAngleDistribution);
            	    energy += dataBuffer [indexCurrentCircle][3] + dataBuffer [indexCurrentCircle][5] + dataBuffer [indexCurrentCircle][9] + dataBuffer [indexCurrentCircle][11];
            	    connectionsMCMCChangeList.add(indexCurrentCircle);
            	}
            	else {
            		energy += dataBuffer [indexCurrentCircle][2] + dataBuffer [indexCurrentCircle][4] + dataBuffer [indexCurrentCircle][8] + dataBuffer [indexCurrentCircle][10];
            	}
            	if ((indexCurrentCircle == indexCircleDelete) || (nearbyMCMCKept [indexCurrentCircle] == indexCircleDelete) || (nearbyMCMCKept [indexCurrentCircle] != possibleConnections [0])) {
            		dataBuffer [indexCurrentCircle][7] = distanceToNearbyCoefficient * distanceToNearbyEnergy (indexCurrentCircle, possibleConnections [0], circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [possibleConnections [0]].getRadius(), distanceMatrix, distanceToNearbyDistribution);
            		energy += dataBuffer [indexCurrentCircle][7];
            		nearbyMCMCChangeList.add(indexCurrentCircle);
            	}
            	else {
            		energy += dataBuffer [indexCurrentCircle][6];
            	}
            	connectionsMCMCProposed [indexCurrentCircle] = bestConnection;
            	nearbyMCMCProposed [indexCurrentCircle] = possibleConnections [0];
    		}
    		else {
    			energy += dataBuffer [indexCurrentCircle][0] + dataBuffer [indexCurrentCircle][1] + dataBuffer [indexCurrentCircle][2] + dataBuffer [indexCurrentCircle][4] + dataBuffer [indexCurrentCircle][8] + dataBuffer [indexCurrentCircle][10];
    			for(int i = 0; i < numberCircles; i++){
            		currentDistance = distanceMatrix [indexCurrentCircle][i];
            		if ((currentDistance < shortestDistance) && (indexCurrentCircle != i)) {
            			shortestDistance = currentDistance;
            			shortestDistanceIndex = i;
            		}
            	}
    			if ((shortestDistanceIndex == nearbyMCMCKept [indexCurrentCircle]) && (shortestDistanceIndex != indexCircleDelete)) {
    				energy += dataBuffer [indexCurrentCircle][6];
    			}
    			else {
    				dataBuffer [indexCurrentCircle][7] = distanceToNearbyCoefficient * distanceToNearbyEnergy (indexCurrentCircle, shortestDistanceIndex, circlesMCMC [indexCurrentCircle].getRadius(), circlesMCMC [shortestDistanceIndex].getRadius(), distanceMatrix, distanceToNearbyDistribution);
    				energy += dataBuffer [indexCurrentCircle][7];
    				nearbyMCMCChangeList.add(indexCurrentCircle);
    			}
            	connectionsMCMCProposed [indexCurrentCircle] = connectionsMCMCKept [indexCurrentCircle];
            	nearbyMCMCProposed [indexCurrentCircle] = shortestDistanceIndex;
    		}
    	}
    	
    	//Returning result
    	return energy;
    	
    }

    public static void birth (Circle [] circlesMCMC, int index, UniformIntegerDistribution xDistribution, UniformIntegerDistribution yDistribution, UniformIntegerDistribution rDistribution) {
    	
    	//Initialization
    	circlesMCMC [index] = new Circle (rDistribution.sample());
    	
    	//Processing
    	circlesMCMC [index].setCenterX(xDistribution.sample());
    	circlesMCMC [index].setCenterY(yDistribution.sample());
    	
    }

    public static void translation (Circle [] circlesMCMC, int index, UniformIntegerDistribution xTRDistribution, UniformIntegerDistribution yTRDistribution, int width, int height) {
    	
    	//Initialization
    	double pastX = circlesMCMC [index].getCenterX();
    	double pastY = circlesMCMC [index].getCenterY();
    	boolean pass = false;
    	
    	//Processing
    	while (!pass) {
    		circlesMCMC [index].setCenterX(pastX + xTRDistribution.sample());
    		circlesMCMC [index].setCenterY(pastY + yTRDistribution.sample());
    		if ((circlesMCMC [index].getCenterX() >= 0) && (circlesMCMC [index].getCenterY() >= 0) && (circlesMCMC [index].getCenterX() < width) && (circlesMCMC [index].getCenterY() < height))
    			pass = true;
    	}
    	
    }

    public static void dilatation (Circle [] circlesMCMC, int index, UniformIntegerDistribution rDIDistribution, int rmin, int rmax) {
    	
        //Initialization
    	double pastR = circlesMCMC [index].getRadius();
    	boolean pass = false;
    	
    	//Processing
    	while (!pass) {
    		circlesMCMC [index].setRadius(pastR + rDIDistribution.sample());
    		if ((circlesMCMC [index].getRadius() >= rmin) && (circlesMCMC [index].getRadius() <= rmax))
    			pass = true;
    	}
    	
    }

    public static void smartShaking (Circle [] circlesMCMC, int index, double [] [] matrixColorProbability, int rmin, int rmax, UniformIntegerDistribution xTRsHDistribution, UniformIntegerDistribution yTRsHDistribution, UniformIntegerDistribution rDIsHDistribution) {
    	
    	//Initialization
    	double colorProbabilityKept = colorProbability (circlesMCMC [index].getCenterX(), circlesMCMC [index].getCenterY(), circlesMCMC [index].getRadius(), matrixColorProbability);
    	double colorProbabilityProposed = 0;
    	double xProposed = 0;
    	double yProposed = 0;
    	double rProposed = 0;
    	boolean pass;
    	int k = 0;
    	
    	//Processing
    	if ((colorProbabilityKept > 0) && !((colorProbabilityKept == 1) && (circlesMCMC [index].getRadius() == rmax))) {
    		while (k < 200) {
        		pass = false;
        		while (!pass) {
        			xProposed = circlesMCMC [index].getCenterX() + xTRsHDistribution.sample();
        			yProposed = circlesMCMC [index].getCenterY() + yTRsHDistribution.sample();
        			rProposed = circlesMCMC [index].getRadius() + rDIsHDistribution.sample();
        			if ((xProposed >= 0) && (yProposed >= 0) && (xProposed < matrixColorProbability.length) && (yProposed < matrixColorProbability [0].length)) {
        				if (colorProbabilityKept == 1) {
        					if ((rProposed >= circlesMCMC [index].getRadius()) && (rProposed >= rmin) && (rProposed <= rmax))
        				        pass = true;
        				}
        				else {
        					if ((rProposed >= rmin) && (rProposed <= rmax))
        						pass = true;
        				}
        			}
        		}
        		colorProbabilityProposed = colorProbability (xProposed, yProposed, rProposed, matrixColorProbability);
        		if ((colorProbabilityKept != 1) && (colorProbabilityProposed > colorProbabilityKept)) {
        			circlesMCMC [index].setCenterX(xProposed);
        			circlesMCMC [index].setCenterY(yProposed);
        			circlesMCMC [index].setRadius(rProposed);
        			colorProbabilityKept = colorProbabilityProposed;
        			k = 0;
        		}
        		else if ((colorProbabilityKept == 1) && (colorProbabilityProposed == 1)) {
        			if (rProposed > circlesMCMC [index].getRadius()) {
        				circlesMCMC [index].setCenterX(xProposed);
            			circlesMCMC [index].setCenterY(yProposed);
            			circlesMCMC [index].setRadius(rProposed);
            			k = 0;
        			}
        			else
        				k++;
        		}
                else
                	k++;	
        	}
    	}
    		
    }
    
    public static void intoSets (int numberCircles, int [] connectionsMCMC, int [] setsRepartition) {
    	
    	//Initialization
    	int nextSetNumber = 1;
    	for(int i = 0; i < numberCircles; i++){
    		if (setsRepartition [i] == 0) {
    			setsRepartition [i] = nextSetNumber;
    			if (setsRepartition [connectionsMCMC [i]] == 0)
    				setsRepartition [connectionsMCMC [i]] = nextSetNumber;
    			nextSetNumber++;
    		}	
    	}
    	
    	//Processing
    	boolean repeat = true;
    	while (repeat) {
    		repeat = false;
	    	for(int i = 0; i < numberCircles; i++){
	    		if (setsRepartition [i] != setsRepartition [connectionsMCMC [i]]) {
	    			mergeSets (setsRepartition [i], setsRepartition [connectionsMCMC [i]], numberCircles, setsRepartition);
	    			repeat = true;
	    		}
	    	}
    	}
    	
    }
    
    public static void mergeSets (int n1, int n2, int numberCircles, int [] setsRepartition) {
    	
    	//Initialization
    	if (n1 > n2) {
    		int temp = n1;
    		n1 = n2;
    		n2 = temp;
    	}
    	    
    	//Processing
    	for(int i = 0; i < numberCircles; i++){
    		if (setsRepartition [i] >= n2) {
    			if (setsRepartition [i] == n2)
    				setsRepartition [i] = n1;
    			else
    				setsRepartition [i]--;
    		}
    	}
    	
    }
    
    public static int [] circleNumberOfConnections (int circleIndex, int numberCircles, int [] connectionsMCMCKept) {
    	
    	//Initialization
    	int [] numberOfConnections = new int [2];
    	numberOfConnections [0] = 1;
    	
    	//Processing
    	for(int i = 0; i < numberCircles; i++){
    		if ((connectionsMCMCKept [i] == circleIndex) && (i != connectionsMCMCKept [circleIndex])) {
    			numberOfConnections [0]++;
    			numberOfConnections [1] = i;
    		}
    	}
    	
    	//Returning result
    	return numberOfConnections;
    	
    }
    
	public static boolean structureOptimizer (int numberCircles, Circle [] circlesMCMC, boolean [] statesMCMC, int [] connectionsMCMCKept, double [] [] distanceMatrix, double [][] dataBuffer, double [] [] matrixColorProbability, double connectionCoefficient, double distanceToConnectionCoefficient, double radiusSimilarityCoefficient, double verticalAngleCoefficient, NormalDistribution distanceToConnectionDistribution, NormalDistribution radiusSimilarityDistribution, NormalDistribution verticalAngleDistribution) {
    	
    	//Initialization
    	boolean optimized = false;
    	boolean [] tobeDeleted = new boolean [numberCircles];
    	double radiusReference, radiusReference2;
    	double radiusRatioThreshold = 0.12;
    	double angleThreshold = 0.06;
    	int [] endPoint1 = new int [2];
    	int [] endPoint2 = new int [2];
    	int k, k2, kp, kp2, kn, kn2;
    	boolean expandEndPoint, lockingIssue, intersectsOtherLockedCircle;
    	
    	//Processing
    	for(int i = 0; i < numberCircles; i++){
    		if (!tobeDeleted [i]) {
    			endPoint1 = circleNumberOfConnections (i, numberCircles, connectionsMCMCKept);
    			if (endPoint1 [0] == 2) {
    				radiusReference = circlesMCMC [i].getRadius();
    				if ((Math.abs(radiusReference - circlesMCMC [connectionsMCMCKept [i]].getRadius()) / radiusReference) <= radiusRatioThreshold) {
    					if ((Math.abs(radiusReference - circlesMCMC [endPoint1 [1]].getRadius()) / radiusReference) <= radiusRatioThreshold) {
    						if (angleLineToLine (circlesMCMC [connectionsMCMCKept [i]].getCenterX(), circlesMCMC [connectionsMCMCKept [i]].getCenterY(), circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [endPoint1 [1]].getCenterX(), circlesMCMC [endPoint1 [1]].getCenterY()) <= angleThreshold) {
    							tobeDeleted [i] = true;
    							k = connectionsMCMCKept [i];
    							kp = i;
    							expandEndPoint = true;
    							do {
    								endPoint2 = circleNumberOfConnections (k, numberCircles, connectionsMCMCKept);
    								if (endPoint2 [0] != 2)
    									expandEndPoint = false;
    								else {
    									if (tobeDeleted [connectionsMCMCKept [k]])
    									   kn = endPoint2 [1];
    									else
    										kn = connectionsMCMCKept [k];
    									if ((Math.abs(radiusReference - circlesMCMC [kn].getRadius()) / radiusReference) <= radiusRatioThreshold) {
				    						if (angleLineToLine (circlesMCMC [kp].getCenterX(), circlesMCMC [kp].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [kn].getCenterX(), circlesMCMC [kn].getCenterY()) <= angleThreshold) {
				    							tobeDeleted [k] = true;
				    							kp = k;
				    						    k = kn;
				    						}
				    						else
				    							expandEndPoint = false;
    									}
    									else
    										expandEndPoint = false;
    								}
    							} while (expandEndPoint);
    							k2 = endPoint1 [1];
    							kp2 = i;
    							expandEndPoint = true;
    							do {
    								endPoint2 = circleNumberOfConnections (k2, numberCircles, connectionsMCMCKept);
    								if (endPoint2 [0] != 2)
    									expandEndPoint = false;
    								else {
    									if (tobeDeleted [connectionsMCMCKept [k2]])
    									   kn2 = endPoint2 [1];
    									else
    										kn2 = connectionsMCMCKept [k2];
    									if ((Math.abs(radiusReference - circlesMCMC [kn2].getRadius()) / radiusReference) <= radiusRatioThreshold) {
				    						if (angleLineToLine (circlesMCMC [kp2].getCenterX(), circlesMCMC [kp2].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [kn2].getCenterX(), circlesMCMC [kn2].getCenterY()) <= angleThreshold) {
				    							tobeDeleted [k2] = true;
				    							kp2 = k2;
				    						    k2 = kn2;
				    						}
				    						else
				    							expandEndPoint = false;
    									}
    									else
    										expandEndPoint = false;
    								}
    							} while (expandEndPoint);
    							if (!((connectionsMCMCKept [k] == k2) && (connectionsMCMCKept [k2] == k))) {
    								statesMCMC [k] = true;
    								statesMCMC [k2] = true;
    								connectionsMCMCKept [k] = k2;
    								connectionsMCMCKept [k2] = k;
    								dataBuffer [k] [2] = dataBuffer [k2] [2] = connectionCoefficient * probabilityToEnergy (connectionProbability (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getRadius(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k2].getRadius(), matrixColorProbability));;
    								dataBuffer [k] [4] = dataBuffer [k2] [4] = distanceToConnectionCoefficient * distanceToConnectionEnergy (k, k2, circlesMCMC [k].getRadius(), circlesMCMC [k2].getRadius(), distanceMatrix, distanceToConnectionDistribution);
    								dataBuffer [k] [8] = dataBuffer [k2] [8] = radiusSimilarityCoefficient * radiusSimilarityEnergy (circlesMCMC [k].getRadius(), circlesMCMC [k2].getRadius(), radiusSimilarityDistribution);
    								dataBuffer [k] [10] = dataBuffer [k2] [10] = verticalAngleCoefficient * verticalAngleEnergy (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), verticalAngleDistribution);
    								optimized = true;
    							}
    						}
    					}
    				}
    			}
    		}
    	}
    	for(int i = 0; i < tobeDeleted.length; i++){
    		if (tobeDeleted [i])
    			statesMCMC [i] = false;
    	}
    	for(int i = 0; i < numberCircles; i++){
    		if ((statesMCMC [i]) && (i < connectionsMCMCKept [i])) {
    			lockingIssue = false;
    			k = 0;
    			while ((k < numberCircles) && (!lockingIssue)) {
    				if ((k != i) && (k != connectionsMCMCKept [i])) {
    					if (((connectionsMCMCKept [k] == i) || (connectionsMCMCKept [k] == connectionsMCMCKept [i])) && (statesMCMC [k] == true))
        					lockingIssue = true;
    				}
    				k++;
    			}
    			if (!lockingIssue) {
    				for(int j = 0; j < numberCircles; j++){
        				if ((statesMCMC [j]) && (j < connectionsMCMCKept [j])) {
        					lockingIssue = false;
        					k = 0;
        	    			while ((k < numberCircles) && (!lockingIssue)) {
        	    				if ((k != j) && (k != connectionsMCMCKept [j])) {
        	    					if (((connectionsMCMCKept [k] == j) || (connectionsMCMCKept [k] == connectionsMCMCKept [j])) && (statesMCMC [k] == true))
        	        					lockingIssue = true;
        	    				}
        	    				k++;
        	    			}
        					if (!lockingIssue) {
        						if (i < j) {
            						k = i;
            						kn = connectionsMCMCKept [i];
            						k2 = j;
            						kn2 = connectionsMCMCKept [j];
            						if (distancePointToPoint (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY()) > distancePointToPoint (circlesMCMC [kn].getCenterX(), circlesMCMC [kn].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY())) {
            							k = connectionsMCMCKept [i];
                						kn = i;
            						}
            						if (distancePointToPoint (circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY()) > distancePointToPoint (circlesMCMC [kn2].getCenterX(), circlesMCMC [kn2].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY())) {
            							k2 = connectionsMCMCKept [j];
                						kn2 = j;
            						}
            						if (angleLineToLine (circlesMCMC [kn].getCenterX(), circlesMCMC [kn].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY()) <= angleThreshold) {
        								if (angleLineToLine (circlesMCMC [kn2].getCenterX(), circlesMCMC [kn2].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY()) <= angleThreshold) {
        									intersectsOtherLockedCircle = false;
        									for(int m = 0; m < numberCircles; m++){
        										if (statesMCMC [m]) {
        											if ((m != k) && (m != k2) && (m != kn) && (m != kn2)) {
        												if (intersectionCircleToSegment (m, k, k2, circlesMCMC, distanceMatrix))
        													intersectsOtherLockedCircle = true;
        											}
        										}
        									}
        									if (!intersectsOtherLockedCircle) {
        								    	radiusReference = (circlesMCMC [k].getRadius() + circlesMCMC [kn].getRadius()) / 2;
        			    						radiusReference2 = (circlesMCMC [k2].getRadius() + circlesMCMC [kn2].getRadius()) / 2;
        			    						if ((Math.abs(radiusReference - radiusReference2) / radiusReference) <= radiusRatioThreshold) {
        			    							if ((Math.abs(radiusReference - radiusReference2) / radiusReference2) <= radiusRatioThreshold) {
        			    								if (connectionProbability (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getRadius(), circlesMCMC [k2].getCenterX(), circlesMCMC [k2].getCenterY(), circlesMCMC [k2].getRadius(), matrixColorProbability) >= 0.999) {
        			    									statesMCMC [k] = false;
            	            								statesMCMC [k2] = false;
            	            								connectionsMCMCKept [kn] = kn2;
            	            								connectionsMCMCKept [kn2] = kn;
            	            								dataBuffer [kn] [2] = dataBuffer [kn2] [2] = connectionCoefficient * probabilityToEnergy (connectionProbability (circlesMCMC [kn].getCenterX(), circlesMCMC [kn].getCenterY(), circlesMCMC [kn].getRadius(), circlesMCMC [kn2].getCenterX(), circlesMCMC [kn2].getCenterY(), circlesMCMC [kn2].getRadius(), matrixColorProbability));;
            	            								dataBuffer [kn] [4] = dataBuffer [kn2] [4] = distanceToConnectionCoefficient * distanceToConnectionEnergy (kn, kn2, circlesMCMC [kn].getRadius(), circlesMCMC [kn2].getRadius(), distanceMatrix, distanceToConnectionDistribution);
            	            								dataBuffer [kn] [8] = dataBuffer [kn2] [8] = radiusSimilarityCoefficient * radiusSimilarityEnergy (circlesMCMC [kn].getRadius(), circlesMCMC [kn2].getRadius(), radiusSimilarityDistribution);
            	            								dataBuffer [kn] [10] = dataBuffer [kn2] [10] = verticalAngleCoefficient * verticalAngleEnergy (circlesMCMC [kn].getCenterX(), circlesMCMC [kn].getCenterY(), circlesMCMC [kn2].getCenterX(), circlesMCMC [kn2].getCenterY(), verticalAngleDistribution);
            	            								optimized = true;
        			    								}
        			    							}
        			    						}
        								    }
        								}
            						}	
            					}
        					}
        				}
        			}
    			}
    		}
    	}
    
		
		//Returning result
	    if (!optimized)
	    	return false;
	    else
	    	return true;
		
    }
	
    public static void additionalConnections00 (int numberCircles, Circle [] circlesMCMC, int [] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int [] setsRepartition, double [] [] distanceMatrix, double [] [] matrixColorProbability, double threshold) {
		
		//Initialization
		int n;
		boolean intersectsCircle, intersectsLine;
		double cost;
		
		//Processing
		boolean [] [] intersectsCircle_Atab = new boolean [numberCircles] [numberCircles];
    	for(int i1 = 0; i1 < numberCircles; i1++){
    		for(int i2 = 0; i2 < numberCircles; i2++){
    			if (i1 < i2) {
    				intersectsCircle = false;
    	    		n = 0;
    	    		while ((n < numberCircles) && (!intersectsCircle)){
    	    			if ((n != i1) && (n != i2)) {
    	    				if (intersectionCircleToSegment (n, i1, i2, circlesMCMC, distanceMatrix))
    	    					intersectsCircle = true;
    	    			}
    	    			n++;
    	    		}
    	    		if (intersectsCircle)
    	    			intersectsCircle_Atab [i1][i2] = intersectsCircle_Atab [i2][i1] = true;
    			}
    		}
    	}
		for(int l = 0; l < numberCircles; l++){
    		for(int p = 0; p < numberCircles; p++){
    			if (l < p) {
    				if (!connectionExists (l, p, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2)) {
    					if (!intersectsCircle_Atab [l][p]) {
    						intersectsLine = false;
    						n = 0;
    						while ((n < numberCircles) && (!intersectsLine)){
        						if ((n != l) && (n != p) && (connectionsMCMCKept [n] != l) && (connectionsMCMCKept [n] != p)) {
        							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY()))
    	    							intersectsLine = true;
        						}
        						n++;
        					}
    						if (!intersectsLine) {
    							if (!additionalConnectionsN1.isEmpty()) {
    								n = 0;
    	    						while ((n < additionalConnectionsN1.size()) && (!intersectsLine)){
    		    						if ((additionalConnectionsN1.get(n) != l) && (additionalConnectionsN1.get(n) != p) && (additionalConnectionsN2.get(n) != l) && (additionalConnectionsN2.get(n) != p)) {
    		    							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY()))
        		    							intersectsLine = true;
    		    						}
    		    						n++;
    		    					}
    							}
    							if (!intersectsLine) {
    								cost = distanceMatrix [l] [p] / (circlesMCMC [l].getRadius() + circlesMCMC [p].getRadius());
    								if (cost <= threshold) {
			    						additionalConnectionsN1.add(l);
			    						additionalConnectionsN2.add(p);
			    						if (setsRepartition [l] != setsRepartition [p])
			    							mergeSets (setsRepartition [l], setsRepartition [p], numberCircles, setsRepartition);
    			    				}
    							}
    						}
    				    }
    			    }
    			}
    	    }
		}
	    	
	}
	
	public static void additionalConnections0 (int numberCircles, Circle [] circlesMCMC, int [] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int [] setsRepartition, double [] [] distanceMatrix, double [] [] matrixColorProbability, double threshold) {
		
		//Initialization
		int n, pixelCover;
		boolean intersectsCircle, intersectsLine;
		boolean [][] matrixCurrentCoverage = new boolean [matrixColorProbability.length][matrixColorProbability [0].length];
		double probabilityCover, cost;
		double cf1 = 1, cf2 = 2;
		
		//Processing
		boolean [] [] intersectsCircle_Atab = new boolean [numberCircles] [numberCircles];
    	for(int i1 = 0; i1 < numberCircles; i1++){
    		for(int i2 = 0; i2 < numberCircles; i2++){
    			if (i1 < i2) {
    				intersectsCircle = false;
    	    		n = 0;
    	    		while ((n < numberCircles) && (!intersectsCircle)){
    	    			if ((n != i1) && (n != i2)) {
    	    				if (intersectionCircleToSegment (n, i1, i2, circlesMCMC, distanceMatrix))
    	    					intersectsCircle = true;
    	    			}
    	    			n++;
    	    		}
    	    		if (intersectsCircle)
    	    			intersectsCircle_Atab [i1][i2] = intersectsCircle_Atab [i2][i1] = true;
    			}
    		}
    	}
    	for(n = 0; n < numberCircles; n++){
    		matrixMCMCFullAdd (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), matrixCurrentCoverage, false);
    		expandConnectionInMatrix (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getRadius(), matrixCurrentCoverage);
    	}
		for(int l = 0; l < numberCircles; l++){
    		for(int p = 0; p < numberCircles; p++){
    			if (l < p) {
    				if (!connectionExists (l, p, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2)) {
    					if (!intersectsCircle_Atab [l][p]) {
    						intersectsLine = false;
    						n = 0;
    						while ((n < numberCircles) && (!intersectsLine)){
        						if ((n != l) && (n != p) && (connectionsMCMCKept [n] != l) && (connectionsMCMCKept [n] != p)) {
        							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY()))
    	    							intersectsLine = true;
        						}
        						n++;
        					}
    						if (!intersectsLine) {
    							if (!additionalConnectionsN1.isEmpty()) {
    								n = 0;
    	    						while ((n < additionalConnectionsN1.size()) && (!intersectsLine)){
    		    						if ((additionalConnectionsN1.get(n) != l) && (additionalConnectionsN1.get(n) != p) && (additionalConnectionsN2.get(n) != l) && (additionalConnectionsN2.get(n) != p)) {
    		    							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY()))
        		    							intersectsLine = true;
    		    						}
    		    						n++;
    		    					}
    							}
    							if (!intersectsLine) {
    								cost = cf1 * (distanceMatrix [l] [p] / (circlesMCMC [l].getRadius() + circlesMCMC [p].getRadius()));
    								boolean [][] matrixTemp = new boolean [matrixColorProbability.length][matrixColorProbability [0].length];
    								expandConnectionInMatrix (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getRadius(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getRadius(), matrixTemp);
    								int res1 [] = searchBoundaries (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getRadius(), matrixColorProbability.length, matrixColorProbability [0].length);
    								int res2 [] = searchBoundaries (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getRadius(), matrixColorProbability.length, matrixColorProbability [0].length);
    								int up = res1 [1];
    								if (res2 [1] > res1 [1])
    									up = res2 [1]; 
    								int down = res1 [0];
    								if (res2 [0] < res1 [0])
    									down = res2 [0]; 
    								int left = res1 [2];
    								if (res2 [2] < res1 [2])
    									left = res2 [2]; 
    								int right = res1 [3];
    								if (res2 [3] > res1 [3])
    									right = res2 [3];
    								pixelCover = 0;
        							probabilityCover = 0;
        							for(int i = left; i <= right; i++){
        								for(int j = down; j <= up; j++){
        									if ((!matrixCurrentCoverage [i] [j]) && (matrixTemp [i] [j])) {
        										probabilityCover += matrixColorProbability [i] [j];
        										pixelCover++;
        									}
        								}
        							}
        							if (pixelCover != 0)
    								   cost += cf2 * probabilityToEnergy (probabilityCover / pixelCover);
    								if (cost <= threshold) {
			    						additionalConnectionsN1.add(l);
			    						additionalConnectionsN2.add(p);
			    						expandConnectionInMatrix (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getRadius(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getRadius(), matrixCurrentCoverage);
			    						if (setsRepartition [l] != setsRepartition [p])
			    							mergeSets (setsRepartition [l], setsRepartition [p], numberCircles, setsRepartition);
    			    				}
    							}
    						}
    				    }
    			    }
    			}
    	    }
		}
	    	
	}
    
    public static void additionalConnections1 (int numberCircles, Circle [] circlesMCMC, int [] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int [] setsRepartition, double [] [] distanceMatrix, double [] [] matrixColorProbability, double probabilityThreshold) {
    	
    	//Initialization
    	int n, n1, n2;
    	double pixelCover, probabilityCover, cost, bestCost;
    	boolean [][] matrixCurrentCoverage = new boolean [matrixColorProbability.length][matrixColorProbability [0].length];
    	boolean intersectsCircle, intersectsLine, work = true;
    	
    	
    	//Processing
    	boolean [] [] intersectsCircle_Atab = new boolean [numberCircles] [numberCircles];
    	for(int i1 = 0; i1 < numberCircles; i1++){
    		for(int i2 = 0; i2 < numberCircles; i2++){
    			if (i1 < i2) {
    				intersectsCircle = false;
    	    		n = 0;
    	    		while ((n < numberCircles) && (!intersectsCircle)){
    	    			if ((n != i1) && (n != i2)) {
    	    				if (intersectionCircleToSegment (n, i1, i2, circlesMCMC, distanceMatrix))
    	    					intersectsCircle = true;
    	    			}
    	    			n++;
    	    		}
    	    		if (intersectsCircle)
    	    			intersectsCircle_Atab [i1][i2] = intersectsCircle_Atab [i2][i1] = true;
    			}
    		}
    	}
    	for(n = 0; n < numberCircles; n++){
    		matrixMCMCFullAdd (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), matrixCurrentCoverage, false);
    		expandConnectionInMatrix (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getRadius(), matrixCurrentCoverage);
    	}
    	for (n = 0; n < additionalConnectionsN1.size(); n++) {
    		expandConnectionInMatrix (circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getRadius(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getRadius(), matrixCurrentCoverage);
    	}
    	while (work) {
        	n1 = -1;
        	n2 = -1;
        	bestCost = Double.POSITIVE_INFINITY;
    		for(int l = 0; l < numberCircles; l++){
        		for(int p = 0; p < numberCircles; p++){
        			if (l < p) {
        				if (true) {  //setsRepartition [l] != setsRepartition [p]
        					if (!connectionExists (l, p, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2)) {
            					if (!intersectsCircle_Atab [l][p]) {
            						intersectsLine = false;
            						n = 0;
            						while ((n < numberCircles) && (!intersectsLine)){
                						if ((n != l) && (n != p) && (connectionsMCMCKept [n] != l) && (connectionsMCMCKept [n] != p)) {
                							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY()))
            	    							intersectsLine = true;
                						}
                						n++;
                					}
            						if (!intersectsLine) {
            							if (!additionalConnectionsN1.isEmpty()) {
            								n = 0;
            	    						while ((n < additionalConnectionsN1.size()) && (!intersectsLine)){
            		    						if ((additionalConnectionsN1.get(n) != l) && (additionalConnectionsN1.get(n) != p) && (additionalConnectionsN2.get(n) != l) && (additionalConnectionsN2.get(n) != p)) {
            		    							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY()))
                		    							intersectsLine = true;
            		    						}
            		    						n++;
            		    					}
            							}
            							if (!intersectsLine) {
            								boolean [][] matrixTemp = new boolean [matrixColorProbability.length][matrixColorProbability [0].length];
            								expandConnectionInMatrix (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getRadius(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getRadius(), matrixTemp);
            								int res1 [] = searchBoundaries (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getRadius(), matrixColorProbability.length, matrixColorProbability [0].length);
            								int res2 [] = searchBoundaries (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getRadius(), matrixColorProbability.length, matrixColorProbability [0].length);
            								int up = res1 [1];
            								if (res2 [1] > res1 [1])
            									up = res2 [1]; 
            								int down = res1 [0];
            								if (res2 [0] < res1 [0])
            									down = res2 [0]; 
            								int left = res1 [2];
            								if (res2 [2] < res1 [2])
            									left = res2 [2]; 
            								int right = res1 [3];
            								if (res2 [3] > res1 [3])
            									right = res2 [3];
            								pixelCover = 0;
                							probabilityCover = 0;
                							for(int i = left; i <= right; i++){
                								for(int j = down; j <= up; j++){
                									if ((!matrixCurrentCoverage [i] [j]) && (matrixTemp [i] [j])) {
                										probabilityCover += matrixColorProbability [i] [j];
                										pixelCover++;
                									}
                								}
                							}
                							if ((probabilityCover / pixelCover) >= probabilityThreshold) {
                							   cost = 1 / pixelCover;
            								   if (cost < bestCost) {
            									   bestCost = cost;
            									   n1 = l;
            							           n2 = p;
            								   }
                							}
            							}
            						}
            					}
        					}	
        				}
        			}
        		}
    		}
    		if (n1 != -1) {
    			additionalConnectionsN1.add(n1);
    			additionalConnectionsN2.add(n2);
    			expandConnectionInMatrix (circlesMCMC [n1].getCenterX(), circlesMCMC [n1].getCenterY(), circlesMCMC [n1].getRadius(), circlesMCMC [n2].getCenterX(), circlesMCMC [n2].getCenterY(), circlesMCMC [n2].getRadius(), matrixCurrentCoverage);
    			mergeSets (setsRepartition [n1], setsRepartition [n2], numberCircles, setsRepartition);
    		}
    		else
    			work = false;
    	}
    	
    }

    public static void additionalConnections2 (int numberCircles, Circle [] circlesMCMC, int [] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int [] setsRepartition, double [] [] distanceMatrix, double [] [] matrixColorProbability, double threshold) {
    	
    	//Initialization
    	int n, n1, n2;
    	double lLowestAngleCost, pLowestAngleCost, temp, cost, bestCost, cf1 = 3, cf2 = 0.1, cf3 = 20;
    	boolean intersectsCircle, intersectsLine, lTree, pTree, work = true;
    	
    	
    	//Processing
    	boolean [] [] intersectsCircle_Atab = new boolean [numberCircles] [numberCircles];
    	for(int i1 = 0; i1 < numberCircles; i1++){
    		for(int i2 = 0; i2 < numberCircles; i2++){
    			if (i1 < i2) {
    				intersectsCircle = false;
    	    		n = 0;
    	    		while ((n < numberCircles) && (!intersectsCircle)){
    	    			if ((n != i1) && (n != i2)) {
    	    				if (intersectionCircleToSegment (n, i1, i2, circlesMCMC, distanceMatrix))
    	    					intersectsCircle = true;
    	    			}
    	    			n++;
    	    		}
    	    		if (intersectsCircle)
    	    			intersectsCircle_Atab [i1][i2] = intersectsCircle_Atab [i2][i1] = true;
    			}
    		}
    	}
    	while (work) {
        	n1 = -1;
        	n2 = -1;
        	bestCost = Double.POSITIVE_INFINITY;
    		for(int l = 0; l < numberCircles; l++){
        		for(int p = 0; p < numberCircles; p++){
        			if (l < p) {
        				if (true) {  //setsRepartition [l] != setsRepartition [p]
        					if (!connectionExists (l, p, connectionsMCMCKept, additionalConnectionsN1, additionalConnectionsN2)) {
            					if (!intersectsCircle_Atab [l][p]) {
            						intersectsLine = false;
            						n = 0;
            						while ((n < numberCircles) && (!intersectsLine)){
                						if ((n != l) && (n != p) && (connectionsMCMCKept [n] != l) && (connectionsMCMCKept [n] != p)) {
                							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [connectionsMCMCKept [n]].getCenterX(), circlesMCMC [connectionsMCMCKept [n]].getCenterY()))
            	    							intersectsLine = true;
                						}
                						n++;
                					}
            						if (!intersectsLine) {
            							if (!additionalConnectionsN1.isEmpty()) {
            								n = 0;
            	    						while ((n < additionalConnectionsN1.size()) && (!intersectsLine)){
            		    						if ((additionalConnectionsN1.get(n) != l) && (additionalConnectionsN1.get(n) != p) && (additionalConnectionsN2.get(n) != l) && (additionalConnectionsN2.get(n) != p)) {
            		    							if (intersectionLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY()))
                		    							intersectsLine = true;
            		    						}
            		    						n++;
            		    					}
            							}
            							if (!intersectsLine) {
            								cost = cf1 * (distanceMatrix [l] [p] / (circlesMCMC [l].getRadius() + circlesMCMC [p].getRadius()));
                							cost += cf2 * (Math.abs(circlesMCMC [l].getRadius() - circlesMCMC [p].getRadius()) / ((circlesMCMC [l].getRadius() + circlesMCMC [p].getRadius()) / 2));
                							lTree = false;
    		    							pTree = false;
    		    							lLowestAngleCost = angleLineToLine (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [connectionsMCMCKept [l]].getCenterX(), circlesMCMC [connectionsMCMCKept [l]].getCenterY());
    		    							pLowestAngleCost = angleLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [connectionsMCMCKept [p]].getCenterX(), circlesMCMC [connectionsMCMCKept [p]].getCenterY());	
    		    							n = 0;
        		    						while ((n < numberCircles) && (!lTree || !pTree)){
            		    						if ((connectionsMCMCKept [n] == l) && (n != connectionsMCMCKept [l])) {
            		    							lTree = true;
            		    							temp = angleLineToLine (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY());
            		    							if (temp < lLowestAngleCost)
            		    								lLowestAngleCost = temp;
            		    						}
            		    						if ((connectionsMCMCKept [n] == p) && (n != connectionsMCMCKept [p])) {
            		    							pTree = true;
            		    							temp = angleLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY());	
            		    							if (temp < pLowestAngleCost)
            		    								pLowestAngleCost = temp;
            		    						}
            		    						n++;
            		    					}
        		    						if (!additionalConnectionsN1.isEmpty()) {
    		    								n = 0;
	        		    						while ((n < additionalConnectionsN1.size()) && (!lTree || !pTree)){
	            		    						if ((additionalConnectionsN1.get(n) == l) || (additionalConnectionsN2.get(n) == l)) {
	            		    							lTree = true;
	            		    							if (additionalConnectionsN1.get(n) == l)
	            		    							    temp = angleLineToLine (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY());
	            		    							else
	            		    								temp = angleLineToLine (circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY());
	            		    							if (temp < lLowestAngleCost)
                		    								lLowestAngleCost = temp;
	            		    						}
	            		    						if ((additionalConnectionsN1.get(n) == p) || (additionalConnectionsN2.get(n) == p)) {
	            		    							pTree = true;
	            		    							if (additionalConnectionsN1.get(n) == p)
	            		    							    temp = angleLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(n)].getCenterY());
	            		    							else
	            		    								temp = angleLineToLine (circlesMCMC [l].getCenterX(), circlesMCMC [l].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [p].getCenterX(), circlesMCMC [p].getCenterY(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(n)].getCenterY());
	            		    							if (temp < pLowestAngleCost)
                		    								pLowestAngleCost = temp;
	            		    						}
	            		    						n++;
	            		    					}
    		    							}
                							cost += cf3 * lLowestAngleCost;
                							cost += cf3 * pLowestAngleCost;
                							if ((cost <= bestCost)) {
        									   bestCost = cost;
        									   n1 = l;
        							           n2 = p;
                							}
            							}
            						}
            					}
        					}	
        				}
        			}
        		}
    		}
    		if (bestCost <= threshold) {
    			additionalConnectionsN1.add(n1);
    			additionalConnectionsN2.add(n2);
    			mergeSets (setsRepartition [n1], setsRepartition [n2], numberCircles, setsRepartition);
    		}
    		else
    			work = false;
    	}	
    	
    }
    
    public static BufferedImage matrixToBufferedImage (boolean [][] matrix) {
    	
    	//Initialization
    	BufferedImage image = new BufferedImage(matrix.length, matrix [0].length, BufferedImage.TYPE_INT_RGB);
    	
    	//Processing
    	for(int i = 0; i < matrix.length; i++){
			for(int j = 0; j < matrix [0].length; j++){
				if (matrix [i] [j])
					image.setRGB(i, j, Color.BLUE.getRGB());
			}
		}
    	
    	//Returning result
    	return image;
    	
    }
    
    public static BufferedImage BufferedImageDeepCopy (BufferedImage imageInput) {
    	
    	//Initialization
    	BufferedImage image = new BufferedImage(imageInput.getWidth(),imageInput.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        //Processing
        g2d.drawImage(imageInput, 0, 0, null);
    	
    	//Returning result
    	return image;
    	
    }
    
    public static BufferedImage circlesToImage (BufferedImage imageInput, int numberCircles, Circle [] circlesMCMC, int upperBound) {
    	
    	//Initialization
    	BufferedImage image = BufferedImageDeepCopy (imageInput);
        int [] boundaries;
        boolean k;
    	
    	//Processing
        for(int n = 0; (n <= upperBound) && (n < numberCircles); n++){
        	boundaries = searchBoundaries (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), imageInput.getWidth(), imageInput.getHeight());
        	for(int i = boundaries [2]; i <= boundaries [3]; i++){
        		k = false;
    			for(int j = boundaries [0]; j <= boundaries [1]; j++){
    				if (!k) {
    					if (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [n].getRadius()) {
    						image.setRGB(i, j, Color.BLUE.getRGB());
    						k = true;
    					}
    				}
    				else {
    					if ((distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [n].getRadius()) && (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 1.5) > circlesMCMC [n].getRadius()))
    						image.setRGB(i, j, Color.BLUE.getRGB());
    				}
    			}
        	}
        	for(int i = boundaries [0]; i <= boundaries [1]; i++){
        		k = false;
    			for(int j = boundaries [2]; j <= boundaries [3]; j++){
    				if (!k) {
    					if (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 0.5, i + 0.5) <= circlesMCMC [n].getRadius()) {
    						image.setRGB(j, i, Color.BLUE.getRGB());
    						k = true;
    					}
    				}
    				else {
    					if ((distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 0.5, i + 0.5) <= circlesMCMC [n].getRadius()) && (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 1.5, i + 0.5) > circlesMCMC [n].getRadius()))
    						image.setRGB(j, i, Color.BLUE.getRGB());
    				}
    			}
        	}
        }    
        
    	//Returning result
    	return image;
    	
    }
    
    public static BufferedImage circlesToImageNoBlobs (BufferedImage imageInput, int numberCircles, Circle [] circlesMCMC, int [] setsRepartition, boolean [] treeOrBlobs) {
    	
    	//Initialization
    	BufferedImage image = BufferedImageDeepCopy (imageInput);
        int [] boundaries;
        boolean k;
    	
    	//Processing
        for(int n = 0; n < numberCircles; n++){
        	if (treeOrBlobs [setsRepartition [n] - 1]) {
        		boundaries = searchBoundaries (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), circlesMCMC [n].getRadius(), imageInput.getWidth(), imageInput.getHeight());
            	for(int i = boundaries [2]; i <= boundaries [3]; i++){
            		k = false;
        			for(int j = boundaries [0]; j <= boundaries [1]; j++){
        				if (!k) {
        					if (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [n].getRadius()) {
        						image.setRGB(i, j, Color.BLUE.getRGB());
        						k = true;
        					}
        				}
        				else {
        					if ((distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 0.5) <= circlesMCMC [n].getRadius()) && (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), i + 0.5, j + 1.5) > circlesMCMC [n].getRadius()))
        						image.setRGB(i, j, Color.BLUE.getRGB());
        				}
        			}
            	}
            	for(int i = boundaries [0]; i <= boundaries [1]; i++){
            		k = false;
        			for(int j = boundaries [2]; j <= boundaries [3]; j++){
        				if (!k) {
        					if (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 0.5, i + 0.5) <= circlesMCMC [n].getRadius()) {
        						image.setRGB(j, i, Color.BLUE.getRGB());
        						k = true;
        					}
        				}
        				else {
        					if ((distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 0.5, i + 0.5) <= circlesMCMC [n].getRadius()) && (distancePointToPoint (circlesMCMC [n].getCenterX(), circlesMCMC [n].getCenterY(), j + 1.5, i + 0.5) > circlesMCMC [n].getRadius()))
        						image.setRGB(j, i, Color.BLUE.getRGB());
        				}
        			}
            	}
        	}
        }    
        
    	//Returning result
    	return image;
    	
    }
    
    public static void connectionsToImage (BufferedImage imageInput, int numberCircles, Circle [] circlesMCMC, boolean [] statesMCMC, int [] connectionsMCMC) {
    	
    	//Initialization
    	Graphics2D connectionUnlocked = imageInput.createGraphics();
    	Graphics2D connectionLocked = imageInput.createGraphics();
    	connectionUnlocked.setColor(Color.green);
    	connectionLocked.setColor(Color.red);
    	connectionLocked.setStroke(new BasicStroke(2));
    	
    	//Processing
    	for(int i = 0; i < numberCircles; i++){
    		if (!statesMCMC [i])
    		   connectionUnlocked.drawLine((int) circlesMCMC [i].getCenterX(), (int) circlesMCMC [i].getCenterY(), (int) circlesMCMC [connectionsMCMC [i]].getCenterX(), (int) circlesMCMC [connectionsMCMC [i]].getCenterY());
    		else
    		   connectionLocked.drawLine((int) circlesMCMC [i].getCenterX(), (int) circlesMCMC [i].getCenterY(), (int) circlesMCMC [connectionsMCMC [i]].getCenterX(), (int) circlesMCMC [connectionsMCMC [i]].getCenterY());
    	}
    	
    }
    
    public static void connectionsToImageNoBlobs (BufferedImage imageInput, int numberCircles, Circle [] circlesMCMC, boolean [] statesMCMC, int [] connectionsMCMC, int [] setsRepartition, boolean [] treeOrBlobs) {
    	
    	//Initialization
    	Graphics2D connectionUnlocked = imageInput.createGraphics();
    	Graphics2D connectionLocked = imageInput.createGraphics();
    	connectionUnlocked.setColor(Color.green);
    	connectionLocked.setColor(Color.red);
    	connectionLocked.setStroke(new BasicStroke(2));
    	
    	//Processing
    	for(int i = 0; i < numberCircles; i++){
    		if (treeOrBlobs [setsRepartition [i] - 1]) {
    			if (!statesMCMC [i])
    	    		   connectionUnlocked.drawLine((int) circlesMCMC [i].getCenterX(), (int) circlesMCMC [i].getCenterY(), (int) circlesMCMC [connectionsMCMC [i]].getCenterX(), (int) circlesMCMC [connectionsMCMC [i]].getCenterY());
    	    		else
    	    		   connectionLocked.drawLine((int) circlesMCMC [i].getCenterX(), (int) circlesMCMC [i].getCenterY(), (int) circlesMCMC [connectionsMCMC [i]].getCenterX(), (int) circlesMCMC [connectionsMCMC [i]].getCenterY());
    		}
    	}
    	
    }
    
    public static void additionalConnectionsToImage (BufferedImage imageInput, Circle [] circlesMCMC, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2) {
    	
    	//Initialization
    	Graphics2D g2d = imageInput.createGraphics();
    	g2d.setColor(Color.MAGENTA);
    	g2d.setStroke(new BasicStroke(2));
    	
    	//Processing
    	for(int i = 0; i < additionalConnectionsN1.size(); i++){
    		   g2d.drawLine((int) circlesMCMC [additionalConnectionsN1.get(i)].getCenterX(), (int) circlesMCMC [additionalConnectionsN1.get(i)].getCenterY(), (int) circlesMCMC [additionalConnectionsN2.get(i)].getCenterX(), (int) circlesMCMC [additionalConnectionsN2.get(i)].getCenterY());
    	}
    	
    }
    
    public static void additionalConnectionsToImageNoBlobs (BufferedImage imageInput, Circle [] circlesMCMC, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int [] setsRepartition, boolean [] treeOrBlobs) {
    	
    	//Initialization
    	Graphics2D g2d = imageInput.createGraphics();
    	g2d.setColor(Color.MAGENTA);
    	g2d.setStroke(new BasicStroke(2));
    	
    	//Processing
    	for(int i = 0; i < additionalConnectionsN1.size(); i++){
    		if (treeOrBlobs [setsRepartition [additionalConnectionsN1.get(i)] - 1])
    		   g2d.drawLine((int) circlesMCMC [additionalConnectionsN1.get(i)].getCenterX(), (int) circlesMCMC [additionalConnectionsN1.get(i)].getCenterY(), (int) circlesMCMC [additionalConnectionsN2.get(i)].getCenterX(), (int) circlesMCMC [additionalConnectionsN2.get(i)].getCenterY());
    	}
    	
    }
    
    public static void expandConnectionInMatrix (double x1, double y1, double r1, double x2, double y2, double r2, boolean [][] matrixMCMCFull) {
    	
    	//Initialization
    	double dx = x1 - x2;
    	double dy = y1 - y2;
    	double distance = Math.sqrt(dx*dx + dy*dy);
    	dx /= distance;
    	dy /= distance;
    	double x3 = x1 + (r1)*dy;
    	double y3 = y1 - (r1)*dx;
    	double x4 = x1 - (r1)*dy;
    	double y4 = y1 + (r1)*dx;
    	double x5 = x2 + (r2)*dy;
    	double y5 = y2 - (r2)*dx;
    	double x6 = x2 - (r2)*dy;
    	double y6 = y2 + (r2)*dx;
    	int ptN1, ptN2;
    	if (r1 >= r2)
    	    ptN1 = (int) (2 * (2 * r1));
    	else
    		ptN1 = (int) (2 * (2 * r2));
    	double x11, y11, x12, y12, ratio1, distance2, ratio2;
    	int x2D, y2D;
    	
    	//Processing
    	for(int i = 1; i <= ptN1; i++){
    		ratio1 = (double) i / ptN1;
    		x11 = (ratio1 * x4) + ((1 - ratio1) * x3);
        	y11 = (ratio1 * y4) + ((1 - ratio1) * y3);
        	x12 = (ratio1 * x6) + ((1 - ratio1) * x5);
        	y12 = (ratio1 * y6) + ((1 - ratio1) * y5);
        	distance2 = distancePointToPoint (x11, y11, x12, y12);
        	ptN2 = (int) (2 * distance2);
        	for(int j = 1; j <= ptN2; j++){
        		ratio2 = (double) j / ptN2;
        		x2D = (int) ((ratio2 * x12) + ((1 - ratio2) * x11));
            	y2D = (int) ((ratio2 * y12) + ((1 - ratio2) * y11));
            	if ((x2D >= 0) && (y2D >= 0) && (x2D < matrixMCMCFull.length) && (y2D < matrixMCMCFull [0].length))
            	   matrixMCMCFull [x2D] [y2D] = true;
        	}
    	}
    	
    }   
    public static void expandConnectionInImage (double x1, double y1, double r1, double x2, double y2, double r2, BufferedImage image) {
    	
    	//Initialization
    	double dx = x1 - x2;
    	double dy = y1 - y2;
    	double distance = Math.sqrt(dx*dx + dy*dy);
    	dx /= distance;
    	dy /= distance;
    	double x3 = x1 + (r1)*dy;
    	double y3 = y1 - (r1)*dx;
    	double x4 = x1 - (r1)*dy;
    	double y4 = y1 + (r1)*dx;
    	double x5 = x2 + (r2)*dy;
    	double y5 = y2 - (r2)*dx;
    	double x6 = x2 - (r2)*dy;
    	double y6 = y2 + (r2)*dx;
    	int ptN1, ptN2;
    	if (r1 >= r2)
    	    ptN1 = (int) (2 * (2 * r1));
    	else
    		ptN1 = (int) (2 * (2 * r2));
    	double x11, y11, x12, y12, ratio1, distance2, ratio2;
    	int x2D, y2D;
    	
    	//Processing
    	for(int i = 1; i <= ptN1; i++){
    		ratio1 = (double) i / ptN1;
    		x11 = (ratio1 * x4) + ((1 - ratio1) * x3);
        	y11 = (ratio1 * y4) + ((1 - ratio1) * y3);
        	x12 = (ratio1 * x6) + ((1 - ratio1) * x5);
        	y12 = (ratio1 * y6) + ((1 - ratio1) * y5);
        	distance2 = distancePointToPoint (x11, y11, x12, y12);
        	ptN2 = (int) (2 * distance2);
        	for(int j = 1; j <= ptN2; j++){
        		ratio2 = (double) j / ptN2;
        		x2D = (int) ((ratio2 * x12) + ((1 - ratio2) * x11));
            	y2D = (int) ((ratio2 * y12) + ((1 - ratio2) * y11));
            	if ((x2D >= 0) && (y2D >= 0) && (x2D < image.getWidth()) && (y2D < image.getHeight()))
            	   image.setRGB(x2D, y2D, Color.WHITE.getRGB());
        	}
    	}
    	
    }
    
    public static int undiscoveredParts (BufferedImage resultImage2, BufferedImage imageGroundTruth, int numberCircles, Circle [] circlesMCMC, int [] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2) {
    	
    	//Initialization
    	int undiscoveredPixels = 0;
    	BufferedImage finalImageTemp = BufferedImageDeepCopy (resultImage2);
    	
    	//Processing
		for(int i = 0; i < numberCircles; i++){
			expandConnectionInImage (circlesMCMC [i].getCenterX(), circlesMCMC [i].getCenterY(), circlesMCMC [i].getRadius(), circlesMCMC [connectionsMCMCKept [i]].getCenterX(), circlesMCMC [connectionsMCMCKept [i]].getCenterY(), circlesMCMC [connectionsMCMCKept [i]].getRadius(), finalImageTemp);
		}
		for(int i = 0; i < additionalConnectionsN1.size(); i++){
			expandConnectionInImage (circlesMCMC [additionalConnectionsN1.get(i)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(i)].getCenterY(), circlesMCMC [additionalConnectionsN1.get(i)].getRadius(), circlesMCMC [additionalConnectionsN2.get(i)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(i)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(i)].getRadius(), finalImageTemp);
		}
		for(int i = 0; i < imageGroundTruth.getWidth(); i++){
			for(int j = 0; j < imageGroundTruth.getHeight(); j++){
				if ((imageGroundTruth.getRGB(i, j) & 0xff) != 0) {
					if ((finalImageTemp.getRGB(i, j) & 0xff) == 0) {
						undiscoveredPixels++;
					}
				}
			}
		}
    	
    	//Returning result
    	return undiscoveredPixels;
    	
    }
    
     public static double areaConnection (int n1, int n2, Circle [] circlesMCMC, int width, int height) {
    	
    	//Initialization
    	double pixelCounter = 0;
    	boolean[][] matrixTemp = new boolean [width][height];
    	
    	//Processing
    	matrixMCMCFullAdd (circlesMCMC [n1].getCenterX(), circlesMCMC [n1].getCenterY(), circlesMCMC [n1].getRadius(), matrixTemp, false);
    	matrixMCMCFullAdd (circlesMCMC [n2].getCenterX(), circlesMCMC [n2].getCenterY(), circlesMCMC [n2].getRadius(), matrixTemp, false);
    	expandConnectionInMatrix (circlesMCMC [n1].getCenterX(), circlesMCMC [n1].getCenterY(), circlesMCMC [n1].getRadius(), circlesMCMC [n2].getCenterX(), circlesMCMC [n2].getCenterY(), circlesMCMC [n2].getRadius(), matrixTemp);
    	for(int i = 0; i < width; i++){
    		for(int j = 0; j < height; j++){
    			if (matrixTemp [i][j])
    				pixelCounter++;
    		}
    	}
    	
    	//Returning result
    	return pixelCounter;
    	
    }
    
    public static int areaSet (int numberCircles, Circle [] circlesMCMC, int[] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2, int[] setsRepartition, int setIndex, int width, int height) {
    	
    	//Initialization
    	int pixelCounter = 0;
    	boolean[][] matrixTemp = new boolean [width][height];
    	
    	//Processing
    	for(int k = 0; k < numberCircles; k++){
    		if (setsRepartition [k] == setIndex) {
    			matrixMCMCFullAdd (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getRadius(), matrixTemp, false);
    			expandConnectionInMatrix (circlesMCMC [k].getCenterX(), circlesMCMC [k].getCenterY(), circlesMCMC [k].getRadius(), circlesMCMC [connectionsMCMCKept [k]].getCenterX(), circlesMCMC [connectionsMCMCKept [k]].getCenterY(), circlesMCMC [connectionsMCMCKept [k]].getRadius(), matrixTemp);
    		}
    	}
    	for(int k = 0; k < additionalConnectionsN1.size(); k++){
    		if (setsRepartition [additionalConnectionsN1.get(k)] == setIndex)
    			expandConnectionInMatrix (circlesMCMC [additionalConnectionsN1.get(k)].getCenterX(), circlesMCMC [additionalConnectionsN1.get(k)].getCenterY(), circlesMCMC [additionalConnectionsN1.get(k)].getRadius(), circlesMCMC [additionalConnectionsN2.get(k)].getCenterX(), circlesMCMC [additionalConnectionsN2.get(k)].getCenterY(), circlesMCMC [additionalConnectionsN2.get(k)].getRadius(), matrixTemp);
    	}
    	for(int i = 0; i < width; i++){
    		for(int j = 0; j < height; j++){
    			if (matrixTemp [i][j])
    				pixelCounter++;
    		}
    	}
    	
    	//Returning result
		return pixelCounter;
		
	}
    
    public static boolean connectionExists (int n1, int n2, int[] connectionsMCMCKept, List<Integer> additionalConnectionsN1, List<Integer> additionalConnectionsN2) {
    	
    	//Initialization
    	boolean exists = false;
    	
    	//Processing
    	if ((connectionsMCMCKept [n1] == n2) || (connectionsMCMCKept [n2] == n1))
    		exists = true;
    	if (!exists) {
    		for(int k = 0; k < additionalConnectionsN1.size(); k++){
        		if (((additionalConnectionsN1.get(k) == n1) && (additionalConnectionsN2.get(k) == n2)) || ((additionalConnectionsN1.get(k) == n2) && (additionalConnectionsN2.get(k) == n1)))
        			exists = true;
        	}
    	}
    	
    	//Returning result
    	return exists;
    	
    }
    
}
