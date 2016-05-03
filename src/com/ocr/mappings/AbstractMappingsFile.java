package com.ocr.mappings;

import java.util.ArrayList;
import java.util.HashMap;

import com.ocr.util.GlobalConstants;


/**
 * This class should be extended by all MappingFile implementations.
 * 
 * @author isuru
 *
 */
public abstract class AbstractMappingsFile {

	
	protected String mappingsFileName;
	protected HashMap<String, Mapping> charMappings;
	
	
	public AbstractMappingsFile( String mappingsFileName, String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + 
				String.format( mappingsFileName, dialect, verticalBlocksPerChar, ocrEngineName );
		charMappings = new HashMap<String, Mapping>();
	}
	
	
	
	public String getName() {
		return mappingsFileName;
	}
	
	
	public Mapping lookupCharCode( String charCode ) {
		return charMappings.get(charCode); 
	}
	
	
	public HashMap<String, Mapping> getCharMap() {
		return charMappings;
	}
	
	
	public abstract void relaodCharMap();
	public abstract boolean setMappings( ArrayList<Mapping> newMappings );
	public abstract boolean deleteMapping( String charCode );
	
}
