package com.ocr.core;

import java.util.HashMap;

import com.ocr.api.OCREngine;
import com.ocr.api.Scanner;
import com.ocr.engine.impl.OCREngineImplv2;
import com.ocr.scanner.impl.ScannerImpl;






/**
 * This class will act as the interface or controller that be used by external classes
 * to communicate with the OCR functions. 
 * 
 * This class will decide which scanner and OCR implementations to use. The outside world
 * does not need to know these details.
 * 
 * 
 * @author isuru
 *
 */
public final class OCRHandler {
	
	
	// TODO: should this class implement an interface?
	
	private Scanner scanner;
	private OCREngine ocrEngine;
	
	
	
	
	public OCRHandler() {
		
		ocrEngine = new OCREngineImplv2();
		scanner = new ScannerImpl( ocrEngine );
		
		/* TODO: Later on we could instantiate scanners and OCREngines based on UI input. Perhaps 
		 * define all available Scanner and OCREngine implementations in an XML or Property file
		 * and then use reflection to dynamically instantiate the appropriate classes based on 
		 * user input. 
		*/
	}
	
	
	
	
	
	public ScanResult scan( ScanRequest req ) {
		return scanner.scan(req);
	}
	

	public String getCharCode( Char c ) {
		return ocrEngine.getCharCode(c);
	}
	
	
	public int getVerticalBlocksPerChar() {
		return ocrEngine.getVerticalBlocksPerChar();
	}
	
	
	public void relaodCharMap(String dialect) {
		scanner.getMappingsFile().relaodCharMap(dialect);
	}
	
	
	public String getCharValueFromMap( String dialect, String charCode ) {
		return scanner.getMappingsFile().getCharValueFromMap( charCode );
	}
	
	
	public boolean setMapping( String dialect, String newCharCode, String newCharValue ) {
		return scanner.getMappingsFile().setMapping( dialect, newCharCode, newCharValue );
	}
	
	
	public boolean setMappings( String dialect, HashMap<String, String> newMappings ) {
		return scanner.getMappingsFile().setMappings( dialect, newMappings );
	}
	
	
	public boolean deleteMapping(String dialect, String charCode) {
		return scanner.getMappingsFile().deleteMapping( dialect, charCode );
	}
	
				

	public int getMinWhitespaceWidth() {
		return scanner.getMinWhitespaceWidth();
	}

	public int getMinBlanklineHeight() {
		return scanner.getMinBlanklineHeight();
	}
	
}
