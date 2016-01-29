package com.ocr.api;

import com.ocr.core.ScanRequest;
import com.ocr.core.ScanResult;
import com.ocr.mappings.MappingsFile;




/**
 * 
 * 
 * @author isuru
 *
 */
public interface Scanner {
	
	
	/**
	 * This method should implement the main scanning algorithm which will identify the lines and
	 * characters within the given image.
	 * 
	 * @param req
	 * @return
	 */
	ScanResult scan( ScanRequest req );
	
	
	MappingsFile getMappingsFile();
	
	
	int getMinBlanklineHeight(); //TODO: Can we avoid using this getter?
	
	
	int getMinWhitespaceWidth(); //TODO: Can we avoid using this getter?
	
	
}
