package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;

public class HelpOnScene extends MTRectangle {

	public HelpOnScene(PApplet pApplet, float width, float height, PImage backGround, PImage[] stepPictures) {
		super(pApplet, width, height);
		
		String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		
		PImage img = pApplet.loadImage(path + "BW_blur_1.png");
		this.setTexture(img);
		
		HelpSteps steps = new HelpSteps(pApplet, 0, 0, 0, 0, 0, 6, 6, 8);
		steps.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent()*0.5f, getHeightXYRelativeToParent()*0.7f));
		addChild(steps);
		
	}

}
