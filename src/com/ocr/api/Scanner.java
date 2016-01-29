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
	
	ScanResult scan( ScanRequest req );
	
	int getMinBlanklineHeight();
	
	int getMinWhitespaceWidth();
	
	MappingsFile getMappingsFile();
	
}
