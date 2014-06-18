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
	
	private ArrayList<ArrayList<Note>> allIdeas;
	
	private ArrayList<Note> showedNotes;
	
	private float einheitX;
	
	
	/*
     * die anzeige auf MTTextArea �ndern, dann brauch nicht extra textfeld in rectangle basteln und anklickbar sind die auch
     * 
     * wenn bereits idee selektiert ist und unten angezeigt wird
     * dann aber eine andere idee selektiert wird
     * wird die unten, wieder nach oben verschoben
     * 
     * dazu nach selektierung vorm verschieben, pr�fen ob bereits was in der liste drin ist
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
		
		//muss noch die ganzen ideen aus dem clustern �bergeben bekommen
		//solange templiste mit ideen
		//createStructureForIdeas(Idea.getAllParents());
		 

	 	allIdeas = new ArrayList<ArrayList<Note>>();
		for(int i=1;i<9;i++)
		{
			ArrayList<Note> bla = new ArrayList<Note>();
			for(int j=0;j<3;j++)
			{
				bla.add(new Note(i +"Idee " +j));
			}
			allIdeas.add(bla);
		} 

		//methodenaufruf
		
		ideas = new ArrayList<Note>();
		rubbish = new ArrayList<Note>();
		bestIdeas = new ArrayList<Note>();
		showedNotes = new ArrayList<Note>();
		
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(new MTColor(235, 247, 248, 255));		
		
		createArea();			
			
		
		einheitX = area.getRadiusX()*2/7;
		//Listen erzeugen
		listMiddle = new MTList(mtApp, 2*einheitX+85, 80,  3*einheitX-20, 250);		 
		
		listLeft = new MTList(mtApp, einheitX+65, 80, einheitX , 200);	 
				
		listRight = new MTList(mtApp,5*einheitX+115, 80, einheitX , 200);	 
		
		//dort passiert das liste f�llen, was bereits implementiert habe
		//zun�chst nur mittlere liste f�llen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateMiddleList();	
		
		//Problembeschreibung
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		40, MTColor.BLACK, false));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setSizeLocal(500, 100);
		textProblem.setPositionGlobal(new Vector3D(this.mtApp.width/2,this.mtApp.height-100));
		textProblem.setText(SetupScene.getProblem());
		this.canv.addChild(textProblem);		
	}

	
	
	//constructor f�r weitere Bewertungsrunde, wenn bereits alle Ideen einmal zugeordnet worden sind
	public EvaluationScene( MTApplication mtApplication, String name, ArrayList<Note> tideas, ArrayList<Note> trubbish)
	{		
		super(mtApplication, name);		
		
		//muss noch die ganzen ideen aus dem clustern �bergeben bekommen
		//solange templiste mit ideen
		ideas = tideas;
		rubbish = trubbish;
		bestIdeas = new ArrayList<Note>();
		createAllIdeasNew(ideas);
		showedNotes = new ArrayList<Note>();
		showedNotes.clear();
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich erzeugen
		createArea();
		
		einheitX = area.getRadiusX()*2/7;
		//Listen erzeugen
		listMiddle = new MTList(mtApp,  2*einheitX+85, 80,  3*einheitX-20, 250);		 
		
		listLeft = new MTList(mtApp, einheitX+65, 80, einheitX , 200);	 
				
		listRight = new MTList(mtApp,5*einheitX+115, 80, einheitX , 200);		 
		
		//dort passiert das liste f�llen, was bereits implementiert habe
		//zun�chst nur mittlere liste f�llen
		//nach jedem drag&drop vorgang die neue liste updaten
		updateMiddleList();
		updateSideList(listLeft,rubbish);
		 
		//Problembeschreibung
		MTTextArea textProblem = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		40, MTColor.BLACK, false));
		textProblem.unregisterAllInputProcessors();
		textProblem.setPickable(false);
		textProblem.setNoFill(true);
		textProblem.setNoStroke(true);
		textProblem.setSizeLocal(500, 100);
		textProblem.setPositionGlobal(new Vector3D(this.mtApp.width/2,this.mtApp.height-100));
		textProblem.setText(SetupScene.getProblem());
		this.canv.addChild(textProblem);		
	}
	
	
	private void createAllIdeasNew(ArrayList<Note> ideas)
	{
		allIdeas = new ArrayList<ArrayList<Note>>();
		for(Note note :  ideas)
		{
			ArrayList<Note> temp = new ArrayList<Note>();
			temp.add(note);
			allIdeas.add(temp);
		}
	}
	
	//Aus dem Datenmodell von Patrick eine angepasste Version machen
	//in allIdeas gibst f�r jeden CLuster eine Liste der Ideen
	private void createStructureForIdeas(LinkedList<Idea> allParents)
	{
		allIdeas = new ArrayList<ArrayList<Note>>();
		for(Idea idea : allParents)
		{
			ArrayList<Note> listOfNotes = new ArrayList<Note>();
			listOfNotes.add(new Note(idea.getText()));
				
			for(MTComponent childIdea : idea.getChildren())
			{
				MTTextArea textArea = (MTTextArea) childIdea;
				listOfNotes.add(new Note(textArea.getText()));
			}
			allIdeas.add(listOfNotes);
						
		}
		/*
		allIdeas = new ArrayList<ArrayList<Note>>();
		for(Idea idea : allParents)
		{
			if(ideaInList(idea.getText()) == false)
			{
				ArrayList<Note> listOfNotes = new ArrayList<Note>();
				listOfNotes.add(new Note(idea.getText()));
				
				for(MTComponent childIdea : idea.getChildren())
				{
					MTTextArea textArea = (MTTextArea) childIdea;
					listOfNotes.add(new Note(textArea.getText()));
				}
				allIdeas.add(listOfNotes);
			}			
		}*/		
	}
	
	
	//�berpr�fen ob ein text bereits in der liste ist, um
	//redundante ideen zu vermeiden
	 private boolean ideaInList(String text)
	 {
		 for(ArrayList<Note> actualList : allIdeas)
		 {
			 for(Note note :actualList)
			 {
				 if(note.getName().equals(text))
				 {
					 return true;					 
				 }
			 }
		 }
		 return false;
	 }
	
	//updatet den Papierkorb und die besseren Ideen aufgrund der Eintr�ge in der Arraylist
	private void updateSideList(MTList listComponent, ArrayList<Note> actualList)
	{	
		
		//liste leeren und erstellen
		listComponent.removeAllListElements();
		listComponent.setFillColor(listColor);
		listComponent.setStrokeColor(listColor);
		area.addChild(listComponent);
				
		int x = 5;
		int y = 5;				
	 
					
		//arraylist durchgehen
		//f�r jede note neue celle erzeugen und idee zupacken
		for(Note actualNote : actualList)
		{				 
			MTListCell cell = createListCell(30);
			listComponent.addChild(cell);			 
			
			MTTextArea rText = new MTTextArea(mtApp);
			rText.setPositionRelativeToParent(new Vector3D(x,y+10));
			rText.setFillColor(MTColor.GREEN);
			rText.setStrokeColor(MTColor.LIME);
			rText.unregisterAllInputProcessors();
			rText.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
			rText.removeAllGestureEventListeners();
			rText.setPickable(true);
			rText.setText(formatString(actualNote.getName(),30));				
			cell.addChild(rText);	
			
			//zur�ckschieben in die mitte soll m�glich sein
			//wenn �ber die Grenzlinien ist, wird zur Liste geaddet
			//aus der aktuellen gel�scht
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
								
								if(centerPoint.x >  2*einheitX+75 && centerPoint.x < 5*einheitX+75)
								{			
									ArrayList<Note> newIdealist = new ArrayList<Note>();
									newIdealist.add(new Note(baseComp.getText()));
									allIdeas.add(newIdealist);
									//todo.vom rubbish entfernen
									baseComp.destroy();					
									updateMiddleList();													
								}
								else if(centerPoint.x>  5*einheitX+75)
								{							 
									ArrayList<Note> newIdealist = new ArrayList<Note>();
									newIdealist.add(new Note(baseComp.getText()));
									allIdeas.add(newIdealist);
									//todo.vom rubbish entfernen
									baseComp.destroy();					
									updateMiddleList();	
								}
																
							}
							//Falls keine Elemente mehr drin sind
							if(allIdeas.size()==0 && bestIdeas.size()==1 && showedNotes.size()==0)
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
							if(allIdeas.size()==0 && bestIdeas.size()>1 && showedNotes.size()==0)
							{
								//rechten ideen in die mitte
								//alle restlichen in papierkorb
								//FinalScene gehen
								mtApp.pushScene();
								if (finalScene == null)
								{				
									//Im konstruktor m�ssen die ideen als 2 listen �bergeben werden
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
	
		}			
	}
	
	
	private void updateCellHeight(MTListCell cell, int newHeight)
	{
		cell.setHeightLocal(newHeight);
	}
	
	private void updateMiddleList()
	{
		//liste leeren und erstellen
		listMiddle.removeAllListElements();
		listMiddle.setFillColor(listColor);
		listMiddle.setStrokeColor(listColor);
		area.addChild(listMiddle);
		
		int x = 5;
		int y = 5;		
		
		//brauch updatemethode, sodass die zellenl�nge vergr��ert wird
		//Gr��e einer Zelle (standardh�he)
		MTListCell cell = createListCell(30);
		listMiddle.addChild(cell);
						
		//arraylist durchgehen
		//f�r jede arraylist das erste element anzeigen als ein symbol
		for(ArrayList<Note> actualList : allIdeas)
		{
			//cluster adden in die zelle
			//komplette cluster soll abgebildet werden
			//aber untereinander direkt
			//die folgende zelle wird dann weiter unter gezeichnet, also wird ein y gemerkt f�r abstand
			//H�he anpassen an die elemente
			if(cell.getHeightXYVectLocal().y<actualList.size()*30+10)
			{
				updateCellHeight(cell,actualList.size()*30+10);
			}
			//nacheinander adden
		
			//wenn element nicht mehr in aktuellen passt
			if(x > 3*einheitX-135) 
			{				 
				x = 5;
				cell = createListCell(0);
				listMiddle.addChild(cell);
			}
			 			
			for(Note actualNote : actualList)
			{
				MTTextArea rText = new MTTextArea(mtApp);
				rText.setPositionRelativeToParent(new Vector3D(x,y+10));
				rText.setFillColor(MTColor.GREEN);
				rText.setStrokeColor(MTColor.LIME);
				rText.unregisterAllInputProcessors();
				rText.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				rText.removeAllGestureEventListeners();
				rText.setPickable(true);
				rText.setText(formatString(actualNote.getName(),30));				
				cell.addChild(rText);						
				listMiddle.addListElement(cell); 		
				y+=30;
			}
			y=5;
			x += 120;
			
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
						MTTextArea childTextarea = (MTTextArea) selectedComponent;
						//wenn in x-Richtung zwischen die maximalen Ausma�e geklickt wurde
						if(childTextarea.getCenterPointGlobal().x - 50 < clickedPosition.x && childTextarea.getCenterPointGlobal().x + 50 > clickedPosition.x)
						{
							//falls unten eine drin ist, die wieder nach oben schieben als cluster
							if(showedNotes.size()>0)
							{
								moveBottomIdeasToTop(); 
							}						 
							//selektierte aus der liste l�schen
							 
							ArrayList<Note> selectedCluster = removeSelectedIdeaFromIdeas(childTextarea);
							//element l�schen
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
				return false;
			}
		});
		
		return cell;
	}
	 
	
	
	
	private ArrayList<Note> removeSelectedIdeaFromIdeas(MTTextArea clickedTextArea)
	{
		ArrayList<Note> result = null;
		//rausbekommen zu welchem cluster die geh�rt
		for(ArrayList<Note> actualList : allIdeas)
		{
			for(Note note : actualList)
			{
				String s1 = formatString(note.getName(),40);
				String s2 = clickedTextArea.getText();
				System.out.println(note.getName());
				System.out.println(clickedTextArea.getText());
				if(s1.equals(s2))
				{
					allIdeas.remove(actualList);
					return actualList;				
				}
				int answer =s1.compareTo(s2);
				if(answer==0)
				{
					return actualList;
				}
			}
		}
		//cluster dann rausl�schen
			 
		return null;
	}
		
	
	private void moveBottomIdeasToTop()
	{
		ArrayList<Note> temp = new ArrayList<Note>();
		for(Note n : showedNotes)
		{
			temp.add(new Note(n.getName()));
		}
		allIdeas.add(temp);
		showedNotes.clear();
		//alle kinder der area l�schen die typ mttextarea sind
		for(MTComponent comp : area.getChildren())
		{
			try
			{
				MTTextArea text = (MTTextArea)comp;
				//Ausnahme einf�gen f�r die Textareas im tabellenkopf
				if(text.getText().equals("verworfen") || text.getText().equals("verbleibend") || text.getText().equals("weiter"))
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
	
	//eingegeben String nach der vorgegeben L�nge formatieren wegen den Zeilenumbr�chen
	 private static String formatString(String str, int length)
	    {
	        String result ="";
	        //alle 50 zeichen eine neue Zeile starten
	          
	        //split bei leerzeichen     
	        String [] splitText = str.split(" ");
	          
	        //textzusammenbauen
	        //wenn text + neues wort �ber 50 ist, dann neue zeile beginnen mit \n
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
	        		30, MTColor.BLACK, false));
			textareaPapierkorb.unregisterAllInputProcessors();
			textareaPapierkorb.setPickable(false);
			textareaPapierkorb.setNoFill(true);
			textareaPapierkorb.setNoStroke(true);
			textareaPapierkorb.setText("Ideen verworfen");
			textareaPapierkorb.setSizeLocal(200, 40);
			textareaPapierkorb.setPositionGlobal(new Vector3D(mtApp.width/10*2,40));
			area.addChild(textareaPapierkorb);
			
			
		
			MTTextArea textareaIdeas = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		30, MTColor.BLACK, false));
			textareaIdeas.unregisterAllInputProcessors();
			textareaIdeas.setPickable(false);
			textareaIdeas.setNoFill(true);
			textareaIdeas.setNoStroke(true);
			textareaIdeas.setText("Ideen verbleibend");
			textareaIdeas.setSizeLocal(200, 40);			 
			textareaIdeas.setPositionGlobal(new Vector3D(mtApp.width/2,40));
			area.addChild(textareaIdeas);
			
			
			MTTextArea textareaBestIdeas = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
	        		30, MTColor.BLACK, false));
			textareaBestIdeas.unregisterAllInputProcessors();
			textareaBestIdeas.setPickable(false);
			textareaBestIdeas.setNoFill(true);
			textareaBestIdeas.setNoStroke(true);
			textareaBestIdeas.setText("Ideen weiter");
			textareaBestIdeas.setSizeLocal(200, 40);
			textareaBestIdeas.setPositionGlobal(new Vector3D(mtApp.width/10*8,40));
			area.addChild(textareaBestIdeas);
	 }
	 
	 
	 
	 
	 
	 private void createSelectedIdea(ArrayList<Note> selectedCluster)
		{	
			//bereich von 400 -540 platz
			//mit mtlist bearbeiten, weil es auch sehr viele ideen sein k�nnen?
			int y=350;
			for(Note note : selectedCluster)
			{

				//breite und h�he an den text anpassen
				MTTextArea rText = new MTTextArea(mtApp);

				rText.setFillColor(MTColor.GREEN);
				rText.setStrokeColor(MTColor.LIME);
				rText.unregisterAllInputProcessors();
				rText.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 25, MTColor.WHITE, true));
				//rText.removeAllGestureEventListeners();
				rText.setPickable(true);
				rText.setText(formatString(note.getName(),80));				
				rText.setPositionGlobal(new Vector3D((mtApp.width/2)-(rText.getWidthXYVectLocal().x/2),y));
					 
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
										rubbish.add(new Note(baseComp.getText()));
										updateShowedNotes(baseComp);
										baseComp.destroy();					
										updateSideList(listLeft,rubbish);													
									}
									else if(centerPoint.x>  5*einheitX+75)
									{							 
										bestIdeas.add(new Note(baseComp.getText()));
										updateShowedNotes(baseComp);										
										baseComp.destroy();
										updateSideList(listRight, bestIdeas);
									}
									else if(centerPoint.x >  2*einheitX+75 && centerPoint.x < 5*einheitX+75 && centerPoint.y < 300 )
									{											
										moveBottomIdeasToTop();
										updateMiddleList();
										baseComp.destroy();
									}								
								}
								//Falls keine Elemente mehr drin sind
								if(allIdeas.size()==0 && bestIdeas.size()==1 && showedNotes.size()==0)
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
								if(allIdeas.size()==0 && bestIdeas.size()>1 && showedNotes.size()==0)
								{
									//rechten ideen in die mitte
									//alle restlichen in papierkorb
									//FinalScene gehen
									mtApp.pushScene();
									if (finalScene == null)
									{				
										//Im konstruktor m�ssen die ideen als 2 listen �bergeben werden
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
				showedNotes.add(note);
				y+=60;
			}
		}
	 
	 
	 private void updateShowedNotes(MTTextArea draggedTextarea)
	 {
		 //muss aus der liste showedNotes gel�scht werden
		 for(Note note :showedNotes)
		 {
			 String s1 = formatString(note.getName(),40);
			 
			 if(s1.equals(draggedTextarea.getText()))
			 {
				 showedNotes.remove(note);
				 break;
			 }
		 }
	 }
			 
		
}
