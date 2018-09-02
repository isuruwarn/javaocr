package org.warn.ocr.mappings;

import java.util.HashMap;
import java.util.Properties;

import org.warn.ocr.util.FileUtils;
import org.warn.ocr.util.GlobalConstants;




/**
 * This class will maintain the list of mappings defined in a physical mappings file. Mappings 
 * are pairs of CharCode (unique Char identifier) and CharValue (actual character like a,b,c). 
 * It is used by the OCREngine in identifying known characters. 
 * 
 * The mappings are loaded in to a Properties object, so that they can be manipulated as key, value 
 * pairs. The calculation of the CharCode depends on the OCREngine implementation. Therefore a 
 * MappingsFile object should belong to a particular OCREngine implementation. Because of this, 
 * the name of the physical mappings file is based on the dialect, vertical_blocks_per_char used 
 * by the OCREngine, and OCREngine name. The OCREngine will decide how the charCode is calculated. 
 * Then it will pass the CharCode to MappingsFile object for lookup.
 * 
 * @author isuru
 *
 */
public class MappingsFile {
	
	
	
	private String mappingsFileName;
	private Properties charMappings;
	
	
	
	public MappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		this.mappingsFileName = GlobalConstants.MAPPINGS_FILE_PATH + String.format( 
				GlobalConstants.MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
		charMappings = FileUtils.loadPropertiesFile( mappingsFileName );
	}
	
	
	
	

	/**
	 * Looks up char code in mappings file and returns the matching String, if found.
	 * 
	 * @param charCode
	 * @return
	 */
	public String lookupCharCode( String charCode ) {
		String charValue = null;
		charValue = charMappings.getProperty(charCode);
		return charValue;
	}
	
	
	

	public void relaodCharMap() {
		charMappings = FileUtils.loadPropertiesFile( mappingsFileName );
	}
	
	
	public Properties getCharMap() {
		return charMappings;
	}
	
		
	
	public boolean setMapping( String newCharCode, String newCharValue ) {
		return FileUtils.setProperty( mappingsFileName, newCharCode, newCharValue );
	}
	
	

	public boolean setMappings( HashMap<String,String> newMappings ) {
		return FileUtils.setMultipleProperties( mappingsFileName, newMappings );
	}
	
	
	
	public boolean deleteMapping( String charCode ) {
		return FileUtils.deleteProperty( mappingsFileName, charCode );
	}
	
	
	
	public String getName() {
		return mappingsFileName;
	}
	
	
}
