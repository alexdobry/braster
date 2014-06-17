package de.braster;

import java.util.LinkedList;

import org.mt4j.MTApplication;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.flickProcessor.FlickEvent.FlickDirection;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;

import processing.core.PApplet;
import processing.core.PImage;



public class BWIdeaView extends MTRectangle{
		private LinkedList<Idea> ideas = Idea.getAllIdeas();
		
		private int iterator = 0;
		private MultiPurposeInterpolator leftAnimation = null, 
				rightAnimation = null, 
				scaleAnimation = null,
				leftReverseAnimation = null,
				rightReverseAnimation = null;
		private Animation animLeft, animReverseLeft, animRight, animReverseRight, animScale, animReverseScale;
		Vector3D trans = new Vector3D(), rot = new Vector3D(), scale = new Vector3D();
		private MTTextArea currentShown = null;
		private PApplet pApplet;
		private MTRectangle swipeleft, swiperight;
		private MTColor green1 = new MTColor(0, 100, 0, 255);
		private MTColor green2 = new MTColor(34, 139, 34, 255);
		
		public BWIdeaView(PApplet pApplet, float width, float height, final BWKeyboard kb) {
			super(pApplet, width, height);
			this.pApplet = pApplet;
			setFillColor(MTColor.WHITE);
			setStrokeColor(MTColor.WHITE);
			setGestureAllowance(DragProcessor.class, false);
			setGestureAllowance(ScaleProcessor.class, false);
			setGestureAllowance(RotateProcessor.class, false);
			
			makeHelp();
			
			//Zur tastatur welchseln
			MTSvgButton keybCloseSvg = new MTSvgButton(pApplet, MT4jSettings.getInstance().getDefaultSVGPath()
					+ "keybClose.svg");

			keybCloseSvg.scale(0.8f, 0.8f, 1, new Vector3D(0,0,0));
			keybCloseSvg.scale(0.7f, 0.7f, 1, new Vector3D(0,0,0)); //2x ist notwendig aufgrund wie die tastatur auf ihre gräße kommt
			
			keybCloseSvg.setPositionRelativeToParent(new Vector3D(this.getWidthXY(TransformSpace.RELATIVE_TO_PARENT)-25, 25,0));
			keybCloseSvg.setBoundsPickingBehaviour(AbstractShape.BOUNDS_ONLY_CHECK);
			keybCloseSvg.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						kb.setVisible(true);
						setVisible(false);
						showHelp();
					}
					return false;
				}
			});
			this.addChild(keybCloseSvg);
				
			registerInputProcessor(new FlickProcessor());
			addGestureListener(FlickProcessor.class, new IGestureEventListener() {
				
				@Override
				public boolean processGestureEvent(MTGestureEvent ge) {
					FlickEvent e = (FlickEvent)ge;
					getLocalMatrix().decompose(trans, rot, scale); //versuch die aktuelle Rotation zu bestimmen
					if (e.getId() == MTGestureEvent.GESTURE_ENDED)  {
						//flick Gesten abhängig der tastatur orientation
						//nord orientation
						if (e.getDirection() == FlickDirection.WEST && rot.z == 0) {
							animLeft.start();
							animScale.start();
						}
						if (e.getDirection() == FlickDirection.EAST && rot.z == 0) {
							animRight.start();
							animScale.start();
							
						}
						
						//ost-orientation
						if (e.getDirection() == FlickDirection.NORTH && rot.z > 0) {
							animLeft.start();
							animScale.start();
						}
						if (e.getDirection() == FlickDirection.SOUTH && rot.z > 0) {
							animRight.start();
							animScale.start();
						}
						
						//west-orientation
						if (e.getDirection() == FlickDirection.SOUTH && rot.z < 0) {
							animLeft.start();
							animScale.start();
						}
						if (e.getDirection() == FlickDirection.NORTH && rot.z < 0) {
							animRight.start();
							animScale.start();
						}
						if (e.isFlick()) {
							hideHelp();	
						}
						
						System.out.println(rot.toString());
					}
						
					return false;
				}
			});
			

			
		}

		private void makeHelp() {
			
			String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		    
			//swipe left hilfe
			PImage swl = pApplet.loadImage(path + "swipe_left.png");
			swipeleft = new MTRectangle(pApplet, swl);
			swipeleft.scale(0.1f, 0.1f, 0, swipeleft.getCenterPointLocal());
			swipeleft.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent()*0.7f, getHeightXYRelativeToParent()*0.3f));
			swipeleft.setNoStroke(true);
			this.addChild(swipeleft);
			
			//swipe right hilfe
			PImage swr = pApplet.loadImage(path + "swipe_right.png");
			swiperight = new MTRectangle(pApplet, swr);
			swiperight.scale(0.1f, 0.1f, 0, swiperight.getCenterPointLocal());
			swiperight.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent()*0.3f, getHeightXYRelativeToParent()*0.3f));
			swiperight.setNoStroke(true);
			this.addChild(swiperight);
			
		}
		
		private void hideHelp() {
			swipeleft.setVisible(false);
			swiperight.setVisible(false);;
		}
		
		private void showHelp() {
			swipeleft.setVisible(true);
			swiperight.setVisible(true);
		}
		

		/**
		 * Wechselt durch die Liste der Ideen in der angegebenen Richtung.
		 * 
		 * @param direction -1 = zurück; 1 = vorwärts
		 */
		public void fillIdeaArea(int direction) {
			Idea i = null;
			if (ideas.size() > 0) {
					
				
				
				//flick nach links = vorwärts
				if (ideas.get(iterator) == ideas.getLast() && direction == 1) {
					iterator = 0;
				} else if (direction == 1) {
					iterator++;
				}
				
				//flick nach rechts = zurück 
				if (ideas.get(iterator) == ideas.getFirst() && direction == -1) {
					iterator = ideas.size()-1;
				} else if (direction == -1) {
					iterator--;
				}
				
//				ideaArea.setText(i.getText());
//				setAnimations(ideaArea, ideaArea.getWidthXY(TransformSpace.LOCAL));
				
				// direction == 0 ist der fall wenn nur von der tastatur zur ideaview gewechselt wird
				if (direction == 0) {
					direction = 1;
				}
				
				i = ideas.get(iterator);
				showIdea(i.getText(), direction);
			}
		}
		
		
		private void showIdea(String s, int direction) {
			//Ideenanzeige
			
			if (currentShown != null) {
				currentShown.destroy();
			}
			
			MTTextArea ideaArea = new MTTextArea(pApplet);
			
			ideaArea.setFillColor(green1);
			ideaArea.setStrokeColor(green2);
			ideaArea.setFont(FontManager.getInstance().createFont(pApplet, "arial.ttf", 24, MTColor.WHITE, true));
			ideaArea.removeAllGestureEventListeners();
			ideaArea.setPickable(false);
			ideaArea.setText(s);
			
			setAnimations(ideaArea, ideaArea.getWidthXY(TransformSpace.LOCAL));
			ideaArea.setWidthXYRelativeToParent(1);

			this.addChild(ideaArea);
			if (direction == -1) {
				ideaArea.setPositionRelativeToParent(new Vector3D(0,getCenterPointLocal().y));
				animReverseLeft.start();
			}
			
			if (direction == 1) {
				ideaArea.setPositionRelativeToParent(new Vector3D(getWidthXYRelativeToParent(), getCenterPointLocal().y));
				animReverseRight.start();
			}
			animReverseScale.start();	
			currentShown = ideaArea;
			
		}
		
		
		
		
		/**
		 * Setzt Animation für eine Textarea.
		 * @param t
		 * @param width
		 */
		private void setAnimations (final MTTextArea t, final float width) {
			scaleAnimation = new MultiPurposeInterpolator(width, 0, 300, 0, 1, 1);
			leftAnimation = new MultiPurposeInterpolator(getWidthXYRelativeToParent()/2, getCenterPointLocal().x-getWidthXYRelativeToParent()/2, 300, 0.1f, 0.8f, 1);
			leftReverseAnimation = new MultiPurposeInterpolator(getCenterPointLocal().x-getWidthXYRelativeToParent()/2,getWidthXYRelativeToParent()/2 , 300, 0.1f, 0.8f, 1);
			rightAnimation = new MultiPurposeInterpolator(getWidthXYRelativeToParent()/2, getWidthXYRelativeToParent(), 300, 0.1f, 0.8f, 1);
			rightReverseAnimation =new MultiPurposeInterpolator(getWidthXYRelativeToParent(), getWidthXYRelativeToParent()/2, 300, 0.1f, 0.8f, 1);
			MultiPurposeInterpolator reverseScaleAnimation = new MultiPurposeInterpolator(0, width, 300, 0, 1, 1);
			
			animScale = new Animation("Idee verschwinden lassen", scaleAnimation, t);
			animScale.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					
					switch (ae.getId()) {
					case AnimationEvent.ANIMATION_STARTED:
						
					case AnimationEvent.ANIMATION_UPDATED:
						t.setWidthXYRelativeToParent(ae.getValue());
						break;
					case AnimationEvent.ANIMATION_ENDED:
//						t.setWidthXYRelativeToParent(width);
						
						break;	
					default:
						break;
					}//switch
				}
			});
			
			animReverseScale = new Animation("Idee erscheinen lassen", reverseScaleAnimation, t);
			animReverseScale.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					switch (ae.getId()) {
					case AnimationEvent.ANIMATION_STARTED:
						
					case AnimationEvent.ANIMATION_UPDATED:
						t.setWidthXYRelativeToParent(ae.getValue());
						break;
					case AnimationEvent.ANIMATION_ENDED:
						
						break;	
					default:
						break;
					}//switch
				}
			});
			
			animLeft = new Animation("Idee nach links", leftAnimation, t);
			animLeft.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					if (ae.getId() == AnimationEvent.ANIMATION_UPDATED) {
						t.setPositionRelativeToParent(new Vector3D(ae.getValue(), getCenterPointLocal().y));	
					}
					if(ae.getId() == AnimationEvent.ANIMATION_ENDED){
						fillIdeaArea(1);
						t.setVisible(true);
						t.destroy();
					}
				}
			});
			
			
			animReverseLeft = new Animation("Idee von links in die Mitte", leftReverseAnimation, t);
			animReverseLeft.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					if (ae.getId() == AnimationEvent.ANIMATION_UPDATED) {
						t.setPositionRelativeToParent(new Vector3D(ae.getValue(), getCenterPointLocal().y));	
					}
					if(ae.getId() == AnimationEvent.ANIMATION_ENDED){
					
					}
				}
			});
			
			animRight = new Animation("Idee nacht rechts", rightAnimation, t);
			animRight.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					if (ae.getId() == AnimationEvent.ANIMATION_UPDATED) {
						t.setPositionRelativeToParent(new Vector3D(ae.getValue(), getCenterPointLocal().y));	
					}
					if(ae.getId() == AnimationEvent.ANIMATION_ENDED){
//						t.setPositionRelativeToParent(getCenterPointLocal());
						fillIdeaArea(-1);
						t.setVisible(true);
						t.destroy();
					}
				}
			});
			
			animReverseRight = new Animation("Idee von rechts in die mitte", rightReverseAnimation, t);
			animReverseRight.addAnimationListener(new IAnimationListener() {
				
				@Override
				public void processAnimationEvent(AnimationEvent ae) {
					if (ae.getId() == AnimationEvent.ANIMATION_UPDATED) {
						t.setPositionRelativeToParent(new Vector3D(ae.getValue(), getCenterPointLocal().y));	
					}
					if(ae.getId() == AnimationEvent.ANIMATION_ENDED){
					}
				}
			});
			
		}
}
		
	