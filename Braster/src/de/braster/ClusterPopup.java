package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class ClusterPopup  extends MTRectangle{
	
	private MTApplication mtApp;
	
	@SuppressWarnings("deprecation")
	public ClusterPopup(MTApplication pApplet, float width, float height, Cluster cluster) {
		super(pApplet, width, height);
		
		mtApp = pApplet;
		this.setFillColor(MTColor.WHITE);
		this.setStrokeColor(MTColor.BLACK);
		this.setSizeLocal(250, cluster.getNotes().size()*34+40);
		this.setPickable(false);
		
		float x = 5;
		float y = 5;
		
		MTTextArea textareaClustername = new MTTextArea(pApplet);
		textareaClustername.setText(cluster.getName());	
		textareaClustername.setFillColor(new MTColor(139,69,0,255));
		textareaClustername.setStrokeColor(new MTColor(205,133,0,255));
		width = textareaClustername.getWidthXYVectLocal().length();
		textareaClustername.unregisterAllInputProcessors();
		textareaClustername.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 14, MTColor.WHITE, true));
		textareaClustername.setPickable(false);	
		textareaClustername.setPositionRelativeToParent(new Vector3D(x+width/2,20));
		this.addChild(textareaClustername);
		
		MTLine line = new MTLine(mtApp, 5, 40,245, 40);  
		line.setFillColor(MTColor.BLACK);
		line.setStrokeColor(MTColor.BLACK);
		line.setStrokeWeight(1);
		line.setPickable(false);
		this.addChild(line);
		
		y= 57;
		for(Note actualNote : cluster.getNotes())
		{
			MTTextArea textareaIdee = new MTTextArea(pApplet);
			textareaIdee.setText(actualNote.getName());	
			textareaIdee.setFillColor(MTColor.GREEN);
			textareaIdee.setStrokeColor(MTColor.LIME);
			width = textareaIdee.getWidthXYVectLocal().length();
			textareaIdee.unregisterAllInputProcessors();
			textareaIdee.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 14, MTColor.WHITE, true));
			textareaIdee.setPickable(false);
			textareaIdee.setPositionRelativeToParent(new Vector3D(x+width/2,y));
			this.addChild(textareaIdee);
			//ideen können gedragt werden> event adden
			//wann geht popup zu? wenn wieder auf cluster oben geklickt wird
			y+=34;
		}		
		
	}

}
