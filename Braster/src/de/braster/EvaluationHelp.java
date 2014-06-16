package de.braster;

import java.util.ArrayList;

import javax.swing.Timer;

import org.mt4j.MTApplication;
import org.mt4j.components.MTCanvas;
import org.mt4j.components.MTComponent;
import org.mt4j.components.TransformSpace;
import org.mt4j.components.visibleComponents.font.FontManager;
import org.mt4j.components.visibleComponents.shapes.MTRoundRectangle;
import org.mt4j.components.visibleComponents.widgets.MTBackgroundImage;
import org.mt4j.components.visibleComponents.widgets.MTListCell;
import org.mt4j.components.visibleComponents.widgets.MTTextArea;
import org.mt4j.components.visibleComponents.widgets.MTTextField;
import org.mt4j.input.inputProcessors.IGestureEventListener;
import org.mt4j.input.inputProcessors.MTGestureEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapEvent;
import org.mt4j.input.inputProcessors.componentProcessors.tapProcessor.TapProcessor;
import org.mt4j.sceneManagement.AbstractScene;
import org.mt4j.sceneManagement.Iscene;
import org.mt4j.util.MTColor;
import org.mt4j.util.animation.Animation;
import org.mt4j.util.animation.AnimationEvent;
import org.mt4j.util.animation.IAnimationListener;
import org.mt4j.util.animation.MultiPurposeInterpolator;
import org.mt4j.util.math.Vector3D;









import processing.core.PImage;

public class EvaluationHelp extends AbstractScene {

	private MTCanvas canv;
	private MTApplication mtApp;
	private Iscene evaluationScene;
	
	public EvaluationHelp(MTApplication mtApplication, String name) {
		super(mtApplication, name);
		canv = getCanvas();
		mtApp = mtApplication;
		//geblurrte Bild als Hintergrund einstellen
		
		//temporär
		PImage image = mtApp.loadImage("C:\\Users\\Stefan\\Dropbox\\SGMCI\\Icons und Bilder\\EV_blur.png");
		//String path = "de" + MTApplication.separator + "braster" + MTApplication.separator + "images" + MTApplication.separator;
		//PImage img = getMTApplication().loadImage(path + "EV_blur.png");
	//	PImage image = mtApp.loadImage("data.images\\EV_blur.png");
		MTBackgroundImage backgroundImage = new MTBackgroundImage(mtApp, image, false);
		canv.addChild(backgroundImage); 
		
		//Textareas im Tabellenkopf adden
		addTextFields();
		//Mitte eine Box erstellen mit dem Helptext und den Animationen,
		//wo die möglichen Aktionen vorgeführt werden
		createHelpArea();
		
		//wenn ins Bild getippt wird, startet die normale EvaluationScene
	}
	
	private void addTextFields()
	{		
		MTTextField textareaPapierkorb = new MTTextField(this.mtApp, 0, 0, 0, 0, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
    		30, MTColor.BLACK, false));
		textareaPapierkorb.unregisterAllInputProcessors();
		textareaPapierkorb.setPickable(false);
		textareaPapierkorb.setNoFill(true);
		textareaPapierkorb.setNoStroke(true);
		textareaPapierkorb.setText("verworfen");
		textareaPapierkorb.setSizeLocal(200, 40);
		textareaPapierkorb.setPositionGlobal(new Vector3D(mtApp.width/2-400,30));
		canv.addChild(textareaPapierkorb);
	
		MTTextField textareaIdeas = new MTTextField(this.mtApp, 0, 0, 0, 0, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
    		30, MTColor.BLACK, false));
		textareaIdeas.unregisterAllInputProcessors();
		textareaIdeas.setPickable(false);
		textareaIdeas.setNoFill(true);
		textareaIdeas.setNoStroke(true);
		textareaIdeas.setText("verbleibend");
		textareaIdeas.setSizeLocal(200, 40);
		textareaIdeas.setPositionGlobal(new Vector3D(mtApp.width/2,30));
		canv.addChild(textareaIdeas);
		
		MTTextField textareaBestIdeas = new MTTextField(this.mtApp, 0, 0, 0, 0, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
    		30, MTColor.BLACK, false));
		textareaBestIdeas.unregisterAllInputProcessors();
		textareaBestIdeas.setPickable(false);
		textareaBestIdeas.setNoFill(true);
		textareaBestIdeas.setNoStroke(true);
		textareaBestIdeas.setText("weiter");
		textareaBestIdeas.setSizeLocal(200, 40);
		textareaBestIdeas.setPositionGlobal(new Vector3D(mtApp.width/2+400,30));
		canv.addChild(textareaBestIdeas);
	}
	
	
	
	
	private void createHelpArea()
	{ 	 
		
		 final MTTextField textfieldContinue = new MTTextField(mtApp, mtApp.width/2, mtApp.height-200, 250, 40, FontManager.getInstance().createFont(this.mtApp, "arial.ttf", 
        		20, MTColor.BLACK, false));
		 textfieldContinue.unregisterAllInputProcessors();
		 textfieldContinue.setPickable(false);
		 textfieldContinue.setNoFill(true);
		 textfieldContinue.setNoStroke(true);
		 textfieldContinue.setText("bei Tippen weiter");
		 canv.addChild(textfieldContinue);
	
		 
		 
		/* 
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				textfieldContinue.setFillColor(MTColor.AQUA);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				textfieldContinue.setFillColor(MTColor.GREEN);
				
			}
		}).start();
		
	      */  
		 
	}

	
	
	
	private ArrayList<String> loadAnimationImages()
	{
		ArrayList<String> picturePaths = new ArrayList<String>();
		picturePaths.add("");
		
		return picturePaths;		 
	}
	
	
	//todo
	//ausprobieren ob alles geht
	//timer einfügen 
	//scrrenshots machen und animationsbilder erstellen
	//text "die bereits geclusterten Ideen werden in der Mitte angezeigt"
	//text 2 "durch selektion eines clusters werden die zugehörigen Ideen unten größer dargestellt und es
	//kann über deren Inhalt diskutiert werden"
	//text 3 "Wird eine Idee durch die Diskussion verworfen, kann diese per Drag&Drop in den temporären Papier
	//korb gescchoben werden"
	//text 4 "wird eine Idee durch die Diskussion als besonders gut kategorisiert, kann diese nach rechts per Drag
	//and Drop verschoben werden"
	//text 5 "sind alle Ideen bewertet, kann eine weitere Diskussion befinden, wo alle guten Ideen nochmals diskutiert werden können"
	//text 6 "bleibt schlussendlich nur noch eine gute idee über, wurde eine Lösung für das Problem gefunden"
}
