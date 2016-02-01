package com.ocr.mappings;

import com.ocr.util.GlobalConstants;




/**
 * Belongs to a particular implementation of the OCREngine. The OCREngine will define
 * how the lookup should be done.
 * 
 * @author isuru
 *
 */
public class MappingsFile {
	
	
	
	private String mappingsFileName;
	
	
	
	public MappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + 
				String.format( GlobalConstants.MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
	}
	
	
	public String getName() {
		return mappingsFileName;
	}
	
	
}
