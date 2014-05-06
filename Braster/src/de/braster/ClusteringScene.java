package de.braster;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.buttons.MTImageButton;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;

public class ClusteringScene extends AbstractScene{

	
	private MTCanvas canv;
	private MTApplication mtApp;
	
	public ClusteringScene( MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		//setzt Hintergrundfarbe
		this.setClearColor(MTColor.WHITE);
		
		createItem();
		
		createLeftMenubar();
	}

	
	
	
	private void createLeftMenubar()
	{		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, 20, 500, 0, 200, 250, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.BLUE);  //Color anpassen noch 	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		this.canv.addChild(mtRoundRectangle);	
	}
	
	private void createItem()
	{
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		30, MTColor.BLACK, false);
		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, 400, 400, 0, 300, 50, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.BLUE);
		mtRoundRectangle.setStrokeColor(MTColor.BLUE);
/*
		Image image = Toolkit.getDefaultToolkit().getImage("notizzettel.jpg");
		
		PImage Pimage = new PImage(image);
		
		MTImageButton button = new MTImageButton(this.mtApp, Pimage);
		mtRoundRectangle.addChild(button);*/
		/*
		MTTextArea rText = new MTTextArea(this.mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("hier steht was");
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
	*/
		mtRoundRectangle.registerInputProcessor(new TapProcessor(getMTApplication()));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(mtRoundRectangle));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
										
					//entweder wird die idee ausgeklappt
					//oder wieder eingeklappt
					//je nach vorherigem status
					//höhe nach text ausrichten
					//dazu ne methode schreiben
					mtRoundRectangle.setSizeLocal(300, 500);
				}
				return false;
			}
		});
		this.canv.addChild(mtRoundRectangle);
		
	}
}
