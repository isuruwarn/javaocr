package org.warn.ocr.core;

import java.util.ArrayList;
import java.util.LinkedList;


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
	private String charCode;
	private String charValue;
	private ArrayList<Integer> blockSequence = new ArrayList<Integer>();
	private ArrayList<Integer> blackKeyPoints = new ArrayList<Integer>();
	private ArrayList<Integer> whiteKeyPoints = new ArrayList<Integer>();
	private LinkedList<Integer> blackPixels = new LinkedList<Integer>();
	private LinkedList<Integer> whitePixels = new LinkedList<Integer>();
	
	
	
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

	public ArrayList<Integer> getBlockSequence() {
		return blockSequence;
	}

	public void setBlockSequence(ArrayList<Integer> blockSequence) {
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
	
	public String getCharCode() {
		return charCode;
	}
	
	public String getCharValue() {
		return charValue;
	}
	
	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}
	
	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}

	public LinkedList<Integer> getBlackPixels() {
		return blackPixels;
	}

	public void setBlackPixels(LinkedList<Integer> blackPixels) {
		this.blackPixels = blackPixels;
	}

	public LinkedList<Integer> getWhitePixels() {
		return whitePixels;
	}

	public void setWhitePixels(LinkedList<Integer> whitePixels) {
		this.whitePixels = whitePixels;
	}

	public ArrayList<Integer> getBlackKeyPoints() {
		return blackKeyPoints;
	}

	public void setBlackKeyPoints(ArrayList<Integer> blackKeyPoints) {
		this.blackKeyPoints = blackKeyPoints;
	}
	
	public ArrayList<Integer> getWhiteKeyPoints() {
		return whiteKeyPoints;
	}

	public void setWhiteKeyPoints(ArrayList<Integer> whiteKeyPoints) {
		this.whiteKeyPoints = whiteKeyPoints;
	}

	
	
}
