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
	
	
	public AbstractMappingsFile( String mappingsFileName, String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + 
				String.format( mappingsFileName, dialect, verticalBlocksPerChar, ocrEngineName );
	}
	
	
	public String getName() {
		return mappingsFileName;
	}
	
	
	public abstract Mapping lookupCharCode( String charCode );
	public abstract void relaodCharMap();
	public abstract HashMap<String, Mapping> getCharMap();
	public abstract boolean setMappings( ArrayList<Mapping> newMappings );
	public abstract boolean deleteMapping( String charCode );
	
}
