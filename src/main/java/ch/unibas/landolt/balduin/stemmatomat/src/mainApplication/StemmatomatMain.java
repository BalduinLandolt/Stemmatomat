package ch.unibas.landolt.balduin.stemmatomat.src.mainApplication;

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
		fakeImport();
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

		// TODO get Information from caller
		
		
		caller.dispose();
		mainGUI.displayTexts(texts);
	}




	private void fakeImport() {
		// TODO remove me as soon as possible 
		Log.log("faking an import...");

		Text text1 = new Text("aaaa", "Manuscript no 1", "Es war ein mal £ vor langer, langer Zeit £ irgend etwas.");
		texts.add(text1);
		Text text2 = new Text("bbbb", "Manuscript no 2", "Es war ein mal £ vor langer, langer Zeit £ irgend eine Sache.");
		texts.add(text2);
		
		//
		

		mainGUI.displayTexts(texts);
	}
	
}
