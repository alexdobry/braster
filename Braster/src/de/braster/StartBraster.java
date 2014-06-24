package de.braster;
import java.util.LinkedList;

import org.mt4j.MTApplication;

import processing.core.PImage;




public class StartBraster extends MTApplication {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static LinkedList<PImage> K1helpBW = new LinkedList<PImage>();
	public static LinkedList<PImage> K2helpBW = new LinkedList<PImage>();
	public static LinkedList<PImage> K3helpBW = new LinkedList<PImage>();
	public static LinkedList<PImage> K4helpBW = new LinkedList<PImage>();
	public static LinkedList<PImage> helpCluster = new LinkedList<PImage>();
	public static LinkedList<PImage> helpEVA = new LinkedList<PImage>();
	public static LinkedList<PImage> helpTest = new LinkedList<PImage>();
	public static PImage evalHighlightLeft;
	public static PImage evalHighlightMiddle;
	public static PImage evalHighlightRight;
	
	public static void main(String[] args) {
		
		
		initialize(true);
	}
 
	@Override
	public void startUp() {
		
		
		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		K1helpBW.add(loadImage(path + "K1_brainwriting1.png"));
		K1helpBW.add(loadImage(path + "K1_brainwriting2.png"));
		K1helpBW.add(loadImage(path + "K1_brainwriting3.png"));
		K1helpBW.add(loadImage(path + "K1_brainwriting4.png"));
		K2helpBW.add(loadImage(path + "K2_brainwriting1.png"));
		K2helpBW.add(loadImage(path + "K2_brainwriting2.png"));
		K2helpBW.add(loadImage(path + "K2_brainwriting3.png"));
		K2helpBW.add(loadImage(path + "K2_brainwriting4.png"));
		K3helpBW.add(loadImage(path + "K3_brainwriting1.png"));
		K3helpBW.add(loadImage(path + "K3_brainwriting2.png"));
		K3helpBW.add(loadImage(path + "K3_brainwriting3.png"));
		K3helpBW.add(loadImage(path + "K3_brainwriting4.png"));
		K4helpBW.add(loadImage(path + "K4_brainwriting1.png"));
		K4helpBW.add(loadImage(path + "K4_brainwriting2.png"));
		K4helpBW.add(loadImage(path + "K4_brainwriting3.png"));
		K4helpBW.add(loadImage(path + "K4_brainwriting4.png"));
		
		helpCluster.add(loadImage(path + "clustering1.png"));
		helpCluster.add(loadImage(path + "clustering2.png"));
		helpCluster.add(loadImage(path + "clustering3.png"));
		helpCluster.add(loadImage(path + "clustering4.png"));
		helpCluster.add(loadImage(path + "clustering5.png"));
		helpCluster.add(loadImage(path + "clustering6.png"));
		
		helpTest.add(loadImage(path + "1.png"));
		helpTest.add(loadImage(path + "2.png"));
		helpTest.add(loadImage(path + "3.png"));
		helpTest.add(loadImage(path + "4.png"));
		
		helpEVA.add(loadImage(path + "evaluation1.png"));
		helpEVA.add(loadImage(path + "evaluation2.png"));
		helpEVA.add(loadImage(path + "evaluation3.png"));
		helpEVA.add(loadImage(path + "evaluation4.png"));
		helpEVA.add(loadImage(path + "evaluation5.png"));

		evalHighlightLeft = loadImage(path +   "evalHighlightLeft.png");
		evalHighlightMiddle = loadImage(path + "evalHighlightMiddle.png");
		evalHighlightRight = loadImage(path +  "evalHighlightRight.png");
		
		addScene(new WelcomeScene(this, "Hello World Scene"));
		//addScene(new SetupScene(this, "Hello World Scene"));
		//addScene(new ClusteringScene(this, "Hello World Scene"));
		//addScene(new EvaluationHelp(this, "Hello World Scene"));
		//addScene(new EvaluationScene(this, "Hello World Scene"));
	}
}

