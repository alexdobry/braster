package de.braster;

import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;

import processing.core.PApplet;
import processing.core.PImage;

public class ClusterPopup  extends MTRectangle{
	
	public ClusterPopup(PApplet pApplet, float width, float height, Cluster cluster) {
		super(pApplet, width, height);
		
		MTTextArea textareaClustername = new MTTextArea(pApplet);
		
	}

}
