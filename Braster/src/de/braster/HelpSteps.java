package de.braster;

import java.util.ArrayList;

import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class HelpSteps extends MTRoundRectangle{

	public HelpSteps(PApplet pApplet, float x, float y, float z, float width,
			float height, float arcWidth, float arcHeight,int schrittanzahl) {
		super(pApplet, x, y, z, width, height,arcWidth , arcHeight);
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
			
			circle.registerInputProcessor(new TapProcessor(pApplet));
			circle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()) 
					{		
						for(MTComponent child :getChildren())
						{
							MTEllipse childcircle = (MTEllipse) child;
							childcircle.setFillColor(MTColor.BLACK);
						}
						circle.setFillColor(MTColor.LIME);
					}
					return false;
				}
			});
		}
	}
	

}
