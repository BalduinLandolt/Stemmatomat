package ch.unibas.landolt.balduin.stemmatomat.src.mainApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.jdom2.Document;
import org.jdom2.Element;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;
import ch.unibas.landolt.balduin.stemmatomat.src.gui.MainGUI;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

public class StemmatomatMain {
	
	private MainGUI mainGUI;
	private Preferences userPreferences;
	
	private ArrayList<Text> texts = new ArrayList<Text>();
	private File saveDirectory = null;

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
		System.out.println("Console only: Log is shut down.");
		mainGUI.dispose();
	}

	private void savePreferences() {
		Settings.savePreferences(userPreferences);
	}
	
	public void importIsDone(ImportDialog caller) {
		Text newT = caller.getText();
		texts.add(newT);
		
		caller.dispose();
		displayTexts();
		Log.log("Imported Text");
	}


	private void displayTexts() {
		normalizeTextLength();
		mainGUI.displayTexts(texts);
	}

	private void normalizeTextLength() {
		int longest = 0;
		for (Text t: texts) {
			t.trimEmptyEnds();
			longest = Math.max(longest, t.getLength());
		}

		for (Text t: texts) {
			t.normalizeLengthTo(longest);
		}
	}

	public ArrayList<Text> getTexts() {
		return texts;
	}

	public boolean hasData() {
		return !(texts == null || texts.isEmpty());
	}

	public File getSaveDirectory() {
		return saveDirectory;
	}

	public void setSaveDirectory(File f) {
		saveDirectory = f;
	}

	public void closeProject() {
		Log.log("Closing current project.");
		// TODO handle unsaved changes etc.
		texts = new ArrayList<Text>();
		saveDirectory = null;
		mainGUI.displayTexts(texts);
	}

	public void loadFromDoc(Document d) {
		Element root = d.getRootElement();
		LinkedList<Element> cc = new LinkedList<>(root.getChildren("text"));
		for (Element c: cc) {
			Text t = Text.buildFromXML(c);
			texts.add(t);
		}
		
		displayTexts();
		Log.log("Loading successful.");
	}
	
	//TODO todos:
	
	//TODO data stage architecture (including menu, keybinding)
	//TODO nexus export
	//TODO doing alignment on right click
	//TODO doing evaluation on left click
	
}
