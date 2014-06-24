package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class WelcomeScene extends AbstractScene {

	private MTApplication mtApp;
	private MTCanvas canv;
	
	public WelcomeScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		mtApp = mtApplication;
		canv = getCanvas();

		
		//temporärer fix; beseitigt lag bei Eingabe der ersten Idee
		Idea i = new Idea(mtApp, canv);
		Idea.getAllIdeas().clear();
		
		
	    //Background
	    MTRectangle background = new MTRectangle(mtApplication, mtApplication.width, mtApplication.height);
	    background.setFillColor(new MTColor(255,255,255,255));
		background.setPickable(false);
	    canv.addChild(background);
	    String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
	    
		//FH Logo
		PImage fh = getMTApplication().loadImage(path + "fhkoeln.jpg");
		MTRectangle fhlogo = new MTRectangle(mtApplication, fh);
		fhlogo.scale(0.5f, 0.5f, 0, new Vector3D(0,0,0));
		canv.addChild(fhlogo);
	    
	    //Braster Logo
		
		PImage img = getMTApplication().loadImage(path + "braster.png");
		
		MTRectangle logo = new MTRectangle(mtApplication, img);
		logo.scale(0.5f, 0.5f, 0, logo.getCenterPointLocal());
		//unterkante oberkante anordnung fh logo und braster logo
		logo.setPositionGlobal(new Vector3D(mtApplication.width/2, 60+fhlogo.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)+logo.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		
		canv.addChild(logo);
		
		//text abstract
		MTTextArea abstractText = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		32, //fontzize 
                		MTColor.BLACK)); //Font color
		abstractText.setText("Durchlaufen Sie den kreativen Prozess und finden Sie mithilfe \nvon Brainwriting und Clustering die beste Loesung fuer Ihr Problem");
		abstractText.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height/2f + 60));
		abstractText.setPickable(false);
		canv.addChild(abstractText);
		
		//text tippen zum starten
		MTTextArea tippen = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		40, //fontzize 
                		MTColor.BLACK)); //Font color
		tippen.setText("Tippen zum starten");
		tippen.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height*0.8f));
		tippen.setPickable(false);
		canv.addChild(tippen);
		
		
		//text footer
		int footerFontSize = 18;
		
		MTTextArea footerSGMCI = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		footerFontSize, //fontzize 
                		MTColor.BLACK)); //Font color
		footerSGMCI.setText("SGMCI - SS14");
		footerSGMCI.setPositionRelativeToParent(new Vector3D(footerSGMCI.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2, mtApplication.height-footerSGMCI.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		footerSGMCI.setPickable(false);
		canv.addChild(footerSGMCI);
		
		MTTextArea footerTeam = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		footerFontSize, //fontzize 
                		MTColor.BLACK)); //Font color
		footerTeam.setText("Patrick Englert, Stefan Heruth, Alex Dobrynin");
		footerTeam.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height-footerTeam.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		footerTeam.setPickable(false);
		canv.addChild(footerTeam);
		
		MTTextArea footerProf = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		footerFontSize, //fontzize 
                		MTColor.BLACK)); //Font color
		footerProf.setText("Prof. Dr. Heiner Klocke");
		footerProf.setPositionRelativeToParent(new Vector3D(mtApplication.width-footerProf.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2, mtApplication.height-footerProf.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		footerProf.setPickable(false);
		canv.addChild(footerProf);
		
		
		
		//Tap zum weiter gehen
		canv.registerInputProcessor(new TapProcessor(mtApp));
		canv.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()) {
					Iscene setupscene = null;
					mtApp.pushScene();
					if (setupscene == null){
						setupscene = new SetupScene(mtApp, "Setup");
						//Konstruktor erweitern um Anzahl Spieler, da genau soviele
						//Tastaturen geladen werden
					//Add the scene to the mt application
						mtApp.addScene(setupscene);
					}
					//Do the scene change
					mtApp.changeScene(setupscene);
				}
				
				return false;
			}
		});
		
		
		
		//farbfilter ï¿½ber alles
		
//		MTRectangle test = new MTRectangle(mtApplication, mtApplication.getWidth(), mtApplication.getHeight());
//		
//		test.setFillColor(new MTColor(30,144,255, 200));
//		test.setPickable(false);
//		canv.addChild(test);
		
		

	}

}
