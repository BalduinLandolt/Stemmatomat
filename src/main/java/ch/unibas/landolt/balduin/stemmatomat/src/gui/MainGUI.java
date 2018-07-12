package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Loggable;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements WindowListener, Loggable {
	
	private StemmatomatMain parent;
	private Actions actions;
	
	// Components
	private JTabbedPane tabs;
	private JTextArea logTextArea;
	private JPanel workspace;
	
	// Menu
	private JMenuBar menuBar;
	private JMenu m_file;
	private JMenuItem mi_importText;
	private JMenu m_edit;
	private JMenu m_settings;
	private JCheckBoxMenuItem cmi_autoOpenLog;

	public MainGUI(StemmatomatMain parent){
		super("Stemmat-o-mat!");
		
		this.parent = parent;
		actions = new Actions();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		addWindowListener(this);
		setExtendedState(MAXIMIZED_BOTH);
		setMinimumSize(new Dimension(300, 200));
		
		setUpGUI();
		Log.addLoggable(this);
		
		Log.log("GUI created.");
	}




	private void setUpMenu() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		m_file = new JMenu("File");
		menuBar.add(m_file);
		
		mi_importText = new JMenuItem("Import Text");
		mi_importText.addActionListener(e -> actions.importText());
		m_file.add(mi_importText);
		
		m_edit = new JMenu("Edit");
		menuBar.add(m_edit);
		
		m_settings = new JMenu("Settings");
		menuBar.add(m_settings);
		
		cmi_autoOpenLog = new JCheckBoxMenuItem("Auto Open Log File on Close");
		cmi_autoOpenLog.setSelected(Settings.openLogOnClose());
		cmi_autoOpenLog.addItemListener(e -> Settings.setOpenLogOnClose(cmi_autoOpenLog.isSelected()));
		m_settings.add(cmi_autoOpenLog);
		
		// TODO add actual content
		
	}




	private void setUpGUI() {
		JPanel p = new JPanel(new BorderLayout());
		setContentPane(p);
		
		setUpMenu();
		
		tabs = new JTabbedPane();
		p.add(tabs);
		
		workspace = new JPanel(new BorderLayout());
		JScrollPane sp = new JScrollPane(workspace);
		tabs.addTab("Wokrspace", null, sp, "Everything of importance happens here.");
		
		logTextArea = new JTextArea();
		logTextArea.setEditable(false);
		sp = new JScrollPane(logTextArea);
		tabs.addTab("Log", null, sp, "You can ignore me. Or show me to Balduin, if there's a problem.");
		
		//TODO more content
	}




	public void log(String s) {
		logTextArea.append(s);
		logTextArea.append(Log.lineSep);
		logTextArea.setCaretPosition(logTextArea.getText().length());
	}



	public void launch() {
		refreshSettings();
		setVisible(true);
		Log.log("GUI shown.");
	}

	private void refreshSettings() {
		Log.log("Refreshing GUI settings");
		cmi_autoOpenLog.setSelected(Settings.openLogOnClose());
	}




	public void openImportDialog() {
		Log.log("Opening Import Dialog.");
		ImportDialog dialog = new ImportDialog(this);
		dialog.setLocation(300, 200);
		dialog.setVisible(true);
	}

	public void importIsDone(ImportDialog caller, boolean openAnother) {
		parent.importIsDone(caller);
		
		if (openAnother)
			openImportDialog();
	}




	public void displayTexts(ArrayList<Text> texts) {
		if (texts == null || texts.isEmpty())
			return;
		
		Log.log("Displaying Texts.");
		
		Vector<String> head = new Vector<String>();
		Vector<Vector<String>> data = new Vector<Vector<String>>();
		
		int length = 0;
		
		for (Text t: texts) {
			Vector<String> tmp = new Vector<String>();
			tmp.addAll(t.getList());
			data.add(tmp);
			length = Math.max(length, tmp.size());
		}
		
		for (int i = 0; i<length; i++) {
			if (i==0)
				head.add("Shelf Mark");
			else if (i==1)
				head.add("ID");
			else
				head.add("#"+(i-1));
		}
		
		JTable table = new JTable(data, head);
		JScrollPane sp = new JScrollPane(table);
		workspace.add(sp, BorderLayout.CENTER);
	}

	

	public void windowClosing(WindowEvent arg0) {
		Log.log("Window Closing requested.");
		parent.terminate();
	}
	
	public void windowActivated(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	
	private class Actions{

		public void importText() {
			Log.log("Action called: import text");
			openImportDialog();
		}
		
	}

}
