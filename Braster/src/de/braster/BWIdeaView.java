package de.braster;

import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class BWIdeaView extends MTRectangle{

	public BWIdeaView(PApplet pApplet, float width, float height) {
		super(pApplet, width, height);
		
		setFillColor(MTColor.GREY);
		setStrokeColor(MTColor.WHITE);
		
		MTTextArea ideaArea = new MTTextArea(pApplet, 0, 0, width, height*2/3);
		ideaArea.setStrokeColor(MTColor.WHITE);
		ideaArea.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.BLACK, true));
		ideaArea.removeAllGestureEventListeners();
		
		MTTextArea editButton = new MTTextArea(pApplet);
		editButton.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.GREEN, true));
		editButton.setPositionRelativeToOther(this, new Vector3D(this.getWidthXY(TransformSpace.LOCAL)*1/4,this.getHeightXY(TransformSpace.LOCAL)*4/5,0));
		editButton.setText("edit");
		//TODO: next/prev vieleicht durch "fling" gesture ersetzen
		MTTextArea nextButton = new MTTextArea(pApplet);
		nextButton.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.GREEN, true));
		nextButton.setPositionRelativeToOther(this, new Vector3D(this.getWidthXY(TransformSpace.LOCAL)*3/4,this.getHeightXY(TransformSpace.LOCAL)*4/5,0));
		nextButton.setText("next");
		
		this.addChild(ideaArea);
		this.addChild(nextButton);
		this.addChild(editButton);
		
		
	}

}
