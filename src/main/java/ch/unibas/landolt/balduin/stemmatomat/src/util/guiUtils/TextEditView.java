package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;

@SuppressWarnings("serial")
public class TextEditView extends JPanel implements DocumentListener {
	
	private ImportDialog parent;
	
	private JTextArea textArea;

	public TextEditView(ImportDialog owner) {
		parent = owner;
		
		setLayout(new GridLayout(1, 2, 8, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		textArea = new JTextArea();
		textArea.setLineWrap(false);
		textArea.setEditable(true);
		textArea.getDocument().addDocumentListener(this);
		JScrollPane sp = new JScrollPane(textArea);
		add(sp, BorderLayout.CENTER);
		
		// TODO eventually, add search & replace options
	}
	
	private void textChanged() {
		//TODO create new Text from changed text area contents, and display
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		textChanged();
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		textChanged();
	}

}
