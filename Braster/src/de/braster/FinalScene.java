package de.braster;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class FinalScene extends AbstractScene{

	@SuppressWarnings("deprecation")
	public FinalScene( final MTApplication mtApplication, String name, String problem, ArrayList<Cluster> result)
	{
		super(mtApplication, name);
		MTCanvas canvas = getCanvas();
		this.setClearColor(MTColor.WHITE);
		
		MTTextArea textArea1 = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		MTColor.BLACK)); //Font color
		
		textArea1.unregisterAllInputProcessors();		 
		textArea1.setPickable(false);
		textArea1.setNoFill(true);
		textArea1.setNoStroke(true);
		textArea1.setText("Fuer das Problem");
		textArea1.setPositionGlobal(new Vector3D(mtApplication.width/2,80));
		canvas.addChild(textArea1);
		
		MTTextArea textArea2 = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		MTColor.BLACK)); //Font color		
		textArea2.unregisterAllInputProcessors();
		textArea2.setPickable(false);
		textArea2.setNoFill(true);
		textArea2.setNoStroke(true);
		textArea2.setText(problem);
		textArea2.setPositionGlobal(new Vector3D(mtApplication.width/2,180));
		canvas.addChild(textArea2);
		
		MTTextArea textArea3 = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		MTColor.BLACK)); //Font color		
		textArea3.unregisterAllInputProcessors();
		textArea3.setPickable(false);
		textArea3.setNoFill(true);
		textArea3.setNoStroke(true);
		textArea3.setText("wurden die folgenden Loesungen gefunden:");
		textArea3.setPositionGlobal(new Vector3D(mtApplication.width/2,mtApplication.height/5*2));
		canvas.addChild(textArea3);
		
		int x =mtApplication.width/(result.size()+1);
		int y= mtApplication.height/5*2+100;

		float laengstes =0; 
		
		for(Cluster cluster : result)
		{
			if(cluster.getNotes().size()>1)
			{
				y =mtApplication.height/5*2+100;
				MTTextArea ordner = new MTTextArea(mtApplication);
				ordner.setText(cluster.getName());	
				ordner.setFillColor(new MTColor(139,69,0,255));
				ordner.setStrokeColor(new MTColor(205,133,0,255));
				ordner.unregisterAllInputProcessors();
				ordner.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 14, MTColor.WHITE, true));
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(false);
				if(ordner.getWidthXYVectLocal().length()>laengstes)
				{
					laengstes = ordner.getWidthXYVectLocal().length();
				}
				canvas.addChild(ordner);
				ordner.setPositionGlobal(new Vector3D(x,y));
				
				y+=30;
				//brauen ordner dazu und grünen ideen daunter
				for(Note notes : cluster.getNotes())
				{
					MTTextArea textAreaIdee = new MTTextArea(mtApplication);
					textAreaIdee.setText(notes.getName());	
					textAreaIdee.setFillColor(MTColor.GREEN);
					textAreaIdee.setStrokeColor(MTColor.LIME);
					textAreaIdee.unregisterAllInputProcessors();
					textAreaIdee.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 14, MTColor.WHITE, true));
					textAreaIdee.removeAllGestureEventListeners();
					textAreaIdee.setPickable(false);
					textAreaIdee.setPositionGlobal(new Vector3D(x,y));
					canvas.addChild(textAreaIdee);
					if(textAreaIdee.getWidthXYVectLocal().length()>laengstes)
					{
						laengstes = textAreaIdee.getWidthXYVectLocal().length();
					}
					y+=30;				
				}
				
				x+=laengstes+100;
			}
			else
			{				
				y =mtApplication.height/5*2+100;//nur eine grüne idee
				MTTextArea textAreaIdee = new MTTextArea(mtApplication);
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setFillColor(MTColor.GREEN);
				textAreaIdee.setStrokeColor(MTColor.LIME);
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 14, MTColor.WHITE, true));
				textAreaIdee.removeAllGestureEventListeners();
				textAreaIdee.setPickable(false);
				if(textAreaIdee.getWidthXYVectLocal().length()>laengstes)
				{
					laengstes = textAreaIdee.getWidthXYVectLocal().length();
				}

				textAreaIdee.setPositionGlobal(new Vector3D(x,y));
				canvas.addChild(textAreaIdee);
				x+=laengstes+100;				
			}
			laengstes=0;
			
			
		}
		

			
	}
}
