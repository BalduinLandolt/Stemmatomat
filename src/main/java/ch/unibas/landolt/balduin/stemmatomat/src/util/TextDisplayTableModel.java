package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class TextDisplayTableModel extends AbstractTableModel {
	
	private String[] head;
	private String[][] data;
	private int cols = 0;
	private int rows = 0;
	

	public TextDisplayTableModel(ArrayList<Text> texts) {
		int length = 0;
		
		for (Text t: texts) {
			length = Math.max(length, t.getList().size());
		}
		
		rows = texts.size();
		cols = length;
		
		
		head = new String[cols];
		data = new String[rows][cols];
		
		for (int r=0; r<rows; r++) {
			Text t = texts.get(r);
			ArrayList<String> tmp = t.getList();
			for (int j=0; j<tmp.size();j++) {
				data[r][j] = tmp.get(j);
			}
			length = Math.max(length, tmp.size());
		}
		
		head[0] = "Shelf Mark";
		head[1] = "ID";
		for (int i = 2; i<length; i++) {
			head[i] = "#"+(i-1);
		}
	}

	@Override
	public int getColumnCount() {
		return cols;
	}

	@Override
	public int getRowCount() {
		return rows;
	}

	@Override
	public Object getValueAt(int r, int c) {
		return data[r][c];
	}

	@Override
	public boolean isCellEditable(int r, int c) {
		return false;
	}
	

}
