package de.braster;

import java.util.ArrayList;
import java.util.List;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.shapes.AbstractShape;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.components.visibleComponents.widgets.MTTextArea.ExpandDirection;
import org.mt4j.components.visibleComponents.widgets.buttons.MTSvgButton;
import org.mt4j.components.visibleComponents.widgets.keyboard.MTKeyboard;
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
import org.mt4j.util.MT4jSettings;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class SetupScene  extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene brainWritingScene;
	private ArrayList<MTRoundRectangle> playerButtons;
	
	
	public SetupScene( final MTApplication mtApplication, String name)
	{
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		this.playerButtons = new ArrayList<MTRoundRectangle>();
		
		//TextField für Titel
		MTTextField textFieldTitle = new MTTextField(mtApplication, mtApplication.width/2f-100, 30, 200, 60, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		50, new MTColor(255, 255, 255, 255)));
		textFieldTitle.setNoFill(true);
		textFieldTitle.setNoStroke(true);
		textFieldTitle.setText("Bruster");
		canv.addChild(textFieldTitle);
		
		//fungiert als Label für die Problembeschreibung
		final MTTextField textFieldProblem = new MTTextField(mtApplication, 40, 150, 270, 60, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		36, new MTColor(255, 255, 255, 255)));
		textFieldProblem.setNoFill(true);
		textFieldProblem.setNoStroke(true);
		textFieldProblem.setText("Problem:");
		canv.addChild(textFieldProblem);
		
		//fungiert als mehrzeilige textarea für die Problembeschreibung
		final MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		textArea.unregisterAllInputProcessors();
		textArea.registerInputProcessor(new TapProcessor(this.mtApp));
		textArea.addGestureListener(TapProcessor.class, new IGestureEventListener(){
			public boolean processGestureEvent(MTGestureEvent ge){
				TapEvent te = (TapEvent) ge;
				   if (te.isTapped()){
                       
                       System.out.println("hallo");
                       MTKeyboard kb1 = makeKB(mtApplication, textArea);                       
                       kb1.setPositionGlobal(new Vector3D(mtApplication.width-(kb1.getWidthXY(TransformSpace.LOCAL)),mtApplication.height-(kb1.getHeightXY(TransformSpace.LOCAL)/2f)));                    
				   }
				   return false;
			}
		});
		
		//wenn angeklickt wird, muss sich Tastatur öffnen
		//eingegebener Text dann im Feld erscheinen
	
		
		textArea.setPositionGlobal(new Vector3D(350, 180));
		textArea.setHeightLocal(200);
		textArea.setWidthLocal(750);
		canv.addChild(textArea);
		
		
		// fungiert als Label für die Anzahl der Player
		MTTextField textFieldPlayer = new MTTextField(mtApplication, 40, 440, 270, 60, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		36, new MTColor(255, 255, 255, 255)));
		textFieldPlayer.setNoFill(true);
		textFieldPlayer.setNoStroke(true);
		textFieldPlayer.setText("Player:");
		canv.addChild(textFieldPlayer);
		
					
		IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		50, MTColor.BLACK, false);
		
		MTRoundRectangle r = getRoundRectWithText(mtApplication.width/2-225, mtApplication.height-100, 450, 55, "Start Brain Writing", font, MTColor.SILVER);
		r.registerInputProcessor(new TapProcessor(getMTApplication()));
		r.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r));
		r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
					
					//Problem auslesen
					MTTextArea textAreaProblem = (MTTextArea) canv.getChildByIndex(2);
					String problem = textAreaProblem.getText();
					int number = 0;
					//herausfinden, wieviel Spieler ausgewählt sind
 					for (MTRoundRectangle item: playerButtons)
					{
						 System.out.println(item.getFillColor().toString());
						 System.out.println("Color{0.0,128.0,0.0_255.0}");
						if(item.getFillColor().toString().equals("Color{0.0,128.0,0.0_255.0}") )
						{
							MTTextArea textArea = (MTTextArea)item.getChildByIndex(0);
							number = Integer.parseInt(textArea.getText());
						}
					}
					//Save the current scene on the scene stack before changing
					mtApp.pushScene();
					if (brainWritingScene == null){
						brainWritingScene = new BrainWritingScene(mtApp, "Brain Writing", problem, number);
						//Konstruktor erweitern um Anzahl Spieler, da genau soviele
						//Tastaturen geladen werden
					//Add the scene to the mt application
					mtApp.addScene(brainWritingScene);
					}
					//Do the scene change
					mtApp.changeScene(brainWritingScene);
				}
				return false;
			}
		});
		canv.addChild(r);
		
		
		//erzeugt 4 Buttons für die Spieleranzahl
		createPlayerButtons();
	}

	
	private void createPlayerButtons()
	{		
		for(int i=1;i<5;i++)
		{
			IFont font = FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		34, MTColor.BLACK, false);
			int x=0,y=0;
			switch (i){
				case 1:	x = 270;
						y = 445;
						break;
				case 2:	x = 470;
						y = 445;
						break;
				case 3:	x = 670;
						y = 445;
						break;
				case 4:	x = 870;
						y = 445;						
			}
			final MTRoundRectangle r = getRoundRectWithText(x, y, 180, 35, i+"", font, MTColor.SILVER);
			r.registerInputProcessor(new TapProcessor(getMTApplication()));
			r.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r));
			r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
				public boolean processGestureEvent(MTGestureEvent ge) {
					TapEvent te = (TapEvent)ge;
					if (te.isTapped()){
						
						//wenn angeklickt wird, werden alle Buttons in die Standardfarbe zurückgeführt
						//und der angeklickte wird grün markiert						
						for (MTRoundRectangle item: playerButtons) {
							   item.setFillColor(MTColor.SILVER);
						}
						r.setFillColor(MTColor.AQUA);
					}
					return false;
				}
			});
			this.canv.addChild(r);
			this.playerButtons.add(r);
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
	

	
public MTKeyboard makeKB(MTApplication mtApplication, MTTextArea t) {
		
		MTKeyboard keyboard = new MTKeyboard(mtApplication);
		
        t.setExpandDirection(ExpandDirection.DOWN);
        t.setFontColor(MTColor.BLACK);		 
		t.setFillColor(MTColor.SILVER);
		t.unregisterAllInputProcessors();
		t.setEnableCaret(true); 
		keyboard.addTextInputListener(t);				
				
		getCanvas().addChild(keyboard);
		
		keyboard.scale(0.8f, 0.8f, 1, new Vector3D(0, 0, 0));
		keyboard.removeAllGestureEventListeners(DragProcessor.class);
		keyboard.removeAllGestureEventListeners(ScaleProcessor.class);
		keyboard.removeAllGestureEventListeners(RotateProcessor.class);		

		return keyboard;		
	}

}
