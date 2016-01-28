package com.ocr.core;

import java.util.HashMap;
import java.util.Properties;

import com.ocr.util.FileUtils;
import com.ocr.util.GlobalConstants;





public class MappingsFile {
	
	
	private String dialect;
	private int verticalBlocksPerChar;
	private String ocrEngineName;
	private Properties charMappings;
	
	
	
	
	public MappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.dialect = dialect;
		this.verticalBlocksPerChar = verticalBlocksPerChar;
		this.ocrEngineName = ocrEngineName;
	}
	
	
	

	public String getMappingFileName() {
		return GlobalConstants.MAPPINGS_FILE_PATH + String.format( GlobalConstants.MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
	}
	
	
	
	public String getCharValueFromMap( String charCode ) {
		String mappingFile = getMappingFileName();
		String c = null;
		if( charMappings == null ) {
			charMappings = FileUtils.loadPropertiesFile(mappingFile);
		}
		c = charMappings.getProperty(charCode);
		return c;
	}
	
	
	
	public void relaodCharMap( String dialect ) {
		charMappings = FileUtils.loadPropertiesFile( getMappingFileName() );
	}
	
		
	
	public boolean setMapping( String dialect, String newCharCode, String newCharValue ) {
		String mappingFile = getMappingFileName();
		return FileUtils.setProperty( mappingFile, newCharCode, newCharValue );
	}
	
	
	
	public boolean setMappings( String dialect, HashMap<String,String> newMappings ) {
		String mappingFile = getMappingFileName();
		return FileUtils.setMultipleProperties( mappingFile, newMappings );
	}
	
	
	
	public boolean deleteMapping( String dialect, String charCode ) {
		String mappingFile = getMappingFileName();
		return FileUtils.deleteProperty( mappingFile, charCode );
	}
	
	
}
