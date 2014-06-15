package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTSceneTexture;
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

	    //Background
	    MTRectangle background = new MTRectangle(mtApplication, mtApplication.width, mtApplication.height);
	    background.setFillColor(new MTColor(255,255,255,255));
		background.setPickable(false);
	    canv.addChild(background);
	    
	    //Braster Logo
		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		PImage img = getMTApplication().loadImage(path + "braster.png");
		
		MTRectangle logo = new MTRectangle(mtApplication, img);
		logo.scale(0.5f, 0.5f, 0, logo.getCenterPointLocal());
		logo.setPositionGlobal(new Vector3D(mtApplication.width/2, logo.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		
		canv.addChild(logo);
		
		//FH Logo
		PImage fh = getMTApplication().loadImage(path + "fhkoeln.jpg");
		MTRectangle fhlogo = new MTRectangle(mtApplication, fh);
		fhlogo.scale(0.5f, 0.5f, 0, new Vector3D(0,0,0));
		canv.addChild(fhlogo);
		
		
		
		//text abstract
		MTTextArea abstrct = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		42, //fontzize 
                		MTColor.BLACK)); //Font color
		abstrct.setText("Abstract ... \n asdjfasdf ");
		abstrct.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		abstrct.setPickable(false);
		canv.addChild(abstrct);
				
		
		
		
		
		//text tippen zum starten
		MTTextArea tippen = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		MTColor.BLACK)); //Font color
		tippen.setText("Tippen zum starten");
		tippen.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height*0.9f));
		tippen.setPickable(false);
		canv.addChild(tippen);
		
		
		//text tippen zum starten
		MTTextArea footer = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		24, //fontzize 
                		MTColor.BLACK)); //Font color
		footer.setText("SGMCI                  Patrick Englert, Stefan Heruth, Alex Dobrynin                  Prof. Dr. Heiner Klocke");
		footer.setPositionRelativeToParent(new Vector3D(mtApplication.width/2f, mtApplication.height-footer.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		footer.setPickable(false);
		canv.addChild(footer);
		
		
		
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
		
	}

}
