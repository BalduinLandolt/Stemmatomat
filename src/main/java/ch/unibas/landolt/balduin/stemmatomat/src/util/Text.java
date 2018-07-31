package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.jdom2.Element;

public class Text {

	private String identifier;
	private String shelfmark;
	
	private LinkedList<StringBuffer> segments;

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

	public void appendText(String txt) {
		segments.getLast().append(txt);
	}

	public void appendSegmentation() {
		segments.add(new StringBuffer(""));
	}
	
	public String getTextWithPilcrow() {
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
		if (c >= getLength())
			return "";
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

	public static Text buildFromXML(Element c) {
		String sm = c.getAttributeValue("shelfmark");
		String id = c.getAttributeValue("id");
		
		Text t = new Text(id, sm);
		
		LinkedList<Element> segments = new LinkedList<>(c.getChildren("segment"));
		for (Element seg: segments) {
			t.appendText(seg.getTextNormalize());
			t.appendSegmentation();
		}
		
		return t;
	}

	public void trimEmptyEnds() {
		while (segments.size()>0 && segments.getLast().length()==0) {
			segments.removeLast();
		}
	}

	public void normalizeLengthTo(int length) {
		while (getLength()<length) {
			appendSegmentation();
		}
	}
	

}
