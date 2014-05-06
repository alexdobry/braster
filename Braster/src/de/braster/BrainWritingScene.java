package de.braster;
import java.awt.event.KeyEvent;

import org.mt4j.AbstractMTApplication;
import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTTextKeyboard;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.input.inputProcessors.globalProcessors.CursorTracer;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;


public class BrainWritingScene extends AbstractScene{

	MTCanvas canv;
	
	public BrainWritingScene(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		canv = getCanvas();
		this.setClearColor(MTColor.BLACK);
		this.registerGlobalInputProcessor(new CursorTracer(mtApplication, this));
		
		MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		
		//Idea textArea =new Idea(mtApplication);
		
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		
		textArea.setText("Problem 1");
		this.getCanvas().addChild(textArea);
		
		textArea.setPositionGlobal(new Vector3D(mtApplication.width/2f, mtApplication.height/2f));
		
		MTKeyboard kb1 = makeKB(mtApplication);
		MTKeyboard kb2 = makeKB(mtApplication);
		MTKeyboard kb3 = makeKB(mtApplication);
		MTKeyboard kb4 = makeKB(mtApplication);
		
		Vector3D keyboardPositionRU = new Vector3D(mtApplication.width/2f+(kb1.getWidthXY(TransformSpace.LOCAL)/2),
				mtApplication.height-(kb1.getHeightXY(TransformSpace.LOCAL)/2f),
				0);

		Vector3D keyboardPositionLU = new Vector3D(mtApplication.width/2f-(kb2.getWidthXY(TransformSpace.LOCAL)/2),
				mtApplication.height-(kb2.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		Vector3D keyboardPositionLO = new Vector3D(mtApplication.width/2f-(kb3.getWidthXY(TransformSpace.LOCAL)-(kb3.getHeightXY(TransformSpace.LOCAL)/2f)),
				(mtApplication.height/2f)-(kb3.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		Vector3D keyboardPositionRO = new Vector3D(mtApplication.width/2f+kb3.getWidthXY(TransformSpace.LOCAL)-kb3.getHeightXY(TransformSpace.LOCAL)/2f,
				mtApplication.height/2f-(kb3.getHeightXY(TransformSpace.LOCAL)/2f),
				0);
		
		
		kb1.setPositionGlobal(keyboardPositionRU);
		kb2.setPositionGlobal(keyboardPositionLU);
		kb3.setPositionGlobal(keyboardPositionLO);
		kb4.setPositionGlobal(keyboardPositionRO);
		kb3.rotateZGlobal(keyboardPositionLO, 90);
		kb4.rotateZGlobal(keyboardPositionRO, 270);
//		MTTextKeyboard test = new MTTextKeyboard(mtApplication);
//		getCanvas().addChild(test);
	}

	public MTKeyboard makeKB(MTApplication mtApplication) {
		
		MTKeyboard keyboard = new MTKeyboard(mtApplication);
		
        final MTTextArea t = new MTTextArea(mtApplication, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 50, MTColor.BLACK)); 
        t.setExpandDirection(ExpandDirection.UP);
		t.setStrokeColor(new MTColor(0,0 , 0, 255));
		t.setFillColor(new MTColor(205,200,177, 255));
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true);
//		t.snapToKeyboard(keyb);
		keyboard.snapToKeyboard(t);
		keyboard.addTextInputListener(t);
		
		
		// extra enter button
		MTSvgButton keybAddTextButton = new MTSvgButton(mtApplication, MT4jSettings.getInstance().getDefaultSVGPath()
				+ "play.svg");
		//Transform
		keybAddTextButton.scale(1.2f, 1.2f, 1, new Vector3D(0,0,0));
		keybAddTextButton.translate(new Vector3D(600,150,0));
		keybAddTextButton.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
		keybAddTextButton.addGestureListener(TapProcessor.class, new IGestureEventListener() {
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
			    		textArea3.setText(t.getText());
			    		
			    		textArea.setName(t.getText());
			    		textArea2.setName(t.getText()+t.getText());
			    		textArea3.setName(t.getText());
			    		
			    		canv.addChild(textArea);
			    		canv.addChild(textArea2);
			    		canv.addChild(textArea3);
			    		textArea.snapToIdea(textArea2);
			    		textArea.snapToIdea(textArea3);
			    		t.clear();
			    		
			    		System.out.print(textArea.getChildCount());
					
					}
					return false;
				}
			});
		keyboard.addChild(keybAddTextButton);
		
		getCanvas().addChild(keyboard);
		
		keyboard.scale(0.8f, 0.8f, 1, new Vector3D(0, 0, 0));
		keyboard.removeAllGestureEventListeners(DragProcessor.class);
		keyboard.removeAllGestureEventListeners(ScaleProcessor.class);
		keyboard.removeAllGestureEventListeners(RotateProcessor.class);
		

		return keyboard;
		
		

		
		
	}
	
	
	
}
