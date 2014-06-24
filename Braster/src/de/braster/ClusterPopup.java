package de.braster;

import java.util.ArrayList;

import org.mt4j.MTApplication;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTLine;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.gestureAction.DefaultDragAction;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;

public class ClusterPopup  extends MTRectangle{
	
	private MTApplication mtApp;
	private EvaluationScene caller;
	private ClusterPopup popup;
	private int side;	

	private MTColor ideaFillColor = new MTColor(0, 100, 0, 255);
	private MTColor ideaStrokeColor = new MTColor(34, 139, 34, 255);
	
	@SuppressWarnings("deprecation")
	public ClusterPopup(MTApplication pApplet, float width, float height, final Cluster cluster, 
			  EvaluationScene call, int seite) {
		super(pApplet, width, height);
		
		 
		side = seite;
		caller =call;
		popup = this;
		mtApp = pApplet;
		this.setFillColor(MTColor.WHITE);
		this.setStrokeColor(MTColor.BLACK);
		this.setSizeLocal(250, cluster.getNotes().size()*34+40); //noch statisch
		this.setPickable(false);
		
		float x = 5;
		float y = 5;
		
		MTTextArea textareaClustername = new MTTextArea(pApplet);
		textareaClustername.setText(cluster.getName());	
		textareaClustername.setFillColor(new MTColor(139,69,0,255));
		textareaClustername.setStrokeColor(new MTColor(205,133,0,255));
		width = textareaClustername.getWidthXYVectLocal().length();
		textareaClustername.unregisterAllInputProcessors();
		textareaClustername.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 14, MTColor.WHITE, true));
		textareaClustername.setPickable(false);	
		textareaClustername.setPositionRelativeToParent(new Vector3D(x+width/2,20));
		this.addChild(textareaClustername);
		
		MTLine line = new MTLine(mtApp, 5, 40,245, 40);  
		line.setFillColor(MTColor.BLACK);
		line.setStrokeColor(MTColor.BLACK);
		line.setStrokeWeight(1);
		line.setPickable(false);
		this.addChild(line);
		
		y= 57;
		for(final Note actualNote : cluster.getNotes())
		{
			final MTTextArea textareaIdee = new MTTextArea(pApplet);
			textareaIdee.setText(actualNote.getName());	
			textareaIdee.setFillColor(ideaFillColor);
			textareaIdee.setStrokeColor(ideaStrokeColor);
			width = textareaIdee.getWidthXYVectLocal().length();
			textareaIdee.unregisterAllInputProcessors();
			textareaIdee.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 14, MTColor.WHITE, true));
			textareaIdee.setPickable(true);
			textareaIdee.setPositionRelativeToParent(new Vector3D(x+width/2,y));
			this.addChild(textareaIdee);
			//ideen können gedragt werden> event adden
			textareaIdee.registerInputProcessor(new DragProcessor(mtApp));
			textareaIdee.addGestureListener(DragProcessor.class, new DefaultDragAction() {
				public boolean processGestureEvent(MTGestureEvent g) {
					DragEvent de = (DragEvent)g;
					 
					switch (de.getId()) {
						case MTGestureEvent.GESTURE_STARTED:								 
							textareaIdee.setFillColor(new MTColor(220,220,220,255));
							
							 break;		
						case MTGestureEvent.GESTURE_UPDATED:
							if (side == 1 && de.getDragCursor().getCurrentEvtPosX() > EvaluationScene.trennlinieLinks.x && de.getDragCursor().getCurrentEvtPosX() < EvaluationScene.trennlinieRechts.x) {
								caller.highlightMiddle.setVisible(true);
								caller.highlightLeft.setVisible(false);
							} else if (side == 1 && de.getDragCursor().getCurrentEvtPosX() < EvaluationScene.trennlinieLinks.x) {
								caller.highlightMiddle.setVisible(false);
								caller.highlightLeft.setVisible(true);
							} else if (side == 0 && de.getDragCursor().getCurrentEvtPosX() > EvaluationScene.trennlinieLinks.x && de.getDragCursor().getCurrentEvtPosX() < EvaluationScene.trennlinieRechts.x) {
								caller.highlightMiddle.setVisible(true);
								caller.highlightRight.setVisible(false);
							} else if (side == 0 && de.getDragCursor().getCurrentEvtPosX() > EvaluationScene.trennlinieRechts.x) {
								caller.highlightRight.setVisible(true);
								caller.highlightMiddle.setVisible(false);
							} else {
								caller.highlightLeft.setVisible(false);
								caller.highlightMiddle.setVisible(false);
								caller.highlightRight.setVisible(false);
							}
							
							break;
						case MTGestureEvent.GESTURE_ENDED:									 								
							textareaIdee.setFillColor(ideaFillColor);
							caller.highlightLeft.setVisible(false);
							caller.highlightMiddle.setVisible(false);
							caller.highlightRight.setVisible(false);
							 Vector3D centerPoint = textareaIdee.getCenterPointGlobal();
							 String ideenText = cluster.getName();
							//von links nach rechts
							if(side ==0  && centerPoint.x> EvaluationScene.trennlinieRechts.x)
							{							 
								 boolean added = false;
								 //überprüfung ob der cluster bereits besteht
								 for(Cluster rightCluster : caller.clusterWeiter)
								 {
									if(rightCluster.getName().equals(ideenText))
									{
										rightCluster.getNotes().add(new Note(textareaIdee.getText(),cluster.getName()));
										added=true;
										break;
									}	 										
								 }
								if(added==false)
								{
									ArrayList<Note> newNotes = new ArrayList<Note>();
									newNotes.add(new Note(textareaIdee.getText(), cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterWeiter.add(newCluster);
								}
								
								cluster.getNotes().remove(actualNote);
								textareaIdee.destroy();					
								caller.updateRightSide();	
								popup.sendToFront();
								if(cluster.getNotes().size()==0)
								{
									popup.destroy();
									caller.updateleftSide();
									caller.popupCluster=null;
								}	
							}
							//von rechts nach links
							if(side ==1 && centerPoint.x < EvaluationScene.trennlinieLinks.x)
							{							 
								 boolean added = false;
								 //überprüfung ob der cluster bereits besteht
								 for(Cluster leftCluster : caller.clusterVerworfen)
								 {
									if(leftCluster.getName().equals(ideenText))
									{
										leftCluster.getNotes().add(new Note(textareaIdee.getText(),cluster.getName()));
										added=true;
										break;
									}	 										
								 }
								if(added==false)
								{
									ArrayList<Note> newNotes = new ArrayList<Note>();
									newNotes.add(new Note(textareaIdee.getText(), cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterVerworfen.add(newCluster);
								}
								
								cluster.getNotes().remove(actualNote);
								textareaIdee.destroy();					
								caller.updateleftSide();	
								popup.sendToFront();
								if(cluster.getNotes().size()==0)
								{
									popup.destroy();
									caller.updateRightSide();
									caller.popupCluster=null;
								}	
							}
							//von links in mitte
							else if(side ==0  &&  centerPoint.x >  EvaluationScene.trennlinieLinks.x && centerPoint.x < EvaluationScene.trennlinieRechts.x)
							 {
								 boolean added = false;
								 //überprüfung ob der cluster bereits besteht
								 for(Cluster middleCluster : caller.clusterVerbleibend)
								 {
									if(middleCluster.getName().equals(ideenText))
									{
										middleCluster.getNotes().add(new Note(textareaIdee.getText(),cluster.getName()));
										added=true;
										break;
									}	 										
								 }
								if(added==false)
								{
									ArrayList<Note> newNotes = new ArrayList<Note>();
									newNotes.add(new Note(textareaIdee.getText(), cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterVerbleibend.add(newCluster);
								}
								
								cluster.getNotes().remove(actualNote);
								textareaIdee.destroy();					
								caller.updateMiddleList();	
								popup.sendToFront();
								if(cluster.getNotes().size()==0)
								{
									popup.destroy();
									caller.updateleftSide();
									caller.popupCluster=null;
								}															 
							}
							//von rechts in mitte
							else if(side == 1 &&  centerPoint.x >  EvaluationScene.trennlinieLinks.x && centerPoint.x < EvaluationScene.trennlinieRechts.x)
							 {
								 boolean added = false;
								 //überprüfung ob der cluster bereits besteht
								 for(Cluster middleCluster : caller.clusterVerbleibend)
								 {
									if(middleCluster.getName().equals(ideenText))
									{
										middleCluster.getNotes().add(new Note(textareaIdee.getText(),cluster.getName()));
										added=true;
										break;
									}	 										
								 }
								if(added==false)
								{
									ArrayList<Note> newNotes = new ArrayList<Note>();
									newNotes.add(new Note(textareaIdee.getText(), cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterVerbleibend.add(newCluster);
								}
								
								cluster.getNotes().remove(actualNote);
								textareaIdee.destroy();					
								caller.updateMiddleList();	
								popup.sendToFront();
								if(cluster.getNotes().size()==0)
								{
									popup.destroy();
									caller.updateRightSide();
									caller.popupCluster=null;
								}
								
							}
							break;							 
						} 
					  
					return false;
				}
			}); 
			//wann geht popup zu? wenn wieder auf cluster oben geklickt wird
			y+=34;
		}		
		
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

}
