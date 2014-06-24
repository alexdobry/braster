package de.braster;

import java.util.ArrayList;
import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
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
	private MTColor ideaFillColor = new MTColor(0, 100, 0, 255);
	private MTColor ideaStrokeColor = new MTColor(34, 139, 34, 255);
 
	private MTEllipse area;	 
	private float einheitX;
	public static Vector3D trennlinieLinks;
	public static Vector3D trennlinieRechts;
	public static Vector3D trennlinieOben;
	
	public ArrayList<Cluster> clusterVerbleibend;
	public ArrayList<Cluster> clusterVerworfen;
	public ArrayList<Cluster> clusterWeiter;
	
	private Cluster showedCluster;
	public Cluster popupCluster;
	
	private ClusterPopup openClusterPopup; 
	public MTList listMiddle;
	
	private Iscene finalScene;
	private EvaluationScene tempScene;	
	public MTRectangle highlightLeft;
	public MTRectangle highlightMiddle;
	public MTRectangle highlightRight;
	private MTRoundRectangle neueIter; 
	
	public EvaluationScene( MTApplication mtApplication, String name)
	{		
		super(mtApplication, name);		
		
		tempScene = this;
		createStructureForIdeas(Idea.getAllParents());
		 
		//temporär
		
		
		
		 /*
	 	clusterVerbleibend = new ArrayList<Cluster>();
		 
	 	 
		ArrayList<Note> ideaTemp = new ArrayList<Note>();
		ideaTemp.add(new Note("fussball", "sport" ));
		Cluster cluster = new Cluster("sport",ideaTemp);
		clusterVerbleibend.add(cluster);
		
		ArrayList<Note> ideaTemp2 = new ArrayList<Note>();		 
		ideaTemp2.add(new Note("Einleitung", "Struktur"));			
		ideaTemp2.add(new Note("Zusammenfassung", "Struktur"));
		ideaTemp2.add(new Note("Fazit", "Struktur"));		
		Cluster cluster2 = new Cluster("Struktur",ideaTemp2);
		clusterVerbleibend.add(cluster2);
		
		ArrayList<Note> ideaTemp3 = new ArrayList<Note>();		 
		ideaTemp3.add(new Note("Anforderungsmanagement", "UNI"));			
		ideaTemp3.add(new Note("MCI", "UNI"));
		ideaTemp3.add(new Note("Noten", "UNI"));		
		Cluster cluster3 = new Cluster("UNI",ideaTemp3);
		clusterVerbleibend.add(cluster3);
		
		ArrayList<Note> ideaTemp5 = new ArrayList<Note>();
		ideaTemp5.add(new Note("hallllooo", "jas" ));
		Cluster cluster5 = new Cluster("jas",ideaTemp5);
		clusterVerbleibend.add(cluster5);
		
		ArrayList<Note> ideaTemp4 = new ArrayList<Note>();
		ideaTemp4.add(new Note("julia ist eine sehr sehr gute freundin", "freundin"));
		Cluster cluster4 = new Cluster("freundin",ideaTemp4);
		clusterVerbleibend.add(cluster4);
		 */
			 
		showedCluster = new Cluster();	
		clusterVerworfen = new ArrayList<Cluster>();
		clusterWeiter = new ArrayList<Cluster>();
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(new MTColor(235, 247, 248, 255));	
		
		createArea();	
		//Listen erzeugen
		listMiddle = new MTList(mtApp, trennlinieLinks.x+10, 80,  3*einheitX-20, 250);		 
		
		//dort passiert das liste fï¿½llen, was bereits implementiert habe
		updateMiddleList();		
		
		if (SetupScene.needHelp) {
			HelpOnScene help = new HelpOnScene(mtApp, mtApp.getWidth(), mtApp.getHeight(), StartBraster.helpEVA, 0.8f);
			canv.addChild(help);
		}
	}
	
	
	//constructor fï¿½r weitere Bewertungsrunde, wenn bereits alle Ideen einmal zugeordnet worden sind
	public EvaluationScene( MTApplication mtApplication, String name, ArrayList<Cluster> tideas, ArrayList<Cluster> trubbish)
	{		
		super(mtApplication, name);		
		tempScene = this;
		clusterVerbleibend = tideas;
		showedCluster = new Cluster();	
		clusterVerworfen = trubbish;
		clusterWeiter = new ArrayList<Cluster>();
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.listColor = new MTColor(211,211,211);
		this.setClearColor(new MTColor(235, 247, 248, 255));	
		
		createArea();	
		//Listen erzeugen
		listMiddle = new MTList(mtApp, trennlinieLinks.x+10, 80,  3*einheitX-20, 250);		 
		
		//dort passiert das liste fï¿½llen, was bereits implementiert habe
		updateMiddleList();	
		updateleftSide();
	}

	
	
	private void createArea()
	{		
		//Bereich zum Bewerten
		area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),mtApp.width/2-75, mtApp.height/10*8);
		area.setFillColor(new MTColor(listColor));
		area.setPickable(false);			
		this.canv.addChild(area);					

		highlightLeft = new MTRectangle(mtApp, StartBraster.evalHighlightLeft);
//		highLeft.setTexture(StartBraster.evalHighlightLeft);
		highlightLeft.setPickable(false);
		highlightLeft.setNoStroke(true);
		highlightLeft.setVisible(false);
		area.addChild(highlightLeft);
		
		highlightRight = new MTRectangle(mtApp,StartBraster.evalHighlightRight);
//		highRight.setTexture(StartBraster.evalHighlightRight);
		highlightRight.setPickable(false);
		highlightRight.setNoStroke(true);
		highlightRight.setVisible(false);
		area.addChild(highlightRight);
		
		highlightMiddle = new MTRectangle(mtApp, StartBraster.evalHighlightMiddle);
//		highMiddle.setTexture(StartBraster.evalHighlightMiddle);
		highlightMiddle.setPickable(false);
		highlightMiddle.setNoStroke(true);
		highlightMiddle.setVisible(false);
		area.addChild(highlightMiddle);
		
		einheitX = area.getRadiusX()*2/7;
		trennlinieLinks = new Vector3D(einheitX*2+75,0);
		trennlinieRechts = new Vector3D(einheitX*5+75,0);
		trennlinieOben = new Vector3D(0,70);
		
		//Trennlinie zum Papierkorb
		MTLine line1 = new MTLine(this.mtApp, trennlinieLinks.x, 0,   trennlinieLinks.x, area.getRadiusY());	 
		line1.setFillColor(MTColor.BLACK);
		line1.setStrokeWeight(5);
		line1.setPickable(false);
		area.addChild(line1);
					
		//Trennlinie zu den besseren Ideen
		MTLine line2 = new MTLine(this.mtApp, trennlinieRechts.x, 0 ,  trennlinieRechts.x, area.getRadiusY());  
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
		
		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, this.mtApp.width-240, this.mtApp.height-80, 0, 200, 60, 12, 12);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setFillColor(MTColor.GREY);  	
		mtRoundRectangle.registerInputProcessor(new TapProcessor(this.mtApp));
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				switch (te.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
						mtRoundRectangle.setFillColor(new MTColor(220,220,220,255));
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (te.isTapped()){
							//Save the current scene on the scene stack before changing
							mtApp.pushScene();
							if (finalScene == null){
								finalScene = new FinalScene(mtApp, "Final", SetupScene.getProblem(), clusterWeiter);
								//Konstruktor erweitern um Anzahl Spieler, da genau soviele
								//Tastaturen geladen werden
							//Add the scene to the mt application
							mtApp.addScene(finalScene);
							}
							//Do the scene change
							mtApp.changeScene(finalScene);
							
						}						
						mtRoundRectangle.setFillColor(MTColor.GREY);  	
						break;
					}				
				return false;
			}
		});
		
		MTTextArea rText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE));
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText("Bewertung beenden");
		
		mtRoundRectangle.scale(1.2f, 1.2f, 1, mtRoundRectangle.getCenterPointRelativeToParent());
		mtRoundRectangle.addChild(rText);
		rText.setPositionRelativeToParent(mtRoundRectangle.getCenterPointLocal());
		canv.addChild(mtRoundRectangle);
		
		
		//neue iteration
		neueIter = new MTRoundRectangle(this.mtApp, this.mtApp.width-240, this.mtApp.height-280, 0, 200, 60, 12, 12);
		neueIter.unregisterAllInputProcessors();
		neueIter.setFillColor(MTColor.GREY);  	
		neueIter.registerInputProcessor(new TapProcessor(this.mtApp));
		neueIter.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				
				switch (te.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
						neueIter.setFillColor(new MTColor(220,220,220,255));
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (te.isTapped()){
							mtApp.pushScene();
							if (finalScene == null)
							{				
								//Im konstruktor mï¿½ssen die ideen als 2 listen ï¿½bergeben werden
								finalScene = new EvaluationScene(mtApp, "Evaluation Again", clusterWeiter, clusterVerworfen);
								mtApp.addScene(finalScene);
							}
							//Do the scene change
							mtApp.changeScene(finalScene);
						}						
						neueIter.setFillColor(MTColor.GREY);  	
						break;
					}				
				return false;
			}
		});
		
		MTTextArea iterText = new MTTextArea(this.mtApp, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.WHITE));
		iterText.unregisterAllInputProcessors();
		iterText.setPickable(false);
		iterText.setNoFill(true);
		iterText.setNoStroke(true);
		iterText.setText("Neue Iteration");
		neueIter.setVisible(false);
		neueIter.scale(1.2f, 1.2f, 1, neueIter.getCenterPointRelativeToParent());
		neueIter.addChild(iterText);
		iterText.setPositionRelativeToParent(neueIter.getCenterPointLocal());
		
		canv.addChild(neueIter);	
	 }
	
	
	
	
	//Füllt die mittlere Liste mit den Clustern
	public void updateMiddleList()
	{
		//liste leeren und erstellen
		listMiddle.removeAllListElements();
		listMiddle.setNoFill(true);
		listMiddle.setNoStroke(true);
		area.addChild(listMiddle);
				
		int x = 5; 
		int y = 5;		
				
		//brauch updatemethode, sodass die zellenlänge vergrößert wird
		//Größe einer Zelle (standardhöhe)
		MTListCell cell = createListCell();
		
								
		//arraylist durchgehen
		//für jede arraylist das erste element anzeigen als ein symbol
		for(Cluster cluster : clusterVerbleibend)
		{
			int widthElement =0;
			MTTextArea textAreaIdee = null;
			MTTextArea ordner = null;
			//wenn im cluster nur eine idee ist, wird diese direkt angezeigt
			if(cluster.getNotes().size()==1)
			{
				textAreaIdee = new MTTextArea(mtApp);
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setFillColor(ideaFillColor);
				textAreaIdee.setStrokeColor(ideaStrokeColor);
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				textAreaIdee.removeAllGestureEventListeners();
				textAreaIdee.setPickable(false);
				widthElement = (int) textAreaIdee.getWidthXYVectLocal().length();
			}
			//andernfalls ein ordner objekt mit namen drauf
			else
			{
				ordner = new MTTextArea(mtApp);
				ordner.setText(cluster.getName());	
				ordner.setFillColor(new MTColor(139,69,0,255));
				ordner.setStrokeColor(new MTColor(205,133,0,255));
				ordner.unregisterAllInputProcessors();
				ordner.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(false);
				widthElement = (int) ordner.getWidthXYVectLocal().length();	
			}				
			
			//überprüfung ob das element reinpasst in die zeile noch
			if(x + widthElement  > 3*einheitX)  //nur wenn element reinpasst
			{
				x = 5;   //x wieder auf zeilenbeginn setzen
				cell = createListCell();				
			}
					
			if(cluster.getNotes().size()==1)
			{
				cell.addChild(textAreaIdee);
				textAreaIdee.setPositionRelativeToParent(new Vector3D(x+textAreaIdee.getWidthXYVectLocal().length()/2,20));
			}
			else
			{
				cell.addChild(ordner);
				ordner.setPositionRelativeToParent(new Vector3D(x+ordner.getWidthXYVectLocal().length()/2,20));
			}	
			x += widthElement +20;		//x erhöhen um länge des elements + abstand 20						
		}				
	}
	
	
	//Erstellt eine Zelle für die Liste und beinhaltet das Click Event, um Cluster aus der Liste nach unten zuschieben
	private MTListCell createListCell()
	{
		MTListCell cell = new MTListCell(this.mtApp,3*einheitX-20, 40);
		cell.setPickable(true);
		cell.setNoStroke(true);
		cell.setNoFill(true);
		cell.unregisterAllInputProcessors();
		listMiddle.addChild(cell);		
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
							MTTextArea actualTextarea = (MTTextArea) selectedComponent;
							@SuppressWarnings("deprecation")
							float halbeLaengeTextarea = actualTextarea.getWidthXYVectLocal().length()/2;
							//wenn in x-Richtung zwischen die maximalen Ausmaï¿½e geklickt wurde
							if(actualTextarea.getCenterPointGlobal().x - halbeLaengeTextarea < clickedPosition.x && actualTextarea.getCenterPointGlobal().x + halbeLaengeTextarea > clickedPosition.x)
							{
								//falls unten eine drin ist, die wieder nach oben schieben als cluster
								 
								if(showedCluster !=null && showedCluster.getNotes().size()>0)  //falls aktuell ideen angezeigt werden, müssen diese in cluster zurückbefördert werden
								{
									moveShowedClusterToList(); 
								}						 
								//selektierte aus der liste lï¿½schen
							 
								Cluster selectedCluster = removeFromClusterList(actualTextarea, clusterVerbleibend);
								//element lï¿½schen
								actualTextarea.destroy();
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
	
	
	//Sucht vorgegebene textarea in der clusterliste und löscht diese raus und gibt sie zurück
	private Cluster removeFromClusterList(MTTextArea idee, ArrayList<Cluster> clusterlist)
	{
		for(Cluster cluster : clusterlist)
		{			
			if(cluster.getName().equals(idee.getText()))
			{
				clusterlist.remove(cluster);
				return cluster;
			}
			for(Note note : cluster.getNotes())
			{				 
				if(note.getName().equals(idee.getText()))
				{						 
					if(cluster.getNotes().size()==1)
					{
						clusterlist.remove(cluster);
						return cluster;					 
					}
					return cluster;
				}
			}
		}
		return null;
	}	
	
	
	//Löscht die aktuell angezeigten Ideen und schiebt sie in die Liste zurück
	private void moveShowedClusterToList()
	{
		if(showedCluster !=null && showedCluster.getNotes().size() >0)
		{
		//showedCluster wieder nach oben schieben
		clusterVerbleibend.add(showedCluster);		 
		showedCluster = null;
		//alle kinder der area löschen die vom Typ MTtextarea sind und in dem bereich liegen
		for(MTComponent comp : area.getChildren())
		{
			try
			{
				MTTextArea text = (MTTextArea)comp;
				//Ausnahme einfügen fï¿½r die Textareas im tabellenkopf
				if(text.getCenterPointGlobal().x> trennlinieLinks.x && text.getCenterPointGlobal().x<trennlinieRechts.x && text.getCenterPointGlobal().y > trennlinieOben.y)
				{
					area.removeChild(text);
				}				
			}
			catch(Exception castexception)
			{
				continue;
			}
		}
		}
	}
	
	
	//Erstellt die selektierten Cluster unten in groß
	private void createSelectedIdea(Cluster selectedCluster)
	{	
		//bereich von 350 -540 platz		 
		int y=350;
		showedCluster = selectedCluster;
		for(Note note : selectedCluster.getNotes())
		{			 
			final MTTextArea rText = new MTTextArea(mtApp);
			rText.setFillColor(ideaFillColor);
			rText.setStrokeColor(ideaStrokeColor);
			rText.unregisterAllInputProcessors();
			rText.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 25, MTColor.WHITE, true));		 
			rText.setPickable(true);
			rText.setText(note.getName());				
			rText.setPositionGlobal(new Vector3D((mtApp.width/2),y));
			area.addChild(rText);
			
			y+=50;  //abstand der elemente
			 
			rText.registerInputProcessor(new DragProcessor(getMTApplication()));		 
			rText.setGestureAllowance(ScaleProcessor.class, true);
			rText.setGestureAllowance(RotateProcessor.class, true);
			rText.addGestureListener(DragProcessor.class, new IGestureEventListener() {				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					DragEvent de = (DragEvent)ge;
					
					switch (de.getId()) {
					case DragEvent.GESTURE_STARTED:						 
						rText.setFillColor(new MTColor(220,220,220,255));
						break;
					case DragEvent.GESTURE_UPDATED:	
						if (de.getDragCursor().getCurrentEvtPosX() < trennlinieLinks.x) {
							highlightLeft.setVisible(true);
						} else if (de.getDragCursor().getCurrentEvtPosX() > trennlinieRechts.x) {
							highlightRight.setVisible(true);
						} else {
							highlightLeft.setVisible(false);
							highlightMiddle.setVisible(false);
							highlightRight.setVisible(false);
						}
						
						break;
					case DragEvent.GESTURE_ENDED:
						rText.setFillColor(ideaFillColor);
						Vector3D centerPoint = rText.getCenterPointRelativeToParent();						
						if(centerPoint.x <  trennlinieLinks.x) //wird nach links verschoben
						{			
							boolean added = false;
							//überprüfung ob der cluster bereits besteht
							for(Cluster leftCluster : clusterVerworfen)
							{								
								if(leftCluster.getName().equals(showedCluster.getName()))
								{
									leftCluster.getNotes().add(new Note(rText.getText(),showedCluster.getName()));
									added=true;
									break;
								}											
							}
							if(added==false)
							{								
								ArrayList<Note> newNotes = new ArrayList<Note>();
								newNotes.add(new Note(rText.getText(),showedCluster.getName()));
								Cluster newCluster = new Cluster(showedCluster.getName(), newNotes);
								clusterVerworfen.add(newCluster);
							}
							updateShowedNotes(rText);
							rText.destroy();					
							updateleftSide();
						}
						else if(centerPoint.x > trennlinieRechts.x) //wird nach rechts verschoben
						{							 
							boolean added = false;
							//überprüfung ob der cluster bereits besteht
							for(Cluster rightCluster : clusterWeiter)
							{
								if(rightCluster.getName().equals(showedCluster.getName()))
								{
									rightCluster.getNotes().add(new Note(rText.getText(), showedCluster.getName()));
									added=true;
									break;
								}											
							}
							if(added==false)
							{											
								ArrayList<Note> newNotes = new ArrayList<Note>();
								newNotes.add(new Note(rText.getText(),showedCluster.getName()));
								Cluster newCluster = new Cluster(showedCluster.getName(), newNotes);
								clusterWeiter.add(newCluster);
							}
							updateShowedNotes(rText);
							rText.destroy();					
							updateRightSide();	
						}
					 	
						
						highlightLeft.setVisible(false);
						highlightMiddle.setVisible(false);
						highlightRight.setVisible(false);
						break;
					}
					//Falls keine Elemente mehr drin sind
					
					if(clusterVerbleibend.size()==0 && clusterWeiter.size()==1 && showedCluster == null)
					{						 
//						//FinalScene gehen
//						mtApp.pushScene();							
//						if (finalScene == null){
//							finalScene = new FinalScene(mtApp, "Final", SetupScene.getProblem(), clusterWeiter);
//							mtApp.addScene(finalScene);
//						}
//						//Do the scene change
//						mtApp.changeScene(finalScene);
						neueIter.setVisible(true);						
					}
					if(clusterVerbleibend.size()==0 && clusterWeiter.size()>1 && showedCluster==null)
					{
						//rechten ideen in die mitte
						//alle restlichen in papierkorb
						//FinalScene gehen
//						mtApp.pushScene();
//						if (finalScene == null)
//						{				
//							//Im konstruktor mï¿½ssen die ideen als 2 listen ï¿½bergeben werden
//							finalScene = new EvaluationScene(mtApp, "Evaluation Again", clusterWeiter, clusterVerworfen);
//							mtApp.addScene(finalScene);
//						}
//						//Do the scene change
//						mtApp.changeScene(finalScene);
						neueIter.setVisible(true);
					}				 
				
				return false;
				}
		});
			
		  			
		}
	}
	
	
	
	//wenn neue idee hinzugefügt wird, muss die anzeige links aktualisiert werden
	public void updateleftSide()
	{		
		//Liste leeren		 
		for(MTComponent component : area.getChildren())
		{
			if(component.toString().contains("MTText"))
			{
				MTTextArea textTemp = (MTTextArea) component;
				if(textTemp.getCenterPointGlobal().x < trennlinieLinks.x && textTemp.getCenterPointGlobal().y > trennlinieOben.y)
				{
					area.removeChild(component);
					continue;
				}
			}
		}
					
		float x = einheitX+75;
		float y =  100;					
		 
		
		
		//arraylist durchgehen
		//für jede arraylist das erste element anzeigen als ein symbol
		for(final Cluster cluster : clusterVerworfen)
		{	 
			if(cluster.getNotes().size()==0)
			{
				clusterVerworfen.remove(cluster);
				continue;
			}
			final MTTextArea textAreaIdee = new MTTextArea(mtApp);
			final MTTextArea ordner = new MTTextArea(mtApp);
			//wenn im cluster nur eine idee ist, wird diese direkt angezeigt
			if(cluster.getNotes().size()==1)
			{				
				textAreaIdee.setText(cluster.getNotes().get(0).getName());	
				textAreaIdee.setFillColor(ideaFillColor);
				textAreaIdee.setStrokeColor(ideaStrokeColor);				 
				textAreaIdee.unregisterAllInputProcessors();
				textAreaIdee.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				textAreaIdee.setPickable(true);				
				textAreaIdee.registerInputProcessor(new DragProcessor(getMTApplication()));
				textAreaIdee.addGestureListener(DragProcessor.class, new DefaultDragAction() {
					public boolean processGestureEvent(MTGestureEvent g) {
						DragEvent de = (DragEvent)g;
						switch (de.getId()) {
							case MTGestureEvent.GESTURE_STARTED:								 
								 textAreaIdee.setFillColor(new MTColor(220,220,220,255));
								 moveShowedClusterToList();
								 updateMiddleList();
								 break;		
							case MTGestureEvent.GESTURE_UPDATED:
								if (de.getDragCursor().getCurrentEvtPosX() > trennlinieLinks.x && de.getDragCursor().getCurrentEvtPosX() < trennlinieRechts.x) {
									highlightMiddle.setVisible(true);
									highlightRight.setVisible(false);
								} else if (de.getDragCursor().getCurrentEvtPosX() > trennlinieRechts.x) {
									highlightRight.setVisible(true);
									highlightMiddle.setVisible(false);
								} else {
									highlightLeft.setVisible(false);
									highlightMiddle.setVisible(false);
									highlightRight.setVisible(false);
								}
								break;
							case MTGestureEvent.GESTURE_ENDED:	
								 textAreaIdee.setFillColor(ideaFillColor);
								 Vector3D centerPoint = textAreaIdee.getCenterPointRelativeToParent();
								 String ideenText = cluster.getName();
								 if(centerPoint.x >  trennlinieLinks.x && centerPoint.x < trennlinieRechts.x)
								 {
									 boolean added = false;
									 //überprüfung ob der cluster bereits besteht
									 for(Cluster middleCluster : clusterVerbleibend)
									 {
										if(middleCluster.getName().equals(ideenText))
										{
											middleCluster.getNotes().add(new Note(textAreaIdee.getText(),cluster.getNotes().get(0).getClusterName()));
											added=true;
											break;
										}	 										
									 }
									if(added==false)
									{
										ArrayList<Note> newNotes = new ArrayList<Note>();
										newNotes.add(new Note(textAreaIdee.getText()));
										Cluster newCluster = new Cluster(ideenText, newNotes);
										clusterVerbleibend.add(newCluster);
									}
									removeFromClusterList(textAreaIdee,clusterVerworfen);
									textAreaIdee.destroy();										
									updateMiddleList();
									updateleftSide();								
								}
								else if(centerPoint.x> trennlinieRechts.x)
								{							 
									boolean added = false;
									 //überprüfung ob der cluster bereits besteht
									 for(Cluster rightCluster : clusterWeiter)
									 {
										if(rightCluster.getName().equals(ideenText))
										{
											rightCluster.getNotes().add(new Note(textAreaIdee.getText(),cluster.getNotes().get(0).getClusterName()));
											added=true;
											break;
										}	 										
									 }
									if(added==false)
									{
										ArrayList<Note> newNotes = new ArrayList<Note>();
										newNotes.add(new Note(textAreaIdee.getText()));
										Cluster newCluster = new Cluster(ideenText, newNotes);
										clusterWeiter.add(newCluster);
									}
									removeFromClusterList(textAreaIdee,clusterVerworfen);
									textAreaIdee.destroy();									 
									updateRightSide();	
									updateleftSide();									
								}
								 highlightLeft.setVisible(false);
									highlightMiddle.setVisible(false);
									highlightRight.setVisible(false);
								 break;
							} 
						  
						return false;
					}
				});  
			}
			
			//andernfalls ein ordner objekt mit namen drauf
			else
			{				
				ordner.setText(cluster.getName());	
				ordner.setFillColor(new MTColor(139,69,0,255));
				ordner.setStrokeColor(new MTColor(205,133,0,255));
				ordner.unregisterAllInputProcessors();
				ordner.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
				ordner.removeAllGestureEventListeners();
				ordner.setPickable(true);
				ordner.registerInputProcessor(new TapProcessor(mtApp));
				
				ordner.addGestureListener(TapProcessor.class, new IGestureEventListener() {
					public boolean processGestureEvent(MTGestureEvent ge) {
						TapEvent te = (TapEvent)ge;
						
						if (te.isTapped() && popupCluster==null) //und wenn dieser nicht bereits geöffnet ist  
						{		
							//öffnet sich clusterpopup
							moveShowedClusterToList();
							updateMiddleList();
							popupCluster = cluster;
							clusterVerworfen.remove(popupCluster);
							openClusterPopup = new ClusterPopup(mtApp, 250, 100, popupCluster,  tempScene,0 );
							openClusterPopup.setPositionGlobal(new Vector3D(ordner.getCenterPointGlobal().x,ordner.getCenterPointGlobal().y+ openClusterPopup.getHeightXYVectLocal().length()/2+15));
							area.addChild(openClusterPopup);							
							 
						}
						else if(te.isTapped() && popupCluster.getName().equals(cluster.getName()))//und bereits geöffnet -> dann schließe den
						{			 
							moveShowedClusterToList();
							updateMiddleList();
							clusterVerworfen.add(popupCluster);
							popupCluster=null;
							openClusterPopup.destroy();						
							updateleftSide();							
							updateRightSide();
						}
						else if(te.isTapped())  //wenn neue cluster geöffnet wird
						{							 
							clusterVerworfen.add(popupCluster);							 
							popupCluster = cluster;
							openClusterPopup.destroy();
							openClusterPopup = new ClusterPopup(mtApp, 250, 100, popupCluster , tempScene,0 );
							openClusterPopup.setPositionGlobal(new Vector3D(ordner.getCenterPointGlobal().x,ordner.getCenterPointGlobal().y+ openClusterPopup.getHeightXYVectLocal().length()/2+15));
							area.addChild(openClusterPopup);							
						}
					
						return false;
					}
				});			
			}			
			
			
			//überprüfung ob das element reinpasst in die zeile noch
			//wenn ja alle elemente des clusters adden
			//wenn nicht nächste zeile beginnen
			
					
			if(cluster.getNotes().size()==1)
			{
				area.addChild(textAreaIdee);
				textAreaIdee.setPositionGlobal(new Vector3D(x,y));
				//textAreaIdee.setPositionRelativeToParent(new Vector3D(x+ textAreaIdee.getWidthXYVectLocal().length()/2,y));				
			}
			else
			{
				area.addChild(ordner);
				//ordner.setPositionRelativeToParent(new Vector3D(x+ordner.getWidthXYVectLocal().length()/2,y));
				ordner.setPositionGlobal(new Vector3D(x ,y));
				
			}
			y+= 35;
		}						
	}
	
	
	
	//wenn neue idee hinzugefügt wird, muss die anzeige rechts aktualisiert werden
		public void updateRightSide()
		{		
			//Liste leeren		 
			for(MTComponent component : area.getChildren())
			{
				if(component.toString().contains("MTText"))
				{
					MTTextArea textTemp = (MTTextArea) component;
					if(textTemp.getCenterPointGlobal().x > trennlinieRechts.x && textTemp.getCenterPointGlobal().y > trennlinieOben.y)
					{
						area.removeChild(component);
						continue;
					}
				}
			}
					
			float x = trennlinieRechts.x+einheitX;
			float y =  100;					
			 						
			//arraylist durchgehen
			//für jede arraylist das erste element anzeigen als ein symbol
			for(final Cluster cluster : clusterWeiter)
			{			
				final MTTextArea textAreaIdee = new MTTextArea(mtApp);
				final MTTextArea ordner =  new MTTextArea(mtApp);
				//wenn im cluster nur eine idee ist, wird diese direkt angezeigt
				if(cluster.getNotes().size()==1)
				{				
					textAreaIdee.setText(cluster.getNotes().get(0).getName());	
					textAreaIdee.setFillColor(ideaFillColor);
					textAreaIdee.setStrokeColor(ideaStrokeColor);
					textAreaIdee.unregisterAllInputProcessors();
					textAreaIdee.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
					textAreaIdee.setPickable(true);					 
					textAreaIdee.registerInputProcessor(new DragProcessor(getMTApplication()));
					textAreaIdee.addGestureListener(DragProcessor.class, new DefaultDragAction() {
						public boolean processGestureEvent(MTGestureEvent g) {
							DragEvent de = (DragEvent)g;
							switch (de.getId()) {
								case MTGestureEvent.GESTURE_STARTED:								 
									 textAreaIdee.setFillColor(new MTColor(220,220,220,255));
										moveShowedClusterToList();
										updateMiddleList();
									 break;		
								case MTGestureEvent.GESTURE_UPDATED:
									if (de.getDragCursor().getCurrentEvtPosX() > trennlinieLinks.x && de.getDragCursor().getCurrentEvtPosX() < trennlinieRechts.x) {
										highlightMiddle.setVisible(true);
										highlightLeft.setVisible(false);
									} else if (de.getDragCursor().getCurrentEvtPosX() < trennlinieLinks.x) {
										highlightMiddle.setVisible(false);
										highlightLeft.setVisible(true);
									} else {
										highlightLeft.setVisible(false);
										highlightMiddle.setVisible(false);
										highlightRight.setVisible(false);
									}
									break;
								case MTGestureEvent.GESTURE_ENDED:	
									textAreaIdee.setFillColor(ideaFillColor);
									 Vector3D centerPoint = textAreaIdee.getCenterPointRelativeToParent();
									 String ideenText = cluster.getName();
									 if(centerPoint.x >  trennlinieLinks.x && centerPoint.x < trennlinieRechts.x)
									 {
										 boolean added = false;
										 //überprüfung ob der cluster bereits besteht
										 for(Cluster middleCluster : clusterVerbleibend)
										 {
											if(middleCluster.getName().equals(ideenText))
											{
												middleCluster.getNotes().add(new Note(textAreaIdee.getText(),cluster.getNotes().get(0).getClusterName()));
												added=true;
												break;
											}	 										
										 }
										if(added==false)
										{
											ArrayList<Note> newNotes = new ArrayList<Note>();
											newNotes.add(new Note(textAreaIdee.getText()));
											Cluster newCluster = new Cluster(ideenText, newNotes);
											clusterVerbleibend.add(newCluster);
										}
										removeFromClusterList(textAreaIdee,clusterWeiter);
										textAreaIdee.destroy();										 
										updateMiddleList();
										updateRightSide();
									}
									else if(centerPoint.x < trennlinieLinks.x)
									{							 
										boolean added = false;
										 //überprüfung ob der cluster bereits besteht
										 for(Cluster leftCluster : clusterVerworfen)
										 {
											if(leftCluster.getName().equals(ideenText))
											{
												leftCluster.getNotes().add(new Note(textAreaIdee.getText(),cluster.getNotes().get(0).getClusterName()));
												added=true;
												break;
											}	 										
										 }
										if(added==false)
										{
											ArrayList<Note> newNotes = new ArrayList<Note>();
											newNotes.add(new Note(textAreaIdee.getText()));
											Cluster newCluster = new Cluster(ideenText, newNotes);
											clusterVerworfen.add(newCluster);
										}
										removeFromClusterList(textAreaIdee,clusterWeiter);
										textAreaIdee.destroy();										
										updateleftSide();
										updateRightSide();
									}
									 
									highlightLeft.setVisible(false);
									highlightMiddle.setVisible(false);
									highlightRight.setVisible(false);
									 break;
								} 
							  
							return false;
						}
					});   
				}
				
				//andernfalls ein ordner objekt mit namen drauf
				else
				{					
					ordner.setText(cluster.getName());	
					ordner.setFillColor(new MTColor(139,69,0,255));
					ordner.setStrokeColor(new MTColor(205,133,0,255));
					ordner.unregisterAllInputProcessors();
					ordner.setFont(FontManager.getInstance().createFont(mtApp, "arial.ttf", 14, MTColor.WHITE, true));
					ordner.removeAllGestureEventListeners();
					ordner.setPickable(true);
					ordner.registerInputProcessor(new TapProcessor(mtApp));
					ordner.addGestureListener(TapProcessor.class, new IGestureEventListener() {
						public boolean processGestureEvent(MTGestureEvent ge) {
							TapEvent te = (TapEvent)ge;
							
							if (te.isTapped() && popupCluster==null) //und wenn dieser nicht bereits geöffnet ist  
							{		
								//öffnet sich clusterpopup
								moveShowedClusterToList();
								updateMiddleList();
								popupCluster = cluster;
								clusterWeiter.remove(popupCluster);
								openClusterPopup = new ClusterPopup(mtApp, 250, 100, popupCluster,  tempScene,1 );
								openClusterPopup.setPositionGlobal(new Vector3D(ordner.getCenterPointGlobal().x,ordner.getCenterPointGlobal().y+ openClusterPopup.getHeightXYVectLocal().length()/2+15));
								area.addChild(openClusterPopup);							 
							}
							else if(te.isTapped() && popupCluster.getName().equals(cluster.getName()))//und bereits geöffnet -> dann schließe den
							{		
								moveShowedClusterToList();
								updateMiddleList();
								clusterWeiter.add(popupCluster);
								popupCluster=null;
								openClusterPopup.destroy();								 
								updateleftSide();							 
								updateRightSide();
							}
							else if(te.isTapped())  //wenn neue cluster geöffnet wird
							{		
								clusterWeiter.add(popupCluster);							 
								popupCluster = cluster;
								openClusterPopup.destroy();
								openClusterPopup = new ClusterPopup(mtApp, 250, 100, popupCluster , tempScene,1 );
								openClusterPopup.setPositionGlobal(new Vector3D(ordner.getCenterPointGlobal().x,ordner.getCenterPointGlobal().y+ openClusterPopup.getHeightXYVectLocal().length()/2+15));
								area.addChild(openClusterPopup);
								moveShowedClusterToList();
							}
							return false;
						}
					});
					 			
				}			
				
				
				//überprüfung ob das element reinpasst in die zeile noch
				//wenn ja alle elemente des clusters adden
				//wenn nicht nächste zeile beginnen
				
						
				if(cluster.getNotes().size()==1)
				{
					area.addChild(textAreaIdee);	
					textAreaIdee.setPositionGlobal(new Vector3D(x,y));					
				}
				else
				{
					area.addChild(ordner);
					ordner.setPositionGlobal(new Vector3D(x,y));						
				}	
				y+= 35;
			}						
		}
	
	
	//wenn showedNote verschoben wird, muss die Datenstruktur geupdated werden, weil eine note raus muss
	 private void updateShowedNotes(MTTextArea draggedTextarea)
	 {
		 //muss aus der liste showedNotes gelï¿½scht werden
		 for(Note note : showedCluster.getNotes())
		 {			 
			 if(note.getName().equals(draggedTextarea.getText()))
			 {
				 showedCluster.getNotes().remove(note);
				 if(showedCluster.getNotes().size()==0)
				 {
					 showedCluster=null;
				 }
				 break;
			 }
		 }
	 }
	 
	 
	 
	//Aus dem Datenmodell von Patrick eine angepasste Version machen
	//in allIdeas gibst fï¿½r jeden CLuster eine Liste der Ideen
	private void createStructureForIdeas(LinkedList<Idea> allParents)
	{		
		clusterVerbleibend = new ArrayList<Cluster>();
		for(Idea idea : allParents)
		{
			//falls kein child hat, ist es nur eine einzelne idee
			//sonst erste idee die kategorie
			//children sind die ideen
			if(idea.getChildren().length==0)
			{
				ArrayList<Note> ideasTemp = new ArrayList<Note>();
				ideasTemp.add(new Note(idea.getText(), idea.getText()));				
				Cluster cluster = new Cluster(idea.getText(),ideasTemp);
				clusterVerbleibend.add(cluster);
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
				clusterVerbleibend.add(newCluster);								
			}
		}
	}
 

}
