package com.ocr.mappings;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;

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
				
				for( Entry<String, JsonValue> mappingEntry: jsonMappingsDoc.entrySet() ) {
					
					String charCode = mappingEntry.getKey();
					JsonObject mappingValue = (JsonObject)  mappingEntry.getValue();
					String charValue = mappingValue.getString(CHAR_VALUE_TAG);
					
					JsonArray jsonarrBlackCells = mappingValue.getJsonArray(BLACK_CELLS_TAG);
					ArrayList<Integer> blackCells = new ArrayList<Integer>();
					for( JsonValue val: jsonarrBlackCells ) {
						blackCells.add( new Integer( val.toString() ) );
					}
					
					JsonArray jsonarrWhiteCells = mappingValue.getJsonArray(WHITE_CELLS_TAG);
					ArrayList<Integer> whiteCells = new ArrayList<Integer>();
					for( JsonValue val: jsonarrWhiteCells ) {
						whiteCells.add( new Integer( val.toString() ) );
					}
					
					int hBlocks = mappingValue.getInt(HBLOCKS_TAG);
					
					//Object [] objarrBlackCells = jsonarrBlackCells.toArray();
					//Object [] objarrWhiteCells = jsonarrWhiteCells.toArray();
					//Integer[] intarrBlackCells = Arrays.copyOf( objarrBlackCells, objarrBlackCells.length, Integer[].class );
					//Integer[] intarrWhiteCells = Arrays.copyOf( objarrWhiteCells, objarrWhiteCells.length, Integer[].class );
					
					//System.out.println( CHAR_CODE_TAG + "=" + charCode);
					//System.out.println( CHAR_VALUE_TAG + "=" + charValue);
					//System.out.println( BLACK_CELLS_TAG + "=" + jsonarrBlackCells);
					//System.out.println( WHITE_CELLS_TAG + "=" + jsonarrWhiteCells);
					//System.out.println( HBLOCKS_TAG + "=" + hBlocks);
					
					Mapping mapping = new Mapping();
					mapping.setCharCode(charCode);
					mapping.setCharValue(charValue);
					mapping.setBlackCells(blackCells);
					mapping.setWhiteCells(whiteCells);
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
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public boolean deleteMapping( String charCode ) {
		// TODO Auto-generated method stub
		return false;
	}
	

	
	/*
	

public class JavaJSONTest {
	

	private static final String CHAR_CODE_TAG = "charCode";
	private static final String CHAR_VALUE_TAG = "charValue";
	private static final String BLACK_CELLS_TAG = "blackCells";
	private static final String WHITE_CELLS_TAG = "whiteCells";
	private static final String HBLOCKS_TAG = "hBlocks";
	
	private String mappingsFileName;
	private HashMap<String, Mapping> charMappings;
	
	public static void main(String[] args) {
		
		JavaJSONTest test = new JavaJSONTest();
		test.init();
		test.write();
	}
	
	
	private void init() {
	
		mappingsFileName = "C:\\Users\\i830520\\dev\\eclipse\\workspace\\isuru\\Test\\src\\map.json";
		charMappings = new HashMap<String, Mapping>();
		
		try (
				InputStream is = new FileInputStream(mappingsFileName);
				JsonReader rdr = Json.createReader(is) ) {
				
				JsonObject jsonMappingsDoc = rdr.readObject();
				
				for( Entry<String, JsonValue> mappingEntry: jsonMappingsDoc.entrySet() ) {
					
					String charCode = mappingEntry.getKey();
					JsonObject mappingValue = (JsonObject)  mappingEntry.getValue();
					String charValue = mappingValue.getString(CHAR_VALUE_TAG);
					
					JsonArray jsonarrBlackCells = mappingValue.getJsonArray(BLACK_CELLS_TAG);
					ArrayList<Integer> blackCells = new ArrayList<Integer>();
					for( JsonValue val: jsonarrBlackCells ) {
						blackCells.add( new Integer( val.toString() ) );
					}
					
					JsonArray jsonarrWhiteCells = mappingValue.getJsonArray(WHITE_CELLS_TAG);
					ArrayList<Integer> whiteCells = new ArrayList<Integer>();
					for( JsonValue val: jsonarrWhiteCells ) {
						whiteCells.add( new Integer( val.toString() ) );
					}
					
					int hBlocks = mappingValue.getInt(HBLOCKS_TAG);
					
					//Object [] objarrBlackCells = jsonarrBlackCells.toArray();
					//Object [] objarrWhiteCells = jsonarrWhiteCells.toArray();
					//Integer[] intarrBlackCells = Arrays.copyOf( objarrBlackCells, objarrBlackCells.length, Integer[].class );
					//Integer[] intarrWhiteCells = Arrays.copyOf( objarrWhiteCells, objarrWhiteCells.length, Integer[].class );
					
					//System.out.println( CHAR_CODE_TAG + "=" + charCode);
					//System.out.println( CHAR_VALUE_TAG + "=" + charValue);
					//System.out.println( BLACK_CELLS_TAG + "=" + jsonarrBlackCells);
					//System.out.println( WHITE_CELLS_TAG + "=" + jsonarrWhiteCells);
					//System.out.println( HBLOCKS_TAG + "=" + hBlocks);
					
					Mapping mapping = new Mapping();
					mapping.setCharCode(charCode);
					mapping.setCharValue(charValue);
					mapping.setBlackCells(blackCells);
					mapping.setWhiteCells(whiteCells);
					mapping.sethBlocks(hBlocks);
					
					charMappings.put( charCode, mapping );
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
	
	
	private void write() {
		
		String charCode = "3845856154721029626652184621819016654167832011119079190791908620366197261820624604825212408420863361921";
		String charValue = "c";
		int [] blackCells = {1,2,3};
		int [] whiteCells = {4,5,6};
		int hBlocks = 20;
		
		JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
		for( int i: blackCells ) {
			arrayBuilder.add(i);
		}
		JsonArray jsonarrBlackCells = arrayBuilder.build();
		
		arrayBuilder = Json.createArrayBuilder();
		for( int i: whiteCells ) {
			arrayBuilder.add(i);
		}
		JsonArray jsonarrWhiteCells = arrayBuilder.build();
		
		JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
		objectBuilder.add( CHAR_VALUE_TAG, charValue);
		objectBuilder.add( BLACK_CELLS_TAG, jsonarrBlackCells );
		objectBuilder.add( WHITE_CELLS_TAG, jsonarrWhiteCells );
		objectBuilder.add( HBLOCKS_TAG, hBlocks );
		JsonObject mappingValue = objectBuilder.build();
		
		objectBuilder = Json.createObjectBuilder();
		objectBuilder.add( charCode, mappingValue );
		JsonObject mappingObject = objectBuilder.build();
		
		try ( 
				OutputStream os = new FileOutputStream( mappingsFileName, true );
				JsonWriter jsonWriter = Json.createWriter(os) ) {
				jsonWriter.writeObject(mappingObject);
			   
		} catch (FileNotFoundException e) {
			e.printStackTrace();
				
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}

	 */
}
