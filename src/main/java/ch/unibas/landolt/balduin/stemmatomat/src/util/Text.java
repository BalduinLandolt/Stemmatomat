package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

public class Text {

	private String identifier;
	private String shelfmark;
	
	private String textContent;
	
	public Text(String id, String shelfM, String text) {
		identifier = id;
		shelfmark = shelfM;
		textContent = text;
	}

	public Text(String id, String sm) {
		identifier=id;
		shelfmark=sm;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getShelfmark() {
		return shelfmark;
	}

	public void setShelfmark(String shelfmark) {
		this.shelfmark = shelfmark;
	}

	public String getTextContent() {
		return textContent;
	}

	public void setTextContent(String textContent) {
		this.textContent = textContent;
	}

	public ArrayList<String> getList() {
		ArrayList<String> v = new ArrayList<String>();
		v.add(shelfmark);
		v.add(identifier);
		String[] ss = textContent.split("£");
		for (String s: ss) {
			v.add(s);
		}
		return v;
	}

	public void appendText(String txt) {
		if (textContent==null || textContent.isEmpty()) {
			textContent = txt;
		} else {
			textContent += " " + txt;
		}
	}

	public void appendSegmentation() {
		if (textContent == null)
			textContent = "";
		textContent += " £";
	}
	
	public String getTextWithPilcrow() {
		String s = getTextContent();
		String sep = " ¶"+Log.lineSep;
		String res = s.replace("£", sep);
		return res;
	}
	

}
