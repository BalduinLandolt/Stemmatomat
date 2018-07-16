package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ch.unibas.landolt.balduin.stemmatomat.src.mainApplication.StemmatomatMain;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;
import ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils.FileSelectionView;

@SuppressWarnings("serial")
public class ImportDialog extends JDialog {

	private MainGUI parent;
	private Text text;

	private JPanel contents;
	private JPanel footer;

	private JButton btn_back;
	private JButton btn_forward;
	private JButton btn_cancel;
	private JButton btn_ok;
	private JButton btn_okAndAnother;
	
	private FileSelectionView view_fs;

	private boolean somethingChanged = false;

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
		btn_ok.addActionListener(e -> actionOK());
		footer.add(btn_ok);

		footer.add(Box.createRigidArea(new Dimension(5, 5)));

		btn_okAndAnother = new JButton("Ok, import another Text");
		btn_okAndAnother.addActionListener(e -> actionOKPlusAnother());
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
		switchToFileSelectionView(true);
		setVisible(true);
	}

	private void switchToFileSelectionView(boolean forward) {
		if (!forward) {
			if (somethingChanged) {
				int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to go back? All changes you just made will be lost.",
						"Sure?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (reply != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}
		
		setTitle("Import Text - Load from File");
		somethingChanged = false;

		btn_back.setEnabled(false);
		btn_forward.setEnabled(!forward);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);

		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToXMLView(true));
		
		view_fs = new FileSelectionView(this);
		contents.add(view_fs, BorderLayout.CENTER);

		Log.log("Switched to File Selection View.");
	}

	private void switchToXMLView(boolean forward) {
		if (forward) {
			getTextFromFileSelection();
		} else {
			if (somethingChanged) {
				int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to go back? All changes you just made will be lost.",
						"Sure?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (reply != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}
		
		setTitle("Import Text - Select from XML");
		somethingChanged = false;

		btn_back.setEnabled(true);
		btn_forward.setEnabled(true);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);

		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToFileSelectionView(false));
		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToTextEditView(true));


		// TODO set contents
		Log.log("Switched to XML Selection View.");
	}

	private void switchToTextEditView(boolean forward) {
		if (forward) {
			getTextFromXMLView();
		} else {
			if (somethingChanged) {
				int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to go back? All changes you just made will be lost.",
						"Sure?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (reply != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}
		
		setTitle("Import Text - Edit Text");
		somethingChanged = false;

		btn_back.setEnabled(true);
		btn_forward.setEnabled(true);
		btn_ok.setEnabled(false);
		btn_okAndAnother.setEnabled(false);

		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToXMLView(false));
		removeActionListeners(btn_forward);
		btn_forward.addActionListener(e -> switchToPreviewView(true));


		// TODO set contents
		Log.log("Switched to Edit View.");
	}

	private void switchToPreviewView(boolean forward) {
		if (forward) {
			getTextFromEditView();
		} else {
			if (somethingChanged) {
				int reply = JOptionPane.showConfirmDialog(this, "Are you sure you want to go back? All changes you just made will be lost.",
						"Sure?", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
				if (reply != JOptionPane.YES_OPTION) {
					return;
				}
			}
		}
		
		setTitle("Import Text - Preview Import");
		somethingChanged = false;

		btn_back.setEnabled(true);
		btn_forward.setEnabled(false);
		btn_ok.setEnabled(true);
		btn_okAndAnother.setEnabled(true);

		removeActionListeners(btn_back);
		btn_back.addActionListener(e -> switchToTextEditView(false));


		// TODO set contents
		Log.log("Switched to Preview View.");
	}

	private void getTextFromEditView() {
		if (!somethingChanged) {
			Log.log("No change has been made.");
			return;
		}

		// TODO implement this
		Log.log("should get text from edit now; not yet implemented!");
	}

	private void getTextFromFileSelection() {
		if (!somethingChanged) {
			Log.log("No change has been made.");
			return;
		}

		// TODO implement this
		Log.log("should get text from file selection now; not yet implemented!");
	}


	private void getTextFromXMLView() {
		if (!somethingChanged) {
			Log.log("No change has been made.");
			return;
		}

		// TODO implement this
		Log.log("should get text from xml selection now; not yet implemented!");
	}

	private void removeActionListeners(JButton btn) {
		ActionListener[] aa = btn.getActionListeners();
		for (ActionListener a: aa) {
			btn.removeActionListener(a);
		}
	}

	public Text getText() {
		return text;
	}

	public void checkFileLoaded(FileSelectionView caller) {
		if (!caller.hasFile()) {
			caller.responde("No file selected.");
			btn_forward.setEnabled(false);
			return;
		}
		if (!caller.hasID()) {
			caller.responde("No ID given - this field is mandatory.");
			btn_forward.setEnabled(false);
			return;
		}
		String id = caller.getID();
		if (! uniqueID(id)) {
			caller.responde("There already is a text with this ID - ID must be unique.");
			btn_forward.setEnabled(false);
			return;
		}
		
		// everything ok!
		btn_forward.setEnabled(true);
		caller.responde("Everything OK");
	}

	private boolean uniqueID(String id) {
		ArrayList<Text> texts = parent.getTexts();
		
		for (Text t: texts) {
			if (t.getIdentifier().equals(id))
				return false;
		}
		
		return true;
	}


}
