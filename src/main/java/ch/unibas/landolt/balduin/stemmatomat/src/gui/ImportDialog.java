package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;

@SuppressWarnings("serial")
public class ImportDialog extends JDialog {
	
	private MainGUI parent;

	public ImportDialog(MainGUI p) {
		super(p, "Import Text", true);
		
		
		setUpComponents();
		pack();
		
		parent = p;
	}

	private void setUpComponents() {
		
		JPanel outer = new JPanel();
		outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
		getContentPane().add(outer);
		
		JPanel filePanel = new JPanel();
		filePanel.setBorder(BorderFactory.createTitledBorder("Input File: "));
		outer.add(filePanel);
		
		JPanel optionPanel = new JPanel();
		optionPanel.setBorder(BorderFactory.createTitledBorder("Options: "));
		outer.add(optionPanel);
		
		JPanel buttonsPanel = new JPanel();
		outer.add(buttonsPanel);

		JButton btnCancel = new JButton("Cancel");
		buttonsPanel.add(btnCancel);
		JButton btnOK = new JButton(" OK ");
		buttonsPanel.add(btnOK);
		JButton btnOKPlusAnother = new JButton("OK, import another");
		buttonsPanel.add(btnOKPlusAnother);
		
		// TODO Auto-generated method stub
		
	}


}
