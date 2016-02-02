package com.ocr.core;

import java.util.ArrayList;



public class OCREngineResult {
	

	private StringBuilder document;
	private ArrayList<String> unrecognizedCharCodes;
	private ArrayList<Char> unrecognizedChars;
	
	public StringBuilder getDocument() {
		return document;
	}
	public void setDocument(StringBuilder document) {
		this.document = document;
	}
	public ArrayList<String> getUnrecognizedCharCodes() {
		return unrecognizedCharCodes;
	}
	public void setUnrecognizedCharCodes(ArrayList<String> unrecognizedCharCodes) {
		this.unrecognizedCharCodes = unrecognizedCharCodes;
	}
	public ArrayList<Char> getUnrecognizedChars() {
		return unrecognizedChars;
	}
	public void setUnrecognizedChars(ArrayList<Char> unrecognizedChars) {
		this.unrecognizedChars = unrecognizedChars;
	}
	
}