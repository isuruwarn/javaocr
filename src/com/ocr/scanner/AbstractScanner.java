package com.ocr.scanner;

import com.ocr.api.Scanner;
import com.ocr.core.ScanResult;



/**
 * This class should be extended by every Scanner implementation
 * 
 * 
 * @author isuru
 *
 */
public abstract class AbstractScanner implements Scanner {
	
	
	protected ScanResult prepareResult( StringBuilder document ) {
		ScanResult res = new ScanResult();
		res.setDocument(document);
		return res;
	}
	
}
