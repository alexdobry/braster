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
		
		int x =mtApplication.width/(result.size()+5);
		int y= mtApplication.height/5*2+100;

		float laengstesDavor =0; 
		
		for(Cluster cluster : result)
		{
			float laengstes =0;
			if(cluster.getNotes().size()>1)
			{
				ArrayList<MTTextArea> sammelList = new ArrayList<MTTextArea>();
				y =mtApplication.height/5*2+100;
				MTTextArea ordner = new MTTextArea(mtApplication);
				ordner.setText(cluster.getName());	
				ordner.setFillColor(new MTColor(139,69,0,255));
				ordner.setStrokeColor(new MTColor(205,133,0,255));
				ordner.unregisterAllInputProcessors();
				ordner.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, MTColor.WHITE, true));
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(false);
				if(ordner.getWidthXYVectLocal().length()>laengstes)
				{
					laengstes = ordner.getWidthXYVectLocal().length();
				}
				sammelList.add(ordner);
				
				//brauen ordner dazu und grünen ideen daunter
				for(Note notes : cluster.getNotes())
				{
					MTTextArea textAreaIdee = new MTTextArea(mtApplication);
					textAreaIdee.setText(notes.getName());	
					textAreaIdee.setFillColor(MTColor.GREEN);
					textAreaIdee.setStrokeColor(MTColor.LIME);
					textAreaIdee.unregisterAllInputProcessors();
					textAreaIdee.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, MTColor.WHITE, true));
					textAreaIdee.removeAllGestureEventListeners();
					textAreaIdee.setPickable(false);
					if(textAreaIdee.getWidthXYVectLocal().length()>laengstes)
					{
						laengstes = textAreaIdee.getWidthXYVectLocal().length();
					}
					sammelList.add(textAreaIdee);					
				}
							
				x+=laengstes/2+ laengstesDavor/2+20;
			
				for(MTTextArea area : sammelList)
				{
					area.setPositionGlobal(new Vector3D(x,y));
					canvas.addChild(area);
					y+=40;
				}				 
				laengstesDavor=laengstes;
			}
			else
			{				
				ArrayList<MTTextArea> sammelList = new ArrayList<MTTextArea>();
				y =mtApplication.height/5*2+100;//nur eine grüne idee
				MTTextArea textAreaIdee = new MTTextArea(mtApplication);
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setFillColor(MTColor.GREEN);
				textAreaIdee.setStrokeColor(MTColor.LIME);
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApplication, "arial.ttf", 24, MTColor.WHITE, true));
				textAreaIdee.removeAllGestureEventListeners();
				textAreaIdee.setPickable(false);
				sammelList.add(textAreaIdee);
				if(textAreaIdee.getWidthXYVectLocal().length()>laengstes)
				{
					laengstes = textAreaIdee.getWidthXYVectLocal().length();
				}

				x+=laengstes/2+ laengstesDavor/2+20;
				
				for(MTTextArea area : sammelList)
				{
					area.setPositionGlobal(new Vector3D(x,y));
					canvas.addChild(area);
					y+=40;
				}				 
				laengstesDavor=laengstes;

			}
			
			
			
		}
		

			
	}
}
