package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;

@SuppressWarnings("serial")
public class XMLSelectionView extends JPanel {
	
	private ImportDialog parent;
	
	private JTextArea textArea;
	private JPanel l1;
	private JPanel l2;

	public XMLSelectionView(ImportDialog owner) {
		parent = owner;
		
		setLayout(new GridLayout(1, 2, 8, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JPanel left = new JPanel(new GridLayout(2, 1, 8, 8));
		add(left);
		l1 = new JPanel();
		l1.setLayout(new BoxLayout(l1, BoxLayout.Y_AXIS));
		l1.setBorder(BorderFactory.createTitledBorder("Include in Text"));
		left.add(l1);
		l2 = new JPanel();
		l2.setLayout(new BoxLayout(l2, BoxLayout.Y_AXIS));
		l2.setBorder(BorderFactory.createTitledBorder("Use for Segmentation"));
		left.add(l2);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea);
		add(sp);
	}

}
