package de.braster;
import org.mt4j.MTApplication;




public class StartBraster extends MTApplication {
	
	
	public static void main(String[] args) {
		initialize(true);
	}
 
	@Override
	public void startUp() {
		addScene(new WelcomeScene(this, "Hello World Scene"));
		//addScene(new SetupScene(this, "Hello World Scene"));
		//addScene(new ClusteringScene(this, "Hello World Scene"));
		//addScene(new EvaluationHelp(this, "Hello World Scene"));
		//addScene(new EvaluationScene(this, "Hello World Scene"));
	}
}

