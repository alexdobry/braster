package de.braster;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.font.IFont;
import org.mt4j.components.visibleComponents.shapes.MTRectangle;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;


import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.dragProcessor.DragProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.rotateProcessor.RotateProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.scaleProcessor.ScaleProcessor;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.math.Vector3D;
 

import de.braster.Keyboard.KeyInfo;

public class SetupScene  extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene brainWritingScene;
	private ArrayList<Positioncomponent> playerButtons;
	private static String problemDefinition = "";
	private boolean problemEdit = false;
	
	
	public SetupScene( final MTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.playerButtons = new ArrayList<Positioncomponent>();
		
		problemDefinition = "Problem eingeben...";
		this.setClearColor(new MTColor(73, 112, 138, 255));
		
		
		
		//fungiert als mehrzeilige textarea f�r die Problembeschreibung
		final MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		MTColor.BLACK)); //Font color
		
		//textArea.setText("Problem eingeben...");
		textArea.setHeightLocal(100);
		textArea.setWidthLocal(500);
		textArea.setPositionGlobal(new Vector3D(mtApplication.width/2, mtApplication.height/4));
		textArea.unregisterAllInputProcessors();
	
		textArea.registerInputProcessor(new TapProcessor(this.mtApp));
		textArea.addGestureListener(TapProcessor.class, new IGestureEventListener(){
			public boolean processGestureEvent(MTGestureEvent ge){
				TapEvent te = (TapEvent) ge;
				   if (te.isTapped())
				   { 		
					   if (!problemEdit) {
						   textArea.setText("");
						   problemEdit = true;
					   }
				       Keyboard keyboard = makeKB(mtApplication, textArea);
                       keyboard.setPositionGlobal(new Vector3D(mtApplication.width/2, mtApplication.height-(keyboard.getHeightXY(TransformSpace.LOCAL)/2f)));                    
				   }
				   return false;
			}
		});
		
		//wenn angeklickt wird, muss sich Tastatur �ffnen
		//eingegebener Text dann im Feld erscheinen		
		
		
		
		canv.addChild(textArea);			
		
		IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		50, MTColor.BLACK, false);
		
		 
		
		MTTextField f1 = new MTTextField(mtApp, mtApp.width/2-200,50, 400, 50, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		40, MTColor.WHITE, false));
		f1.setFillColor(new MTColor(73, 112, 138, 255));
	f1.setStrokeColor(new MTColor(73, 112, 138, 255));
		f1.setText("Problem definieren");
		canv.addChild(f1);
		
		
		 
		
		final MTRoundRectangle r = getRoundRectWithText(mtApplication.width/2-225, mtApplication.height-100, 450, 55, "Brainwriting starten", font, MTColor.GREY);
		r.registerInputProcessor(new TapProcessor(getMTApplication()));
		r.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r));
		r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			final ArrayList<Positioncomponent> temp = playerButtons;
			
			public boolean processGestureEvent(MTGestureEvent ge) {
				
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					switch (te.getId()) {
						case MTGestureEvent.GESTURE_STARTED:
							r.setFillColor(new MTColor(220,220,220,255));
							break;
						case MTGestureEvent.GESTURE_ENDED:
					 
							//Problem auslesen
							MTTextArea textAreaProblem = (MTTextArea) canv.getChildByIndex(0);					 
							problemDefinition = textAreaProblem.getText();
							int number = 0;
							//herausfinden, wieviel Spieler ausgew�hlt sind
							for (Positioncomponent item: temp)
							{ 				
								MTRectangle r2 = item.getRectangle();
								System.out.println(r2.getStrokeColor());
								if(r2.getStrokeColor().toString().equals("Color{255.0,0.0,0.0_255.0}"))
								{							 
									number = item.getPersonenanzahl();
								}
							}
							//Save the current scene on the scene stack before changing
							//wenn beides eingegeben wurde, wird in die n�chste szene geleitet
							if(number>0 && problemDefinition.length()>0)
							{
								//Save the current scene on the scene stack before changing
								mtApp.pushScene();
								if (brainWritingScene == null){
									brainWritingScene = new BrainWritingScene(mtApp, "Brain Writing", problemDefinition, number);
									//Konstruktor erweitern um Anzahl Spieler, da genau soviele
									//Tastaturen geladen werden
									//Add the scene to the mt application
									mtApp.addScene(brainWritingScene);
								}
								//Do the scene change
								mtApp.changeScene(brainWritingScene);
							}
 					
							//andernfalls kleinen popup, was fehlt
							else if(problemDefinition.length()==0 || problemDefinition.equals("Problem eingeben..."))
							{
								JOptionPane.showMessageDialog(null, "Bitte das Problem definieren.");
								r.setFillColor(MTColor.GREY);
								
							}
							else if(number==0)
							{
								JOptionPane.showMessageDialog(null, "Bitte die Personenanzahl eingeben.");
								r.setFillColor(MTColor.GREY);
							}
						}
				}
				return false;
			}
		});
		canv.addChild(r);
		
		
		//erzeugt 4 Buttons f�r die Spieleranzahl
		createPlayerButtons();	
		
		Checkbox c = new Checkbox(mtApplication,0,0, 0, 0, 0, 0, 0, "Hilfe notwendig?");
		c.setPositionRelativeToParent(new Vector3D( mtApp.width/2-210 ,mtApp.height/6*4));        
		canv.addChild(c);
	}

	
	private void createPlayerButtons()
	{		
		//new Playerbuttons
		Positioncomponent p1 = new Positioncomponent(this.mtApp,1);
		MTRectangle parent1 = p1.getRectangle();
		parent1.setPositionGlobal(new Vector3D(this.mtApp.width/5,mtApp.height/2));		
		this.canv.addChild(parent1);
		this.playerButtons.add(p1);
		
		Positioncomponent p2 = new Positioncomponent(this.mtApp,2);
		MTRectangle parent2 = p2.getRectangle();		
		parent2.setPositionGlobal(new Vector3D((this.mtApp.width/5)*2,mtApp.height/2));		
		this.canv.addChild(parent2);
		this.playerButtons.add(p2);
		
		Positioncomponent p3 = new Positioncomponent(this.mtApp,3);
		MTRectangle parent3 = p3.getRectangle();		
		parent3.setPositionGlobal(new Vector3D((this.mtApp.width/5)*3,mtApp.height/2));		
		this.canv.addChild(parent3);
		this.playerButtons.add(p3);
		
		Positioncomponent p4 = new Positioncomponent(this.mtApp,4);
		MTRectangle parent4 = p4.getRectangle();
		parent4.setPositionGlobal(new Vector3D((this.mtApp.width/5)*4,mtApp.height/2));		
		this.canv.addChild(parent4);
		this.playerButtons.add(p4);
	
		for(final Positioncomponent pc :this.playerButtons)
		{
			final ArrayList<Positioncomponent> temp = this.playerButtons;
			final MTRectangle r = pc.getRectangle();
			r.registerInputProcessor(new TapProcessor(getMTApplication()));
			r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped())
					{
						//wenn angeklickt wird, werden alle Buttons in die Standardfarbe zur�ckgef�hrt
						//und der angeklickte wird gr�n markiert						
						for (Positioncomponent item: temp  ) 
						{
							MTRectangle r2 = item.getRectangle();
							r2.setStrokeColor(new MTColor(73, 112, 138, 255));
						}
						r.setStrokeColor(MTColor.RED);
						r.setStrokeWeight(8);
					}
					return false;
				}
			});			
		}	
	}
	
	private MTRoundRectangle getRoundRectWithText(float x, float y, float width, float height, String text, IFont font, MTColor background)
	{
		MTRoundRectangle r = new MTRoundRectangle(getMTApplication(), x, y, 0, width, height, 12, 12);
		r.unregisterAllInputProcessors();
		r.setFillColor(background);
		r.setStrokeColor(MTColor.BLACK);
		MTTextArea rText = new MTTextArea(getMTApplication(), font);
		rText.unregisterAllInputProcessors();
		rText.setPickable(false);
		rText.setNoFill(true);
		rText.setNoStroke(true);
		rText.setText(text);
		r.addChild(rText);
		rText.setPositionRelativeToParent(r.getCenterPointLocal());
		return r;
	}
	

	
public Keyboard makeKB(MTApplication mtApplication, final MTTextArea t1) {
		
	
	
		final Keyboard keyboard = new Keyboard(mtApplication);
	
		  final MTTextArea t = new MTTextArea(mtApp, FontManager.getInstance().createFont(mtApp, "arial.ttf", 50, MTColor.BLACK)); 
	        t.setExpandDirection(ExpandDirection.UP);
			t.setStrokeColor(new MTColor(0,0 , 0, 255));
			t.setFillColor(new MTColor(205,200,177, 255));
			t.unregisterAllInputProcessors();
			t.setEnableCaret(true);
//			t.snapToKeyboard(keyb);
			keyboard.snapToKeyboard(t);
			keyboard.addTextInputListener(t);
			
			
        //t.setExpandDirection(ExpandDirection.DOWN);
        //t.setFontColor(MTColor.BLACK);		 
		//t.setFillColor(MTColor.WHITE);
		//t.setEnableCaret(true); 
		//t.setWidthLocal(500);
		//t.setHeightLocal(100);
		//keyboard.addTextInputListener(t);				
				
		getCanvas().addChild(keyboard);
		
		keyboard.scale(0.7f, 0.7f, 0, keyboard.getCenterPointRelativeToParent());
	//	keyboard.setPositionGlobal(new Vector3D(mtApp.width/2,mtApp.height-(keyboard.getHeightXYVectLocal().y/2)));
		keyboard.removeAllGestureEventListeners(DragProcessor.class);
		keyboard.removeAllGestureEventListeners(ScaleProcessor.class);
		keyboard.removeAllGestureEventListeners(RotateProcessor.class);		

		//enter
		KeyInfo enter = keyboard.new KeyInfo("f", "\n", "\n", 		new Vector3D(615, 105),KeyInfo.NORMAL_KEY);
		
		//Event listener f�r den enter key
		IGestureEventListener tp = new IGestureEventListener() {
			@Override
			public boolean processGestureEvent(MTGestureEvent ge) {
			TapEvent te = (TapEvent)ge;
			if (te.isTapped() )
			{				
				keyboard.onCloseButtonClicked();
				t.setEnabled(false);
			}
			return false;
			}
		};
		keyboard.addKeyFromOutside(enter, tp);
		
		return keyboard;		
	}

	public static String getProblem()
	{
		return problemDefinition;
	}

}
