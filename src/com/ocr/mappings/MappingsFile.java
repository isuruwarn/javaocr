package com.ocr.mappings;

import com.ocr.util.GlobalConstants;




/**
 * 
 * @author isuru
 *
 */
public class MappingsFile { //implements MappingsFile {
	
	
	
	private String mappingsFileName;
	
	
	
	public MappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + String.format( GlobalConstants.MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );;
	}
	
	
	
//	public boolean saveMapping( String newCharCode, String newCharValue ) {
//		return FileUtils.setProperty( mappingsFileName, newCharCode, newCharValue );
//	}
//	
//	
//	
//	public boolean saveMappings( HashMap<String,String> newMappings ) {
//		return FileUtils.setMultipleProperties( mappingsFileName, newMappings );
//	}
//	
//	
//	
//	public boolean deleteMapping( String charCode ) {
//		return FileUtils.deleteProperty( mappingsFileName, charCode );
//	}



	//@Override
	public String getName() {
		return mappingsFileName;
	}
	
	
}
