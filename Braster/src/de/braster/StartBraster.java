package de.braster;
import org.mt4j.MTApplication;




public class StartBraster extends MTApplication {
	
	
	public static void main(String[] args) {
		initialize();
	}
 
	@Override
	public void startUp() {
		addScene(new WelcomeScene(this, "Hello World Scene"));
		//addScene(new BrainWritingScene(this, "Hello World Scene"));
	}
}

