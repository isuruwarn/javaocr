package com.ocr.core;

import java.awt.image.BufferedImage;

public class ScanRequest {
	
	
	private BufferedImage image;
	
	private String dialect;
	
	private int minBlanklineHeight;
	private int minWhitespaceWidth;
	private int verticalBlocksPerChar;
	
	
	
	public BufferedImage getImage() {
		return image;
	}
	
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	
	public String getDialect() {
		return dialect;
	}
	
	public void setDialect(String dialect) {
		this.dialect = dialect;
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
	
	public int getVerticalBlocksPerChar() {
		return verticalBlocksPerChar;
	}
	
	public void setVerticalBlocksPerChar(int verticalBlocksPerChar) {
		this.verticalBlocksPerChar = verticalBlocksPerChar;
	}
	

}
