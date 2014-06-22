package de.braster;
import java.util.LinkedList;

import org.mt4j.MTApplication;

import processing.core.PImage;




public class StartBraster extends MTApplication {
	
	public static LinkedList<PImage> helpBW = new LinkedList<PImage>();
	public static LinkedList<PImage> helpEVA = new LinkedList<PImage>();
	
	public static void main(String[] args) {
		
		
		initialize(true);
	}
 
	@Override
	public void startUp() {
		
		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		helpBW = new LinkedList<>();
		helpBW.add(loadImage(path + "brainwriting1.png"));
		helpBW.add(loadImage(path + "brainwriting2.png"));
		helpBW.add(loadImage(path + "brainwriting3.png"));
		helpBW.add(loadImage(path + "brainwriting4.png"));
		
		helpEVA.add(loadImage(path + "Evaluation_1.png"));
		helpEVA.add(loadImage(path + "Evaluation_2.png"));
		helpEVA.add(loadImage(path + "Evaluation_3.png"));
		helpEVA.add(loadImage(path + "Evaluation_4.png"));
		helpEVA.add(loadImage(path + "Evaluation_5.png"));
		helpEVA.add(loadImage(path + "Evaluation_6.png"));	 
		helpEVA.add(loadImage(path + "Evaluation_8.png"));
		
		
		//addScene(new WelcomeScene(this, "Hello World Scene"));
		addScene(new SetupScene(this, "Hello World Scene"));
		//addScene(new ClusteringScene(this, "Hello World Scene"));
		//addScene(new EvaluationHelp(this, "Hello World Scene"));
		//addScene(new EvaluationScene(this, "Hello World Scene"));
	}
}

