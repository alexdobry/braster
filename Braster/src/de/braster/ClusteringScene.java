package de.braster;

import java.util.LinkedList;
import java.util.Random;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle.PositionAnchor;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTCheckbox;

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
		
		ideas = Idea.getAllIdeas();
		float posx = 0;
		float posy = 100;
		
		float padding = 50;
		
		Random rng = new Random();
		for (Idea idea : ideas) {
			idea.setAnchor(PositionAnchor.UPPER_LEFT);
			idea.setVisible(false);
			canv.addChild(idea);
			//			posy = rng.nextFloat() * (mtApp.height-padding*2);
//			posx = rng.nextFloat() * (mtApp.width-padding*2);
			idea.setPositionGlobal(new Vector3D(posx+padding, posy+padding));
			idea.updateCanvas(canv); //TODO:
			idea.setAnchor(PositionAnchor.CENTER); //sonnst gibts beim gruppieren probleme TODO: fix it?
		}
		
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
						
						if (ideas.size()-count != 0) {
							mtRoundRectangle.setFillColor(MTColor.GREEN);
						} else {
							mtRoundRectangle.setFillColor(MTColor.RED);
						}
						
					}
					break;
				}
				
				return false;
			}
		});
		

		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		mtRoundRectangle.scale(1.2f, 1.2f, 1, new Vector3D(0, 0, 0));
		
		canv.addChild(mtRoundRectangle);
//		createItem();
//		createLeftMenubar();
		createRightMenubar();
	}

	
	
	//create left menubar for options
	private void createLeftMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, 20,this.mtApp.height-180, 0, 200, 160, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.GREY); 	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		
		final MTCheckbox checkBox1 = new MTCheckbox(this.mtApp, 20);
		checkBox1.setFillColor(MTColor.WHITE);		 
        checkBox1.setBooleanValue(false);
        checkBox1.addGestureListener(TapProcessor.class, new IGestureEventListener() {
            
            @Override
            public boolean processGestureEvent(MTGestureEvent ge) {
                if(checkBox1.getBooleanValue()){
                    
                }
                else{
                   
                }
                return false;
            }
        });
 
		
		checkBox1.setPositionRelativeToParent(new Vector3D(50,this.mtApp.height-150));
		mtRoundRectangle.addChild(checkBox1);
		
		
		final MTCheckbox checkBox2 = new MTCheckbox(this.mtApp, 20);
		checkBox2.setFillColor(MTColor.WHITE);
        checkBox2.setBooleanValue(true);
        checkBox2.addGestureListener(TapProcessor.class, new IGestureEventListener() {
            
            @Override
            public boolean processGestureEvent(MTGestureEvent ge) {
                if(checkBox2.getBooleanValue()){
                    
                }
                else{
                   
                }
                return false;
            }
        });
 
		
		checkBox2.setPositionRelativeToParent(new Vector3D(50,this.mtApp.height-105));
		mtRoundRectangle.addChild(checkBox2);
		
		
		final MTCheckbox checkBox3 = new MTCheckbox(this.mtApp, 20);
		checkBox3.setFillColor(MTColor.WHITE);
        checkBox3.setBooleanValue(true);
        checkBox3.addGestureListener(TapProcessor.class, new IGestureEventListener() {
            
            @Override
            public boolean processGestureEvent(MTGestureEvent ge) {
                if(checkBox3.getBooleanValue()){
                    
                }
                else{
                   
                }
                return false;
            }
        });
 
		
		checkBox3.setPositionRelativeToParent(new Vector3D(50,this.mtApp.height-60));
		mtRoundRectangle.addChild(checkBox3);
			
		 
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
				15, MTColor.WHITE, false);
		
        final MTTextArea textArea1 = new MTTextArea(this.mtApp,font);
        textArea1.setText("show tips");
        textArea1.setFillColor(MTColor.GREY);
        textArea1.setStrokeColor(MTColor.GREY);
        textArea1.setPositionRelativeToParent(new Vector3D(120,this.mtApp.height-150));
        mtRoundRectangle.addChild(textArea1);
		
        final MTTextArea textArea2 = new MTTextArea(this.mtApp,font);
        textArea2.setText("show sorted ideas");
        textArea2.setFillColor(MTColor.GREY);
        textArea2.setStrokeColor(MTColor.GREY);
        textArea2.setPositionRelativeToParent(new Vector3D(140,this.mtApp.height-105));
        mtRoundRectangle.addChild(textArea2);
        
        final MTTextArea textArea3 = new MTTextArea(this.mtApp,font);
        textArea3.setText("Anleitung");
        textArea3.setFillColor(MTColor.GREY);
        textArea3.setStrokeColor(MTColor.GREY);
        textArea3.setPositionRelativeToParent(new Vector3D(120,this.mtApp.height-60));
        mtRoundRectangle.addChild(textArea3);
		
		this.canv.addChild(mtRoundRectangle);	
		

		
		//3 Radiobuttons mit jeweils label dahinter
	
	}
	
	
	//create right menubar, um mit der nächsten Scene zu beginnen
	private void createRightMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, this.mtApp.width-240, this.mtApp.height-80, 0, 200, 60, 12, 12);
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
		
		mtRoundRectangle.scale(1.2f, 1.2f, 1, mtRoundRectangle.getCenterPointRelativeToParent());
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
