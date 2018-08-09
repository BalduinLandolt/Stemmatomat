package ch.unibas.landolt.balduin.stemmatomat.src.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Vector;

import org.jdom2.Attribute;
import org.jdom2.DataConversionException;
import org.jdom2.Element;

public class Text {

	private String identifier;
	private String shelfmark;
	
	
	private LinkedList<TextSegment> segments;

	public Text(String id, String sm) {
		identifier=id;
		shelfmark=sm;
		segments = new LinkedList<>();
		segments.add(new TextSegment(this));
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
		segments.add(new TextSegment(this));
	}
	
	public String getTextWithPilcrow() {
		StringBuffer res = new StringBuffer();
		String sep = "¶"+Log.lineSep;
		for (TextSegment sb: segments) {
			res.append(sb.toString());
			res.append(sep);
		}
		return res.toString();
	}

	public int getLength() {
		return segments.size();
	}

	public String getSegmentTextAt(int c) {
		if (c >= getLength() || c<0)
			return "";
		return segments.get(c).toString();
	}

	public Element getXMLRepresentation() {
		Element e = new Element("text");
		e.setAttribute("shelfmark", shelfmark);
		e.setAttribute("id", identifier);
		
		for (TextSegment sb: segments) {
			Element c = new Element("segment");
			c.addContent(sb.toString());
			c.setAttribute("val", Integer.toString(sb.getStemVal()));
			e.addContent(c);
		}
		
		return e;
	}

	public void trimToSize(int textLength) {
		while (getLength()>textLength) {
			TextSegment sb = segments.removeLast();
			segments.getLast().append(sb.toString());//TODO shold I really add it?
		}
	}

	public static Text buildFromXML(Element c) {
		String sm = c.getAttributeValue("shelfmark");
		String id = c.getAttributeValue("id");
		
		Text t = new Text(id, sm);
		
		LinkedList<Element> segments = new LinkedList<>(c.getChildren("segment"));
		for (Element seg: segments) {
			TextSegment seg_new = new TextSegment(seg.getTextNormalize(), t);
			Attribute v = seg.getAttribute("val");
			int val = -1;
			try {
				if (v != null)
					val = v.getIntValue();
			} catch (DataConversionException e) {
				Log.log("some problem converting attributes");
			} finally {
				seg_new.setVal(val);
				t.addSegment(seg_new);
			}
		}
		
		return t;
	}

	private void addSegment(TextSegment seg_new) {
		segments.add(seg_new);
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

	public int getStemmaticValue(int c) {
		return segments.get(c).getStemVal();
	}

	public void setStemVal(int clickedSegmentIndex, int val) {
		segments.get(clickedSegmentIndex).setVal(val);
	}

	public TextSegment getSegmentAt(int c) {
		return segments.get(c);
	}
	

}
