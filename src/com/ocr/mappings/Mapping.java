package com.ocr.mappings;



import java.util.ArrayList;




public class Mapping {
	
	
	private String charCode;
	private String charValue;
	private ArrayList<Integer> blackKeyPoints;
	private ArrayList<Integer> whiteKeyPoints;
	private ArrayList<Integer> blackCells;
	private ArrayList<Integer> whiteCells;
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
	
	public void setBlackCells(ArrayList<Integer> blackCells) {
		this.blackCells = blackCells;
	}
	
	public ArrayList<Integer> getWhiteCells() {
		return whiteCells;
	}
	
	public void setWhiteCells(ArrayList<Integer> whiteCells) {
		this.whiteCells = whiteCells;
	}
	
	public void sethBlocks(int hBlocks) {
		this.hBlocks = hBlocks;
	}
	
	public int gethBlocks() {
		return hBlocks;
	}
	
	public ArrayList<Integer> getBlackCells() {
		return blackCells;
	}

	
}
