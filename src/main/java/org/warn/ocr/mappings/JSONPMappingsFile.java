package org.warn.ocr.mappings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

import org.warn.ocr.util.GlobalConstants;




public class JSONPMappingsFile extends AbstractMappingsFile {

	
	public static final String CHAR_CODE_TAG = "charCode";
	public static final String CHAR_VALUE_TAG = "charValue";
	public static final String BLACK_PIXELS_TAG = "blackPixels";
	public static final String WHITE_PIXELS_TAG = "whitePixels";
	public static final String HBLOCKS_TAG = "hBlocks";
	
	
	// TODO: use jackson or gson for obj to json conversion
	
	public JSONPMappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName) {
		super( GlobalConstants.JSON_MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
		init();
	}
	
	
	
	private void init() {
		
		try (
				InputStream is = new FileInputStream(mappingsFileName);
				JsonReader rdr = Json.createReader(is) ) {
				
				JsonObject jsonMappingsDoc = rdr.readObject();
				
				for( Entry<String, JsonValue> mappingEntry: jsonMappingsDoc.entrySet() ) {
					
					String charCode = mappingEntry.getKey();
					JsonObject mappingValue = (JsonObject)  mappingEntry.getValue();
					String charValue = mappingValue.getString(CHAR_VALUE_TAG);
					
					JsonArray jsonarrBlackPixels = mappingValue.getJsonArray(BLACK_PIXELS_TAG);
					LinkedList<Integer> blackPixels = new LinkedList<Integer>();
					for( JsonValue val: jsonarrBlackPixels ) {
						blackPixels.add( new Integer( val.toString() ) );
					}
					
					JsonArray jsonarrWhitePixels = mappingValue.getJsonArray(WHITE_PIXELS_TAG);
					LinkedList<Integer> whitePixels = new LinkedList<Integer>();
					for( JsonValue val: jsonarrWhitePixels ) {
						whitePixels.add( new Integer( val.toString() ) );
					}
					
					int hBlocks = mappingValue.getInt(HBLOCKS_TAG);
					
					//System.out.println( CHAR_CODE_TAG + "=" + charCode);
					//System.out.println( CHAR_VALUE_TAG + "=" + charValue);
					//System.out.println( BLACK_PIXELS_TAG + "=" + jsonarrBlackPixels);
					//System.out.println( WHITE_PIXELS_TAG + "=" + jsonarrWhitePixels);
					//System.out.println( HBLOCKS_TAG + "=" + hBlocks);
					
					Mapping mapping = new Mapping();
					mapping.setCharCode(charCode);
					mapping.setCharValue(charValue);
					mapping.setBlackPixels(blackPixels);
					mapping.setWhitePixels(whitePixels);
					mapping.sethBlocks(hBlocks);
					
					charMappings.put( charCode, mapping );
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
		
			JsonObjectBuilder mappingObjectBuilder = Json.createObjectBuilder();
		
			for( Entry<String,Mapping> e: charMappings.entrySet() ) {
				
				Mapping m = e.getValue();
				
				JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
				for( int i: m.getBlackPixels() ) {
					arrayBuilder.add(i);
				}
				JsonArray jsonarrBlackPixels = arrayBuilder.build();
				
				arrayBuilder = Json.createArrayBuilder();
				for( int i: m.getWhitePixels() ) {
					arrayBuilder.add(i);
				}
				JsonArray jsonarrWhitePixels = arrayBuilder.build();
				
				JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
				objectBuilder.add( CHAR_VALUE_TAG, m.getCharValue() );
				objectBuilder.add( BLACK_PIXELS_TAG, jsonarrBlackPixels );
				objectBuilder.add( WHITE_PIXELS_TAG, jsonarrWhitePixels );
				objectBuilder.add( HBLOCKS_TAG, m.gethBlocks() );
				JsonObject mappingValue = objectBuilder.build();
				
				mappingObjectBuilder.add( m.getCharCode(), mappingValue );
				
			}
			
			JsonObject mappingObject = mappingObjectBuilder.build();
			try ( 
					OutputStream os = new FileOutputStream( mappingsFileName, false );
					JsonWriter jsonWriter = Json.createWriter(os) ) {
					jsonWriter.writeObject(mappingObject);
					return true;
					
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return false;
					
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	

	
}
