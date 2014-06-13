package de.braster;

import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
import org.mt4jx.components.visibleComponents.widgets.MTCheckbox;

import processing.core.PApplet;

public class Checkbox extends  MTRoundRectangle {

	public Checkbox(PApplet pApplet, float x, float y, float z, float width,
			float height, float arcWidth, float arcHeight, String text) {
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
		 
		MTCheckbox checkBox = new MTCheckbox(pApplet, 15);
		checkBox.setSizeLocal(20, 20);
		checkBox.setPositionGlobal(new Vector3D(0,15));
		checkBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
			//event code here
			return false;
			}
		});
	 
		this.addChild(checkBox);
		
		MTTextField textField = new MTTextField(pApplet, 40,0, 70, 30, FontManager.getInstance().createFont(pApplet, "arial.ttf", 
        		20, //fontzize 
        		MTColor.WHITE));
		textField.setText(text);
		textField.setFillColor(MTColor.BLACK);
		textField.setStrokeColor(MTColor.BLACK);
		this.addChild(textField);
	}

}
