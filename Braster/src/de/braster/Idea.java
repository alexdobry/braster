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
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
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

import de.braster.BWKeyboard.KeyInfo;


public class Idea extends MTTextArea {
	
	private static MTApplication app;
	private MTCanvas canvas;
	private Idea self = null;
	private static final LinkedList<Idea> ideas = new LinkedList<Idea>();
	private LinkedList<Idea> children = new LinkedList<Idea>();
	private MTColor ideaFillColor = new MTColor(0, 100, 0, 255);;
	private MTColor ideaStrokeColor = new MTColor(34, 139, 34, 255);;
	private MTColor ideaTextColor = MTColor.WHITE;
	/**
	 * Farbänderung bei Berührung durch touch Geste
	 */
	private MTColor ideaFlashColor = new MTColor(220,220,220,255); 
	private MTColor ideaHoverOverColor = new MTColor(30,144,255, 255);
	
	
	public Idea(MTApplication pApplet, MTCanvas canv) {
		super(pApplet);
		this.canvas=canv;
		app = pApplet;
		this.self = this;
		ideas.add(this);
		
		setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, ideaTextColor, true));
		setStrokeColor(ideaStrokeColor);
		setFillColor(IdeaColors.FILL);
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
					self.setFillColor(IdeaColors.FLASH);
					
					
					for (Idea i : ideas) {
						if (i != self) {
							i.setFillColor(IdeaColors.FILL);	
						}
					}
					
					
					PickResult pr2 = self.getParent().pick(de.getDragCursor().getCurrentEvtPosX(), de.getDragCursor().getCurrentEvtPosY());
					List<PickEntry> pe2 = pr2.getPickList();
					
					if (pe2.size() >= 2) {
						Object obj = pe2.get(1).hitObj;
						if (obj instanceof Idea) {
							Idea id = (Idea)obj;
							id.setFillColor(IdeaColors.HOVEROVER);
						}
					} 
					

					
					break;
				case DragEvent.GESTURE_ENDED:
					
					PickResult pr = self.getParent().pick(de.getDragCursor().getCurrentEvtPosX(), de.getDragCursor().getCurrentEvtPosY());
					
					List<PickEntry> pe = pr.getPickList();
					if (pe.size() >= 2) {
						Object obj = pe.get(1).hitObj;
						if (obj instanceof Idea) {
							Idea i = (Idea)obj;
							
							
							if (i.getParent() instanceof Idea) {
								if (self.isCategory()) {
									self.snapToIdea((Idea)i.getParent());
								} else {
									((Idea)i.getParent()).snapToIdea(self);
								}
							} else {
								if (self.isCategory()) {
									self.snapToIdea(i);
								} else {
									(i).snapToIdea(self);
								}
							}
							
						}
					}

					for (Idea i : ideas) {
						i.setFillColor(IdeaColors.FILL);	
					}
					
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
						self.setFillColor(IdeaColors.FLASH);
						break;
					case MTGestureEvent.GESTURE_UPDATED:
						break;
					case MTGestureEvent.GESTURE_ENDED:
						if (te.isTapped() || te.isTapCanceled()) {
							self.setFillColor(IdeaColors.FILL);	
						}
						
						
						break;
					}
				
				return false;
			}
		});
				
	}
	
	
	/**
	 * Konstruktor für Kategorie
	 * @param mtApplication
	 * @param canvas
	 * @param fillColor
	 * @param strokeColor
	 * @param textColor
	 * @param flashColor
	 * @param hoverOverColor
	 */
	public Idea (MTApplication mtApplication, MTCanvas canvas, MTColor fillColor, MTColor strokeColor, MTColor textColor, MTColor flashColor, MTColor hoverOverColor) {
		this(mtApplication, canvas);
		ideaFillColor = fillColor;
		ideaStrokeColor = strokeColor;
		ideaTextColor = textColor;
		ideaFlashColor = flashColor;
		ideaHoverOverColor = hoverOverColor;
		
		setFillColor(IdeaColors.FILL);
		setStrokeColor(ideaStrokeColor);
		
	}

	public static LinkedList<Idea> getAllIdeas() {
		
		return ideas;
	}

	public void updateCanvas(MTCanvas canv) {
		this.canvas = canv;
	}
	
	
	/**
	 * Fügt sich selbst und alle ihre Kinder an die übergebene Idee als kinder.
	 * 
	 * @param ideaToSelft
	 */
	public void snapToIdea(final Idea ideaToSelft) {

		if (ideaToSelft.getChildren().length >= 1) {
			MTComponent[] newIdeas = ideaToSelft.getChildren();
			for (MTComponent mtComponent : newIdeas) {
				if (mtComponent instanceof Idea) {
					Idea i = (Idea)mtComponent;
					i.removeFromParent();
					this.snapToIdea(i);
				}
			}
		}
		
		//die position der idee berechnen
		List<MTComponent> list = getChildList(); //alle schon vorhanden kinder durchgehen und position errechnen
		float height = 0f;
		for (MTComponent mtComponent : list) {
			if (mtComponent instanceof Idea) {
				height += ((Idea) mtComponent).getHeightXY(TransformSpace.LOCAL);
			}
			
		}
		
		
		this.addChild(ideaToSelft);
		//positionieren

		ideaToSelft.setPositionRelativeToParent(new Vector3D(ideaToSelft.getWidthXY(TransformSpace.LOCAL)/2f, getHeightXY(TransformSpace.LOCAL)*1.5f+height));
		

		
		//setzt das neue kind auf die größe und ausrichtung der parent idee
//		idea.setLocalMatrix(getLocalMatrix());
		
		//das neue kind passiv stellen
		ideaToSelft.setGestureAllowance(DragProcessor.class, false);
		ideaToSelft.setGestureAllowance(ScaleProcessor.class, false);
		ideaToSelft.setGestureAllowance(RotateProcessor.class, false);
		
		//TODO: implementieren der matrizenübertragung
//		Vector3D trans = new Vector3D();
//		Vector3D rot = new Vector3D();
//		Vector3D scale = new Vector3D();
//		
//		getLocalMatrix().decompose(trans, rot, scale);
//		idea.getLocalMatrix().scale(scale);
//		idea.getLocalMatrix().rotateVect(rot);
		
//		setGestureAllowance(TapAndHoldProcessor.class, true); //das todo unten
		
		ideaToSelft.registerInputProcessor(new TapAndHoldProcessor(app, 1500));
		ideaToSelft.addGestureListener(TapAndHoldProcessor.class, new TapAndHoldVisualizer(app, canvas));
		//TODO: bei erneuten ausführen wird der processor erneut hinzugefügt
		//WARN - Warning: The same type of input processor (tap and hold processor) is already registered at component:
		ideaToSelft.addGestureListener(TapAndHoldProcessor.class, new IGestureEventListener() {
			
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
						MTComponent parent = ideaToSelft.getParent();
						if (parent instanceof Idea) { //wenn der parent eine idee ist <> wenn die aktuelle idee ein kind von einer idee ist
							Idea p = (Idea)parent;
							ideaToSelft.removeFromParent();
							canvas.addChild(ideaToSelft);
							ideaToSelft.setPositionGlobal(th.getLocationOnScreen());
							
							//TODO: logik für parent implementieren
							ideaToSelft.setGestureAllowance(DragProcessor.class, true);
							ideaToSelft.setGestureAllowance(ScaleProcessor.class, false);
							ideaToSelft.setGestureAllowance(RotateProcessor.class, false);
							ideaToSelft.setGestureAllowance(TapAndHoldProcessor.class, false);
							p.repositionChildren();
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
	
	
	public static LinkedList<Idea> getAllParents() {
		LinkedList<Idea> p = new LinkedList<Idea>();
		for (Idea i : ideas) {
			Object obj = i.getParent();
			if (obj instanceof Idea) {
				if (!p.contains((Idea)obj)){
					p.add((Idea)obj);
				}
					
			}
			
			if (i.getChildCount() == 0 && !(obj instanceof Idea)) {
				p.add(i);
			}
		}
		return p;
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


	public void setDesignColors(MTColor fillColor, MTColor strokeColor, MTColor textColor, MTColor flashColor, MTColor hoverOverColor) {
		ideaFillColor = fillColor;
		ideaStrokeColor = strokeColor;
		ideaTextColor = textColor;
		ideaFlashColor = flashColor;
		ideaHoverOverColor = hoverOverColor;
	}
	
	/**
	 * ist eine Kategorie
	 * @return
	 */
	public boolean isCategory(){
		
		return (self instanceof IdeaCategory);
	}

	public void setFillColor(IdeaColors ic) {
		
		switch (ic) {
		case FILL:
			super.setFillColor(ideaFillColor);
			break;
		case FLASH:
			super.setFillColor(ideaFlashColor);
			break;
		case HOVEROVER:
			super.setFillColor(ideaHoverOverColor);
			break;

		default:
			break;
		}
	}
	

	private enum IdeaColors {
		FILL,STROKE,TEXT,FLASH,HOVEROVER
	}
}
