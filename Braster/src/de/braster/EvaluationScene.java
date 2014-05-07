package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;

public class EvaluationScene extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;

	
	public EvaluationScene( MTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
	
		this.setClearColor(MTColor.WHITE);		
		
	}

}
