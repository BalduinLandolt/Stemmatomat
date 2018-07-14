package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionListener;

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
		btn_cancel.addActionListener(e -> actionCancel());
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

	public void run() {
		setMinimumSize(new Dimension(600, 400));
		pack();
		setLocation(300, 200);
		switchToFileSelectionView();
		setVisible(true);
	}

	private void switchToFileSelectionView() {
		setTitle("Import Text - Load from File");

		btn_back.setEnabled(false);
		btn_forward.setEnabled(true);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);
		
		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToXMLView());
		
		
		
		// TODO set contents
		
	}

	private void switchToXMLView() {
		setTitle("Import Text - Select from XML");

		btn_back.setEnabled(true);
		btn_forward.setEnabled(true);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);
		
		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToFileSelectionView());
		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToTextEditView());
		
		
		// TODO set contents
	}

	private void switchToTextEditView() {
		setTitle("Import Text - Edit Text");

		btn_back.setEnabled(true);
		btn_forward.setEnabled(true);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);
		
		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToXMLView());
		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToPreviewView());
		
		
		// TODO set contents
	}

	private void switchToPreviewView() {
		setTitle("Import Text - Preview Import");

		btn_back.setEnabled(true);
		btn_forward.setEnabled(false);
		btn_ok.setEnabled(true);
		btn_okAndAnother.setEnabled(true);
		
		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToTextEditView());
		
		
		// TODO set contents
	}

	private void removeActionListeners(JButton btn) {
		ActionListener[] aa = btn.getActionListeners();
		for (ActionListener a: aa) {
			btn.removeActionListener(a);
		}
	}


}
