package com.ocr.core;

import java.util.ArrayList;




public class CharMapping {
	
	
	private Char c;
	//private String charCode;
	private String charValue;
	private ArrayList<Integer> keyPoints;
	
	
	
	public CharMapping( Char c, String charValue, ArrayList<Integer> keyPoints ) {
		this.c = c;
		this.charValue = charValue;
		this.keyPoints = keyPoints;
	}
	
	
	
	public Char getChar() {
		return c;
	}
	public void setChar(Char c) {
		this.c = c;
	}
//	public String getCharCode() {
//		return charCode;
//	}
//	public void setCharCode(String charCode) {
//		this.charCode = charCode;
//	}
	public String getCharValue() {
		return charValue;
	}
	public void setCharValue(String charValue) {
		this.charValue = charValue;
	}
	public ArrayList<Integer> getKeyPoints() {
		return keyPoints;
	}
	public void setKeyPoints(ArrayList<Integer> keyPoints) {
		this.keyPoints = keyPoints;
	}
	
	

}
