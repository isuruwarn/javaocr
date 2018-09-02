package org.warn.ocr.api;

import java.io.IOException;

import org.warn.ocr.core.ScanRequest;
import org.warn.ocr.core.ScanResult;




/**
 * 
 * 
 * @author isuru
 *
 */
public interface Scanner {
	
	int BW_THRESHOLD = -10800000; // average RGB values less than this will be saved as black pixels. 
								// average RGB values greater than this will be saved as white pixels
	
	/**
	 * This method should implement the main scanning algorithm which will identify the lines and
	 * characters within the given image.
	 * 
	 * @param req
	 * @return
	 */
	ScanResult scanImage( ScanRequest req ) throws IOException;
	
	
	int getMinBlanklineHeight(); //TODO: Can we avoid using this getter?
	
	
	int getMinWhitespaceWidth(); //TODO: Can we avoid using this getter?
	
	
}
