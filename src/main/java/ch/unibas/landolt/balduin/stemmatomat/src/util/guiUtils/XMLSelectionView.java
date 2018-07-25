package ch.unibas.landolt.balduin.stemmatomat.src.util.guiUtils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.ItemSelectable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import ch.unibas.landolt.balduin.stemmatomat.src.gui.ImportDialog;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Log;
import ch.unibas.landolt.balduin.stemmatomat.src.util.Text;

@SuppressWarnings("serial")
public class XMLSelectionView extends JPanel {
	
	private ImportDialog parent;
	
	private JTextArea textArea;
	private JPanel l1;
	private JPanel l2;

	private SetList list_l1;
	private SetList list_l2;

	private Document doc_orig;
	private Document doc_tmp;
	private Text text;

	public XMLSelectionView(ImportDialog owner) {
		parent = owner;
		doc_orig = owner.getXMLDocument();
		doc_tmp = doc_orig.clone();
		list_l1 = new SetList();
		list_l2 = new SetList();
		
		setLayout(new GridLayout(1, 2, 8, 8));
		setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
		
		JPanel left = new JPanel(new GridLayout(2, 1, 8, 8));
		add(left);
		
		l1 = new JPanel();
		l1.setLayout(new BoxLayout(l1, BoxLayout.Y_AXIS));
		JPanel l1_tmp = new JPanel(new BorderLayout());
		l1_tmp.add(new JScrollPane(l1), BorderLayout.CENTER);
		l1_tmp.setBorder(BorderFactory.createTitledBorder("Include in Text"));
		left.add(l1_tmp);
		
		l2 = new JPanel();
		l2.setLayout(new BoxLayout(l2, BoxLayout.Y_AXIS));
		JPanel l2_tmp = new JPanel(new BorderLayout());
		l2_tmp.add(new JScrollPane(l2), BorderLayout.CENTER);
		l2_tmp.setBorder(BorderFactory.createTitledBorder("Use for Segmentation"));
		left.add(l2_tmp);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea);
		add(sp);
	}

	public void refresh() {
		Log.log("XMLSelectionView: Refreshing view.");
		if (doc_orig == null) {
			Log.log("XMLSelectionView: Got no XML Data to display.");
			return;
		}

		refresh_l1();
		refresh_l2();
		disableElements_l1();
		refresh_txt();
		
		revalidate();
		repaint();
	}

	private void disableElements_l1() {
		for (Component c: l1.getComponents()) {
			if (c instanceof JCheckBox) {
				JCheckBox cb = (JCheckBox)c;
				cb.setEnabled(isToBeEnabled(cb));
			}
		}
		if (l1.getComponent(0) instanceof JCheckBox)
			((JCheckBox)l1.getComponent(0)).setEnabled(false);
	}

	private boolean isToBeEnabled(JCheckBox cb) {
	//	if (list_l2.isEmpty())
	//		return true;
			
		if (list_l2.hasName(cb.getText())) {
			return true;
		} else {
			return !cb.isSelected();
		}
	}

	private void refresh_l1() {
		Iterator<Content> i = doc_orig.getDescendants();
		SetList m = new SetList();
		while (i.hasNext()) {
			Content c = (Content) i.next();
			if (c instanceof Element) {
				Element e = (Element) c;
				String name = e.getName();
				boolean b = true;
				if (list_l1.hasName(name))
					b = list_l1.getValue(name);
				m.put(name, b);
			}
		}
		list_l1 = m;
		
		l1.removeAll();
		for (Set s: list_l1) {
			JCheckBox cb = new JCheckBox(s.getName());
			cb.setSelected(list_l1.getValue(s.getName()));
			cb.addItemListener(e -> checkboxChange(list_l1, (JCheckBox) e.getItem()));
			l1.add(cb);
		}
	}

	private void refresh_l2() {
		Iterator<Content> i = doc_tmp.getDescendants();
		SetList m = new SetList();
		while (i.hasNext()) {
			Content c = (Content) i.next();
			if (c instanceof Element) {
				Element e = (Element) c;
				String name = e.getName();
				boolean b = false;
				if (list_l2.hasName(name))
					b = list_l2.getValue(name);
				m.put(name, b);
			}
		}
		list_l2 = m;
		
		l2.removeAll();
		for (Set s: list_l2) {
			JCheckBox cb = new JCheckBox(s.getName());
			cb.setSelected(list_l2.getValue(s.getName()));
			cb.addItemListener(e -> checkboxChange(list_l2, (JCheckBox) e.getItem()));
			l2.add(cb);
		}
	}

	private void refresh_txt() {
		generateText();
		textArea.setText(text.getTextWithPilcrow());
	}

	private void generateText() {
		Text t = new Text(parent.getID(), parent.getShelfmark());
		
		Iterator<Content> i = doc_tmp.getDescendants();
		while (i.hasNext()) {
			Content c = (Content) i.next();
			if (c instanceof Element) {
				Element e = (Element) c;
				
				String txtContent = e.getTextNormalize();
				String name = e.getName();
				
				if (isToBeIncluded(name)) {
					if (!txtContent.isEmpty())
						t.appendText(txtContent);
				}
				if (isSemgmentator(name)) {
					t.appendSegmentation();
				}
			}
		}
		text = t;
	}

	private boolean isSemgmentator(String name) {
		if (list_l2.hasName(name))
			return list_l2.getValue(name);
		
		return false;
	}

	private boolean isToBeIncluded(String name) {
		if (list_l1.hasName(name))
			return list_l1.getValue(name);
		
		return false;
	}

	private void checkboxChange(SetList l, JCheckBox source) {
		Log.log("CheckBox has been changed: '"+source.getText()+"' to '"+source.isSelected()+"'");
		l.setValue(source.getText(), source.isSelected());
		removeDeactivatedFromTmp();
		refresh();
		parent.setChangesMade(true);
	}
	
	private void removeDeactivatedFromTmp() {
		doc_tmp = doc_orig.clone();
		for (Set s: list_l1) {
			if (!s.getValue()) {
				String n = s.getName();
				removeDeactivatedFromTmpByName(n);
			}
		}
	}

	private void removeDeactivatedFromTmpByName(String n) {
		while (docHasName(doc_tmp, n)) {
			removeDeactivatedFromTmpByNameOnce(n);
		}
	}

	private void removeDeactivatedFromTmpByNameOnce(String n) {
		Iterator<Content> i = doc_tmp.getDescendants();
		while (i.hasNext()) {
			Content c = (Content) i.next();
			if (c instanceof Element) {
				Element e = (Element) c;
				if (e.getName().equals(n)) {
					e.detach();
					return;
				}
			}
		}
	}

	private boolean docHasName(Document d, String n) {
		Iterator<Content> i = d.getDescendants();
		while (i.hasNext()) {
			Content c = (Content) i.next();
			if (c instanceof Element) {
				Element e = (Element) c;
				if (e.getName().equals(n))
					return true;
			}
		}
		return false;
	}

	private class Set {
		private String name;
		private boolean value;

		public Set(String n, boolean b) {
			name=n;
			value=b;
		}

		public String getName() {
			return name;
		}

		public void setValue(boolean b) {
			value = b;
		}
		
		public boolean getValue() {
			return value;
		}
	}
	
	private class SetList extends LinkedList<Set> {

		public boolean getValue(String name) {
			if (hasName(name))
				return getSet(name).getValue();
			
			return false;
		}

		public void setValue(String n, boolean b) {
			if (hasName(n)) {
				getSet(n).setValue(b);
			}
		}

		public void put(String name, boolean b) {
			if (hasName(name)) {
				getSet(name).setValue(b);
			} else {
				add(new Set(name, b));
			}
		}

		private Set getSet(String name) {
			for (Set s: this) {
				if (s.getName().equals(name))
					return s;
			}
			return null;
		}

		private boolean hasName(String name) {
			for (Set s: this) {
				if (s.getName().equals(name))
					return true;
			}
			
			return false;
		}
		
	}

	public Text getText() {
		return text;
	}

}
