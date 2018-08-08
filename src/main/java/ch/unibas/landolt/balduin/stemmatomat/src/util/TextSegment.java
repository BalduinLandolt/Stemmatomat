package ch.unibas.landolt.balduin.stemmatomat.src.util;

public class TextSegment {
	private String text = "";
	private int val = -1;

	public TextSegment(String t) {
		text = t;
	}

	public TextSegment() {
		text = "";
	}

	public void append(String txt) {
		text += txt;
	}
	
	public String toString() {
		return text;
	}

	public int length() {
		return text.length();
	}

	public void setVal(int val) {
		this.val=val;
	}

	public int getStemVal() {
		return val;
	}

}
