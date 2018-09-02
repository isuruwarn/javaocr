package org.warn.ocr.core;

import java.awt.image.BufferedImage;



public class ScanRequest {
	
	
	private BufferedImage image;
	private int minBlanklineHeight;
	private int minWhitespaceWidth;
	
	
	
	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public int getMinBlanklineHeight() {
		return minBlanklineHeight;
	}
	
	public void setMinBlanklineHeight(int minBlanklineHeight) {
		this.minBlanklineHeight = minBlanklineHeight;
	}
	
	public int getMinWhitespaceWidth() {
		return minWhitespaceWidth;
	}
	
	public void setMinWhitespaceWidth(int minWhitespaceWidth) {
		this.minWhitespaceWidth = minWhitespaceWidth;
	}
	
}
