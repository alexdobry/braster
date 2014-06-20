package de.braster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import de.braster.ClusterKeyboard.KeyInfo;

public class ClusteringScene extends AbstractScene{

	
	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene evaluationScene;
	private LinkedList<Idea> ideas;
	private MTRoundRectangle mtRoundRectangle;
	private ClusterKeyboard keyboard;
	
	public ClusteringScene( MTApplication mtApplication, String name) {
		super(mtApplication, name);
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		//setzt Hintergrundfarbe
		this.setClearColor(new MTColor(208, 224, 235, 255));
		
		mtRoundRectangle = new MTRoundRectangle(this.mtApp, 30, 60, 0, 200, 60, 12, 12);
		
		mtRoundRectangle.setPositionGlobal(new Vector3D(mtApp.getWidth()/2, 180));
		ideas = Idea.getAllIdeas();

		Vector3D ideapos = new Vector3D(mtApp.getWidth()/2, 250);
		for (Idea idea : ideas) {
			idea.setVisible(false);
			canv.addChild(idea);
			idea.setPositionGlobal(ideapos);
			idea.updateCanvas(canv);
		}
		
		//Titel
		MTTextArea titel = new MTTextArea(mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		40, MTColor.BLACK));
		titel.unregisterAllInputProcessors();
		titel.setPickable(false);
		titel.setNoFill(true);
		titel.setNoStroke(true);
		titel.setText("Clustering der Ideen");
		canv.addChild(titel);
		titel.setPositionRelativeToParent(new Vector3D(mtApp.width/2, 80));
		
		//ideen stack
		final MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE));
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(ideaSizeWithoutCategory() + " Ideen uebrig");
		
		
		mtRoundRectangle.unregisterAllInputProcessors();
		if (ideaSizeWithoutCategory() != 0 ) {
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
							
							
							if (count < ideaSizeWithoutCategory()) {
								ideas.get(count++).setVisible(true);
								rText.setText(ideaSizeWithoutCategory()-count + " Ideen uebrig");
							}
							
						}
						
						if (ideaSizeWithoutCategory()-count != 0) {
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
		makeKB();
		makeKBButton();
	}
	
	private void makeKBButton() {
		PImage keyboardImg = mtApp.loadImage("advanced" + MTApplication.separator + "flickrMT"+ MTApplication.separator + "data"+ MTApplication.separator 
				+ "keyb128.png");
		final MTImageButton keyboardButton = new MTImageButton(mtApp, keyboardImg);
		keyboardButton.setFillColor(new MTColor(255,255,255,200));
		keyboardButton.setName("KeyboardButton");
		keyboardButton.setNoStroke(true);
		keyboardButton.translateGlobal(new Vector3D(-2,mtApp.height-keyboardButton.getWidthXY(TransformSpace.GLOBAL)+2,0));
		canv.addChild(keyboardButton);
//		keyboardButton.scale(0.8f, 0.8f, 0, keyboardButton.getCenterPointRelativeToParent());
		
		keyboardButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {
				
				switch (ae.getID()) {
				case TapEvent.TAPPED:
					keyboard.setVisible(true);
					break;

				default:
					break;
				}
				
			}
		});
		
		
	}
	
	private void makeKB() {
		keyboard = new ClusterKeyboard(mtApp, 40);
		
		final MTTextArea t = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial.ttf", 32, MTColor.WHITE)); 
        t.setExpandDirection(ExpandDirection.UP);
        t.setFillColor(new MTColor(139,69,0,255));
        t.setStrokeColor(new MTColor(205,133,0,255));
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true);
		
		keyboard.addChild(t);
		t.setPositionRelativeToParent(new Vector3D(40, -t.getHeightXY(TransformSpace.LOCAL)*0.5f));
		keyboard.addTextInputListener(t);
		
		// eigener enter button
		KeyInfo ki = keyboard.new KeyInfo("f", "\n", "\n", 		new Vector3D(615, 105),KeyInfo.NORMAL_KEY);
			
		//Event listener f�r den enter key
		IGestureEventListener tp = new IGestureEventListener() {
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped() && t.getText().length() != 0){

						IdeaCategory cat = new IdeaCategory(mtApp, canv);
						cat.setText(t.getText());
						canv.addChild(cat);
						cat.setPositionRelativeToOther(t, new Vector3D(t.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2, -50));
						t.clear();
					}
					return false;
		
				}
		};
		
		keyboard.addKeyFromOutside(ki, tp);
		
		canv.addChild(keyboard);
		keyboard.setVisible(false);
//		keyboard.setPickable(false); // nicht verwenden
		keyboard.setGestureAllowance(DragProcessor.class, false);
		keyboard.setGestureAllowance(ScaleProcessor.class, false);
		keyboard.setGestureAllowance(RotateProcessor.class, false);
		keyboard.scale(0.7f, 0.7f, 1, keyboard.getCenterPointRelativeToParent());
		keyboard.setPositionRelativeToParent(new Vector3D(mtApp.getWidth()/2, mtApp.getHeight()-keyboard.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2));
		
	}
	
	
	//create right menubar, um mit der n�chsten Scene zu beginnen
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
		
		MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE));
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("Ideen bewerten");
		
		mtRoundRectangle.scale(1.2f, 1.2f, 1, mtRoundRectangle.getCenterPointRelativeToParent());
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		
		this.canv.addChild(mtRoundRectangle);	
	}
	
	private int ideaSizeWithoutCategory() {
		int count = 0;
		for (int i = 0; i < ideas.size(); i++) {
			if (!(ideas.get(i) instanceof IdeaCategory)) {
				count++;
			}
		}
		
		return count;
	}
	
}
