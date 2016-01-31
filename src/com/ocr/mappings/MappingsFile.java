package com.ocr.mappings;

import java.util.HashMap;
import java.util.Properties;

import com.ocr.util.FileUtils;
import com.ocr.util.GlobalConstants;





public class MappingsFile {
	
	private String mappingsFileName;
	private Properties charMappings;
	
	
	
	public MappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + String.format( GlobalConstants.MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );;
	}
	
	
	
	
	/**
	 * Looks up char code in mappings file and returns the matching String, if found.
	 * 
	 * @param charCode
	 * @return
	 */
	public String lookupCharCode( String charCode ) {
		String c = null;
		if( charMappings == null ) {
			charMappings = FileUtils.loadPropertiesFile(mappingsFileName);
		}
		c = charMappings.getProperty(charCode);
		return c;
	}
	
	
	
	public void relaodCharMap() {
		charMappings = FileUtils.loadPropertiesFile(mappingsFileName);
	}
	
		
	
	public boolean saveMapping( String newCharCode, String newCharValue ) {
		return FileUtils.setProperty( mappingsFileName, newCharCode, newCharValue );
	}
	
	
	
	public boolean saveMappings( HashMap<String,String> newMappings ) {
		return FileUtils.setMultipleProperties( mappingsFileName, newMappings );
	}
	
	
	
	public boolean deleteMapping( String charCode ) {
		return FileUtils.deleteProperty( mappingsFileName, charCode );
	}
	
	
}
