package org.warn.ocr.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;



public class OCREngineRequest {
	

	private String dialect;
	private ArrayList<Line> lines;
	private byte [][] bitmap;
	private BufferedImage inputImage;
	
	
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
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
	public BufferedImage getInputImage() {
		return inputImage;
	}
	public void setInputImage(BufferedImage inputImage) {
		this.inputImage = inputImage;
	}
	
}
