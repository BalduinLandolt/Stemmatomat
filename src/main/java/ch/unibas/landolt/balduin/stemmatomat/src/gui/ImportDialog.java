package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;

@SuppressWarnings("serial")
public class ImportDialog extends JDialog {
	
	private MainGUI parent;
	
	private JPanel contents;
	private JPanel footer;

	private JButton btn_back;
	private JButton btn_forward;
	private JButton btn_cancel;
	private JButton btn_ok;
	private JButton btn_okAndAnother;

	public ImportDialog(MainGUI p) {
		super(p, "Import Text", true);
		
		
		setUpComponents();
		setMinimumSize(new Dimension(600, 400));
		pack();
		
		parent = p;
	}

	private void setUpComponents() {
		
		JPanel outest = new JPanel(new BorderLayout());
		getContentPane().add(outest);
		
		createContents(outest);
		
		createFooter(outest);
		
		
//		JPanel outer = new JPanel();
//		outer.setLayout(new BoxLayout(outer, BoxLayout.Y_AXIS));
//		getContentPane().add(outer);
//		
//		JPanel filePanel = new JPanel();
//		filePanel.setBorder(BorderFactory.createTitledBorder("Input File: "));
//		outer.add(filePanel);
//		
//		JPanel optionPanel = new JPanel();
//		optionPanel.setBorder(BorderFactory.createTitledBorder("Options: "));
//		outer.add(optionPanel);
//		
//		JPanel buttonsPanel = new JPanel();
//		outer.add(buttonsPanel);
//
//		JButton btnCancel = new JButton("Cancel");
//		btnCancel.addActionListener(e -> actionCancel());
//		buttonsPanel.add(btnCancel);
//		
//		JButton btnOK = new JButton("  OK  ");
//		btnOK.addActionListener(e -> actionOK());
//		buttonsPanel.add(btnOK);
//		
//		JButton btnOKPlusAnother = new JButton("OK, import another");
//		btnOKPlusAnother.addActionListener(e -> actionOKPlusAnother());
//		buttonsPanel.add(btnOKPlusAnother);
		
		// TODO more
		
	}

	private void createContents(JPanel outest) {
		contents = new JPanel(new BorderLayout());
		outest.add(contents, BorderLayout.CENTER);
	}

	private void createFooter(JPanel outest) {
		footer = new JPanel();
		footer.setLayout(new BoxLayout(footer, BoxLayout.X_AXIS));
		footer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		outest.add(footer, BorderLayout.SOUTH);

		btn_back = new JButton("Back");
		footer.add(btn_back);
		
		footer.add(Box.createRigidArea(new Dimension(5, 5)));

		btn_forward = new JButton("Forward");
		footer.add(btn_forward);

		
		footer.add(Box.createRigidArea(new Dimension(15, 5)));
		footer.add(Box.createHorizontalGlue());

		btn_cancel = new JButton("Cancel");
		footer.add(btn_cancel);
		
		footer.add(Box.createRigidArea(new Dimension(5, 5)));

		btn_ok = new JButton(" OK ");
		footer.add(btn_ok);
		
		footer.add(Box.createRigidArea(new Dimension(5, 5)));

		btn_okAndAnother = new JButton("Ok, import another Text");
		footer.add(btn_okAndAnother);
	}

	private void actionOKPlusAnother() {
		Log.log("Import Dialog: OK, and open another import.");
		parent.importIsDone(this, true);
	}

	private void actionOK() {
		Log.log("Import Dialog: OK");
		parent.importIsDone(this, false);
	}

	private void actionCancel() {
		Log.log("Import Dialog: Canceled");
		dispose();
	}


}
