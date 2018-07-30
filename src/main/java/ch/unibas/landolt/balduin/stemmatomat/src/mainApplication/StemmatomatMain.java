package ch.unibas.landolt.balduin.stemmatomat.src.mainApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;
import ch.unibas.landolt.balduin.stemmatomat.src.gui.MainGUI;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

public class StemmatomatMain {
	
	private MainGUI mainGUI;
	private Preferences userPreferences;
	
	private ArrayList<Text> texts = new ArrayList<Text>();

	public StemmatomatMain() {
		super();
		
		Log.initialize();

		Log.log("Running under Version: "+Runtime.class.getPackage().getImplementationVersion());
		Log.log("Running under Version: "+System.getProperty("java.version"));
		
		generatePreferences();
		
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
		savePreferences();
		
		Log.terminate();
		System.out.println("Consloe only: Log is shut down.");
		//System.exit(0);
	}

	private void savePreferences() {
		Settings.savePreferences(userPreferences);
	}
	
	public void importIsDone(ImportDialog caller) {
		Text newT = caller.getText();
		texts.add(newT);
		
		caller.dispose();
		mainGUI.displayTexts(texts);
		Log.log("Imported Text");
	}


	public ArrayList<Text> getTexts() {
		return texts;
	}

	public boolean hasData() {
		return !(texts == null || texts.isEmpty());
	}

	public File getSaveDirectory() {
		// TODO Auto-generated method stub
		return null;
	}
	
	//TODO todos:
	//TODO data stage architecture (including menu, keybinding)
	//TODO loading project (in menu)
	//TODO quit application (in menu)
	//TODO nexus export
	//TODO doing alignment on right click
	//TODO doing ecvaluation on left click
	//TODO option save as ... in menu
	
}
