package de.braster;

import java.util.LinkedList;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class HelpSteps extends MTRoundRectangle{
	
	LinkedList<MTEllipse> points = new LinkedList<MTEllipse>();
	HelpOnScene caller;
	
	public HelpSteps(PApplet pApplet, float x, float y, float z, float width,
			float height, float arcWidth, float arcHeight,int schrittanzahl, HelpOnScene call) {
		super(pApplet, x, y, z, width, height,arcWidth , arcHeight);
		
		caller = call;
		
		//soll so kreise drauf haben, die anklickbar sind == ellippsen
		//anzahl abhängig vom eingabeparameter
		//größe variert dann
		this.setFillColor(new MTColor(73, 112, 138, 255));
		this.setStrokeColor(new MTColor(73, 112, 138, 255));
		this.setSizeLocal(schrittanzahl*20+10, 30);
		this.setPickable(false);
		
		
		for(int i=0;i<schrittanzahl;i++)
		{
			final MTEllipse circle = new MTEllipse(pApplet, new Vector3D(15+i*20,15),5,5);
			circle.setFillColor(MTColor.BLACK);
			circle.setStrokeColor(MTColor.BLACK);
			circle.setPickable(true);
			this.addChild(circle);
			points.add(circle);
			final int i1 = points.size()-1;
			circle.unregisterAllInputProcessors();
			circle.registerInputProcessor(new TapProcessor(pApplet));
			circle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()) 
					{		
						highlightPoint(i1);
						caller.setStep(i1);
					}
					return false;
				}
			});
		}
		
		highlightPoint(0);
	}
	
	
	public void highlightPoint(int i) {
		MTEllipse point = points.get(i);
		for(MTComponent child :getChildren())
		{
			MTEllipse childcircle = (MTEllipse) child;
			childcircle.setFillColor(MTColor.BLACK);
		}
		point.setFillColor(MTColor.LIME);
	}
	
}
