package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.TableColumn;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Loggable;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;
import ch.unibas.landolt.balduin.stemmatomat.src.util.TextDisplayTableModel;

@SuppressWarnings("serial")
public class MainGUI extends JFrame implements WindowListener, Loggable {
	
	private StemmatomatMain parent;
	
	// Components
	private JTabbedPane tabs;
	private JTextArea logTextArea;
	private JPanel workspace;
	private JTable textTable;
	
	// Menu
	private JMenuBar menuBar;
	private JMenu m_file;
	private JMenuItem mi_importText;
	private JMenuItem mi_saveProject;
	private JMenu m_edit;
	private JMenu m_settings;
	private JCheckBoxMenuItem cmi_autoOpenLog;
	
	private JPopupMenu popup;

	public MainGUI(StemmatomatMain parent){
		super("Stemmat-o-mat!");
		
		this.parent = parent;
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
		mi_importText.addActionListener(e -> importText());
		m_file.add(mi_importText);
		
		m_file.addSeparator();
		
		mi_saveProject = new JMenuItem("Save Project");
		mi_saveProject.addActionListener(e -> saveProject());
		m_file.add(mi_saveProject);
		
		m_edit = new JMenu("Edit");
		menuBar.add(m_edit);
		
		m_settings = new JMenu("Settings");
		menuBar.add(m_settings);
		
		cmi_autoOpenLog = new JCheckBoxMenuItem("Auto Open Log File on Close");
		cmi_autoOpenLog.setSelected(Settings.openLogOnClose());
		cmi_autoOpenLog.addItemListener(e -> Settings.setOpenLogOnClose(cmi_autoOpenLog.isSelected()));
		m_settings.add(cmi_autoOpenLog);
		
		// TODO add actual content
		
		// TODO option to remove text/texts
	}




	private void setUpGUI() {
		JPanel p = new JPanel(new BorderLayout());
		setContentPane(p);
		
		setUpMenu();
		createPopUp();
		
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




	private void createPopUp() {
		popup = new JPopupMenu();
		JMenuItem test = new JMenuItem("Test");
		popup.add(test);
	}




	public void log(String s) {
		logTextArea.append(s);
		logTextArea.append(Log.lineSep);
		logTextArea.setCaretPosition(logTextArea.getText().length());
	}



	public void launch() {
		refreshSettings();
		displayTexts(null);
		setVisible(true);
		refreshUI();
		Log.log("GUI shown.");
	}

	private void refreshSettings() {
		Log.log("Refreshing GUI settings");
		cmi_autoOpenLog.setSelected(Settings.openLogOnClose());
	}




	public void openImportDialog() {
		Log.log("Opening Import Dialog.");
		ImportDialog dialog = new ImportDialog(this);
		dialog.run();
	}

	public void importIsDone(ImportDialog caller, boolean openAnother) {
		parent.importIsDone(caller);
		
		if (openAnother)
			openImportDialog();
	}




	public void displayTexts(ArrayList<Text> texts) {
		workspace.removeAll();
		
		if (texts == null || texts.isEmpty()) {
			workspace.add(new JTextField("No Texts loaded. Hit 'File > Import Text'."), BorderLayout.NORTH);
			return;
		}
		
		TextDisplayTableModel model = new TextDisplayTableModel(texts);
		
		Log.log("Displaying Texts.");
		
		textTable = new JTable(model);
		textTable.addMouseListener(new PopUpListener());
		textTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		textTable.doLayout();
		JScrollPane sp = new JScrollPane(textTable);
		//textTable.setFillsViewportHeight(true);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		workspace.add(sp, BorderLayout.CENTER);
		
		revalidate();
		repaint();
		
		adjustColumnWidth(textTable);
		
		refreshUI();
	}
	

	private void adjustColumnWidth(JTable t) { //TODO geht so nicht
		TableColumn c = null;
		for (int i=0; i<t.getColumnCount(); i++) {
			c = t.getColumnModel().getColumn(i);
			c.sizeWidthToFit();
			c.setPreferredWidth(c.getPreferredWidth()+50);
			//c.setMinWidth(c.getPreferredWidth()+50);
			Log.log("Col: "+i+" Width: "+c.getWidth()+" PrefWidth: "+c.getPreferredWidth());
		}

		revalidate();
		repaint();

		for (int i=0; i<t.getColumnCount(); i++) {
			c = t.getColumnModel().getColumn(i);
			//c.setPreferredWidth(c.getPreferredWidth()+20);
			//c.setMinWidth(c.getPreferredWidth()+50);
			Log.log("Col: "+i+" Width: "+c.getWidth()+" PrefWidth: "+c.getPreferredWidth());
		}
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
	

	public void importText() {
		Log.log("Action called: import text");
		openImportDialog();
	}

	public void saveProject() {
		Log.log("Action called: save project");
		File f = parent.getSaveDirectory();
		
		if (f == null) {
			f = getSaveDirectory();
		}
		
		Log.log("Saving to: "+f.getAbsolutePath());
		
		
		
		//TODO
	}
	
	private File getSaveDirectory() {
		File f = new File(".");
		f = f.getParentFile();
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(f);
		chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
		int res = chooser.showOpenDialog(this);
		
		if (res != JFileChooser.APPROVE_OPTION) {
			Log.log("No File Chosen.");
			return null;
		}
		
		f = chooser.getSelectedFile();
		if (!f.getName().endsWith(".xml"))
			f = new File(f.getPath()+".xml");
		
		//TODO check if it exists. ask about override
//		
//		if (!f.exists())
//			f.createNewFile()
		
//		if (!f.exists() || f.isDirectory() || !f.getName().endsWith(".xml")) {
//			Log.log("Invalid File Chosen.");
//			return;
//		}
		
		return f;
	}

	private class PopUpListener extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e) {
			Log.log("Mouse clicked on Table. Showing popup.");
            popup.show(e.getComponent(), e.getX(), e.getY());
            Log.log(e.getComponent());
            Log.log(e.getPoint());
            Point p = e.getPoint();
            String s = textTable.getModel().getValueAt(textTable.rowAtPoint(p), textTable.columnAtPoint(p)).toString();
            Log.log(s);
		}
	    
	    
// would be applicable, if popup should only show on right click.
//		public void mousePressed(MouseEvent e) {
//	        maybeShowPopup(e);
//	    }
//
//	    public void mouseReleased(MouseEvent e) {
//	        maybeShowPopup(e);
//	    }
//
//	    private void maybeShowPopup(MouseEvent e) {
//	        if (e.isPopupTrigger()) {
//	            popup.show(e.getComponent(), e.getX(), e.getY());
//	            Log.log(e.getComponent());
//	            Log.log(e.getPoint());
//	            Point p = e.getPoint();
//	            String s = textTable.getModel().getValueAt(textTable.rowAtPoint(p), textTable.columnAtPoint(p)).toString();
//	            Log.log(s);
//	        }
//	    }
	}

	public ArrayList<Text> getTexts() {
		return parent.getTexts();
	}
	
	public void refreshUI() {
		mi_saveProject.setEnabled(parent.hasData());
		//TODO stuff here
	}

}
