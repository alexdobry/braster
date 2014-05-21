package de.braster;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.interfaces.IMTComponent3D;
import org.mt4j.components.visibleComponents.shapes.MTEllipse;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class EvaluationScene extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
	private ArrayList<Note> ideas;
    private ArrayList<MTRoundRectangle> notes;
    private MTEllipse area;
	/** The last event. */
	private MTGestureEvent lastEvent;
	/** The use custom target. */
	private boolean useCustomTarget;
	/** The drag target. */
	private IMTComponent3D dragTarget;

	
	public EvaluationScene( MTApplication mtApplication, String name)
	{
		
		
		
		super(mtApplication, name);
		
		
		//muss noch die ganzen ideen aus dem clustern übergeben bekommen
		//solange templiste mit ideen
		ideas = new ArrayList<Note>();
		notes = new ArrayList<MTRoundRectangle>();
				
		Note n1 = new Note("1");
		ideas.add(n1);
		
		Note n2 = new Note("2");
		ideas.add(n2);
		
		Note n3 = new Note("3");
		ideas.add(n3);
		
		Note n4 = new Note("4");
		ideas.add(n4);
		
		Note n5 = new Note("5");
		ideas.add(n5);
		
		Note n6 = new Note("6");
		ideas.add(n6);
		
				
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
	
		this.setClearColor(MTColor.WHITE);		
		
		//Bereich
		area = new MTEllipse(this.mtApp, new Vector3D(this.mtApp.width/2,0),600, 600);
		area.setFillColor(new MTColor(135,206,250));
		area.setPickable(false);
		this.canv.addChild(area);
				
		MTLine line1 = new MTLine(this.mtApp, 400, 0, 400, 500);
		line1.setFillColor(MTColor.BLACK);
		line1.setPickable(false);
		area.addChild(line1);
				
		MTLine line2 = new MTLine(this.mtApp, this.mtApp.width-400, 0, this.mtApp.width-400, 500);
		line2.setFillColor(MTColor.BLACK);
		line2.setPickable(false);
		area.addChild(line2);
		
		//diese dann alle in der mitte anzeigen lassen	
		//oben mitte alle ideen als icons
		//selektierte dann weiter unten großzeigen
		//problembeschreibung unter dem kreis
		
		
		//möglichkeiten
		//1. grid erzeugen als komponente
		//2. platz ausrechnen und einfach durch iterieren
		//problem? was bei sehr vielen ideen
		//bereich defnieren mit scrollwindow?
		
		int x =410;
		int y= 10;	
		for(Note n: this.ideas)
		{			
			if(x > this.mtApp.width-510) 
			{
				y+=40;
				x=410;
			}
			createNoteItem(n.getName(), x , y );
			x += 120;		  
		}
		//bei anklicken müssen die ideen groß angezeigt werden
		//bei geste nach rechts nach links verschieben in papierkorb
		//bei nach rechts in den ordner für bessere ideen verschieben		
	}
	
	
	
	
	private void createNoteItem(String name, int x, int y)
	{
		IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		10, MTColor.BLACK, false);
		
		//Algorithmuss überlegen auf welche Position die Idee kommt, da sonst alle übereinenader gestapelt sind
		//dazu Höhe auslesen, in welchem Bereich diese liegen dürfen
		
		final MTRoundRectangle mtRoundRectangle = new MTRoundRectangle(this.mtApp, x, y, 0, 100, 30, 12, 12);
		mtRoundRectangle.setSizeLocal(100,30);
		mtRoundRectangle.unregisterAllInputProcessors();
		mtRoundRectangle.setStrokeColor(MTColor.WHITE); 
 
		
		MTTextArea rText = new MTTextArea(this.mtApp, font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(name);
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
							
						translate(dragTarget, dragEvent);
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						translate(dragTarget, dragEvent);
						break;
					case MTGestureEvent.GESTURE_CANCELED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						break;
					default:
						break;
					}
				}
				return false;
			}
		});
		mtRoundRectangle.addGestureListener(TapProcessor.class, new IGestureEventListener() {

			@SuppressWarnings("deprecation")
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
						 		
					//die idee wird vergrößert dargestellt
					//alle andere ideen werden verkleinert wieder					
					//je nach vorherigem status
				

				}
				return false;
			}
		});
	
		//this.clearAllGestures(mtRoundRectangle);
		mtRoundRectangle.registerInputProcessor(new FlickProcessor(300, 5));
		mtRoundRectangle.addGestureListener(FlickProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				FlickEvent e = (FlickEvent)ge;
				if (e.getId() == MTGestureEvent.GESTURE_ENDED)
				{
					mtRoundRectangle.setFillColor(MTColor.AQUA);
					
				}
					
				return false;
			}
		});
		 
		this.area.addChild(mtRoundRectangle);
		this.notes.add(mtRoundRectangle);
		
	}

}
