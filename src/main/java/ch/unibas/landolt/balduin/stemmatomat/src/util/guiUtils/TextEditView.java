package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.jdom2.Document;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

@SuppressWarnings("serial")
public class TextEditView extends JPanel implements DocumentListener {
	
	private ImportDialog parent;
	
	private JTextArea textArea;
	
	private Text text;

	public TextEditView(ImportDialog owner) {
		parent = owner;
		text = parent.getText();
		
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
		generateText();
		refresh();
	}

	private void generateText() {
		Text newText = new Text(text.getIdentifier(), text.getShelfmark());
		
		String s = textArea.getText();
		s = s.replace("¶", "");
		s = s.replace("\n", Log.lineSep);
		
		String[] ss = s.split(Log.lineSep);
		
		for (String l: ss) {
			l = l.trim();
			newText.appendText(l);
			newText.appendSegmentation();
		}
		
		text = newText;
	}

	@Override
	public void changedUpdate(DocumentEvent arg0) {
		// do nothing here
	}

	@Override
	public void insertUpdate(DocumentEvent arg0) {
		textChanged();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) {
		textChanged();
	}

	public void refresh() {
		int c = textArea.getCaretPosition();
		PlainDocument d = new PlainDocument();
		try {
			d.insertString(0, text.getTextWithPilcrow(), null);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		textArea.setDocument(d);
		d.addDocumentListener(this);
		textArea.setCaretPosition(c);
	}

	public Text getText() {
		return text;
	}

}
