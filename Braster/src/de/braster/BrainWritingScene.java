package de.braster;
import java.awt.event.KeyEvent;
import java.util.LinkedList;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import de.braster.BWKeyboard.KeyInfo;


public class BrainWritingScene extends AbstractScene{

	MTCanvas canv;
	final MTApplication mtApp;
	
	BWKeyboard kb1;
	BWKeyboard kb2;
	BWKeyboard kb3;
	BWKeyboard kb4;
	
	Vector3D keyboardPositionRU;
	Vector3D keyboardPositionLU;
	Vector3D keyboardPositionRO;
	Vector3D keyboardPositionLO;
	
	public BrainWritingScene(MTApplication mtApplication, String name, String problem, int players) {
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
		
		textArea.registerInputProcessor(new TapProcessor(mtApplication, 25, true, 350));
		textArea.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isDoubleTap()){
					
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
		
		
		kb1.setPositionGlobal(keyboardPositionRU);
		kb2.setPositionGlobal(keyboardPositionLU);
		kb3.setPositionGlobal(keyboardPositionLO);
		kb4.setPositionGlobal(keyboardPositionRO);
		
		kb3.rotateZGlobal(keyboardPositionLO, 90);
		kb4.rotateZGlobal(keyboardPositionRO, 270);

//		canv.addChild(kb2); //temp
		//Keyboards Ende //
		
		//Objektbereiche definieren
		
		MTComponent field = new MTComponent(mtApplication);
		MTTextArea test = new MTTextArea(mtApplication);
		test.setFillColor(new MTColor(255,255,255, 255));
		test.setSizeXYGlobal(mtApplication.width*0.7f, mtApplication.height*0.7f);
		test.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height, 0));
		
		//canv.addChild(test);
		
		
		BWIdeaView iv1 = new BWIdeaView(mtApplication, kb1.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb1.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb1);
		BWIdeaView iv2 = new BWIdeaView(mtApplication, kb2.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb2.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb2);
		BWIdeaView iv3 = new BWIdeaView(mtApplication, kb3.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb3);
		BWIdeaView iv4 = new BWIdeaView(mtApplication, kb4.getWidthXY(TransformSpace.RELATIVE_TO_PARENT), kb4.getHeightXY(TransformSpace.RELATIVE_TO_PARENT), kb4);
		
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
		
		
		/////////////////////////
		
//		final MTRectangle rect = new MTRectangle(mtApplication,0, 0, 100, 100);
//        rect.unregisterAllInputProcessors();
//        rect.removeAllGestureEventListeners();
//        getCanvas().addChild(rect);
//
//        rect.registerInputProcessor(new DragProcessor(mtApplication));
//        rect.addGestureListener(DragProcessor.class, new DefaultDragAction());
//        
//        rect.registerInputProcessor(new TapProcessor(mtApplication));
//        rect.addGestureListener(TapProcessor.class, new IGestureEventListener() {
//              @Override
//              public boolean processGestureEvent(MTGestureEvent ge) {
//                    TapEvent te = (TapEvent)ge;
//                    if(te.getId() == TapEvent.GESTURE_STARTED){
//                          float r = (float)(Math.random()*255);
//                          float g = (float)(Math.random()*255);
//                          float b = (float)(Math.random()*255);
//                          rect.setFillColor(new MTColor(r, g,b ));
//                    }
//
//                    return false;
//              }
//        }); 
//        
//		MTEllipse ellipse = new MTEllipse(mtApplication, new Vector3D(mtApplication.width/2, mtApplication.height/2), 30, 30);
//		 ellipse.unregisterAllInputProcessors();
//		 ellipse.removeAllGestureEventListeners();
//		 ellipse.registerInputProcessor(new FlickProcessor());
//		 ellipse.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
//		@Override
//		public boolean processGestureEvent(MTGestureEvent ge) {
//		FlickEvent fe = (FlickEvent)ge;
//		int x = 0;
//		int y = 0;
//		if(fe.getId() == FlickEvent.GESTURE_ENDED){
//		         switch (fe.getDirection()) {
//		case EAST:
//		x = 10;
//		break;
//		case WEST:
//		x = -10;
//		break;
//		case NORTH:
//		y = -10;
//		break;
//		case NORTH_WEST:
//		x = -10;
//		y = -10;
//		break;
//		case NORTH_EAST:
//		x = 10;
//		y = -10;
//		break;
//		case SOUTH:
//		y = 10;
//		break;
//		case SOUTH_WEST:
//		x = -10;
//		y = 10;
//		break;
//		case SOUTH_EAST:
//		x = 10;
//		         y = 10;
//		break;
//		default:
//		break;
//		}
//		rect.translate(new Vector3D(x, y));
//		}
//		return false;
//		}
//		});
//		getCanvas().addChild(ellipse);
		
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
			    		Idea textArea = new Idea(getMTApplication(), canv);
			    		Idea textArea2 = new Idea(getMTApplication(), canv);
			    		Idea textArea3 = new Idea(getMTApplication(), canv);
			    		
			    		textArea.setText(t.getText());
			    		textArea2.setText(t.getText()+t.getText());
			    		textArea3.setText(t.getText()+t.getText()+t.getText());
			    		
			    		textArea.setName(t.getText());
			    		textArea2.setName(t.getText()+t.getText());
			    		textArea3.setName(t.getText()+t.getText()+t.getText());
			    		
//			    		canv.addChild(textArea);
//			    		canv.addChild(textArea2);
//			    		canv.addChild(textArea3);
//			    		textArea.snapToIdea(textArea2);
//			    		textArea.snapToIdea(textArea3);
			    		t.clear();
			    		
			    		System.out.print(textArea.getChildCount());
					
					}
					return false;
				}
			};
			
		keyboard.addKeyFromOutside(ki, tp);

		
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
			
			ideaArea.removeAllGestureEventListeners();
			ideaArea.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
			IGestureEventListener gl = new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						
						fillIdeaArea();
					}
					return false;
				}
			};
			this.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
			this.addGestureListener(TapProcessor.class, gl);
			ideaArea.addGestureListener(TapProcessor.class, gl);
			
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

			
			float radius = 20;
//			MTEllipse circle = new MTEllipse(pApplet, new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-radius,this.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-radius,0), radius, radius);
//			circle.setStrokeColor(MTColor.LIME);
//			circle.setGestureAllowance(DragProcessor.class, false);
//			circle.setGestureAllowance(ScaleProcessor.class, false);
			
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
			this.addChild(addButton);
//			this.addChild(circle);
			
		}

		
		public void fillIdeaArea() {
			Idea i = null;
			if (ideas.size() > 0) {
				i = ideas.get(iterator);
				
				if (ideas.get(iterator) == ideas.getLast()) {
					iterator = 0;
				} else {
					iterator++;
				}
				ideaArea.setText(i.getText());
			}
			
			ideaArea.setPositionRelativeToParent(getCenterPointLocal());
		}
	}
	
	
	
}
