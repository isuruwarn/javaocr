package org.warn.ocr.core;





public class OCRRequest {
	
	
	
	private String dialect;
	private String imagePath;
	private int minBlanklineHeight;
	private int minWhitespaceWidth;

	
	
	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
