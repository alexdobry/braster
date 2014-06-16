package de.braster;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.PickResult;
import org.mt4j.components.PickResult.PickEntry;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.input.IMTInputEventListener;
import org.mt4j.input.gestureAction.TapAndHoldVisualizer;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.AbstractComponentProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapAndHoldProcessor.TapAndHoldProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;








import processing.core.PApplet;

public class Idea extends MTTextArea {
	
	private static MTApplication app;
	private MTCanvas canvas;
	private Idea self = null;
	private static final LinkedList<Idea> ideas = new LinkedList<Idea>();
	private static LinkedList<Idea> parents = new LinkedList<Idea>();
	private LinkedList<Idea> children = new LinkedList<Idea>();
	
	public Idea(MTApplication pApplet, MTCanvas canv) {
		super(pApplet);
		this.canvas=canv;
		app = pApplet;
		this.self = this;
		ideas.add(this);
		parents.add(this);
		
		setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.WHITE, true));
		setStrokeColor(MTColor.LIME);
		setFillColor(MTColor.GREEN);
		setGestureAllowance(ScaleProcessor.class, false);
		setGestureAllowance(RotateProcessor.class, false);
		
		addGestureListener(DragProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				DragEvent de = (DragEvent)ge;
				
				switch (de.getId()) {
				case DragEvent.GESTURE_STARTED:
					
					break;
				case DragEvent.GESTURE_UPDATED:
					self.setFillColor(new MTColor(220,220,220,255));
					break;
				case DragEvent.GESTURE_ENDED:
					
					PickResult pr = self.getParent().pick(de.getDragCursor().getCurrentEvtPosX(),de.getDragCursor().getCurrentEvtPosY());
					
					List<PickEntry> pe = pr.getPickList();
					if (pe.size() >= 2) {
						Object obj = pe.get(1).hitObj;
						if (obj instanceof Idea) {
							Idea i = (Idea)obj;
							if (i.getParent() instanceof Idea) {
								((Idea)i.getParent()).snapToIdea(self);
							} else {
								(i).snapToIdea(self);
							}
							parents.remove(i); //als parent von der liste entfernen
						}
					}
					self.setFillColor(MTColor.GREEN);	
					System.out.println(parents);
					break;
				default:
					break;
				}
				return false;
			}
		});
		
		registerInputProcessor(new TapProcessor(pApplet));
		addGestureListener(TapProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				switch (te.getId()) {
					case MTGestureEvent.GESTURE_STARTED:
						self.setFillColor(new MTColor(220,220,220,255));
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (te.isTapped() || te.isTapCanceled()) {
							self.setFillColor(MTColor.GREEN);	
						}
						
						
						break;
					}
				
				return false;
			}
		});
				
	}

	public static LinkedList<Idea> getAllIdeas() {
		
		return ideas;
	}

	public void updateCanvas(MTCanvas canv) {
		this.canvas = canv;
	}
	
	
	/**
	 * Fügt die Idee und alle ihre Kinder an die übergebene Idee als kinder.
	 * 
	 * @param idea
	 */
	public void snapToIdea(final Idea idea) {
		
		if (idea.getChildren().length >= 1) {
			MTComponent[] newIdeas = idea.getChildren();
			for (MTComponent mtComponent : newIdeas) {
				if (mtComponent instanceof Idea) {
					Idea i = (Idea)mtComponent;
					i.removeFromParent();
					this.snapToIdea(i);
				}
			}
		}
		
		//die position der idee berechnen
		List<MTComponent> list = getChildList();
		float height = 0f;
				for (MTComponent mtComponent : list) {
			if (mtComponent instanceof Idea) {
				height += ((Idea) mtComponent).getHeightXY(TransformSpace.LOCAL);
			}
		}
		
		
		this.addChild(idea);
		//positionieren
		idea.setPositionRelativeToParent(new Vector3D(idea.getWidthXY(TransformSpace.LOCAL)/2f, getHeightXY(TransformSpace.LOCAL)*1.5f+height));
		
		//setzt das neue kind auf die größe und ausrichtung der parent idee
//		idea.setLocalMatrix(getLocalMatrix());
		
		//das neue kind passiv stellen
		idea.setGestureAllowance(DragProcessor.class, false);
		idea.setGestureAllowance(ScaleProcessor.class, false);
		idea.setGestureAllowance(RotateProcessor.class, false);
		
		Vector3D trans = new Vector3D();
		Vector3D rot = new Vector3D();
		Vector3D scale = new Vector3D();
		
		getLocalMatrix().decompose(trans, rot, scale);
		idea.getLocalMatrix().scale(scale);
		idea.getLocalMatrix().rotateVect(rot);
		
//		setGestureAllowance(TapAndHoldProcessor.class, true); //das todo unten
		
		idea.registerInputProcessor(new TapAndHoldProcessor(app, 1500));
		idea.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, canvas));
		//TODO: bei erneuten ausführen wird der processor erneut hinzugefügt
		//WARN - Warning: The same type of input processor (tap and hold processor) is already registered at component:
		idea.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapAndHoldEvent th = (TapAndHoldEvent)ge;
				switch (th.getId()) {
				case TapAndHoldEvent.GESTURE_STARTED:
					break;
				case TapAndHoldEvent.GESTURE_UPDATED:
					break;
				case TapAndHoldEvent.GESTURE_ENDED:
					if (th.isHoldComplete()){
						MTComponent parent = idea.getParent();
						if (parent instanceof Idea) { //wenn der parent eine idee ist <> wenn die aktuelle idee ein kind von einer idee ist
							Idea i = (Idea)parent;
							idea.removeFromParent();
							canvas.addChild(idea);
							idea.setPositionGlobal(th.getLocationOnScreen());
							
							//TODO: logik für parent implementieren
							idea.setGestureAllowance(DragProcessor.class, true);
							idea.setGestureAllowance(ScaleProcessor.class, true);
							idea.setGestureAllowance(RotateProcessor.class, true);
							idea.setGestureAllowance(TapAndHoldProcessor.class, false);
							i.repositionChildren();
							parents.add(idea); //wird zu einem neuen parent
						}
					}
					break;
				default:
					break;
				}
				return false;
			}
		});
	}
	
	
	public LinkedList<Idea> getAllParents() {
		return parents;
	}
	
	
	/**
	 * Rückt bei entfernten Kindern alle zusammen damit keine lücken auftreten
	 */
	protected void repositionChildren() {
		List<MTComponent> cl = getChildList();
		List<Idea> i = new LinkedList<Idea>();
		for (MTComponent mtComponent : cl) {
			if (mtComponent instanceof Idea) {
				i.add((Idea)mtComponent);
			}
		}
		
		for (Idea idea : i) {
			idea.removeFromParent();
		}
		
		for (Idea idea : i) {
			self.snapToIdea(idea);
		}
		
		
	}
	

}
