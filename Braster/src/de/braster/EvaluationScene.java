package de.braster;

import java.util.ArrayList;
import java.util.LinkedList;

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
	private MTColor listColor;
 
 

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
	
	private Iscene finalScene;
	private LinkedList<Idea> ideasPa;
	
	public EvaluationScene( MTApplication mtApplication, String name)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern übergeben bekommen
		//solange templiste mit ideen
		ideasPa = Idea.getAllIdeas();
		ideas = new ArrayList<Note>();
		rubbish = new ArrayList<Note>();
		bestIdeas = new ArrayList<Note>();
			
		for (Idea i : ideasPa)
		{
			Note n = new Note(i.getText());
			ideas.add(n);
		}
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(255,127,0);
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich
		area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),600, 600);
		area.setFillColor(new MTColor(255,127,0));
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
		
		//dort passiert das liste füllen, was bereits implementiert habe
		//zunächst nur mittlere liste füllen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateList(listMiddle,ideas);		
		 
		//Problembeschreibung
		MTRoundRectangle rectangleProblem = new MTRoundRectangle(this.mtApp, this.mtApp.width/2-250, this.mtApp.height-100, 0, 500, 60, 12, 12);			
		rectangleProblem.setStrokeColor(MTColor.BLACK);	
		
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		30, MTColor.BLACK, false));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setText(SetupScene.getProblem());
		textProblem.setPositionRelativeToParent(rectangleProblem.getCenterPointLocal());
		rectangleProblem.addChild(textProblem);
		
		this.canv.addChild(rectangleProblem);		
	}
	
	public EvaluationScene( MTApplication mtApplication, String name, ArrayList<Note> tideas, ArrayList<Note> trubbish)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern übergeben bekommen
		//solange templiste mit ideen
		ideas = tideas;
		rubbish = trubbish;
		bestIdeas = new ArrayList<Note>();
				
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(255,127,0);
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich
		area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),600, 600);
		area.setFillColor(listColor);
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
		
		//dort passiert das liste füllen, was bereits implementiert habe
		//zunächst nur mittlere liste füllen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateList(listMiddle,ideas);
		updateLeftList(listLeft,rubbish);
		 
		//Problembeschreibung
		MTRoundRectangle rectangleProblem = new MTRoundRectangle(this.mtApp, this.mtApp.width/2-250, this.mtApp.height-100, 0, 500, 60, 12, 12);			
		rectangleProblem.setStrokeColor(MTColor.BLACK);	
		
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		30, MTColor.BLACK, false));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setText("Hier steht das Problem...");
		textProblem.setPositionRelativeToParent(rectangleProblem.getCenterPointLocal());
		rectangleProblem.addChild(textProblem);
		
		this.canv.addChild(rectangleProblem);		
	}
	
	private void updateLeftList(MTList item, ArrayList<Note> list)
	{//die liste leeren
		item.removeAllListElements();
		//und dann nacheinander die elemente der arraylist in die komponente packen 
	//	MTList item = new MTList(this.mtApp, 410,0, this.mtApp.width-820,300);	 
		item.setFillColor(listColor);
		item.setStrokeColor(listColor);
		//Gesten für eine Idee
				
		area.addChild(item);
		
		int x = 10;
		int y = 15;
		
		MTListCell cell = new MTListCell(this.mtApp,240, 60);
		cell.setStrokeColor(listColor);
		cell.setFillColor(listColor);
				
		for(Note n: list)
		{			
			if(x >  120) 
			{				 
				x = 10;
				cell = new MTListCell(this.mtApp,240, 60);	
				cell.setStrokeColor(listColor);
				cell.setFillColor(listColor);
				item.addChild(cell);
			}
	 
			MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, x, y, 0, 80, 30, 0, 0);			
			mtRoundRectangle.setStrokeColor(MTColor.LIME);	
			mtRoundRectangle.setFillColor(MTColor.GREEN);
			MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		10, MTColor.WHITE, false));
			rText.unregisterAllInputProcessors();
			rText.setPickable(false);
			rText.setFillColor(MTColor.GREEN);
			rText.setNoStroke(true);
			rText.setText(n.getName());
			mtRoundRectangle.addChild(rText);
			rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
			
			mtRoundRectangle.unregisterAllInputProcessors();
			 
			cell.addChild(mtRoundRectangle);			 
			item.addChild(cell);
			
			x += 90;		  
		}		
	}
	
	
	//aufruf nachdem ein element verschoben wurde
	//und zu beginn für die mittlere Liste
	private void updateList(MTList item, ArrayList<Note> list)
	{
		//die liste leeren
		item.removeAllListElements();	
		
		//und dann nacheinander die elemente der arraylist in die komponente packen 
	//	MTList item = new MTList(this.mtApp, 410,0, this.mtApp.width-820,300);	 
		item.setFillColor(listColor);
		item.setStrokeColor(listColor);
		//Gesten für eine Idee
		
		area.addChild(item);
		
		int x = 10;
		int y = 15;
		
		MTListCell cell = new MTListCell(this.mtApp,420, 60);
		cell.setPickable(true);
		cell.setStrokeColor(listColor);
		cell.setFillColor(listColor);
		cell.unregisterAllInputProcessors();
		cell.registerInputProcessor(new TapProcessor(mtApp));
		cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()) 
				{		
					//wird die unten angezeigt und aus der liste entfernt
					MTListCell clickedCell = (MTListCell) te.getTarget();
					Vector3D clickedPosition = te.getCursor().getPosition();
					for(MTComponent selectedComponent : clickedCell.getChildren())
					{
						MTRoundRectangle childRectangle = (MTRoundRectangle) selectedComponent;
						//wenn in x-Richtung zwischen die maximalen Ausmaße geklickt wurde
						if(childRectangle.getCenterPointGlobal().x - 50 < clickedPosition.x && childRectangle.getCenterPointGlobal().x + 50 > clickedPosition.x)
						{
							createSelectedIdea(childRectangle);
						 
							//selektierte aus der liste löschen
							removeNote(childRectangle);
							childRectangle.destroy();
							updateList(listMiddle,ideas);
							break;
						}
					}
					
					 
				}
				return false;
			}
		});
		for(Note n: this.ideas)
		{			
			if(x > 320) 
			{				 
				x = 10;
				cell = new MTListCell(this.mtApp,420, 60);	
				cell.setStrokeColor(listColor);
				cell.setFillColor(listColor);
				cell.unregisterAllInputProcessors();
				cell.registerInputProcessor(new TapProcessor(mtApp));
				cell.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent)ge;
						if (te.isTapped()) 
						{		
							//wird die unten angezeigt und aus der liste entfernt
							MTListCell clickedCell = (MTListCell) te.getTarget();
							Vector3D clickedPosition = te.getCursor().getPosition();
							for(MTComponent selectedComponent : clickedCell.getChildren())
							{
								MTRoundRectangle childRectangle = (MTRoundRectangle) selectedComponent;
								//wenn in x-Richtung zwischen die maximalen Ausmaße geklickt wurde
								if(childRectangle.getCenterPointGlobal().x - 50 < clickedPosition.x && childRectangle.getCenterPointGlobal().x + 50 > clickedPosition.x)
								{
									createSelectedIdea(childRectangle);
								 
									//selektierte aus der liste löschen
									removeNote(childRectangle);
									childRectangle.destroy();
									updateList(listMiddle,ideas);
									break;
								}
							}
							
							 
						}
						return false;
					}
				});
				item.addListElement(cell);				
			}
	 
			MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, x, y, 0, 100, 30, 0, 0);			
			mtRoundRectangle.setStrokeColor(MTColor.LIME);
			mtRoundRectangle.setFillColor(MTColor.GREEN);
			mtRoundRectangle.setPickable(true);
			MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		10, MTColor.WHITE, false));
			rText.unregisterAllInputProcessors();
			rText.setPickable(false);
			rText.setFillColor(MTColor.GREEN);
			rText.setNoStroke(true);
			rText.setText(n.getName());
			mtRoundRectangle.addChild(rText);
			rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
			
			cell.addChild(mtRoundRectangle);
			item.addListElement(cell); 
			 			
			x += 120;		  
		}			
	}
	
	 
	
	private void removeNote(MTRoundRectangle rectangle)
	{
		MTTextArea textArea = (MTTextArea) rectangle.getChildren()[0];
		for(Note n : ideas)
		{			 
			if(n.getName().equals(textArea.getText()))
			{
				ideas.remove(n);
				break;
			}
		}
	}
	
	
	private void createSelectedIdea(MTRoundRectangle selectedRectangle)
	{		 
		MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, area.getRadiusX()-100, 400, 0, 200, 60, 0, 0);			
		mtRoundRectangle.setStrokeColor(MTColor.LIME);			
		mtRoundRectangle.setFillColor(MTColor.GREEN);
		MTTextArea tempText = (MTTextArea) selectedRectangle.getChildren()[0];		
		
		MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		10, MTColor.WHITE, false));
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setFillColor(MTColor.GREEN);
		rText.setNoStroke(true);
		rText.setText(tempText.getText());
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
							
						//translate(dragTarget, dragEvent);
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						//translate(dragTarget, dragEvent);
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
								baseComp.destroy();
							}
							else if(centerPoint.x >  area.getCenterPointLocal().x-240 && centerPoint.x < area.getCenterPointLocal().x+240 && centerPoint.y < 300 )
							{
								MTTextArea tempText = (MTTextArea)  baseComp.getChildren()[0];
								ideas.add(new Note(tempText.getText()));
								updateList(listMiddle, ideas);
								baseComp.destroy();
							}
							
						}
						//Falls keine Elemente mehr drin sind
						if(ideas.size()==0 && bestIdeas.size()==1)
						{
							String result = bestIdeas.get(0).getName();
							//FinalScene gehen
							mtApp.pushScene();							
							if (finalScene == null){
								finalScene = new FinalScene(mtApp, "Final", SetupScene.getProblem(), result);
								//Konstruktor erweitern um Anzahl Spieler, da genau soviele
								//Tastaturen geladen werden
							//Add the scene to the mt application
							mtApp.addScene(finalScene);
							}
							//Do the scene change
							mtApp.changeScene(finalScene);
						}
						if(ideas.size()==0 && bestIdeas.size()>1)
						{
							//rechten ideen in die mitte
							//alle restlichen in papierkorb
							//FinalScene gehen
							mtApp.pushScene();
							if (finalScene == null)
							{				
								//Im konstruktor müssen die ideen als 2 listen übergeben werden
								finalScene = new EvaluationScene(mtApp, "Evaluation Again", bestIdeas, rubbish);
								mtApp.addScene(finalScene);
							}
							//Do the scene change
							mtApp.changeScene(finalScene);
					
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
		
	}
		 
}
