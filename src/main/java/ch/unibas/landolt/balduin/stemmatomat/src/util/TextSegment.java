package ch.unibas.landolt.balduin.stemmatomat.src.util;

public class TextSegment {
	private String text = "";
	private int val = -1;
	private Text owner;

	public TextSegment(String t, Text p) {
		text = t;
		owner = p;
	}

	public TextSegment(Text p) {
		text = "";
		owner = p;
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

	public Text getContainingText() {
		return owner;
	}

	public void setText(String s) {
		text = s;
	}

}
