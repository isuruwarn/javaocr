package com.ocr.mappings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;

import com.ocr.util.GlobalConstants;




public class JSONMappingsFile extends AbstractMappingsFile {

	
	private static final String CHAR_CODE_TAG = "charCode";
	private static final String CHAR_VALUE_TAG = "charValue";
	private static final String BLACK_CELLS_TAG = "blackCells";
	private static final String WHITE_CELLS_TAG = "whiteCells";
	private static final String HBLOCKS_TAG = "hBlocks";
	
	
	
	public JSONMappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName) {
		super( GlobalConstants.JSON_MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
	}
	
	
	
	private void init() {
		
		try (
				InputStream is = new FileInputStream(mappingsFileName);
				JsonReader rdr = Json.createReader(is) ) {
				
				JsonObject jsonMappingsDoc = rdr.readObject();
				
				for( Entry<String, JsonValue> mapping: jsonMappingsDoc.entrySet() ) {
					
					String charCode = mapping.getKey();
					System.out.println("charCode: " + charCode);
					
					JsonObject mappingValue = (JsonObject)  mapping.getValue();
					
					String charValue = mappingValue.getString("charValue");
					JsonArray jsonarrBlackCells = mappingValue.getJsonArray("blackCells");
					JsonArray jsonarrWhiteCells = mappingValue.getJsonArray("whiteCells");
					int hBlocks = mappingValue.getInt("hBlocks");
					
					System.out.println("charValue: " + charValue);
					System.out.println("blackCells: " + jsonarrBlackCells);
					System.out.println("whiteCells: " + jsonarrWhiteCells);
					System.out.println("hBlocks: " + hBlocks);
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
	public boolean setMappings(ArrayList<Mapping> newMappings) {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean deleteMapping(String charCode) {
		// TODO Auto-generated method stub
		return false;
	}
	

}
