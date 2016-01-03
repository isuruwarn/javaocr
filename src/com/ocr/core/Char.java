package com.ocr.core;

import java.util.ArrayList;


/**
 * Is the rectangular bounds of a character. Also holds the bitmap representation of the character.
 * 
 * @author isuru
 *
 */
public class Char extends ScanItem {
	
	private int charNumber;
	private boolean whiteSpace;
	private String name;
	private ArrayList<Byte> sequence = new ArrayList<Byte>();
	

	public Char() {
	}
	
	public Char(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharNumber() {
		return charNumber;
	}

	public void setCharNumber(int charNumber) {
		this.charNumber = charNumber;
	}
	
	public boolean isWhiteSpace() {
		return whiteSpace;
	}

	public void setWhiteSpace(boolean whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	public ArrayList<Byte> getSequence() {
		return sequence;
	}

	public void setSequence(ArrayList<Byte> sequence) {
		this.sequence = sequence;
	}
	
	
	public void printSequence() {
		String [] sb = new String [Scanner.VERTICAL_BLOCKS_PER_CHAR];
		for( int i=0; i<sequence.size(); i++ ) {
			int j = i % Scanner.VERTICAL_BLOCKS_PER_CHAR;
			if( sb[j] == null ) sb[j] = "";
			sb[j] += sequence.get(i) + " ";
		}
		for(String s : sb) {
			System.out.println(s);
		}
	}
	
	
	
	
	public String getCharCode() {
		String s = "";
		String charCode = "";
		for( int i=0; i<sequence.size(); i++ ) {
			s += sequence.get(i);
			if( (i+1) % Scanner.VERTICAL_BLOCKS_PER_CHAR == 0 ) {
				int n = Integer.parseInt( s, 2 );
				charCode += n;
				s = "";
			}
		}
		return charCode;
	}
	
}
