package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

@SuppressWarnings("serial")
public class FileSelectionView extends JPanel {
	
	private JTextField path;
	private JButton btn_selectFile;

	public FileSelectionView(Text text) {
		setLayout(new BorderLayout(8,8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JPanel p = new JPanel(new BorderLayout(8,8));
		path = new JTextField();
		path.setEditable(false);
		if (text == null) {
			path.setText("no text selected.");
		} else {
			// TODO put file path here
		}
		p.add(path, BorderLayout.CENTER);
		
		btn_selectFile = new JButton("...");
		btn_selectFile.addActionListener(e -> selectFile());
		p.add(btn_selectFile, BorderLayout.EAST);
		
		this.add(p, BorderLayout.NORTH);
		
		//TODO do rest of GUI here
	}

	private void selectFile() {
		// TODO Auto-generated method stub
	}

}
