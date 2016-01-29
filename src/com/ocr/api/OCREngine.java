package com.ocr.api;

import com.ocr.core.Char;


public interface OCREngine {
	
	
	/**
	 * This method should implement the main character recognition algorithm. Should return a
	 * char code which can be used to identify the given <Char> in the mappings file. 
	 * 
	 * @param c
	 * @param bitmap
	 * @return
	 */
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
	
	
	/**
	 * Needed by the front-end to generate block image, display default value, etc.
	 * 
	 * @return
	 */
	int getVerticalBlocksPerChar(); //TODO: Can we avoid using this getter?
	
}
