package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.util.MTColor;

public class IdeaCategory extends Idea {
	private static MTColor ideaFillColor = new MTColor(139,69,0,255);
	private static MTColor ideaStrokeColor = new MTColor(205,133,0,255);
	private static MTColor ideaTextColor = MTColor.WHITE;
	/**
	 * Farbänderung bei Berührung durch touch Geste
	 */
	private static MTColor ideaFlashColor = new MTColor(220,220,220,255); 
	private static MTColor ideaHoverOverColor = new MTColor(30,144,255, 255);
	
	public IdeaCategory(MTApplication pApplet, MTCanvas canv) {
		super(pApplet, canv, ideaFillColor, ideaStrokeColor, ideaTextColor, ideaFlashColor, ideaHoverOverColor);
		
		
	}

}
