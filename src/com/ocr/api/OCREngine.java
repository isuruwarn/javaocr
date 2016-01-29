package com.ocr.api;

import com.ocr.core.Char;


public interface OCREngine {
	
	
	
	String processChar( Char c, byte [][] bitmap );
	
	/**
	 * Returns char code for given char based on OCR algorithm
	 * 
	 * @param c
	 * @param verticalBlocksPerChar
	 * @return
	 */
	String getCharCode( Char c );
	
	/**
	 * The OCR algorithm name is important in identifying the mappings file name. 
	 * 
	 * @return
	 */
	String getName();
	
	
	int getVerticalBlocksPerChar();
	
}
