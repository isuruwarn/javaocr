package com.ocr.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import com.ocr.api.OCREngine;
import com.ocr.api.Scanner;
import com.ocr.engine.OCREngineImplv2;
import com.ocr.scanner.ScannerImpl;






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
		// TODO: later on we could instantiate scanners and OCREngines based on UI input
		ocrEngine = new OCREngineImplv2();
		scanner = new ScannerImpl( ocrEngine );
	}
	
	
	
	
	
	public StringBuilder scan( BufferedImage inputImage, String dialect ) {
		return scanner.scan( inputImage, dialect );
	}
	

	public String getCharCode( Char c ) {
		return ocrEngine.getCharCode( c, scanner.getVerticalBlocksPerChar() );
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
	
	
	
	// TODO: consider removing the following methods through the use of DTO objects like ScanRequest and ScanResult
	
	public ArrayList<Line> getLines() {
		return scanner.getLines();
	}
	
	
	public ArrayList<Char> getUnrecognizedChars() {
		return scanner.getUnrecognizedChars();
	}
	
	
	public int getLinesRead() {
		return scanner.getLinesRead();
	}

	public int getCharsRead() {
		return scanner.getCharsRead();
	}

	
	public int getWidth() {
		return scanner.getWidth(); 
	}

	public Object getHeight() {
		return scanner.getHeight();
	}
	

	public int getVerticalBlocksPerChar() {
		return scanner.getVerticalBlocksPerChar();
	}
								

	public int getMinWhitespaceWidth() {
		return scanner.getMinWhitespaceWidth();
	}

	public int getMinBlanklineHeight() {
		return scanner.getMinBlanklineHeight();
	}

	public void setVerticalBlocksPerChar(int verticalBlocksPerChar) {
		scanner.setVerticalBlocksPerChar(verticalBlocksPerChar);
	}

	public void setMinWhitespaceWidth(int minWhitespaceWidth) {
		scanner.setMinWhitespaceWidth(minWhitespaceWidth);
	}

	public void setMinBlanklineHeight(int minBlanklineHeight) {
		scanner.setMinBlanklineHeight(minBlanklineHeight);
	}
	
	
}
