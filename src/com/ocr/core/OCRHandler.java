package com.ocr.core;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import com.ocr.api.OCREngine;
import com.ocr.api.Scanner;
import com.ocr.engine.impl.OCREngineImplv5;
import com.ocr.scanner.impl.ScannerImpl;
import com.ocr.util.ImageUtils;






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
	
	
	private Scanner scanner;
	private OCREngine ocrEngine;
	
	
	
	public OCRHandler() {
		
		ocrEngine = new OCREngineImplv5();
		scanner = new ScannerImpl();
		
		/* TODO: Later on we could instantiate scanners and OCREngines based on UI input. Perhaps 
		 * define all available Scanner and OCREngine implementations in an XML or Property file
		 * and then use reflection to dynamically instantiate the appropriate classes based on 
		 * user input. 
		*/
	}
	
	
	
	public OCRResult scan( OCRRequest req ) throws IOException {
		
		BufferedImage inputImage = ImageUtils.loadImage( req.getImagePath() );
		
		ScanRequest scanReq = new ScanRequest();
		scanReq.setImage(inputImage);
		scanReq.setMinBlanklineHeight( req.getMinBlanklineHeight() );
		scanReq.setMinWhitespaceWidth( req.getMinWhitespaceWidth() );
		ScanResult scanRes = scanner.scanImage( scanReq ); //reads image and prepares Line and Char objects
		
		OCREngineRequest ocrEngReq = new OCREngineRequest();
		ocrEngReq.setDialect( req.getDialect() );
		ocrEngReq.setBitmap( scanRes.getBitmap() );
		ocrEngReq.setLines( scanRes.getLines() );
		ocrEngReq.setInputImage(inputImage);
		OCREngineResult ocrEngRes = ocrEngine.processLines(ocrEngReq);
		
		OCRResult res = new OCRResult( scanRes, ocrEngRes );
		res.setInputImage(inputImage);
		res.setVerticalBlocksPerChar( ocrEngine.getVerticalBlocksPerChar() );
		
		return res;
	}
	

	public int getMinWhitespaceWidth() {
		return scanner.getMinWhitespaceWidth();
	}

	
	public int getMinBlanklineHeight() {
		return scanner.getMinBlanklineHeight();
	}

		
	public int saveMappings( ArrayList<Char> mappings ) {
		return ocrEngine.saveMappings( mappings );
	}
	
	
}
