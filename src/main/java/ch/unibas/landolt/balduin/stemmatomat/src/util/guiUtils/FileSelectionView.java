package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

@SuppressWarnings("serial")
public class FileSelectionView extends JPanel {
	
	private ImportDialog parent;
	
	private JTextField path;
	private JButton btn_selectFile;
	private JTextField shelfmark;
	private JTextField shelfmark_input;
	private JTextField id;
	private JTextField id_input;
	private JTextField response;
	
	private DocChangeListener docChange;

	public FileSelectionView(ImportDialog owner) {
		setLayout(new BorderLayout(8,8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		parent = owner;
		
		JPanel p = new JPanel(new BorderLayout(8,8));
		path = new JTextField();
		path.setEditable(false);
		p.add(path, BorderLayout.CENTER);
		
		btn_selectFile = new JButton("...");
		btn_selectFile.addActionListener(e -> selectFile());
		p.add(btn_selectFile, BorderLayout.EAST);
		
		this.add(p, BorderLayout.NORTH);
		
		JPanel p2 = new JPanel(new BorderLayout(8, 8));
		this.add(p2, BorderLayout.CENTER);
		
		JPanel p3 = new JPanel(new GridLayout(2,2,8,8));
		p3.setBorder(BorderFactory.createTitledBorder("Meta Information"));
		docChange = new DocChangeListener();
		shelfmark = new JTextField("Shelfmark: ");
		shelfmark.setEditable(false);
		shelfmark.setToolTipText("The 'Shelfmark' should make the manuscript identifiable for you. It's never used by the program.");
		p3.add(shelfmark);
		shelfmark_input = new JTextField("");
		shelfmark_input.setToolTipText("The 'Shelfmark' should make the manuscript identifiable for you. It's never used by the program.");
		shelfmark_input.setEditable(true);
		shelfmark_input.getDocument().addDocumentListener(docChange);
		p3.add(shelfmark_input);
		id = new JTextField("ID: ");
		id.setEditable(false);
		id.setToolTipText("The 'ID' must be unique.");
		p3.add(id);
		id_input = new JTextField("");
		id_input.setEditable(true);
		id_input.setToolTipText("The 'ID' must be unique.");
		id_input.getDocument().addDocumentListener(docChange);
		p3.add(id_input);
		p2.add(p3, BorderLayout.NORTH);
		
		response = new JTextField();
		response.setEditable(false);
		p2.add(response, BorderLayout.SOUTH);

		if (owner.getText() == null) {
			path.setText("No text selected.");
			response.setText("No Text selected.");
		} else {
			// TODO put file path here
		}
	}

	private void selectFile() {
		//TODO eventually, I will want another standard path, I guess
		File f = new File("res");Log.log(f.getAbsolutePath());
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(f);
		chooser.setFileFilter(new FileNameExtensionFilter("xml", "xml"));
		int res = chooser.showOpenDialog(this);
		
		if (res != JFileChooser.APPROVE_OPTION) {
			Log.log("No File Chosen.");
			return;
		}
		
		f = chooser.getSelectedFile();
		
		if (!f.exists() || f.isDirectory() || !f.getName().endsWith(".xml")) {
			Log.log("Invalid File Chosen.");
			return;
		}
		
		path.setText(f.getAbsolutePath());
		loadFile(f);
	}

	private void loadFile(File f) {
		// TODO implement loading files
		Log.log("Loading File not yet implemented.");
		
		parent.checkFileLoaded(this);
	}
	
	public void responde(String msg) {
		response.setText(msg);
	}

	public boolean hasFile() {
		// TODO implement checking, if a file has been loaded
		return true;
	}

	public boolean hasID() {
		return !id_input.getText().trim().equals("");
	}

	public String getID() {
		return id_input.getText().trim();
	}
	
	private class DocChangeListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent arg0) {
			parent.checkFileLoaded(FileSelectionView.this);
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {
			parent.checkFileLoaded(FileSelectionView.this);
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			parent.checkFileLoaded(FileSelectionView.this);
		}
	}

}
