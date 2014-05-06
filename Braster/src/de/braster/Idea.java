package de.braster;

import java.util.List;

import org.mt4j.AbstractMTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.PickResult;
import org.mt4j.components.PickResult.PickEntry;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import com.sun.glass.events.GestureEvent;

import processing.core.PApplet;

public class Idea extends MTTextArea {
	
	PApplet app;
	MTCanvas canv;
	Idea self;

	public Idea(PApplet pApplet, MTCanvas canv) {
		super(pApplet);
		this.canv=canv;
		app = pApplet;
		this.self = this;
		
		setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.WHITE, true));
		setStrokeColor(MTColor.LIME);
		setFillColor(MTColor.GREEN);
		
	
		addGestureListener(DragProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				
				switch (de.getId()) {
				case DragEvent.GESTURE_STARTED:
					break;
				case DragEvent.GESTURE_UPDATED:
					break;
				case DragEvent.GESTURE_ENDED:
					
					System.out.print("I was here\n");
					PickResult pr = pick(de.getDragCursor().getCurrentEvtPosX(),de.getDragCursor().getCurrentEvtPosY());
					
					List<PickEntry> pe = pr.getPickList();
					for (PickEntry pickEntry : pe) {
						System.out.print(pickEntry.hitObj.toString() + "\n");
					}
					
					
//					MTComponent comp = pr.getNearestPickResult();
//					if (comp instanceof Idea) {
//						self.snapToIdea((Idea)comp);
//					}
					
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	public void snapToIdea(final Idea idea) {
		
		
		List<MTComponent> list = getChildList();
		float height = 0f;
		
		for (MTComponent mtComponent : list) {
			if (mtComponent instanceof Idea) {
				height += ((Idea) mtComponent).getHeightXY(TransformSpace.LOCAL);
			}
		}
		
		
		this.addChild(idea);
		idea.setPositionRelativeToParent(new Vector3D(idea.getWidthXY(TransformSpace.LOCAL)/2f, getHeightXY(TransformSpace.LOCAL)*1.5f+height));
//		idea.removeAllGestureEventListeners();
		idea.setGestureAllowance(DragProcessor.class, false);
		
		idea.registerInputProcessor(new TapAndHoldProcessor((AbstractMTApplication) app, 1500));
		
		idea.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()){
						MTComponent parent = idea.getParent();
						if (parent instanceof Idea) {
							
							idea.removeFromParent();
							canv.addChild(idea);
							idea.setPositionGlobal(th.getLocationOnScreen());
							
							//TODO: logik für parent implementieren
							idea.setGestureAllowance(DragProcessor.class, true);
						}
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	

}
