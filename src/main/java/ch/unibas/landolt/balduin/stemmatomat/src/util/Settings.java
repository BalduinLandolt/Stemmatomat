/**
 * 
 */
package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * @author Balduin Landolt
 *
 */
public class Settings {
	private static boolean openLogOnClose;
	private static boolean askBeforeShutDown;
	private static boolean autoOverwrite;
	
	public static boolean openLogOnClose() {
		return openLogOnClose;
	}
	
	public static void getSettings(Preferences userPreferences) {
		Log.log("loading Settings...");
		
		openLogOnClose = userPreferences.getBoolean("openLogOnClose", true);
		Log.log("Got Setting: openLogOnClose="+openLogOnClose);
		
		askBeforeShutDown = userPreferences.getBoolean("askBeforeShutDown", false);
		Log.log("Got Setting: askBeforeShutDown="+askBeforeShutDown);
		
		autoOverwrite = userPreferences.getBoolean("autoOverwrite", false);
		Log.log("Got Setting: autoOverwrite="+autoOverwrite);
		
		Log.log("Done loading Settings.");
	}

	/**
	 * @return
	 */
	public static boolean askBeforeShutDown() {
		return askBeforeShutDown;
	}

	/**
	 * @return
	 */
	public static boolean autoOverwrite() {
		return autoOverwrite;
	}

	public static void savePreferences(Preferences userPreferences) {
		Log.log("Saving Settings...");

		userPreferences.putBoolean("openLogOnClose", openLogOnClose);
		userPreferences.putBoolean("askBeforeShutDown", askBeforeShutDown);
		userPreferences.putBoolean("autoOverwrite", autoOverwrite);
		
		int length = -1;
		try {
			length = userPreferences.keys().length;
		} catch (BackingStoreException e) {
			Log.log("Problem with preferences.");
			e.printStackTrace();
		}
		
		Log.log("Settings saved: "+length);
	}
}
