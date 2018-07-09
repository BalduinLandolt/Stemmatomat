/**
 * 
 */
package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;



/**
 * @author Balduin Landolt
 *
 */
public class Log {
	public static final String lineSep = System.getProperty("line.separator");
	
	private static Log lastLog;
	private File logFile;
	private PrintStream printStream;
	private LogStream logStream;
	private LinkedList<Loggable> loggables;

	public Log() {
		createLogFile();
		Log.lastLog = this;
		logStream = new LogStream();
		loggables = new LinkedList<Loggable>();
		println("Log File created.");
	}
	
	public static Log getLatestLogInstance() {
		return lastLog;
	}


	public static synchronized void log(String msg){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(msg);
	}
	public static synchronized void log(Iterable<Object> i){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(i);
	}
	public static synchronized void log(Object[] oo){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(oo);
	}
	public static synchronized void log(Object o){
		if (lastLog == null){
			new Log();
		}
		lastLog.println(o);
	}
	public static synchronized void log(){
		if (lastLog == null){
			new Log();
		}
		lastLog.println();
	}
	


	public synchronized void println (){
		println("");
	}
	public synchronized void println (Object o){
		println(o.toString());
	}
	public synchronized void println (Object[] oo){
		println(Arrays.asList(oo));
	}
	public synchronized void println (Iterable<Object> i){
		println(i.toString());
		for (Object o: i) {
			println("   "+o.toString());
		}
	}
	public synchronized void println (String msg){
		System.out.println(msg);
		printStream.println(msg);
		for (Loggable l: loggables) {
			l.log(msg);
		}
	}
	

	private void createLogFile() {
		try {
			logFile = new File("log.txt");
			createAndFlush(logFile);
			FileOutputStream o = new FileOutputStream(logFile);
			printStream = new PrintStream(o);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	private void createAndFlush(File log) throws IOException {
		if (log.exists())
			log.delete();
		log.createNewFile();
	}
	public void terminate(boolean b) {
		println("Closing log...");
		println("");
		println("Shut down.");
		printStream.close();
		if (b)
			displayLogFile();
	}
	public void displayLogFile() {
		if (Desktop.isDesktopSupported()){
			Desktop d = Desktop.getDesktop();
			try {
				d.open(logFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public static void initialize() {
		new Log();
	}

	/**
	 * 
	 */
	public static void terminate() {
		lastLog.terminate(Settings.openLogOnClose());
	}
	
	public LogStream getLogStream() {
		return logStream;
	}
	
	public static LogStream getCurrentLogStream() {
		return lastLog.getLogStream();
	}
	
	class LogStream extends OutputStream {

		/* (non-Javadoc)
		 * @see java.io.OutputStream#write(int)
		 */
		@Override
		public void write(int arg0) throws IOException {
			printStream.write(arg0);
			System.out.write(arg0);
		}


		
	}

	/**
	 * @param ex
	 */
	public static void err(Exception ex) {
		log("ERROR!");
		log(ex.getMessage());
		StringWriter writer = new StringWriter();
		ex.printStackTrace(new PrintWriter(writer));
		String stackTrace = writer.toString();
		log(stackTrace);
		//ex.printStackTrace(new PrintStream(getCurrentLogStream()));
	}

	/**
	 * @param gui
	 */
	public static void addLoggable(Loggable l) {
		lastLog.addLoggableToList(l);
	}

	/**
	 * @param l
	 */
	private void addLoggableToList(Loggable l) {
		loggables.add(l);
	}
}
