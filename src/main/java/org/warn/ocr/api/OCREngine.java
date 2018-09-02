package org.warn.ocr.api;

import java.util.ArrayList;

import org.warn.ocr.core.Char;
import org.warn.ocr.core.OCREngineRequest;
import org.warn.ocr.core.OCREngineResult;


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
	 * 
	 * @param mappings
	 * @return
	 */
	int saveMappings( ArrayList<Char> mappings );
		
	
	
	
	/**
	 * Needed by the front-end to generate block image, display default value, etc.
	 * 
	 * @return
	 */
	int getVerticalBlocksPerChar(); //TODO: Can we avoid using this getter?
	
}
