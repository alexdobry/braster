package de.braster;

import java.util.LinkedList;

import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class HelpOnScene extends MTRectangle {

	
	private LinkedList<PImage> stepPics;
	private int activeStep = 0;
	private HelpSteps steps;
	private MTRoundRectangle weiter;
	
	public HelpOnScene(PApplet pApplet, float width, float height, LinkedList<PImage> stepPictures, float heightFactor) {
		super(pApplet, width, height);
		stepPics = stepPictures;
//		final MTRectangle test = new MTRectangle(pApplet, pApplet.getWidth(), pApplet.getHeight());
//		
//		test.setFillColor(new MTColor(136, 171, 194, 200));
//		test.setPickable(false);
////		canv.addChild(test);
//		scene.getCanvas().addChild(test);
		setTexture(stepPictures.get(activeStep));
		removeAllGestureEventListeners();
		steps = new HelpSteps(pApplet, 0, 0, 0, 0, 0, 6, 6, stepPics.size(), this);
		steps.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent()*0.5f, getHeightXYRelativeToParent()*heightFactor));
		
		addChild(steps);
		registerInputProcessor(new FlickProcessor(300,2));
		addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent fe = (FlickEvent)ge;
				
				switch (fe.getId()) {
				case FlickEvent.GESTURE_STARTED:
									
					break;
				case FlickEvent.GESTURE_UPDATED:
					
					break;
				case FlickEvent.GESTURE_ENDED:
						if (fe.getDirection() == FlickDirection.WEST) {
							nextPic();
						}
						
						if (fe.getDirection() == FlickDirection.EAST) {
							previousPic();
						}
					break;

				default:
					break;
				}
					
				return false;
			}
		});
		
		
		
		weiter = new MTRoundRectangle(pApplet, pApplet.getWidth()/2, pApplet.getHeight()*0.8f, 0, 150, 40, 12, 12);
		weiter.setPositionRelativeToOther(steps, new Vector3D(300, steps.getHeightXY(TransformSpace.RELATIVE_TO_PARENT)/2f, 0));
		weiter.unregisterAllInputProcessors();
		weiter.setFillColor(MTColor.GRAY);
		MTTextArea rText = new MTTextArea(pApplet, FontManager.getInstance().createFont(pApplet, "arial.ttf", 
        		18, //fontzize 
        		MTColor.BLACK));
		rText.unregisterAllInputProcessors();
		rText.setNoFill(true);
		rText.setPickable(false);
		rText.setNoStroke(true);
		rText.setText("Weiter");
		weiter.addChild(rText);
		rText.setPositionRelativeToParent(weiter.getCenterPointLocal());
		weiter.setNoStroke(true);
		weiter.registerInputProcessor(new TapProcessor(pApplet));
		weiter.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(weiter));
		weiter.setVisible(false);
		addChild(weiter);
		
		weiter.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()) {
//					test.destroy();
					destroy();
				}
				return false;
			}
		});
	}
	
	private void nextPic() {
		if (activeStep<stepPics.size()-1) {
			++activeStep;
			setStep(activeStep);
		} 
	}
	
	private void previousPic() {
		if (activeStep > 0) {
			--activeStep;
			setStep(activeStep);
		}
	}
	
	public void setStep(int step) {
		activeStep = step;
		if (activeStep == stepPics.size()-1) {
			weiter.setVisible(true);
		} else {
			weiter.setVisible(false);
		}
		setTexture(stepPics.get(activeStep));
		steps.highlightPoint(activeStep);
		
	}
	
}
