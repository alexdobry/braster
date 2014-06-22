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

import processing.core.PApplet;
import processing.core.PImage;

public class ClusterPopup  extends MTRectangle{
	
	private MTApplication mtApp;
	private EvaluationScene caller;
	private ClusterPopup popup;
	private int side;
	
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
		for(Note actualNote : cluster.getNotes())
		{
			final MTTextArea textareaIdee = new MTTextArea(pApplet);
			textareaIdee.setText(actualNote.getName());	
			textareaIdee.setFillColor(MTColor.GREEN);
			textareaIdee.setStrokeColor(MTColor.LIME);
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
							break;
						case MTGestureEvent.GESTURE_ENDED:									 								
							
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
									newNotes.add(new Note(textareaIdee.getText(),cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterWeiter.add(newCluster);
								}
								removeFromClusterList(textareaIdee,caller.clusterVerworfen);
								textareaIdee.destroy();								 
								caller.updateRightSide();
								popup.sendToFront();
							}
							//von rechts nach links
							if(side ==1 && centerPoint.x < EvaluationScene.trennlinieLinks.x)
							{							 
								boolean added = false;
								 //überprüfung ob der cluster bereits besteht
								 for(Cluster rightCluster : caller.clusterVerworfen)
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
									newNotes.add(new Note(textareaIdee.getText(),cluster.getName()));
									Cluster newCluster = new Cluster(ideenText, newNotes);
									caller.clusterVerworfen.add(newCluster);
								}
								removeFromClusterList(textareaIdee,caller.clusterWeiter);
								textareaIdee.destroy();								 
								caller.updateleftSide();
								popup.sendToFront();
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
								removeFromClusterList(textareaIdee,caller.clusterVerworfen);
								textareaIdee.destroy();					
								caller.updateMiddleList();
								popup.sendToFront();
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
								removeFromClusterList(textareaIdee,caller.clusterWeiter);
								textareaIdee.destroy();					
								caller.updateMiddleList();
								popup.sendToFront();
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
