package com.ocr.mappings;

import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ocr.util.GlobalConstants;
import com.ocr.util.XMLUtils;




/**
 * 
 * 
 * @author isuru
 *
 */
public class XMLMappingsFile extends AbstractMappingsFile {
	
	
	private static final String MAPPING_TAG = "Mapping";
	private static final String CHAR_CODE_TAG = "CharCode";
	private static final String CHAR_VALUE_TAG = "CharValue";
	private static final String BLACK_KEYPOINTS_TAG = "BlackKeyPoints";
	private static final String WHITE_KEYPOINTS_TAG = "WhiteKeyPoints";
	
	//private String mappingsFileName;
	private Document xmlMappingsDoc;
	private HashMap<String, Mapping> charMappings;
	
	
	
	
	public XMLMappingsFile( String dialect, int verticalBlocksPerChar, String ocrEngineName ) {
		super( GlobalConstants.XML_MAPPINGS_FILENAME, dialect, verticalBlocksPerChar, ocrEngineName );
		init();
	}
	
	
	
	private void init() {
		
		xmlMappingsDoc = XMLUtils.parse(mappingsFileName); // TODO exception handling
		charMappings = new HashMap<String, Mapping>();
		
		NodeList mappingsNodeList = xmlMappingsDoc.getElementsByTagName( MAPPING_TAG );
        
		for( int i=0; i<mappingsNodeList.getLength(); i++ )  {
			
			Element mappingElement = (Element) mappingsNodeList.item(i);
			
			Node charCodeNode = mappingElement.getElementsByTagName(CHAR_CODE_TAG).item(0);
			String charCode = charCodeNode == null ? "" : charCodeNode.getTextContent();
			
			Node charValueNode = mappingElement.getElementsByTagName(CHAR_VALUE_TAG).item(0);
			String charValue = charValueNode == null ? "" : charValueNode.getTextContent();
			
			Node blackKeyPointsNode = mappingElement.getElementsByTagName(BLACK_KEYPOINTS_TAG).item(0);
			String strBlackKeyPoints = blackKeyPointsNode == null ? "" : blackKeyPointsNode.getTextContent();
			
			Node whiteKeyPointsNode = mappingElement.getElementsByTagName(WHITE_KEYPOINTS_TAG).item(0);
			String strWhiteKeyPoints = whiteKeyPointsNode == null ? "" : whiteKeyPointsNode.getTextContent();
			
			Mapping mapping = new Mapping();
			mapping.setCharCode(charCode);
			mapping.setCharValue(charValue);
			mapping.setBlackKeyPoints( getIntArrayList( strBlackKeyPoints ) );
			mapping.setWhiteKeyPoints( getIntArrayList( strWhiteKeyPoints ) );
			
			charMappings.put(charCode, mapping);
		}
		
	}
	
	
	
	private static ArrayList<Integer> getIntArrayList( String strNumberList ) {
		strNumberList = strNumberList.replace("[", "").replace("]", "").replace(" ", "");
		ArrayList<Integer> intList = new ArrayList<Integer>();
		if( strNumberList != null && strNumberList.length() > 0 ) {
			for( String s: strNumberList.split(",") ) {
				try {
					intList.add( Integer.parseInt(s));
				} catch( NumberFormatException nfe ) {
					System.err.println( "IntParseError - " + nfe.getMessage() );
				}
			}
		}
		return intList;
	}
	
	
	
	
	

	/**
	 * Looks up char code in mappings file and returns the matching String, if found.
	 * 
	 * @param charCode
	 * @return
	 */
	public Mapping lookupCharCode( String charCode ) {
		return charMappings.get(charCode); 
	}
	
	
	

	public void relaodCharMap() {
		init();
	}
	
	
	public HashMap<String, Mapping> getCharMap() {
		return charMappings;
	}
	
	

	public boolean setMappings( ArrayList<Mapping> newMappings ) {
		
		for( Mapping m: newMappings ) {
				
			String charCode = m.getCharCode();
			Node root = xmlMappingsDoc.getDocumentElement();
			XMLUtils.deleteNode( charCode, root );
			
			Element charCodeNode = xmlMappingsDoc.createElement(CHAR_CODE_TAG); 
			Element charValueNode = xmlMappingsDoc.createElement(CHAR_VALUE_TAG); 
			Element BlackKeyPointsNode = xmlMappingsDoc.createElement(BLACK_KEYPOINTS_TAG); 
			Element WhiteKeyPointsNode = xmlMappingsDoc.createElement(WHITE_KEYPOINTS_TAG); 
			
			charCodeNode.setTextContent(charCode);
			charValueNode.setTextContent( m.getCharValue() );
			BlackKeyPointsNode.setTextContent( m.getBlackKeyPoints() == null ? null : m.getBlackKeyPoints().toString() );
			WhiteKeyPointsNode.setTextContent( m.getWhiteKeyPoints() == null ? null : m.getWhiteKeyPoints().toString() );
			
			Element newChild = xmlMappingsDoc.createElement(MAPPING_TAG);
			newChild.setAttribute("id", charCode);
			newChild.appendChild(charCodeNode);
			newChild.appendChild(charValueNode);
			newChild.appendChild(BlackKeyPointsNode);
			newChild.appendChild(WhiteKeyPointsNode);
			root.appendChild(newChild);
			
		}
		
		return XMLUtils.update( mappingsFileName, xmlMappingsDoc );
	}
	
	
	
	public boolean deleteMapping( String charCode ) {
		Node root = xmlMappingsDoc.getDocumentElement();
		boolean nodeRemoved = XMLUtils.deleteNode( charCode, root );
		if( nodeRemoved ) {
			return XMLUtils.update( mappingsFileName, xmlMappingsDoc );
		}
		return false;
	}
	
	
}
