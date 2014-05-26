package de.braster;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;

public class EvaluationScene extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
 
 

	/** The last event. */
	private MTGestureEvent lastEvent;
	/** The use custom target. */
	private boolean useCustomTarget;
	/** The drag target. */
	private IMTComponent3D dragTarget;	

    private MTEllipse area;
    
	private ArrayList<Note> ideas;
	private ArrayList<Note> rubbish;
	private ArrayList<Note> bestIdeas;
	
	private MTList listMiddle;
	private MTList listLeft;
	private MTList listRight;

	
	public EvaluationScene( MTApplication mtApplication, String name)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern �bergeben bekommen
		//solange templiste mit ideen
		ideas = new ArrayList<Note>();
		rubbish = new ArrayList<Note>();
		bestIdeas = new ArrayList<Note>();
			
		for(int i=1;i<30;i++)
		{
			Note n = new Note(i+"");
			ideas.add(n);			
		}
		 
				
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
	
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich
		area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),600, 600);
		area.setFillColor(new MTColor(135,206,250));
		area.setPickable(false);
		this.canv.addChild(area);
				
	 	MTLine line1 = new MTLine(this.mtApp, area.getCenterPointLocal().x-240, 0,  area.getCenterPointLocal().x-240, 500);	 
		line1.setFillColor(MTColor.BLACK);
		line1.setPickable(false);
		area.addChild(line1);
				
		MTLine line2 = new MTLine(this.mtApp, area.getCenterPointLocal().x+240, 0,area.getCenterPointLocal().x+240, 500);  
		line2.setFillColor(MTColor.BLACK);
		line2.setPickable(false);
		area.addChild(line2);
		
		//Listen erzeugen
	 	listMiddle = new MTList(mtApp,  area.getCenterPointLocal().x-220, 10,  440, 300);		 
			
		listLeft = new MTList(mtApp, area.getCenterPointLocal().x-500, 10, 240 , 200);	 
				
		listRight = new MTList(mtApp,area.getCenterPointLocal().x+260, 10, 240 , 200);
		 
		
		//dort passiert das liste f�llen, was bereits implementiert habe
		//zun�chst nur mittlere liste f�llen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateList(listMiddle,ideas);
		
		 
		
		//tempor�r untere rectangle f�r die selektierte idee in der mitte
		MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, area.getRadiusX()-100, 400, 0, 200, 60, 12, 12);			
		mtRoundRectangle.setStrokeColor(MTColor.BLACK);		
		
		MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		10, MTColor.BLACK, false));
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("bla");
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		mtRoundRectangle.registerInputProcessor(new TapProcessor(getMTApplication()));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(mtRoundRectangle));
		mtRoundRectangle.addGestureListener(DragProcessor.class, new DefaultDragAction() {
			public boolean processGestureEvent(MTGestureEvent g) {
				if (g instanceof DragEvent){
					DragEvent dragEvent = (DragEvent)g;
					lastEvent = dragEvent;
					
					if (!useCustomTarget)
						dragTarget = dragEvent.getTarget(); 
					
					switch (dragEvent.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
					case MTGestureEvent.GESTURE_RESUMED:
						//Put target on top -> draw on top of others
						if (dragTarget instanceof MTComponent){
							MTComponent baseComp = (MTComponent)dragTarget;
							
							baseComp.sendToFront();						 
						}
							
//						translate(dragTarget, dragEvent);
						break;
					case MTGestureEvent.GESTURE_UPDATED:
//						translate(dragTarget, dragEvent);
						break;
					case MTGestureEvent.GESTURE_CANCELED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						//wenn Gesture zu Ende, wird die Position bestimmt
						if (dragTarget instanceof MTRoundRectangle){
							MTRoundRectangle baseComp = (MTRoundRectangle)dragTarget;
							
							Vector3D centerPoint = baseComp.getCenterPointRelativeToParent();
							//wenn links von MTline1 dann wird zur listeLeft gehauen
							//element verschwindet
							//liste updaten
							//rechts dasselbe
							if(centerPoint.x <  area.getCenterPointLocal().x-240)
							{
								MTTextArea tempText = (MTTextArea) baseComp.getChildren()[0];
								rubbish.add(new Note(tempText.getText()));
								updateLeftList(listLeft,rubbish);
								baseComp.destroy();
							}
							else if(centerPoint.x>  area.getCenterPointLocal().x+240)
							{
								MTTextArea tempText = (MTTextArea)  baseComp.getChildren()[0];
								bestIdeas.add(new Note(tempText.getText()));
								updateLeftList(listRight, bestIdeas);
							}
						}
						
						break;
					default:
						break;
					}
				}
				return false;
			}
		});
		 
		area.addChild(mtRoundRectangle);
		
		//diese dann alle in der mitte anzeigen lassen	
		//oben mitte alle ideen als icons
		//selektierte dann weiter unten gro�zeigen
		//problembeschreibung unter dem kreis
		
		
		 
		 
		//bei anklicken m�ssen die ideen gro� angezeigt werden
		//bei geste nach rechts nach links verschieben in papierkorb
		//bei nach rechts in den ordner f�r bessere ideen verschieben		
	}
	
	
	
	private void updateLeftList(MTList item, ArrayList<Note> list)
	{//die liste leeren
		item.removeAllListElements();
		//und dann nacheinander die elemente der arraylist in die komponente packen 
	//	MTList item = new MTList(this.mtApp, 410,0, this.mtApp.width-820,300);	 
		item.setFillColor(new MTColor(135,206,250));
		item.setStrokeColor(new MTColor(135,206,250));
		//Gesten f�r eine Idee
				
		area.addChild(item);
		
		int x = 10;
		int y = 15;
		
		MTListCell cell = new MTListCell(this.mtApp,240, 60);
		cell.setStrokeColor(new MTColor(135,206,250));
		cell.setFillColor(new MTColor(135,206,250));
				
		for(Note n: list)
		{			
			if(x >  120) 
			{				 
				x = 10;
				cell = new MTListCell(this.mtApp,240, 60);	
				cell.setStrokeColor(new MTColor(135,206,250));
				cell.setFillColor(new MTColor(135,206,250));
				item.addChild(cell);
			}
	 
			MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, x, y, 0, 80, 30, 12, 12);			
			mtRoundRectangle.setStrokeColor(MTColor.BLACK);	
			
			MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		10, MTColor.BLACK, false));
			rText.unregisterAllInputProcessors();
			rText.setPickable(false);
			rText.setNoFill(true);
			rText.setNoStroke(true);
			rText.setText(n.getName());
			mtRoundRectangle.addChild(rText);
			rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
			
			mtRoundRectangle.unregisterAllInputProcessors();
			//mtRoundRectangle.setGestureAllowance(TapProcessor.class, false);
			mtRoundRectangle.registerInputProcessor(new TapProcessor(mtApp));
			mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped())
					{
					 
					//	MTRoundRectangle newRectangle = new MTRoundRectangle(mtApp , 410, 400, 0, 100, 30, 12, 12);		
						//newRectangle.setStrokeColor(MTColor.BLACK);
						//area.addChild(newRectangle);
						System.out.println("klappptptppt");
					}
					return false;
				}
			});
			cell.addChild(mtRoundRectangle);
			 
			item.addChild(cell);
			
			x += 90;		  
		}		
	}
	
	
	//aufruf nachdem ein element verschoben wurde
	//und zu beginn f�r die mittlere Liste
	private void updateList(MTList item, ArrayList<Note> list)
	{
		//die liste leeren
		item.removeAllListElements();
		//und dann nacheinander die elemente der arraylist in die komponente packen 
	//	MTList item = new MTList(this.mtApp, 410,0, this.mtApp.width-820,300);	 
		item.setFillColor(new MTColor(135,206,250));
		item.setStrokeColor(new MTColor(135,206,250));
		//Gesten f�r eine Idee
				
		area.addChild(item);
		
		int x = 10;
		int y = 15;
		
		MTListCell cell = new MTListCell(this.mtApp,420, 60);
		cell.setStrokeColor(new MTColor(135,206,250));
		cell.setFillColor(new MTColor(135,206,250));
				
		for(Note n: this.ideas)
		{			
			if(x > 320) 
			{				 
				x = 10;
				cell = new MTListCell(this.mtApp,420, 60);	
				cell.setStrokeColor(new MTColor(135,206,250));
				cell.setFillColor(new MTColor(135,206,250));
				item.addChild(cell);
			}
	 
			MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, x, y, 0, 100, 30, 12, 12);			
			mtRoundRectangle.setStrokeColor(MTColor.BLACK);	
			
			MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		10, MTColor.BLACK, false));
			rText.unregisterAllInputProcessors();
			rText.setPickable(false);
			rText.setNoFill(true);
			rText.setNoStroke(true);
			rText.setText(n.getName());
			mtRoundRectangle.addChild(rText);
			rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
			
			mtRoundRectangle.unregisterAllInputProcessors();
			//mtRoundRectangle.setGestureAllowance(TapProcessor.class, false);
			mtRoundRectangle.registerInputProcessor(new TapProcessor(mtApp));
			mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped())
					{
					 
						//MTRoundRectangle newRectangle = new MTRoundRectangle(mtApp , 410, 400, 0, 100, 30, 12, 12);		
						//newRectangle.setStrokeColor(MTColor.BLACK);
						//area.addChild(newRectangle);
						System.out.println("klappptptppt");
					}
					return false;
				}
			});
			cell.addChild(mtRoundRectangle);
			 
			item.addChild(cell);
			
			x += 120;		  
		}		
	}
	
	 
	
	
	
	
	
	 
}
