package ch.unibas.landolt.balduin.stemmatomat.src.mainApplication;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.MainGUI;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;

public class StemmatomatMain {
	
	private MainGUI mainGUI;

	public StemmatomatMain() {
		super();
		
		Log.initialize();
		
		//TODO get preferences
		
		//TODO other initialisations?
		
		//TODO create GUI
		mainGUI = new MainGUI(this);
	}

	public void run() {
		// TODO launch GUI
		mainGUI.launch();
	}

	public void terminate() {
		// TODO Auto-generated method stub
		
		
		Log.terminate();
		System.exit(0);
	}
	
	
	
	//
}
