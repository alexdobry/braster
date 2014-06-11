package de.braster;

import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class ClusteringScene extends AbstractScene{

	
	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene evaluationScene;
	private LinkedList<Idea> ideas;
	private MTRoundRectangle mtRoundRectangle;
	
	public ClusteringScene( MTApplication mtApplication, String name) 
	{
		super(mtApplication, name);
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		//setzt Hintergrundfarbe
		this.setClearColor(MTColor.WHITE);
		
		mtRoundRectangle = new MTRoundRectangle(this.mtApp, 30, 60, 0, 200, 60, 12, 12);
		
		mtRoundRectangle.setPositionGlobal(new Vector3D(mtApp.getWidth()/2, 180));
		ideas = Idea.getAllIdeas();

		Vector3D ideapos = new Vector3D(mtApp.getWidth()/2, 250);
		for (Idea idea : ideas) {
			idea.setVisible(false);
			canv.addChild(idea);
			//			posy = rng.nextFloat() * (mtApp.height-padding*2);
//			posx = rng.nextFloat() * (mtApp.width-padding*2);
			
			idea.setPositionGlobal(ideapos);
			idea.updateCanvas(canv);
		}
		
		
		//Titel
		IFont font1 = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		42, MTColor.BLACK, false);
		
		MTTextArea titel = new MTTextArea(mtApp, font1);
		titel.unregisterAllInputProcessors();
		titel.setPickable(false);
		titel.setNoFill(true);
		titel.setNoStroke(true);
		titel.setText("Clusterung der Ideen");
		canv.addChild(titel);
		titel.setPositionRelativeToParent(new Vector3D(mtApp.width/2, 80));
		
		
		//ideen stack
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE, false);
		
		final MTTextArea rText = new MTTextArea(this.mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(ideas.size() + " Ideen übrig");
		
		
		mtRoundRectangle.unregisterAllInputProcessors();
		if (ideas.size() != 0 ) {
			mtRoundRectangle.setFillColor(MTColor.GREEN); 
		} else {
			mtRoundRectangle.setFillColor(MTColor.RED);
		}
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			int count = 0;
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
						mtRoundRectangle.setFillColor(new MTColor(220,220,220,255));
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (te.isTapped()){
							
							
							if (count < ideas.size()) {
								ideas.get(count++).setVisible(true);
								rText.setText(ideas.size()-count + " Ideen übrig");
							}
							
						}
						
						if (ideas.size()-count != 0) {
							mtRoundRectangle.setFillColor(MTColor.GREEN);
						} else {
							mtRoundRectangle.setFillColor(MTColor.RED);
						}
						
						break;
					}
				
				return false;
			}
		});
		

		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		mtRoundRectangle.scale(1.2f, 1.2f, 1, mtRoundRectangle.getCenterPointGlobal());
		
		canv.addChild(mtRoundRectangle);
//		createItem();
//		createLeftMenubar();
		createRightMenubar();
	}

	
	
	//create right menubar, um mit der nï¿½chsten Scene zu beginnen
	private void createRightMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, this.mtApp.width-240, this.mtApp.height-80, 0, 200, 60, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.GREY);  	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				switch (te.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
						mtRoundRectangle.setFillColor(new MTColor(220,220,220,255));
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
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
						
						mtRoundRectangle.setFillColor(MTColor.GREY);  	
						break;
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
		
		mtRoundRectangle.scale(1.2f, 1.2f, 1, mtRoundRectangle.getCenterPointRelativeToParent());
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		
		this.canv.addChild(mtRoundRectangle);	
	}
	
	
}
