package de.braster;

import org.mt4j.components.MTCanvas;
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

	private static boolean needHelp;
 
	
	public Checkbox(PApplet pApplet, float x, float y, float z, float width,
			float height, float arcWidth, float arcHeight, String text) {
		super(pApplet, x, y, z, width, height, arcWidth, arcHeight);
		 
		needHelp = false; 
		final MTCheckbox checkBox = new MTCheckbox(pApplet, 50);
        checkBox.setFillColor(MTColor.NAVY);
        checkBox.setBooleanValue(false);       
        checkBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
            
            @Override
            public boolean processGestureEvent(MTGestureEvent ge)
            {                      
                 needHelp = checkBox.getBooleanValue();
                 return false;
            }
        });

              
        this.addChild(checkBox);
		 
		
		MTTextField textField = new MTTextField(pApplet, 80,0, 100, 60, FontManager.getInstance().createFont(pApplet, "arial.ttf", 
        		40, //fontzize 
        		MTColor.WHITE));
		textField.setText(text);
		textField.setFillColor(MTColor.BLACK);
		textField.setStrokeColor(MTColor.BLACK);
		this.addChild(textField);
	}

}
