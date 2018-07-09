package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Loggable;

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
		setVisible(true);
		Log.log("GUI shown.");
	}

	public void openImportDialog() {
		// TODO Auto-generated method stub
		Log.log("Opening Import Dialog.");
		ImportDialog dialog = new ImportDialog(this);
		dialog.setLocation(300, 200);
		dialog.setVisible(true);
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
			// TODO Auto-generated method stub
		}
		
	}

}
