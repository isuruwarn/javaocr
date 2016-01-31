package com.ocr.api;

import com.ocr.core.Char;
import com.ocr.dto.OCREngineRequest;
import com.ocr.dto.OCREngineResult;


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
