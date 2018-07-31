package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.jdom2.Element;

public class Text {

	private String identifier;
	private String shelfmark;
	
//	private String textContent;
	private LinkedList<StringBuffer> segments;
	
//	public Text(String id, String shelfM, String text) {
//		this(id, shelfM);
//		textContent = text;
//		
//	}

	public Text(String id, String sm) {
		identifier=id;
		shelfmark=sm;
		segments = new LinkedList<>();
		segments.add(new StringBuffer(""));
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

//	public String getTextContent() {
//		return textContent;
//	}
//
//	public void setTextContent(String textContent) {
//		this.textContent = textContent;
//	}

//	public ArrayList<String> getList() {
//		ArrayList<String> v = new ArrayList<String>();
//		v.add(shelfmark);
//		v.add(identifier);
//		String[] ss = textContent.split("£");
//		for (String s: ss) {
//			v.add(s);
//		}
//		return v;
//	}

	public void appendText(String txt) {
//		if (textContent==null || textContent.isEmpty()) {
//			textContent = txt;
//		} else {
//			textContent += " " + txt;
//		}
		segments.getLast().append(txt);
	}

	public void appendSegmentation() {
//		if (textContent == null)
//			textContent = "";
//		textContent += "£";
//		textContent = textContent.replace("£ ", "£");
		segments.add(new StringBuffer(""));
	}
	
	public String getTextWithPilcrow() {
//		String s = getTextContent();
//		String sep = "¶"+Log.lineSep;
//		String res = s.replace("£", sep);
//		return res;
		StringBuffer res = new StringBuffer();
		String sep = "¶"+Log.lineSep;
		for (StringBuffer sb: segments) {
			res.append(sb.toString());
			res.append(sep);
		}
		return res.toString();
	}

	public int getLength() {
		return segments.size();
	}

	public String getSegmentAt(int c) {
		return segments.get(c).toString();
	}

	public Element getXMLRepresentation() {
		Element e = new Element("text");
		e.setAttribute("shelfmark", shelfmark);
		e.setAttribute("id", identifier);
		
		for (StringBuffer sb: segments) {
			Element c = new Element("segment");
			c.addContent(sb.toString());
			//TODO add data from user analysis
			e.addContent(c);
		}
		
		return e;
	}

	public void trimToSize(int textLength) {
		while (getLength()>textLength) {
			StringBuffer sb = segments.removeLast();
			segments.getLast().append(sb.toString());//TODO shold I really add it?
		}
	}
	

}
