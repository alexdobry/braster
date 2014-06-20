package de.braster;

import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
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
		 
		
		MTTextField textField = new MTTextField(pApplet, 0,0, 300, 60, FontManager.getInstance().createFont(pApplet, "arial.ttf", 
        		40, //fontzize 
        		MTColor.WHITE));
		textField.setText(text);
		textField.setFillColor(new MTColor(73, 112, 138, 255));
		textField.setStrokeColor(new MTColor(73, 112, 138, 255));
		textField.setGestureAllowance(DragProcessor.class, false);
		this.addChild(textField);
		
		needHelp = false;
		
		final MTCheckbox checkBox = new MTCheckbox(pApplet, 40);
         
        checkBox.setPositionRelativeToOther(textField, new Vector3D(320,30));
        checkBox.setBooleanValue(false);       
        checkBox.addGestureListener(TapProcessor.class, new IGestureEventListener() {
            
            @Override
            public boolean processGestureEvent(MTGestureEvent ge)
            {           
            	if(checkBox.getBooleanValue())
            	{
            		checkBox.setFillColor(MTColor.GREEN);
            	}
            	
                 needHelp = checkBox.getBooleanValue();
                 return false;
            }
        });

              
        this.addChild(checkBox);
		 
		
		
	}

}
