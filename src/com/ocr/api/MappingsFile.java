package com.ocr.api;

import java.util.HashMap;


/**
 * Belongs to a particular implementation of the OCREngine. The OCREngine will define
 * how the lookup should be done.
 * 
 * @author isuru
 *
 */
public interface MappingsFile {
	
	
	String getName();
	
	
	boolean saveMapping( String newCharCode, String newCharValue );
	
	
	boolean saveMappings( HashMap<String,String> newMappings );
	
	
	boolean deleteMapping( String charCode );

}
