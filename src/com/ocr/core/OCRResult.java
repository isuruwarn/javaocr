package com.ocr.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class OCRResult {
	

	private int height = 0;
	private int width = 0;
	private int linesRead = 0;
	private int charsRead = 0;
	private int verticalBlocksPerChar = 12;
	
	private ArrayList<Line> lines;
	private ArrayList<Char> recognizedChars;
	private ArrayList<Char> unrecognizedChars;
	
	private StringBuilder document;	
	private BufferedImage inputImage;
	

	
	public OCRResult() {
		
	}
	
	
	public OCRResult( ScanResult scanResult, OCREngineResult ocrEngRes ) {
		
		this.height = scanResult.getHeight();
		this.width = scanResult.getWidth();
		this.linesRead = scanResult.getLinesRead();
		this.charsRead = scanResult.getCharsRead();
		this.lines = scanResult.getLines();
		
		this.document = ocrEngRes.getDocument();	
		this.unrecognizedChars =  ocrEngRes.getUnrecognizedChars();
		this.recognizedChars = ocrEngRes.getRecognizedChars();
		
		// TODO: is it OK to set these objects as references? Or should all objects be cloned? 
		
	}
	
	
	
	
	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getLinesRead() {
		return linesRead;
	}

	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}

	public int getCharsRead() {
		return charsRead;
	}

	public void setCharsRead(int charsRead) {
		this.charsRead = charsRead;
	}
	
	public int getVerticalBlocksPerChar() {
		return verticalBlocksPerChar;
	}

	public void setVerticalBlocksPerChar(int vBlocksPerChar) {
		this.verticalBlocksPerChar = vBlocksPerChar;
	}

	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}

	public ArrayList<Char> getRecognizedChars() {
		return recognizedChars;
	}

	public void setRecognizedChars(ArrayList<Char> recognizedChars) {
		this.recognizedChars = recognizedChars;
	}

	public ArrayList<Char> getUnrecognizedChars() {
		return unrecognizedChars;
	}

	public void setUnrecognizedChars(ArrayList<Char> unrecognizedChars) {
		this.unrecognizedChars = unrecognizedChars;
	}

	public StringBuilder getDocument() {
		return document;
	}

	public void setDocument(StringBuilder document) {
		this.document = document;
	}
	
	public BufferedImage getInputImage() {
		return inputImage;
	}

	public void setInputImage(BufferedImage inputImage) {
		this.inputImage = inputImage;
	}
	
	
}
