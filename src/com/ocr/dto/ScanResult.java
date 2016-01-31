package com.ocr.dto;

import java.util.ArrayList;

import com.ocr.core.Line;




public class ScanResult {
	
	
	private int height;
	private int width;
	private int linesRead;
	private int charsRead;
	private byte [][] bitmap;
	private ArrayList<Line> lines;
	
	
	
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
	
	public ArrayList<Line> getLines() {
		return lines;
	}

	public void setLines(ArrayList<Line> lines) {
		this.lines = lines;
	}
	
	public byte[][] getBitmap() {
		return bitmap;
	}

	public void setBitmap(byte[][] bitmap) {
		this.bitmap = bitmap;
	}


}
