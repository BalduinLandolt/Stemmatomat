package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TextDisplayTableModel extends AbstractTableModel {
	
	private int cols = 0;
	private int rows = 0;
	private ArrayList<Text> texts;
	

	public TextDisplayTableModel(ArrayList<Text> texts) {
		this.texts = texts;
		int length = 0;
		
		for (Text t: texts) {
			length = Math.max(length, t.getLength());
		}
		
		//TODO make sure every text is equally long - still necessary?
		
		rows = texts.size();
		cols = length+2;
		
	}

	@Override
	public int getColumnCount() {
		return cols;
	}

	@Override
	public String getColumnName(int c) {
		if (c == 0) {
			return "Shelf Mark";
		} else if (c == 1) {
			return "ID";
		} else {
			return Integer.toString(c-1);
		}
	}

	@Override
	public int getRowCount() {
		return rows;
	}

	@Override
	public Object getValueAt(int r, int c) {
		Text t = texts.get(r);
		if (c == 0) {
			return t.getShelfmark();
		} else if (c == 1) {
			return t.getIdentifier();
		} else {
			String s = "<html>"+t.getSegmentAt(c-2);
			s += " <font color=\"red\">[";
			s += t.getStemmaticValue(c);
			s += "]</font></html>";
			return s;
		}
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}

	public Text getTextAtRow(int r) {
		if (r<rows)
			return texts.get(r);
		else
			return null;
	}
	

}
