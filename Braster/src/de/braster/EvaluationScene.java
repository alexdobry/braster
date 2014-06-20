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
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

import processing.core.PImage;


public class EvaluationScene extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
	private MTColor listColor;

	/** The last event. */
	private MTGestureEvent lastEvent;
	/** The drag target. */
	private IMTComponent3D dragTarget;	

    /** The use custom target. */
	private boolean useCustomTarget;
	private MTEllipse area;
    
	private ArrayList<Note> ideas;
	private ArrayList<Note> rubbish;
	private ArrayList<Note> bestIdeas;
	
	private ArrayList<Cluster> rubbishCluster;
	private ArrayList<Cluster> bestIdeasCluster;
	
	private MTList listMiddle;
	//private MTList listLeft;
	//private MTList listRight;
	
	private Iscene finalScene;
	
	private ArrayList<Cluster> allCluster;
	
	private ArrayList<Note> showedCluster;
	
	private float einheitX;
	
	
	/*
     * die anzeige auf MTTextArea ï¿½ndern, dann brauch nicht extra textfeld in rectangle basteln und anklickbar sind die auch
     * 
     * wenn bereits idee selektiert ist und unten angezeigt wird
     * dann aber eine andere idee selektiert wird
     * wird die unten, wieder nach oben verschoben
     * 
     * dazu nach selektierung vorm verschieben, prï¿½fen ob bereits was in der liste drin ist
     * wenn nicht, normal nach unten 
     * 
     * sonst
     * die bisher angezeigten in liste adden
     * updateliste
     * showedTextAreas leeren
     * neue angeklickte adden und in liste abspeichern
     * 
     * nach selektierung und der verschiebung nach unten, die angezeigten ideen in einer liste abspeichern (vom typ mttextarea)
     * zu den ideas wieder adden
     */
	
	public EvaluationScene( MTApplication mtApplication, String name)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern ï¿½bergeben bekommen
		//solange templiste mit ideen
		//createStructureForIdeas(Idea.getAllParents());
		 
		//temporär
	 	allCluster = new ArrayList<Cluster>();
		 
		ArrayList<Note> ideaTemp = new ArrayList<Note>();
		ideaTemp.add(new Note("eine neue gute Idee ", "einleitung" ));
		Cluster cluster = new Cluster("cluster",ideaTemp);
		allCluster.add(cluster);
		
		ArrayList<Note> ideaTemp2 = new ArrayList<Note>();
		for(int i=0;i<3;i++)
		{
			ideaTemp2.add(new Note("fdshfisuhgi "+i, "fazit"));			
		}
		
		Cluster cluster2 = new Cluster(" cludsgster",ideaTemp2);
		allCluster.add(cluster2);
		//methodenaufruf
		
		
		showedCluster = new ArrayList<Note>();		
		rubbishCluster = new ArrayList<Cluster>();
		bestIdeasCluster = new ArrayList<Cluster>();
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(new MTColor(235, 247, 248, 255));		
		
		createArea();						
		
		
		
		einheitX = area.getRadiusX()*2/7;
		//Listen erzeugen
		listMiddle = new MTList(mtApp, 2*einheitX+85, 80,  3*einheitX-20, 250);		 
		
		//dort passiert das liste fï¿½llen, was bereits implementiert habe
		updateMiddleList();
		
		//Problembeschreibung
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		40, MTColor.BLACK));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setText(SetupScene.getProblem());
		textProblem.setPositionGlobal(new Vector3D(this.mtApp.width/2,this.mtApp.height-100));
		this.canv.addChild(textProblem);		
	}

	
	
	//constructor fï¿½r weitere Bewertungsrunde, wenn bereits alle Ideen einmal zugeordnet worden sind
	public EvaluationScene( MTApplication mtApplication, String name, ArrayList<Note> tideas, ArrayList<Note> trubbish)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern ï¿½bergeben bekommen
		//solange templiste mit ideen
		ideas = tideas;
		rubbish = trubbish;
		bestIdeas = new ArrayList<Note>();
		//createAllIdeasNew(ideas);
		showedCluster = new ArrayList<Note>();
		showedCluster.clear();
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich erzeugen
		createArea();						
		
		einheitX = area.getRadiusX()*2/7;
		//Listen erzeugen
		listMiddle = new MTList(mtApp, 2*einheitX+85, 80,  3*einheitX-20, 250);		 
		
		//dort passiert das liste fï¿½llen, was bereits implementiert habe
		//zunï¿½chst nur mittlere liste fï¿½llen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateMiddleList();
		 
		//Problembeschreibung
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		40, MTColor.BLACK));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setSizeLocal(500, 100);
		textProblem.setPositionGlobal(new Vector3D(this.mtApp.width/2,this.mtApp.height-100));
		textProblem.setText(SetupScene.getProblem());
		this.canv.addChild(textProblem);		
	}
	
	
	//Aus dem Datenmodell von Patrick eine angepasste Version machen
	//in allIdeas gibst fï¿½r jeden CLuster eine Liste der Ideen
	private void createStructureForIdeas(LinkedList<Idea> allParents)
	{		
		allCluster = new ArrayList<Cluster>();
		for(Idea idea : allParents)
		{
			//falls kein child hat, ist es nur eine einzelne idee
			//sonst erste idee die kategorie
			//children sind die ideen
			if(idea.getChildren().length==0)
			{
				ArrayList<Note> ideasTemp = new ArrayList<Note>();
				ideasTemp.add(new Note(idea.getText()));				
				Cluster cluster = new Cluster("",ideasTemp);
				allCluster.add(cluster);
			}
			else
			{
				ArrayList<Note> ideasTemp = new ArrayList<Note>();
				String clusterName = idea.getText();
				for(MTComponent childIdea : idea.getChildren())
				{
					MTTextArea textArea = (MTTextArea) childIdea;
					ideasTemp.add(new Note(textArea.getText(),clusterName));
				}
				Cluster newCluster = new Cluster(clusterName,ideasTemp); 
				allCluster.add(newCluster);								
			}
		}
	}
	
	
	
	
	
	@SuppressWarnings("deprecation")
	private void updateMiddleList()
	{
		//liste leeren und erstellen
		listMiddle.removeAllListElements();
		listMiddle.setFillColor(listColor);
		listMiddle.setStrokeColor(listColor);
		area.addChild(listMiddle);
				
		int x = 5;
		int y = 5;		
				
		//brauch updatemethode, sodass die zellenlänge vergrößert wird
		//Größe einer Zelle (standardhöhe)
		MTListCell cell = createListCell(70);
		listMiddle.addChild(cell);
								
		//arraylist durchgehen
		//für jede arraylist das erste element anzeigen als ein symbol
		for(Cluster cluster : allCluster)
		{
			int widthElement =0;
			MTTextArea textAreaIdee = null;
			MTRectangle ordner = null;
			//wenn im cluster nur eine idee ist, wird diese direkt angezeigt
			if(cluster.getNotes().size()==1)
			{
				textAreaIdee = new MTTextArea(mtApp);
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setPositionRelativeToParent(new Vector3D(x+textAreaIdee.getWidthXYVectLocal().length()/2,25));
				textAreaIdee.setFillColor(MTColor.GREEN);
				textAreaIdee.setStrokeColor(MTColor.LIME);
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				textAreaIdee.removeAllGestureEventListeners();
				textAreaIdee.setPickable(false);
				widthElement = (int) textAreaIdee.getWidthXYVectLocal().length();
			}
			//andernfalls ein ordner objekt mit namen drauf
			else
			{
				ordner = new MTRectangle(mtApp, x, y, 50, 40);
				ordner.setName(cluster.getName());
				ordner.setStrokeColor(listColor);
				ordner.unregisterAllInputProcessors();
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(false);
				widthElement=80;
				String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
				PImage img = mtApp.loadImage(path + "ordner-symbol.png");
				ordner.setTexture(img);	
			}			
			
			x += widthElement +20;
			//überprüfung ob das element reinpasst in die zeile noch
			//wenn ja alle elemente des clusters adden
			//wenn nicht nächste zeile beginnen
			if(x  > 3*einheitX) 
			{
				x = 5;
				cell = createListCell(50);
				listMiddle.addChild(cell);
			}
					
			if(cluster.getNotes().size()==1)
			{
				cell.addChild(textAreaIdee);
			}
			else
			{
				cell.addChild(ordner);
			}	
								
		}				
	}
	
	private void updateRightSide()
	{
		
	}
	
	@SuppressWarnings("deprecation")
	private void updateleftSide()
	{		
		//liste leeren
		//indem die position auslese und alle links von der linie1 lösche
		for(MTComponent component : area.getChildren())
		{
			if(component.toString().contains("MTText"))
			{
				MTTextArea textTemp = (MTTextArea) component;
				if(textTemp.getCenterPointGlobal().x<einheitX*2+75 && textTemp.getCenterPointGlobal().y>80)
				{
					area.removeChild(component);
					continue;
				}
			}
		}
	
				
		int x = (int) (einheitX +75);
		int y = (int) 100;		
				
		 						
		//arraylist durchgehen
		//für jede arraylist das erste element anzeigen als ein symbol
		for(Cluster cluster : rubbishCluster)
		{
			
			MTTextArea textAreaIdee = null;
			MTRectangle ordner = null;
			//wenn im cluster nur eine idee ist, wird diese direkt angezeigt
			if(cluster.getNotes().size()==1)
			{
				textAreaIdee = new MTTextArea(mtApp);
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setPositionRelativeToParent(new Vector3D(x,y));
				textAreaIdee.setFillColor(MTColor.GREEN);
				textAreaIdee.setStrokeColor(MTColor.LIME);
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				textAreaIdee.removeAllGestureEventListeners();
				textAreaIdee.setPickable(false);
			}
			//andernfalls ein ordner objekt mit namen drauf
			else
			{
				ordner = new MTRectangle(mtApp, x, y, 50, 40);
				ordner.setName(cluster.getName());
				ordner.setStrokeColor(listColor);
				ordner.unregisterAllInputProcessors();
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(false);
				String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
				PImage img = mtApp.loadImage(path + "ordner-symbol.png");
				ordner.setTexture(img);	
				ordner.registerInputProcessor(new TapProcessor(mtApp));
				ordner.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent)ge;
						if (te.isTapped()) 
						{		
							//öffnet sich clusterpopup
						}
						return false;
					}
				});
			
			}			
			
			
			//überprüfung ob das element reinpasst in die zeile noch
			//wenn ja alle elemente des clusters adden
			//wenn nicht nächste zeile beginnen
			y+= 70;
					
			if(cluster.getNotes().size()==1)
			{
				area.addChild(textAreaIdee);		
			}
			else
			{
				area.addChild(ordner);
			}	
								
		}
						
	}
	
	
	private MTListCell createListCell(int height)
	{
		MTListCell cell = new MTListCell(this.mtApp,3*einheitX-20, height);
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
						if(selectedComponent.toString().contains("MTTextArea"))
						{
							MTTextArea childTextarea = (MTTextArea) selectedComponent;
							//wenn in x-Richtung zwischen die maximalen Ausmaï¿½e geklickt wurde
							if(childTextarea.getCenterPointGlobal().x - 50 < clickedPosition.x && childTextarea.getCenterPointGlobal().x + 50 > clickedPosition.x)
							{
								//falls unten eine drin ist, die wieder nach oben schieben als cluster
								if(showedCluster.size()>0)
								{
									moveBottomIdeasToTop(); 
								}						 
								//selektierte aus der liste lï¿½schen
							 
								Cluster selectedCluster = removeSelectedIdeaFromIdeas(childTextarea);
								//element lï¿½schen
								childTextarea.destroy();
								//die selektierte unten erstellen
								if(selectedCluster!=null)
								{
									createSelectedIdea(selectedCluster);
								}
								//liste updaten
								updateMiddleList();
								break;
							}
						}
						//Popup muss aufgehen für weitere Ideen
						else if(selectedComponent.toString().contains("MTRect"))
						{
							MTRectangle childTextarea = (MTRectangle) selectedComponent;
							//wenn in x-Richtung zwischen die maximalen Ausmaï¿½e geklickt wurde
							if(childTextarea.getCenterPointGlobal().x - 25 < clickedPosition.x && childTextarea.getCenterPointGlobal().x + 25 > clickedPosition.x)
							{
								//falls unten eine drin ist, die wieder nach oben schieben als cluster
								if(showedCluster.size()>0)
								{
									moveBottomIdeasToTop(); 
								}						 
								//selektierte aus der liste lï¿½schen
							 
								Cluster selectedCluster = removeSelectedIdeaFromIdeas(childTextarea);
								//element lï¿½schen
								childTextarea.destroy();
								//die selektierte unten erstellen
								if(selectedCluster!=null)
								{
									createSelectedIdea(selectedCluster);
								}
								//liste updaten
								updateMiddleList();
								break;
							}
						}
					}
				}
				return false;
			}
		});
		
		return cell;
	}
	 
	
	private MTListCell createListCellOnSide(int height)
	{
		MTListCell cell = new MTListCell(this.mtApp,3*einheitX-20, height);
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
					//wenn getapped wird
					/*
					//wird die unten angezeigt und aus der liste entfernt
					MTListCell clickedCell = (MTListCell) te.getTarget();
					Vector3D clickedPosition = te.getCursor().getPosition();
					for(MTComponent selectedComponent : clickedCell.getChildren())
					{
						if(selectedComponent.toString().contains("MTTextArea"))
						{
							MTTextArea childTextarea = (MTTextArea) selectedComponent;
							//wenn in x-Richtung zwischen die maximalen Ausmaï¿½e geklickt wurde
							if(childTextarea.getCenterPointGlobal().x - 50 < clickedPosition.x && childTextarea.getCenterPointGlobal().x + 50 > clickedPosition.x)
							{
								//falls unten eine drin ist, die wieder nach oben schieben als cluster
								if(showedCluster.size()>0)
								{
									moveBottomIdeasToTop(); 
								}						 
								//selektierte aus der liste lï¿½schen
							 
								Cluster selectedCluster = removeSelectedIdeaFromIdeas(childTextarea);
								//element lï¿½schen
								childTextarea.destroy();
								//die selektierte unten erstellen
								if(selectedCluster!=null)
								{
									createSelectedIdea(selectedCluster);
								}
								//liste updaten
								updateMiddleList();
								break;
							}
						}
						//Popup muss aufgehen für weitere Ideen
						else if(selectedComponent.toString().contains("MTRect"))
						{
							MTRectangle childTextarea = (MTRectangle) selectedComponent;
							//wenn in x-Richtung zwischen die maximalen Ausmaï¿½e geklickt wurde
							if(childTextarea.getCenterPointGlobal().x - 25 < clickedPosition.x && childTextarea.getCenterPointGlobal().x + 25 > clickedPosition.x)
							{
								//falls unten eine drin ist, die wieder nach oben schieben als cluster
								if(showedCluster.size()>0)
								{
									moveBottomIdeasToTop(); 
								}						 
								//selektierte aus der liste lï¿½schen
							 
								Cluster selectedCluster = removeSelectedIdeaFromIdeas(childTextarea);
								//element lï¿½schen
								childTextarea.destroy();
								//die selektierte unten erstellen
								if(selectedCluster!=null)
								{
									createSelectedIdea(selectedCluster);
								}
								//liste updaten
								updateMiddleList();
								break;
							}
						}
					}*/
				}
				return false;
			}
		});
		
		return cell;
	}
	 
	
	
	private Cluster removeSelectedIdeaFromIdeas(MTTextArea clickedTextArea)
	{
		//rausbekommen zu welchem cluster die gehï¿½rt
		for(Cluster actualCluster : allCluster)
		{
			for(Note note : actualCluster.getNotes())
			{
				String s1 = formatString(note.getName(),40);
				String s2 = clickedTextArea.getText();
				System.out.println(note.getName());
				System.out.println(clickedTextArea.getText());
				if(s1.equals(s2))
				{
					allCluster.remove(actualCluster);
					return actualCluster;				
				}
				int answer =s1.compareTo(s2);
				if(answer==0)
				{
					return actualCluster;
				}
			}
		}
		//cluster dann rauslï¿½schen
			 
		return null;
	}
		
	
	
	private Cluster removeSelectedIdeaFromIdeas(MTRectangle clickedRectangle)
	{
		//rausbekommen zu welchem cluster die gehï¿½rt
		for(Cluster actualCluster : allCluster)
		{
			String s1 = actualCluster.getName();
			String s2 = clickedRectangle.getName();
			
			if(s1.equals(s2))
			{
				allCluster.remove(actualCluster);
				return actualCluster;				
			}
			
		}
		//cluster dann rauslï¿½schen
			 
		return null;
	}
	
	
	private void moveBottomIdeasToTop()
	{
		ArrayList<Note> ideasTemp = new ArrayList<Note>();
		for(Note n : showedCluster)
		{
			ideasTemp.add(new Note(n.getName()));
		}
		Cluster cluster = new Cluster("",ideasTemp);
		allCluster.add(cluster);
		showedCluster.clear();
		//alle kinder der area lï¿½schen die typ mttextarea sind
		for(MTComponent comp : area.getChildren())
		{
			try
			{
				MTTextArea text = (MTTextArea)comp;
				//Ausnahme einfï¿½gen fï¿½r die Textareas im tabellenkopf
				if(text.getText().equals("Ideen verworfen") || text.getText().equals("Ideen verbleibend") || text.getText().equals("Ideen weiter"))
				{
					continue;
				}
				area.removeChild(text);
			}
			catch(Exception castexception)
			{
				continue;
			}
		}
	}
	
	//eingegeben String nach der vorgegeben Lï¿½nge formatieren wegen den Zeilenumbrï¿½chen
	 private static String formatString(String str, int length)
	    {
	        String result ="";
	        //alle 50 zeichen eine neue Zeile starten
	          
	        //split bei leerzeichen     
	        String [] splitText = str.split(" ");
	          
	        //textzusammenbauen
	        //wenn text + neues wort ï¿½ber 50 ist, dann neue zeile beginnen mit \n
	        String aktuelleZeile = "";
	        for(int i=0;i<splitText.length;i++)
	        {
	            if(aktuelleZeile.length() + splitText[i].length() <length)
	            {
	                result+= splitText[i]+ " ";
	                aktuelleZeile+= splitText[i] +" ";          
	            }
	            else
	            {
	                result += "\n"+ splitText[i]+ " ";
	                aktuelleZeile=splitText[i]+  " ";
	            }
	        }       
	        return result;
	    } 
	 
	 
	
	 
	 
	 private void createArea()
	 {
		//Bereich zum Bewerten
			area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),mtApp.width/2-75, mtApp.height/10*8);
			area.setFillColor(new MTColor(listColor));
			area.setPickable(false);			
			this.canv.addChild(area);
					
			
			//Trennlinie zum Papierkorb
		 	MTLine line1 = new MTLine(this.mtApp, (area.getRadiusX()*2)/7*2+75, 0,  (area.getRadiusX()*2)/7*2+75, area.getRadiusY());	 
			line1.setFillColor(MTColor.BLACK);
			line1.setStrokeWeight(5);
			line1.setPickable(false);
 
			area.addChild(line1);
					
			//Trennlinie zu den besseren Ideen
			MTLine line2 = new MTLine(this.mtApp, (area.getRadiusX()*2)/7*5+75, 0 , (area.getRadiusX()*2)/7*5+75, area.getRadiusY());  
			line2.setFillColor(MTColor.BLACK);
			line2.setStrokeWeight(5);
			line2.setPickable(false);
	 
			area.addChild(line2);
			
			//Trennlinie zu den besseren Ideen
			MTLine line3 = new MTLine(this.mtApp, area.getCenterPointLocal().x-area.getRadiusX()+30, 70,area.getCenterPointLocal().x+area.getRadiusX()-30, 70);  
			line3.setFillColor(MTColor.BLACK);
			line3.setStrokeWeight(3);
			line3.setPickable(false);
			area.addChild(line3);
			
			MTTextArea textareaPapierkorb = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		24, MTColor.BLACK));	 
			textareaPapierkorb.unregisterAllInputProcessors();
			textareaPapierkorb.setPickable(false);
			textareaPapierkorb.setNoFill(true);
			textareaPapierkorb.setNoStroke(true);
			textareaPapierkorb.setText("Ideen verworfen");
			textareaPapierkorb.setSizeLocal(200, 40);
			textareaPapierkorb.setPositionGlobal(new Vector3D(mtApp.width/10*2,40));
			area.addChild(textareaPapierkorb);
	
			MTTextArea textareaIdeas = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		24, MTColor.BLACK));
			textareaIdeas.unregisterAllInputProcessors();
			textareaIdeas.setPickable(false);
			textareaIdeas.setNoFill(true);
			textareaIdeas.setNoStroke(true);
			textareaIdeas.setText("Ideen verbleibend");
			textareaIdeas.setSizeLocal(200, 40);			 
			textareaIdeas.setPositionGlobal(new Vector3D(mtApp.width/2,40));
			area.addChild(textareaIdeas);
				
			MTTextArea textareaBestIdeas = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		24, MTColor.BLACK));
			textareaBestIdeas.unregisterAllInputProcessors();
			textareaBestIdeas.setPickable(false);
			textareaBestIdeas.setNoFill(true);
			textareaBestIdeas.setNoStroke(true);
			textareaBestIdeas.setText("Ideen weiter");
			textareaBestIdeas.setSizeLocal(200, 40);
			textareaBestIdeas.setPositionGlobal(new Vector3D(mtApp.width/10*8,40));
			area.addChild(textareaBestIdeas);
	 }
	 
	 
	 
	 @SuppressWarnings("deprecation")
	private void createSelectedIdea(Cluster selectedCluster)
		{	
			//bereich von 400 -540 platz
			//mit mtlist bearbeiten, weil es auch sehr viele ideen sein kï¿½nnen?
			int y=350;
			for(Note note : selectedCluster.getNotes())
			{
				//breite und hï¿½he an den text anpassen
				MTTextArea rText = new MTTextArea(mtApp);

				rText.setFillColor(MTColor.GREEN);
				rText.setStrokeColor(MTColor.LIME);
				rText.unregisterAllInputProcessors();
				rText.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 25, MTColor.WHITE, true));
				//rText.removeAllGestureEventListeners();
				rText.setPickable(true);
				rText.setText(formatString(note.getName(),80));				
				rText.setPositionGlobal(new Vector3D((mtApp.width/2) /*-(rText.getWidthXYVectLocal().x/2)*/,y));
					 
				rText.registerInputProcessor(new DragProcessor(getMTApplication()));
				rText.addGestureListener(DragProcessor.class, new DefaultDragAction() {
					public boolean processGestureEvent(MTGestureEvent g) {
						if (g instanceof DragEvent){
							DragEvent dragEvent = (DragEvent)g;
							lastEvent = dragEvent;
							
							if (!useCustomTarget)
								dragTarget = dragEvent.getTarget(); 
							
							switch (dragEvent.getId()) {
							case MTGestureEvent.GESTURE_RESUMED:
								//Put target on top -> draw on top of others
								if (dragTarget instanceof MTComponent){
									MTComponent baseComp = (MTComponent)dragTarget;
									
									baseComp.sendToFront();						 
								}
								break;
							case MTGestureEvent.GESTURE_ENDED:
								//wenn Gesture zu Ende, wird die Position bestimmt
								if (dragTarget instanceof MTTextArea){
									MTTextArea baseComp = (MTTextArea)dragTarget;
									
									Vector3D centerPoint = baseComp.getCenterPointRelativeToParent();
									//wenn links von MTline1 dann wird zur listeLeft gehauen
									//element verschwindet
									//liste updaten
									//rechts dasselbe
									if(centerPoint.x <  2*einheitX+75)
									{			
										boolean added = false;
										//überprüfung ob der cluster bereits besteht
										for(Cluster leftCluster : rubbishCluster)
										{
											
											if(leftCluster.getName().equals(showedCluster.get(0).getClusterName()))
											{
												leftCluster.getNotes().add(new Note(baseComp.getText()));
												added=true;
												break;
											}											
										}
										if(added==false)
										{
											
											ArrayList<Note> newNotes = new ArrayList<Note>();
											newNotes.add(new Note(baseComp.getText()));
											Cluster newCluster = new Cluster(showedCluster.get(0).getClusterName(), newNotes);
											rubbishCluster.add(newCluster);
										}
										updateShowedNotes(baseComp);
										baseComp.destroy();					
										updateleftSide();
									}
									else if(centerPoint.x>  5*einheitX+75)
									{							 
										boolean added = false;
										//überprüfung ob der cluster bereits besteht
										for(Cluster rightCluster : bestIdeasCluster)
										{
											Note fo = showedCluster.get(0);
											if(rightCluster.getName().equals(showedCluster.get(0).getClusterName()))
											{
												rightCluster.getNotes().add(new Note(baseComp.getText()));
												added=true;
												break;
											}											
										}
										if(added==false)
										{											
											ArrayList<Note> newNotes = new ArrayList<Note>();
											newNotes.add(new Note(baseComp.getText()));
											Cluster newCluster = new Cluster(showedCluster.get(0).getClusterName(), newNotes);
											bestIdeasCluster.add(newCluster);
										}
										updateShowedNotes(baseComp);
										baseComp.destroy();					
										updateRightSide();	
									}
									else if(centerPoint.x >  2*einheitX+75 && centerPoint.x < 5*einheitX+75 && centerPoint.y < 300 )
									{											
										moveBottomIdeasToTop();
										updateMiddleList();
										baseComp.destroy();
									}								
								}
								//Falls keine Elemente mehr drin sind
								int i1 = allCluster.size();
								int i2= bestIdeasCluster.size();
								int i3= showedCluster.size();
								if(allCluster.size()==0 && bestIdeasCluster.size()==1 && showedCluster.size()==0)
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
								if(allCluster.size()==0 && bestIdeas.size()>1 && showedCluster.size()==0)
								{
									//rechten ideen in die mitte
									//alle restlichen in papierkorb
									//FinalScene gehen
									mtApp.pushScene();
									if (finalScene == null)
									{				
										//Im konstruktor mï¿½ssen die ideen als 2 listen ï¿½bergeben werden
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
				 
				area.addChild(rText);
				showedCluster.add(note);
				y+=60;
			}
		}
	 
	 
	 private void updateShowedNotes(MTTextArea draggedTextarea)
	 {
		 //muss aus der liste showedNotes gelï¿½scht werden
		 for(Note note : showedCluster)
		 {
			 String s1 = formatString(note.getName(),40);
			 
			 if(s1.equals(draggedTextarea.getText()))
			 {
				 showedCluster.remove(note);
				 break;
			 }
		 }
	 }
			 
		
}
