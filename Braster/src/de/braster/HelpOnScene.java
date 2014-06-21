package de.braster;

import java.util.LinkedList;

import javafx.scene.input.SwipeEvent;

import org.mt4j.components.PickResult.PickEntry;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.util.math.Vector3D;

import com.sun.glass.events.SwipeGesture;

import processing.core.PApplet;
import processing.core.PImage;

public class HelpOnScene extends MTRectangle {

	
	private LinkedList<PImage> stepPics;
	private int activeStep = 0;
	private HelpSteps steps;
	
	public HelpOnScene(PApplet pApplet, float width, float height, LinkedList<PImage> stepPictures) {
		super(pApplet, width, height);
		stepPics = stepPictures;
		
		setTexture(stepPictures.get(activeStep));
		removeAllGestureEventListeners();
		steps = new HelpSteps(pApplet, 0, 0, 0, 0, 0, 6, 6, 4, this);
		steps.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent()*0.5f, getHeightXYRelativeToParent()*0.7f));
		
		addChild(steps);
		
		registerInputProcessor(new FlickProcessor());
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
		
	}

	
	
	private void nextPic() {
		if (activeStep<stepPics.size()-1) {
			setTexture(stepPics.get(++activeStep));
			steps.highlightPoint(activeStep);
		} 
	}
	
	private void previousPic() {
		if (activeStep > 0) {
			setTexture(stepPics.get(--activeStep));
			steps.highlightPoint(activeStep);
		}
	}
	
	public void setStep(int step) {
		setTexture(stepPics.get(step));
		steps.highlightPoint(step);
		activeStep = step;
	}
	
}
