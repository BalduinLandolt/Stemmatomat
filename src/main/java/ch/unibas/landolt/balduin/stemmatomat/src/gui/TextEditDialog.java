package ch.unibas.landolt.balduin.stemmatomat.src.gui;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;

import javax.swing.JDialog;

import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

public class TextEditDialog extends JDialog {
	MainGUI parent;

	public TextEditDialog(MainGUI owner) {
		super(owner, "Edit Text", true);
		parent=owner;
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setUpDialog();
	}

	private void setUpDialog() {
		// TODO Auto-generated method stub
		
	}

	public void run(Text text) {
		setVisible(true);
	}

}
