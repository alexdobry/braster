package de.braster;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTList;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.gestureAction.DefaultButtonClickAction;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.font.FontManager;
import org.mt4j.util.font.IFont;
import org.mt4j.util.math.Vector3D;

public class SetupScene  extends AbstractScene{

	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene brainWritingScene;
	
	
	public SetupScene(final MTApplication mtApplication, String name) {
		super(mtApplication, name);
		
		this.mtApp = mtApplication;		
		this.canv = getCanvas();
		
		//TextField für Titel
		MTTextField textFieldTitle = new MTTextField(mtApplication, mtApplication.width/2f-100, 30, 200, 60, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		50, new MTColor(255, 255, 255, 255)));
		textFieldTitle.setNoFill(true);
		textFieldTitle.setNoStroke(true);
		textFieldTitle.setText("Bruster");
		canv.addChild(textFieldTitle);
		
		//fungiert als Label für die Problembeschreibung
		MTTextField textFieldProblem = new MTTextField(mtApplication, 40, 150, 270, 60, FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		36, new MTColor(255, 255, 255, 255)));
		textFieldProblem.setNoFill(true);
		textFieldProblem.setNoStroke(true);
		textFieldProblem.setText("Problem:");
		canv.addChild(textFieldProblem);
		
		//fungiert als mehrzeilige textarea für die Problembeschreibung
		MTTextArea textArea = new MTTextArea(mtApplication,                                
                FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
                		50, //fontzize 
                		new MTColor(255, 255, 255, 255))); //Font color
		
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
		
		//Liste ähnlich einer Dropdownlist mit der Spielerzahl
		MTList aList = new MTList(mtApplication, 350, 455, 100, 25); //(100, 50, 200, 400, mtApplication);
		
		MTListCell listCell = new MTListCell(mtApplication, 100, 25 );
		MTTextArea texta = new MTTextArea(mtApplication, 0,0, 100,25);
		texta.setText("1");
		listCell.addChild(texta);
		
		MTListCell listCell2 = new MTListCell(mtApplication, 100, 25 );
		MTTextArea texta2 = new MTTextArea(mtApplication, 0,0, 100,25);
		texta2.setText("2");
		listCell2.addChild(texta2);
		
		MTListCell listCell3 = new MTListCell(mtApplication, 100, 25 );
		MTTextArea texta3 = new MTTextArea(mtApplication, 0,0, 100,25);
		texta3.setText("3");
		listCell3.addChild(texta3);
		
		MTListCell listCell4 = new MTListCell(mtApplication, 100, 25 );
		MTTextArea texta4 = new MTTextArea(mtApplication, 0,0, 100,25);
		texta4.setText("4");
		listCell4.addChild(texta4);
		
		aList.addListElement(listCell);
		aList.addListElement(listCell2);
		aList.addListElement(listCell3);
		aList.addListElement(listCell4);
		
		getCanvas().addChild(aList);
			
		IFont font = FontManager.getInstance().createFont(mtApplication, "arial.ttf", 
        		50, MTColor.WHITE, false);
		
		MTRoundRectangle r = getRoundRectWithText(mtApplication.width/2f-90, mtApplication.height-100, 180, 35, "Start Brain Writing", font);
		r.registerInputProcessor(new TapProcessor(getMTApplication()));
		r.addGestureListener(TapProcessor.class, new DefaultButtonClickAction(r));
		r.addGestureListener(TapProcessor.class, new IGestureEventListener() {
			public boolean processGestureEvent(MTGestureEvent ge) {
				TapEvent te = (TapEvent)ge;
				if (te.isTapped()){
										
					//Save the current scene on the scene stack before changing
					mtApp.pushScene();
					if (brainWritingScene == null){
						brainWritingScene = new BrainWritingScene(mtApp, "Brain Writing");
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
	}

	
	private MTRoundRectangle getRoundRectWithText(float x, float y, float width, float height, String text, IFont font){
		MTRoundRectangle r = new MTRoundRectangle(getMTApplication(), x, y, 0, width, height, 12, 12);
		r.unregisterAllInputProcessors();
		r.setFillColor(MTColor.BLACK);
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
	


}
