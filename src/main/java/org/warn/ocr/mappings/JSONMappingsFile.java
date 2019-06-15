package org.warn.ocr.mappings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.warn.ocr.util.GlobalConstants;
import org.warn.utils.json.JsonUtil;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectWriter;




public class JSONMappingsFile extends AbstractMappingsFile {

	
	public JSONMappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName) {
		super( GlobalConstants.JSON_MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
		init();
	}
	
	
	
	private void init() {
		
		try {
			//Map<String, Object> rawMappings = mapper.readValue( new File(mappingsFileName), Map.class );
			TypeReference<HashMap<String,Mapping>> typeRef = new TypeReference<HashMap<String,Mapping>>() {};
			HashMap<String,Mapping> rawMappings = JsonUtil.mapper.readValue( new File(mappingsFileName), typeRef);
			for( Map.Entry<String,Mapping> entry: rawMappings.entrySet() ) {
				String charCode = entry.getKey();
				Mapping m = entry.getValue();
				charMappings.put( charCode, m );
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Override
	public void relaodCharMap() {
		init();
	}
		

	@Override
	public boolean setMappings( ArrayList<Mapping> newMappings ) {
		if( newMappings != null && newMappings.size() > 0 ) {	
			for( Mapping m: newMappings ) {
				charMappings.put( m.getCharCode(), m );
			}
			return persist();
		} else {
			return false;
		}
	}

	
	@Override
	public boolean deleteMapping( String charCode ) {
		Mapping m = charMappings.get(charCode);
		if( m == null ) {
			return false;
		} else {
			charMappings.remove(charCode);
			return persist();
		}
	}
	
	
	
	private boolean persist() {
		
		try {
			//mapper.writeValue( new File(mappingsFileName), charMappings );
			ObjectWriter ow = JsonUtil.mapper.writer().withDefaultPrettyPrinter();
			ow.writeValue( new File(mappingsFileName), charMappings );
			return true;
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	

	
}
