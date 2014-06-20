package de.braster;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;
import de.braster.BWKeyboard.KeyInfo;


public class BrainWritingScene extends AbstractScene{

	private static final int INPUT_SIZE = 40;
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
	private int players;
	private List<MTEllipse> readyButtons = new LinkedList<MTEllipse>();
	private MTColor green1= new MTColor(0,100,0, 255);
	private MTColor green2 = new MTColor(34, 139, 34, 255);
	
	public BrainWritingScene(MTApplication mtApplication, String name, String problem, final int players) {
		super(mtApplication, name);
		this.mtApp = mtApplication;
		this.players = players;
		canv = getCanvas();
		this.setClearColor(new MTColor(136, 171, 194, 255));
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		MTTextArea problemTextArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		40, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		
		problemTextArea.setNoFill(true);
		problemTextArea.setNoStroke(true);
		
		problemTextArea.setText(problem);
		problemTextArea.setGestureAllowance(DragProcessor.class, false);
		problemTextArea.registerInputProcessor(new TapProcessor(mtApplication, 25, true, 350));
		problemTextArea.addGestureListener(TapProcessor.class, new IGestureEventListener() {
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
		this.getCanvas().addChild(problemTextArea);
		
		problemTextArea.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height*0.2f));
		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
	    
		
		PImage texture = getMTApplication().loadImage(path + "tastatur_test.png");
		
		
		MTRectangle test = new MTRectangle(mtApplication, texture);
		
		test.setPositionGlobal(new Vector3D(mtApplication.getWidth()/2, mtApplication.getHeight()*0.8f, 0));
		canv.addChild(test);
		//Keyboards erstellen und positionieren
		kb1 = makeKB();
//		BWKeyboard kb2 = new BWKeyboard(mtApplication); //tmp
		kb2 = makeKB();
		kb3 = makeKB();
		kb4 = makeKB();
		

		final BWIdeaView iv1 = new BWIdeaView(mtApplication, kb1);
		final BWIdeaView iv2 = new BWIdeaView(mtApplication, kb2);
		final BWIdeaView iv3 = new BWIdeaView(mtApplication, kb3);
		final BWIdeaView iv4 = new BWIdeaView(mtApplication, kb4);
		

		kb1.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		kb2.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		kb3.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		kb4.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		
		iv1.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		iv2.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		iv3.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		iv4.scale(0.7f, 0.7f, 1, new Vector3D(0, 0, 0));
		

		
		keyboardPositionRU = new Vector3D(mtApplication.width-(kb1.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2),
				mtApplication.height-(kb1.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f),
				0);

		keyboardPositionLU = new Vector3D((kb2.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)/2),
				mtApplication.height-(kb2.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f),
				0);
		
		keyboardPositionLO = new Vector3D((kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2),
				(mtApplication.height/2f)-(kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f),
				0);
		
		keyboardPositionRO = new Vector3D(mtApplication.width-kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2,
				mtApplication.height/2f-(kb3.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f),
				0);
		
		keyboardPositionMiddle = new Vector3D(mtApplication.width/2f,
				mtApplication.height-(kb1.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f),
				0);

		
		kb1.setPositionGlobal(keyboardPositionRU);
		kb2.setPositionGlobal(keyboardPositionLU);
		kb3.setPositionGlobal(keyboardPositionLO);
		kb4.setPositionGlobal(keyboardPositionRO);
		
		kb3.rotateZGlobal(keyboardPositionLO, 90);
		kb4.rotateZGlobal(keyboardPositionRO, 270);

		//Keyboards Ende //
		
		

		
		//canv.addChild(test);
		
		

		
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
		
//		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
//		PImage bg = mtApplication.loadImage(path + "BW_blur_1.png");
//		final MTRectangle help = new MTRectangle(mtApplication, bg);
//
//		MTColor ideaFillColor = new MTColor(0, 100, 0, 255);
//		MTColor ideaStrokeColor = new MTColor(34, 139, 34, 255);
//		MTColor ideaTextColor = MTColor.WHITE;
//		MTColor ideaFlashColor = new MTColor(220,220,220,255); 
//		MTColor ideaHoverOverColor = new MTColor(30,144,255, 255);
//		
//		MTTextArea h1 = new MTTextArea(mtApplication);
//		h1.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, ideaTextColor, true));
//		h1.setText("Idee 1");
//		h1.setStrokeColor(ideaStrokeColor);
//		h1.setFillColor(ideaFillColor);
//		h1.setPickable(false);
//		h1.setPositionRelativeToParent(new Vector3D(help.getCenterPointLocal().x-200,help.getCenterPointLocal().y));
//		help.addChild(h1);
//		
//		final MTTextArea h2 = new MTTextArea(mtApplication);
//		h2.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, ideaTextColor, true));
//		h2.setText("Idee 2");
//		h2.setStrokeColor(ideaStrokeColor);
//		h2.setFillColor(ideaFillColor);
//		h2.setPickable(false);
//		h2.setPositionRelativeToParent(help.getCenterPointLocal());
//		help.addChild(h2);
//		
//		MultiPurposeInterpolator moveOver = new MultiPurposeInterpolator(0, 200, 2000, 0, 1, -1);
//		
//		Animation anim1 = new Animation("Idee verschwinden lassen", moveOver, h2);
//		anim1.addAnimationListener(new IAnimationListener() {
//			
//			@Override
//			public void processAnimationEvent(AnimationEvent ae) {
//				
//				switch (ae.getId()) {
//				case AnimationEvent.ANIMATION_STARTED:
//					
//				case AnimationEvent.ANIMATION_UPDATED:
//					h2.setPositionRelativeToParent(new Vector3D(help.getCenterPointRelativeToParent().x-ae.getValue(), help.getCenterPointRelativeToParent().y));
//					break;
//				case AnimationEvent.ANIMATION_ENDED:
////					t.setWidthXYRelativeToParent(width);
//					h2.setPositionRelativeToParent(help.getCenterPointLocal());
//					break;	
//				default:
//					break;
//				}//switch
//			}
//		});
//		anim1.start();
//		//hilfe verschwinden lassen
//		help.addGestureListener(TapProcessor.class, new IGestureEventListener() {
//			
//			@Override
//			public boolean processGestureEvent(MTGestureEvent ge) {
//				TapEvent te = (TapEvent)ge;
//				if (te.isTapped()) {
//					help.setVisible(false);
//				}
//				return false;
//			}
//		});
//		canv.addChild(help);
	
	
//		MTRectangle test = new MTRectangle(mtApplication, mtApplication.getWidth(), mtApplication.getHeight());		
//		test.setFillColor(new MTColor(30,144,255, 200));
//		test.setPickable(false);
//		canv.addChild(test);
	
	}

	private boolean checkReady(int players) {
		int count = 0;
		for (MTEllipse ellipse : readyButtons) {
			if (ellipse.getFillColor().equals(green1)) {
				count++;
				System.out.println(count);
			}
		}
		
		return  count == players ? true : false;
	}

	public BWKeyboard makeKB() {
		
		final BWKeyboard keyboard = new BWKeyboard(mtApp, INPUT_SIZE);
		
        final MTTextArea t = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial.ttf", 32, MTColor.WHITE)); 
        t.setExpandDirection(ExpandDirection.UP);
//		t.setStrokeColor(new MTColor(0,0 , 0, 255));
		t.setFillColor(green1);
        t.setStrokeColor(green2);
//        t.setFillColor(MTColor.GREEN);
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true);
//		t.snapToKeyboard(keyb);
		keyboard.snapToKeyboard(t);
		keyboard.addTextInputListener(t);
		
		// eigener enter button
		KeyInfo ki = keyboard.new KeyInfo("f", "\n", "\n", 		new Vector3D(615, 105),KeyInfo.NORMAL_KEY);
			
		//Event listener f�r den enter key
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
			    		keyboard.resetSize(INPUT_SIZE);
			    		t.clear();
			    		
			    		System.out.print(idea.getChildCount());
					
					}
					return false;
				}
			};
			
				
		keyboard.addKeyFromOutside(ki, tp);

		//ready button
		final MTTextArea bereit = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial.ttf", 20, MTColor.BLACK)); 
		bereit.setText("Bereit");
		bereit.setNoFill(true);
		bereit.setNoStroke(true);
		
		
		float radius = 35;
		final MTEllipse circle = new MTEllipse(mtApp, 
				new Vector3D(keyboard.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-radius*1.2f, keyboard.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)-radius*1.2f, 0),
				radius, 
				radius);
		circle.setStrokeColor(MTColor.GRAY);
		circle.setFillColor(MTColor.WHITE);
		circle.setGestureAllowance(DragProcessor.class, false);
		circle.setGestureAllowance(ScaleProcessor.class, false);
		readyButtons.add(circle); //f�r das �berpr�fen aller farben
		
		
		
		
		circle.registerInputProcessor(new TapProcessor(mtApp, 25, true, 350));
		circle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					if (circle.getFillColor().equals(MTColor.WHITE)) {
						circle.setFillColor(green1); //bereit
						bereit.setFontColor(MTColor.WHITE);
						
					} else {
						circle.setFillColor(MTColor.WHITE);
						bereit.setFontColor(MTColor.BLACK);
					}
					
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
		
		

		
		circle.addChild(bereit);
		bereit.setPositionRelativeToParent(circle.getCenterPointLocal());
		bereit.setPickable(false);
		keyboard.addChild(circle);
		
		
		getCanvas().addChild(keyboard);
		
		
		keyboard.removeAllGestureEventListeners(DragProcessor.class);
		keyboard.removeAllGestureEventListeners(ScaleProcessor.class);
		keyboard.removeAllGestureEventListeners(RotateProcessor.class);
		

		return keyboard;
	}
	
}
