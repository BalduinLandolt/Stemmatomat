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
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;

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
		btnCancel.addActionListener(e -> actionCancel());
		buttonsPanel.add(btnCancel);
		
		JButton btnOK = new JButton("  OK  ");
		btnOK.addActionListener(e -> actionOK());
		buttonsPanel.add(btnOK);
		
		JButton btnOKPlusAnother = new JButton("OK, import another");
		btnOKPlusAnother.addActionListener(e -> actionOKPlusAnother());
		buttonsPanel.add(btnOKPlusAnother);
		
		// TODO more
		
	}

	private void actionOKPlusAnother() {
		Log.log("Import Dialog: OK, and open another import.");
		// TODO Auto-generated method stub
	}

	private void actionOK() {
		Log.log("Import Dialog: OK");
		// TODO Auto-generated method stub
	}

	private void actionCancel() {
		Log.log("Import Dialog: Canceled");
		dispose();
	}


}
