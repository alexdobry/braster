package de.braster;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class FinalScene extends AbstractScene{

	public FinalScene( final MTApplication mtApplication, String name, String problem, ArrayList<Cluster> result)
	{
		super(mtApplication, name);
		MTCanvas canvas = getCanvas();
		
		
		MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		
		textArea.unregisterAllInputProcessors();
		 
		
		//wenn angeklickt wird, muss sich Tastatur �ffnen
		//eingegebener Text dann im Feld erscheinen
		 
		textArea.setPickable(false);
		textArea.setNoFill(true);
		textArea.setNoStroke(true);
		textArea.setHeightLocal(600);
		textArea.setWidthLocal(600);
		String loesung ="";
		for(Cluster cluster : result)
		{
			for(Note notes : cluster.getNotes())
			{
				loesung+= notes.getName()+"\n";
			}
		}
		textArea.setText("Für das Problem \n \n" + problem +"\nwurde folgende Lösung gefunden  \n " +loesung+".");		
		textArea.setPositionRelativeToParent(new Vector3D(mtApplication.width/2,mtApplication.height/2));
		canvas.addChild(textArea);	
	}
}
