package de.braster;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class ClusteringScene extends AbstractScene{

	
	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene evaluationScene;
	
	public ClusteringScene( MTApplication mtApplication, String name) 
	{
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		//setzt Hintergrundfarbe
		this.setClearColor(MTColor.WHITE);
		
		createItem();
		
		createLeftMenubar();
		
		createRightMenubar();
	}

	
	
	//create left menubar for options
	private void createLeftMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, 20,this.mtApp.height-180, 0, 200, 160, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.GREY); 	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		this.canv.addChild(mtRoundRectangle);	
		
		//3 Radiobuttons mit jeweils label dahinter
	}
	
	
	//create right menubar, um mit der nächsten Scene zu beginnen
	private void createRightMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, this.mtApp.width-220, this.mtApp.height-80, 0, 200, 60, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.GREY);  	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
										
					//Save the current scene on the scene stack before changing
					mtApp.pushScene();
					if (evaluationScene == null){
						evaluationScene = new EvaluationScene(mtApp, "Evaluation");
						//Konstruktor erweitern um Anzahl Spieler, da genau soviele
						//Tastaturen geladen werden
					//Add the scene to the mt application
					mtApp.addScene(evaluationScene);
					}
					//Do the scene change
					mtApp.changeScene(evaluationScene);
				}
				return false;
			}
		});
		
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE, false);
		
		MTTextArea rText = new MTTextArea(this.mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("             Go to \n Evaluation Scene");
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		
		this.canv.addChild(mtRoundRectangle);	
	}
	
	private void createItem()
	{
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		30, MTColor.BLACK, false);
		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, 400, 400, 0, 300, 50, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
	//	mtRoundRectangle.setFillColor(MTColor.BLUE);
		mtRoundRectangle.setStrokeColor(MTColor.BLUE);

		//Bild als Hintergrund eines Rectangle haben
		// final PImage eraser = this.mtApp.loadImage("C:\\Users\\Stefan\\Desktop\\notizzettel.jpg");
	    //mtRoundRectangle.setTexture(eraser);
	
	      /*
		MTTextArea rText = new MTTextArea(this.mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("hier steht was");
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
	*/
		mtRoundRectangle.registerInputProcessor(new TapProcessor(getMTApplication()));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(mtRoundRectangle));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
										
					//entweder wird die idee ausgeklappt
					//oder wieder eingeklappt
					//je nach vorherigem status
					//höhe nach text ausrichten
					//dazu ne methode schreiben
					
					mtRoundRectangle.setSizeLocal(300, 500);
				}
				return false;
			}
		});
		this.canv.addChild(mtRoundRectangle);
		
	}
}
