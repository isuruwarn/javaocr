package com.ocr.core;

import java.util.ArrayList;


/**
 * Is the rectangular bounds of a character. Also holds the bitmap representation of the character.
 * 
 * @author isuru
 *
 */
public class Char extends ScannedItem {
	
	
	private int charNumber;
	private int lineNumber;
	private int blockLength;
	private int noOfHBlocks;
	private boolean whiteSpace;
	private ArrayList<Byte> blockSequence = new ArrayList<Byte>();
	
	
	
	public Char() {
		
	}
	
	public Char(String name) {
		this.name = name;
	}

	public int getCharNumber() {
		return charNumber;
	}

	public void setCharNumber(int charNumber) {
		this.charNumber = charNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public boolean isWhiteSpace() {
		return whiteSpace;
	}

	public void setWhiteSpace(boolean whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	public ArrayList<Byte> getBlockSequence() {
		return blockSequence;
	}

	public void setBlockSequence(ArrayList<Byte> blockSequence) {
		this.blockSequence = blockSequence;
	}
	
	
	public int getBlockLength() {
		return blockLength;
	}

	public void setBlockLength(int blockLength) {
		this.blockLength = blockLength;
	}
	
	public int getNoOfHBlocks() {
		return noOfHBlocks;
	}

	public void setNoOfHBlocks(int noOfHBlocks) {
		this.noOfHBlocks = noOfHBlocks;
	}
	
}
