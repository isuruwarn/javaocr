package com.ocr.api;

import java.util.ArrayList;

import com.ocr.core.Char;
import com.ocr.core.CharMapping;
import com.ocr.core.OCREngineRequest;
import com.ocr.core.OCREngineResult;


public interface OCREngine {
	
	
	/**
	 * This method should implement the main character recognition algorithm. Should return a
	 * <OCRResult> object which contains the characters of the scanned document, and other details
	 * such as unrecognized characters and charcodes.
	 * 
	 * @param OCRRequest
	 * @return 
	 */
	OCREngineResult processLines( OCREngineRequest req );
	
	
	/**
	 * Returns char code for given char based on OCR algorithm
	 * 
	 * @param c
	 * @param verticalBlocksPerChar
	 * @return
	 */
	String getCharCode( Char c );
	
	
	
	/**
	 * 
	 * @param mappings
	 * @return
	 */
	int saveMappings( ArrayList<CharMapping> mappings );
		
	
	
	
	/**
	 * Needed by the front-end to generate block image, display default value, etc.
	 * 
	 * @return
	 */
	int getVerticalBlocksPerChar(); //TODO: Can we avoid using this getter?
	
}
