package org.warn.ocr.core;

import java.util.ArrayList;


/**
 * Is the rectangular bounds of a line. Also holds a list of chars contained in the line
 * 
 * @author isuru
 *
 */
public class Line extends ScannedItem {
	
	private int lineNumber;
	private boolean blankLine;
	private ArrayList<Char> chars = new ArrayList<Char>();
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public boolean isBlankLine() {
		return blankLine;
	}

	public void setBlankLine(boolean blankLine) {
		this.blankLine = blankLine;
	}
	
	public ArrayList<Char> getChars() {
		return chars;
	}

	public void setChars(ArrayList<Char> chars) {
		this.chars = chars;
	}
	
	

}
