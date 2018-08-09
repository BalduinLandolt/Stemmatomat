package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
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
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Loggable;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Settings;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;
import ch.unibas.landolt.balduin.stemmatomat.src.util.TextDisplayTableModel;
import ch.unibas.landolt.balduin.stemmatomat.src.util.TextSegment;

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
	private JMenuItem mi_saveAsProject;
	private JMenuItem mi_loadProject;
	private JMenuItem mi_quit;
	private JMenu m_edit;
	private JMenu m_settings;
	private JCheckBoxMenuItem cmi_autoOpenLog;
	
	private CustomPopup popup;

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


// TODO prevalidate stemmatic values (e.g. setting all values of the first text to 0)

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

		mi_saveAsProject = new JMenuItem("Save Project As ...");
		mi_saveAsProject.addActionListener(e -> saveAsProject());
		m_file.add(mi_saveAsProject);
		
		mi_loadProject = new JMenuItem("Load Project");
		mi_loadProject.addActionListener(e -> loadProject());
		m_file.add(mi_loadProject);
		
		m_file.addSeparator();

		mi_quit = new JMenuItem("Quit Application");
		mi_quit.addActionListener(e -> quit());
		m_file.add(mi_quit);
		
		m_edit = new JMenu("Edit");
		menuBar.add(m_edit);
		
		m_settings = new JMenu("Settings");
		menuBar.add(m_settings);
		
		cmi_autoOpenLog = new JCheckBoxMenuItem("Auto Open Log File on Close");
		cmi_autoOpenLog.setSelected(Settings.openLogOnClose());
		cmi_autoOpenLog.addItemListener(e -> Settings.setOpenLogOnClose(cmi_autoOpenLog.isSelected()));
		m_settings.add(cmi_autoOpenLog);
		
		// TODO option to remove text/texts
	}




	private void quit() {
		// TODO Handle unsaved Changes
		parent.terminate();
	}




	private void loadProject() {
		Log.log("Action called: load project");
		
		//TODO handle potential unsaved changes
		
		File f = getFileToLoad();
		
		if (f == null || !f.exists()) {
			Log.log("No File to Load.");
			return;
		}

		Log.log("Loading from File: "+f.getAbsolutePath());
		
		Document d = getDataFromFile(f);
		if (!isValidSave(d)) {
			Log.log("File is not a valid saved project.");
			return;
		}
		
		parent.closeProject();
		parent.loadFromDoc(d);
		parent.setSaveDirectory(f);
	}




	private boolean isValidSave(Document d) {
		Element e = d.getRootElement();
		if (!e.getName().equals("StremmatomatProject"))
			return false;
		
		// TODO more validation? something in meta data?
		return true;
	}




	private Document getDataFromFile(File f) {
		Document r = null;
		try {
			r = new SAXBuilder().build(f);
		} catch (Exception e) {
			Log.log("Failed to load XML Document.");
			e.printStackTrace();
		}
		return r;
	}




	private File getFileToLoad() {
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
		
		return f;
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
	}




	private void createPopUp(MouseEvent e) {
		popup = new CustomPopup(e);

		if (SwingUtilities.isRightMouseButton(e)) {
			popup.setUpAsRightClick();
		} else if (SwingUtilities.isLeftMouseButton(e)) {
			popup.setUpAsLeftClick();
		} else {
			Log.log("clicked weird mouse button.");
			return;
		}
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
		TextTableCellRenderer renderer = new TextTableCellRenderer();
		
		Log.log("Displaying Texts.");
		
		textTable = new JTable(model);
		textTable.setDefaultRenderer(String.class, renderer);
		textTable.setFont(Settings.getStandardFont());//TODO 
		setColRenderers(textTable, renderer);
		textTable.setRowHeight(25);
		textTable.addMouseListener(new PopUpListener());
		textTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		JScrollPane sp = new JScrollPane(textTable);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		workspace.add(sp, BorderLayout.CENTER);
		adjustColumnWidth(textTable);
		
		refreshUI();
	}
	

	private void setColRenderers(JTable t, TextTableCellRenderer r) {
		TableColumn col = null;
		for (int i=2; i<t.getColumnCount(); i++) {
			col = t.getColumnModel().getColumn(i);
			col.setCellRenderer(r);
			Log.log();
		}
	}


	private void adjustColumnWidth(JTable t) {
		TableColumn col = null;
		for (int i=0; i<t.getColumnCount(); i++) {
			col = t.getColumnModel().getColumn(i);
		    int preferredWidth = col.getMinWidth();
		    
		    for (int row = 0; row < t.getRowCount(); row++) {
		    	TableCellRenderer cellRenderer = t.getCellRenderer(row, i);
		        Component c = t.prepareRenderer(cellRenderer, row, i);
		        int width = c.getPreferredSize().width + t.getIntercellSpacing().width;
		        preferredWidth = Math.max(preferredWidth, width);
		    }
		    
		    //"shelfmark" has a long header
		    if (i==0)
		    	preferredWidth+=30;
		    
		    col.setMaxWidth(preferredWidth+100);
		    col.setPreferredWidth(preferredWidth+30);
		    col.setWidth(preferredWidth+30);
		    Log.log("Col: "+i+" Width: "+col.getWidth()+" PrefWidth: "+preferredWidth);
		    revalidate();
		    repaint();

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




	private void saveAsProject() {
		File f = getSaveDirectory();
		parent.setSaveDirectory(f);
		saveProject();
	}

	public void saveProject() {
		Log.log("Action called: save project");
		File f = parent.getSaveDirectory();
		
		if (f == null) {
			saveAsProject();
			return;
		}
		
		Log.log("Saving to: "+f.getAbsolutePath());
		
		Document d = getDataForSave();
		
		XMLOutputter xmlOutput = new XMLOutputter();
		xmlOutput.setFormat(Format.getPrettyFormat());
		try {
			xmlOutput.output(d, new FileOutputStream(f));
		} catch (IOException e) {
			Log.log("Error: Couldn't save data to disc!");
			e.printStackTrace();
		}

		Log.log("Saving successful.");
		Log.log("("+f.getPath()+")");
		
		//TODO do I want this? make it an option
		try {
			Desktop.getDesktop().open(f.getParentFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Document getDataForSave() {
		Element e = new Element("StremmatomatProject");
		e.setAttribute("Date", java.time.LocalDateTime.now().toString());
		Document d = new Document(e);
		
		for (Text t: parent.getTexts()) {
			Element el_txt = t.getXMLRepresentation();
			e.addContent(el_txt);
		}
		
		return d;
	}




	private File getSaveDirectory() {
		File f = new File(".");
		f = f.getParentFile();
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(f);
		chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
		int res = chooser.showSaveDialog(this);
		
		if (res != JFileChooser.APPROVE_OPTION) {
			Log.log("No File Chosen.");
			return null;
		}
		
		f = chooser.getSelectedFile();
		if (!f.getName().endsWith(".xml"))
			f = new File(f.getPath()+".xml");
		
		//TODO check if it exists. ask about override
		
		return f;
	}

	private class PopUpListener extends MouseAdapter{
		
		public void mouseClicked(MouseEvent e) {
			Log.log("Mouse clicked on Table. Showing popup.");
			createPopUp(e);
            popup.show(e.getComponent(), e.getX(), e.getY());
		}
		
	}
	
	private class CustomPopup extends JPopupMenu{
		MouseEvent e;
		Text clickedText;
		int clickedSegmentIndex;
		String clickedSegmentText;

		public CustomPopup(MouseEvent ev) {
			e = ev;

	        Point p = e.getPoint();
	        TextDisplayTableModel model = (TextDisplayTableModel) textTable.getModel();
	        clickedText = model.getTextAtRow(textTable.rowAtPoint(p));
	        clickedSegmentIndex = textTable.columnAtPoint(p)-2;
	        clickedSegmentText = clickedText.getSegmentTextAt(clickedSegmentIndex);
	        
	        Log.log("Click: Text '"+clickedText.getIdentifier()+"' Segment #"+clickedSegmentIndex+" "+clickedSegmentText);
		}

		public void setUpAsLeftClick() {		
			addExistingOptions();
			addSeparator();
			addNextOption();
		}

		private void addNextOption() {
			int v = getHighestVal()+1;
			JMenuItem m = new JMenuItem(v+" [not yet in use]");
			m.setActionCommand(""+v);
			m.addActionListener(e -> setMeToStemVal(Integer.parseInt(e.getActionCommand())));
			add(m);
		}

		private void addExistingOptions() {
			int highest = getHighestVal();

			HashMap<Integer, String> map = new HashMap<>();
			
			for (Text t: parent.getTexts()) {
				if (t == clickedText) {
					continue;
				}
				String s = t.getSegmentTextAt(clickedSegmentIndex);
				int stemVal = t.getStemmaticValue(clickedSegmentIndex);
				map.put(Integer.valueOf(stemVal), s);
			}
			
			for (int i=0; i<=highest; i++) {
				String s = map.get(Integer.valueOf(i));
				
				JMenuItem m = new JMenuItem(i+" ("+s+")");
				m.setActionCommand(""+i);
				m.addActionListener(e -> setMeToStemVal(Integer.parseInt(e.getActionCommand())));
				add(m);
			}
		}

		private void setMeToStemVal(int val) {
			clickedText.setStemVal(clickedSegmentIndex, val);
			Log.log("Changed Stemmatic Value: (Text: "+clickedText.getIdentifier()+", '"+clickedSegmentText+"') to "+val);
			refreshUI();
		}

		private int getHighestVal() {
			int max = 0;
			for (Text t: parent.getTexts()) {
				int stemVal = t.getStemmaticValue(clickedSegmentIndex);
				max = Math.max(max, stemVal);
			}
			return max;
		}

		public void setUpAsRightClick() {
			//TODO enable items, as soon as implemented
			JMenuItem i = null;

			i = new JMenuItem("Edit Text");
			i.setEnabled(false);
			i.addActionListener(e -> editText(clickedText));
			add(i);
			
			addSeparator();
			
			i = new JMenuItem("Delete Segment");
			i.setEnabled(false);
			i.addActionListener(e -> removeSegmentFromText(clickedText, clickedSegmentIndex));
			add(i);
			
			i = new JMenuItem("<< Move Segment Left (removes content to the left)");
			i.setEnabled(false);
			i.addActionListener(e -> moveSegmentToLeft(clickedText, clickedSegmentIndex));
			add(i);
			
			i = new JMenuItem(">> Move Segment Right");
			i.setEnabled(false);
			i.addActionListener(e -> moveSegmentToRight(clickedText, clickedSegmentIndex));
			add(i);
			
			addSeparator();
			
			i = new JMenuItem("Split Segment");
			i.setEnabled(false);
			i.addActionListener(e -> splitSegment(clickedText, clickedSegmentIndex));
			add(i);
			
			i = new JMenuItem("Split Column");
			i.setEnabled(false);
			i.addActionListener(e -> splitColum(clickedSegmentIndex));
			add(i);
		}
		
	}

	public ArrayList<Text> getTexts() {
		return parent.getTexts();
	}




	public void splitSegment(Text text, int segmentIndex) {
		// TODO Auto-generated method stub
	}


	public void splitColum(int segmentIndex) {
		// TODO Auto-generated method stub
	}


	public void moveSegmentToLeft(Text text, int segmentIndex) {
		// TODO Auto-generated method stub
	}

	
	public void moveSegmentToRight(Text text, int segmentIndex) {
		// TODO Auto-generated method stub
	}




	public void removeSegmentFromText(Text text, int segmentIndex) {
		// TODO Auto-generated method stub
	}




	private void editText(Text text) {
		// TODO Auto-generated method stub
	}




	public void refreshUI() {
		mi_saveProject.setEnabled(parent.hasData());
		mi_saveAsProject.setEnabled(parent.hasData());

		//TODO stuff here
		
		revalidate();
		repaint();
	}
	
	private class TextTableCellRenderer extends DefaultTableCellRenderer {
		public TextTableCellRenderer() {super();}

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			
			if (value instanceof TextSegment) {
				// TODO change color according to stemVal
				// TODO change text accordingly
				//c.setBackground(Color.RED);
				Text t = ((TextSegment)value).getContainingText();
				setToolTipText(t.getIdentifier());
			}
			
			return c;
		}
		
	}

}
