package ch.unibas.landolt.balduin.stemmatomat.src.mainApplication;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.MainGUI;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;

public class StemmatomatMain {
	
	private MainGUI mainGUI;
	private Preferences userPreferences;

	public StemmatomatMain() {
		super();
		
		Log.initialize();

		Log.log("Running under Version: "+Runtime.class.getPackage().getImplementationVersion());
		Log.log("Running under Version: "+System.getProperty("java.version"));
		
		generatePreferences();
		
		//TODO other initialisations?
		
		//TODO create GUI
		mainGUI = new MainGUI(this);
	}

	private void generatePreferences() {
		userPreferences = Preferences.userNodeForPackage(this.getClass());
		try {
			Log.log("Created Preferences: "+userPreferences.keys().length);
		} catch (BackingStoreException e) {
			Log.log("Failed to load Preferences.");
			e.printStackTrace();
		}
	}

	public void run() {
		Settings.getSettings(userPreferences);
		mainGUI.launch();
	}

	public void terminate() {
		// TODO Auto-generated method stub
		
		savePreferences();
		
		Log.terminate();
		System.exit(0);
	}

	private void savePreferences() {
		// TODO Auto-generated method stub
		

		//updateSettings();
		Settings.savePreferences(userPreferences);
		
	}
	
}
