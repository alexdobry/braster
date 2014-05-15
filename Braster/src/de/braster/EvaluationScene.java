package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class EvaluationScene extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;

	
	public EvaluationScene( MTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
	
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich
				MTEllipse area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),600, 600);
				area.setFillColor(MTColor.BLUE);
				this.canv.addChild(area);
				
				MTLine line1 = new MTLine(this.mtApp, 400, 0, 400, 500);
				line1.setFillColor(MTColor.BLACK);
				area.addChild(line1);
				
				MTLine line2 = new MTLine(this.mtApp, this.mtApp.width-400, 0, this.mtApp.width-400, 500);
				line2.setFillColor(MTColor.BLACK);
				area.addChild(line2);
		
	}

}
