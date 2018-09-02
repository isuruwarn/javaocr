package org.warn.ocr.mappings;



import java.util.ArrayList;
import java.util.LinkedList;




public class Mapping {
	
	
	private String charCode;
	private String charValue;
	private ArrayList<Integer> blackKeyPoints;
	private ArrayList<Integer> whiteKeyPoints;
	private LinkedList<Integer> blackPixels;
	private LinkedList<Integer> whitePixels;
	private int hBlocks;
	
	
	
	public String getCharCode() {
		return charCode;
	}
	
	public void setCharCode(String charCode) {
		this.charCode = charCode;
	}
	
	public String getCharValue() {
		return charValue;
	}
	
	public void setCharValue(String charValue) {
		this.charValue = charValue;
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
	
	public LinkedList<Integer> getBlackPixels() {
		return blackPixels;
	}
	
	public void setBlackPixels( LinkedList<Integer> blackPixels ) {
		this.blackPixels = blackPixels;
	}
	
	public LinkedList<Integer> getWhitePixels() {
		return whitePixels;
	}
	
	public void setWhitePixels( LinkedList<Integer> whitePixels ) {
		this.whitePixels = whitePixels;
	}
	
	public void sethBlocks(int hBlocks) {
		this.hBlocks = hBlocks;
	}
	
	public int gethBlocks() {
		return hBlocks;
	}

	
}
