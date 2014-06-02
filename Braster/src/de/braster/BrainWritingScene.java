package de.braster;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import de.braster.BWKeyboard.KeyInfo;


public class BrainWritingScene extends AbstractScene{

	private MTCanvas canv;
	final MTApplication mtApp;
	
	private BWKeyboard kb1;
	private BWKeyboard kb2;
	private BWKeyboard kb3;
	private BWKeyboard kb4;
	
	private Vector3D keyboardPositionRU;
	private Vector3D keyboardPositionLU;
	private Vector3D keyboardPositionRO;
	private Vector3D keyboardPositionLO;
	private Vector3D keyboardPositionMiddle;
	
	private List<MTEllipse> readyButtons = new LinkedList<MTEllipse>();
	
	public BrainWritingScene(MTApplication mtApplication, String name, String problem, final int players) {
		super(mtApplication, name);
		this.mtApp = mtApplication;
		canv = getCanvas();
		this.setClearColor(MTColor.BLACK);
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		
		textArea.setText(problem);
		textArea.setGestureAllowance(DragProcessor.class, false);
		textArea.registerInputProcessor(new TapProcessor(mtApplication, 25, true, 350));
		textArea.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isDoubleTap()){
					if (checkReady(players)) {
						
					
						Iscene clusteringIscene = null;
						mtApp.pushScene();
						if (clusteringIscene == null){
							clusteringIscene = new ClusteringScene(mtApp, "Clustering");
							//Konstruktor erweitern um Anzahl Spieler, da genau soviele
							//Tastaturen geladen werden
						//Add the scene to the mt application
						mtApp.addScene(clusteringIscene);
						}
						//Do the scene change
						mtApp.changeScene(clusteringIscene);
					}
				}
				return false;
			}
		});
		this.getCanvas().addChild(textArea);
		
		textArea.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		
		
		
		//Keyboards erstellen und positionieren
		kb1 = makeKB();
//		BWKeyboard kb2 = new BWKeyboard(mtApplication); //tmp
		kb2 = makeKB();
		kb3 = makeKB();
		kb4 = makeKB();
		
		keyboardPositionRU = new Vector3D(mtApplication.width/2f+(kb1.getWidthXY(TransformSpace.LOCAL)/2),
				mtApplication.height-(kb1.getHeightXY(TransformSpace.LOCAL)/2f),
				0);

		keyboardPositionLU = new Vector3D(mtApplication.width/2f-(kb2.getWidthXY(TransformSpace.LOCAL)/2),
				mtApplication.height-(kb2.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		keyboardPositionLO = new Vector3D(mtApplication.width/2f-(kb3.getWidthXY(TransformSpace.LOCAL)-(kb3.getHeightXY(TransformSpace.LOCAL)/2f)),
				(mtApplication.height/2f)-(kb3.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		keyboardPositionRO = new Vector3D(mtApplication.width/2f+kb3.getWidthXY(TransformSpace.LOCAL)-kb3.getHeightXY(TransformSpace.LOCAL)/2f,
				mtApplication.height/2f-(kb3.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		keyboardPositionMiddle = new Vector3D(mtApplication.width/2f,
				mtApplication.height-(kb1.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		kb1.setPositionGlobal(keyboardPositionRU);
		kb2.setPositionGlobal(keyboardPositionLU);
		kb3.setPositionGlobal(keyboardPositionLO);
		kb4.setPositionGlobal(keyboardPositionRO);
		
		kb3.rotateZGlobal(keyboardPositionLO, 90);
		kb4.rotateZGlobal(keyboardPositionRO, 270);

		//Keyboards Ende //
		
		

		
		//canv.addChild(test);
		
		
		final BWIdeaView iv1 = new BWIdeaView(mtApplication, kb1.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb1.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb1);
		final BWIdeaView iv2 = new BWIdeaView(mtApplication, kb2.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb2.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb2);
		final BWIdeaView iv3 = new BWIdeaView(mtApplication, kb3.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb3);
		final BWIdeaView iv4 = new BWIdeaView(mtApplication, kb4.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb4.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb4);
		

		
		iv1.registerInputProcessor(new FlickProcessor());
		iv1.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent)ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED)
					if (e.getDirection() == FlickDirection.WEST) {
						iv1.fillIdeaArea(1);
					}
					if (e.getDirection() == FlickDirection.EAST) {
						iv1.fillIdeaArea(-1);
					}
				return false;
			}
		});
		
		
		iv2.registerInputProcessor(new FlickProcessor());
		iv2.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent)ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED)
					if (e.getDirection() == FlickDirection.WEST) {
						iv2.fillIdeaArea(1);
					}
					if (e.getDirection() == FlickDirection.EAST) {
						iv2.fillIdeaArea(-1);
					}
				return false;
			}
		});
		
		iv3.registerInputProcessor(new FlickProcessor());
		iv3.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent)ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED)
					if (e.getDirection() == FlickDirection.NORTH) {
						iv3.fillIdeaArea(1);
					}
					if (e.getDirection() == FlickDirection.SOUTH) {
						iv3.fillIdeaArea(-1);
					}
				return false;
			}
		});
		
		iv4.registerInputProcessor(new FlickProcessor());
		iv4.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent)ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED)
					if (e.getDirection() == FlickDirection.SOUTH) {
						iv4.fillIdeaArea(1);
					}
					if (e.getDirection() == FlickDirection.NORTH) {
						iv4.fillIdeaArea(-1);
					}
				return false;
			}
		});

		
		canv.addChild(iv1);
		canv.addChild(iv2);
		canv.addChild(iv3);
		canv.addChild(iv4);
		iv1.setPositionGlobal(keyboardPositionRU);
		iv2.setPositionGlobal(keyboardPositionLU);
		iv3.setPositionGlobal(keyboardPositionLO);
		iv4.setPositionGlobal(keyboardPositionRO);
			
		iv3.rotateZGlobal(keyboardPositionLO, 90);
		iv4.rotateZGlobal(keyboardPositionRO, 270);
		
		iv1.setVisible(false);
		iv2.setVisible(false);
		iv3.setVisible(false);
		iv4.setVisible(false);
		
		kb1.setBWIV(iv1);
		kb2.setBWIV(iv2);
		kb3.setBWIV(iv3);
		kb4.setBWIV(iv4);
		
		
		switch (players) {
		case 1:
			kb1.setPositionGlobal(keyboardPositionMiddle);
			iv1.setPositionGlobal(keyboardPositionMiddle);
			kb1.setVisible(true);
			kb2.setVisible(false);
			kb3.setVisible(false);
			kb4.setVisible(false);
			break;
		case 2:
			kb1.setVisible(false);
			kb2.setVisible(false);
			kb3.setVisible(true);
			kb4.setVisible(true);
			
			break;
		case 3:
			kb1.setPositionGlobal(keyboardPositionMiddle);
			iv1.setPositionGlobal(keyboardPositionMiddle);
			kb1.setVisible(true);
			kb2.setVisible(false);
			kb3.setVisible(true);
			kb4.setVisible(true);
			
			break;
		case 4:
			kb1.setVisible(true);
			kb2.setVisible(true);
			kb3.setVisible(true);
			kb4.setVisible(true);
			
			break;

		default:
			break;
		}
		
		
	}

	private boolean checkReady(int players) {
		int count = 0;
		for (MTEllipse ellipse : readyButtons) {
			if (ellipse.getFillColor().equals(MTColor.GREEN)) {
				count++;
				System.out.println(count);
			}
		}
		
		return  count == players ? true : false;
	}

	public BWKeyboard makeKB() {
		
		BWKeyboard keyboard = new BWKeyboard(mtApp);
		
        final MTTextArea t = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial.ttf", 50, MTColor.BLACK)); 
        t.setExpandDirection(ExpandDirection.UP);
		t.setStrokeColor(new MTColor(0,0 , 0, 255));
		t.setFillColor(new MTColor(205,200,177, 255));
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true);
//		t.snapToKeyboard(keyb);
		keyboard.snapToKeyboard(t);
		keyboard.addTextInputListener(t);
		
		// eigener enter button
		KeyInfo ki = keyboard.new KeyInfo("f", "\n", "\n", 		new Vector3D(615, 105),KeyInfo.NORMAL_KEY);
			
		//Event listener für den enter key
		IGestureEventListener tp = new IGestureEventListener() {
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped() && t.getText().length() != 0){
//						MTTextArea textArea = new MTTextArea(getMTApplication(),                                
//			                    FontManager.getInstance().createFont(getMTApplication(), "arial.ttf", 
//			                    		50, //fontzize 
//			                    		new MTColor(255, 255, 255, 255))); //Font color
//			    		
//			    		textArea.setNoFill(true);
//			    		textArea.setNoStroke(true);
			    		Idea idea = new Idea(getMTApplication(), canv);
			    		idea.setText(t.getText());
			    		idea.setName(t.getText());
			    		
			    		t.clear();
			    		
			    		System.out.print(idea.getChildCount());
					
					}
					return false;
				}
			};
			
				
		keyboard.addKeyFromOutside(ki, tp);

		//ready button
		
		float radius = 35;
		final MTEllipse circle = new MTEllipse(mtApp, 
				new Vector3D(keyboard.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-radius*1.2f, keyboard.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-radius*1.2f, 0),
				radius, 
				radius);
		circle.setStrokeColor(MTColor.GRAY);
		circle.setFillColor(MTColor.WHITE);
		circle.setGestureAllowance(DragProcessor.class, false);
		circle.setGestureAllowance(ScaleProcessor.class, false);
		readyButtons.add(circle); //für das überprüfen aller farben
		
		circle.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
		circle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (circle.getFillColor().equals(MTColor.WHITE)) {
						circle.setFillColor(MTColor.GREEN);
					} else {
						circle.setFillColor(MTColor.WHITE);
					}
				}
				return false;
			}
		});
		
		
		keyboard.addChild(circle);
		
		
		getCanvas().addChild(keyboard);
		
		keyboard.scale(0.8f, 0.8f, 1, new Vector3D(0, 0, 0));
		keyboard.removeAllGestureEventListeners(DragProcessor.class);
		keyboard.removeAllGestureEventListeners(ScaleProcessor.class);
		keyboard.removeAllGestureEventListeners(RotateProcessor.class);
		

		return keyboard;
		
		

		
		
	}
	
	public class BWIdeaView extends MTRectangle{

		
		private LinkedList<Idea> ideas = Idea.getAllIdeas();
		
		private int iterator = 0;
		private MTTextArea ideaArea = null;
		
		public BWIdeaView(PApplet pApplet, float width, float height, final BWKeyboard kb) {
			super(pApplet, width, height);
			
			setFillColor(MTColor.GREY);
			setStrokeColor(MTColor.WHITE);
			
			ideaArea = new MTTextArea(pApplet);
			ideaArea.setPositionRelativeToParent(getCenterPointLocal());
			ideaArea.setStrokeColor(MTColor.WHITE);
			ideaArea.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.BLACK, true));
			ideaArea.removeAllGestureEventListeners();
//			ideaArea.setText("test");
			
			ideaArea.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
			IGestureEventListener gl = new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						
//						fillIdeaArea();
					}
					return false;
				}
			};
			
//			IGestureEventListener fl = new IGestureEventListener() {
//				
//				@Override
//				public boolean processGestureEvent(MTGestureEvent ge) {
//					FlickEvent e = (FlickEvent)ge;
//					if (e.getId() == MTGestureEvent.GESTURE_ENDED)
//						if (e.getDirection() == FlickDirection.WEST) {
//							fillIdeaArea();
//						}
//					return false;
//				}
//			};
//			
//			
////			this.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
//			this.registerInputProcessor(new FlickProcessor());
////			this.addGestureListener(TapProcessor.class, gl);
//			this.addGestureListener(FlickProcessor.class, fl);
////			ideaArea.addGestureListener(TapProcessor.class, gl);
//			ideaArea.addGestureListener(FlickProcessor.class, fl);
//			
			
			MTTextArea addButton = new MTTextArea(pApplet);
			addButton.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.GREEN, true));
			addButton.setPositionRelativeToOther(this, new Vector3D(this.getWidthXY(TransformSpace.LOCAL)*1/4,this.getHeightXY(TransformSpace.LOCAL)*4/5,0));
			addButton.setText("  ADD  ");
			//TODO: next/prev vieleicht durch "fling" gesture ersetzen
//			MTTextArea nextButton = new MTTextArea(pApplet);
//			nextButton.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.GREEN, true));
//			nextButton.setPositionRelativeToOther(this, new Vector3D(this.getWidthXY(TransformSpace.LOCAL)*3/4,this.getHeightXY(TransformSpace.LOCAL)*4/5,0));
//			nextButton.setText("next");
//			
//			nextButton.removeAllGestureEventListeners();
			addButton.removeAllGestureEventListeners();
			
			addButton.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
			addButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						kb.setVisible(true);
						setVisible(false);
					}
					return false;
				}
			});

			
			MTSvgButton keybCloseSvg = new MTSvgButton(pApplet, MT4jSettings.getInstance().getDefaultSVGPath()
					+ "keybClose.svg");
			//Transform
			keybCloseSvg.scale(0.8f, 0.8f, 1, new Vector3D(0,0,0));
			keybCloseSvg.scale(0.8f, 0.8f, 1, new Vector3D(0,0,0)); //2x ist notwendig
			
			keybCloseSvg.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-25, 25,0));
			keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			keybCloseSvg.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						kb.setVisible(true);
						setVisible(false);
					}
					return false;
				}
			});
			this.addChild(keybCloseSvg);
			
			
//			float radius = 20;
//			MTEllipse circle = new MTEllipse(pApplet, new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-radius,radius,0), radius, radius);
//			circle.setStrokeColor(MTColor.LIME);
//			circle.setGestureAllowance(DragProcessor.class, false);
//			circle.setGestureAllowance(ScaleProcessor.class, false);
//			
//			circle.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
//			circle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
//				
//				@Override
//				public boolean processGestureEvent(MTGestureEvent ge) {
//					TapEvent te = (TapEvent)ge;
//					if (te.isTapped()){
//						kb.setVisible(true);
//						setVisible(false);
//					}
//					return false;
//				}
//			});
			
			this.addChild(ideaArea);
//			this.addChild(nextButton);
//			this.addChild(addButton);
//			this.addChild(circle);
			
			setGestureAllowance(DragProcessor.class, false);
			
		}

		
		public void fillIdeaArea(int direction) {
			Idea i = null;
			if (ideas.size() > 0) {
					
				i = ideas.get(iterator);
				
				if (ideas.get(iterator) == ideas.getLast() && direction == 1) {
					iterator = 0;
				} else if (direction == 1) {
					iterator++;
				}
				
				if (ideas.get(iterator) == ideas.getFirst() && direction == -1) {
					iterator = ideas.size()-1;
				} else if (direction == -1) {
					iterator--;
				}
				
				ideaArea.setText(i.getText());
				
			}
			
			ideaArea.setPositionRelativeToParent(getCenterPointLocal());
		}
	}
	
	
	
}
