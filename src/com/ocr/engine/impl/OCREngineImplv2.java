package com.ocr.engine.impl;

import java.util.ArrayList;

import com.ocr.core.Char;
import com.ocr.core.Line;
import com.ocr.core.OCREngineRequest;
import com.ocr.core.OCREngineResult;
import com.ocr.engine.AbstractOCREngine;
import com.ocr.mappings.AbstractMappingsFile;
import com.ocr.mappings.Mapping;
import com.ocr.mappings.XMLMappingsFile;
import com.ocr.text.Symbol;





public class OCREngineImplv2 extends AbstractOCREngine {
	

	private static final String NAME = "v2";
	private static final String SEP = "-";
	private static final int VBLOCKS_PER_CHAR = 15;
	
	
	private AbstractMappingsFile mappingsFile;
	//private HashMap<String, ArrayList<Integer>> blackKeyPointsMap;
	//private HashMap<String, ArrayList<Integer>> whiteKeyPointsMap;
	
	
	
	public OCREngineResult processLines( OCREngineRequest req ) {
		
		byte [][] bitmap = req.getBitmap();
		ArrayList<String> recognizedCharCodes = new ArrayList<String>();
		ArrayList<String> unrecognizedCharCodes = new ArrayList<String>();
		ArrayList<Char> recognizedChars = new ArrayList<Char>();
		ArrayList<Char> unrecognizedChars = new ArrayList<Char>();
		mappingsFile = new XMLMappingsFile( req.getDialect(), VBLOCKS_PER_CHAR, NAME );
		StringBuilder sb = new StringBuilder();
		
		//initializeMappings();
		
		for( Line l: req.getLines() ) {
			
			if( l.isBlankLine() ) {
				sb.append("\n");
				continue;
			}
			
			String kombuwa = ""; 
			
			for( Char c: l.getChars() ) {
				
				if( c.isWhiteSpace() ) {
					sb.append(" ");
					continue;
				}
				
				// a character needs to have a minimum height of verticalBlocksPerChar pixels, otherwise we cannot identify it
				if( c.getH() < VBLOCKS_PER_CHAR ) {
					sb.append("?");
					continue;
				}
				
				processChar( c, bitmap ); // map each character into a grid (block representation) of 1s and 0s, 
										// calculate charCode and lookup charMap for possible matches
				String charValue = c.getCharValue();
				String charCode = c.getCharCode();
				
				if( charValue == null || charValue.length() < 1 ) { // unrecognized char
					
					sb.append("?");
					
					if( unrecognizedCharCodes.indexOf( charCode ) == -1 ) { // eliminate duplicates
						unrecognizedCharCodes.add(charCode);
						unrecognizedChars.add(c);
					}
					
				} else { // known character
					
					if( recognizedCharCodes.indexOf( charCode ) == -1 ) { // eliminate duplicates
						recognizedCharCodes.add(charCode);
						recognizedChars.add(c);
					}
					
					if( kombuwa.length() > 0 ) {
						
						if( charValue.equals( Symbol.KOMBUVA.getUnicodeValue() ) ) {
							kombuwa += charValue;
						} else {
							sb.append( charValue + kombuwa );
							kombuwa = "";
						}
						
					} else if( charValue.equals( Symbol.KOMBUVA.getUnicodeValue() ) ) {
						kombuwa = charValue;
						
					} else {
						sb.append(charValue);
					}
				}
				
			}
			
			sb.append("\n");
		}
		
		OCREngineResult res = new OCREngineResult();
		res.setDocument(sb);
		res.setRecognizedChars(recognizedChars);
		res.setUnrecognizedChars(unrecognizedChars);
		return res;
	}
	
	
	
	
	
	/**
	 * Contains the main character recognition algorithm. Maps given char to a grid based on 
	 * OCR algorithm, which is a fixed number of blocks vertically (BLOCKS_PER_CHAR), and a
	 * variable number of blocks horizontally. For example, A might be represented as:
	 * 
	 * 	0 0 0 0 0 0 0 0 
	 *	0 0 0 1 1 0 0 0 
	 *	0 0 0 1 1 1 0 0 
	 *	0 0 0 1 0 1 0 0 
	 *	0 0 1 1 0 1 1 0 
	 *	0 0 1 1 0 1 1 0 
	 *	0 1 1 1 1 1 1 0 
	 *	0 1 1 1 1 1 1 1 
	 *	0 1 0 0 0 0 1 1 
	 *	1 1 0 0 0 0 0 1 
	 *	1 1 0 0 0 0 0 1 
	 *	0 0 0 0 0 0 0 0 
	 *	0 0 0 0 0 0 0 0 
	 *	0 0 0 0 0 0 0 0 
	 *	0 0 0 0 0 0 0 0
	 * 
	 * @param c
	 */
	private void processChar( Char c, byte [][] bitmap ) {
		
		ArrayList<Integer> blackPixelPositions = new ArrayList<Integer>();
		ArrayList<Integer> whitePixelPositions = new ArrayList<Integer>();
		int blockNumber = 0;
		int blockLength = c.getH() / VBLOCKS_PER_CHAR;
		int noOfVBlocks = VBLOCKS_PER_CHAR;
		int noOfHBlocks = c.getW() / blockLength;
		if( noOfHBlocks == 0 ) {
			noOfHBlocks = 1;
		}
		
		c.setBlockLength(blockLength);
		c.setNoOfHBlocks(noOfHBlocks);
		
		/* 
		Logic to capture pixel block left out after blockLength calculation. 
		Eg: If Char height is 37, blockLength is 2, then the total height covered by the block representation 
		is 15 * 2 = 30. Therefore 7 pixels are left out. To counter this, we distribute the remainder, 
		1 pixel at a time, across the first few blocks, until remainder is 0. In above example, we will add 
		an additional pixel to each of the first 7 vertical blocks. So they will have a blockLength of 3, 
		where as the next 8 will revert back to the original blockLength of 2. 
		*/
		int vBlockRemainder = c.getH() % VBLOCKS_PER_CHAR;
		int hBlockRemainder = c.getW() % noOfHBlocks;
		int vBlockLength = blockLength;
		int hBlockLength = blockLength;
		
		if( vBlockRemainder > 0 ) { vBlockLength++; vBlockRemainder--; }
		if( hBlockRemainder > 0 ) { hBlockLength++; hBlockRemainder--; }
		
		int blockStartX = c.getX();
		int blockStartY = c.getY();
		int blockEndX = c.getX() + hBlockLength;
		if( blockEndX > c.getX() + c.getW() ) {
			blockEndX = c.getX() + c.getW();
		}
		int blockEndY = c.getY() + vBlockLength;
		
		// move through grid vertically
		for( int h=0; h<noOfHBlocks; h++ ) {
			
			for( int v=0; v<noOfVBlocks; v++ ) {
				
				boolean whiteSpace = true;
				
				for( int x=blockStartX; x<blockEndX; x++ ) {
					
					for( int y=blockStartY; y<blockEndY; y++ ) {
						
						try {
							int b = bitmap[y][x];
							if( b != 0 ) {
								whiteSpace = false;
								break;
							}
						} catch(ArrayIndexOutOfBoundsException e ) {
							e.printStackTrace();
						}
					}
					if(!whiteSpace) break;
				}
				
				if(whiteSpace) {
					c.getBlockSequence().add( blockNumber, 0 );
					whitePixelPositions.add(blockNumber);
				} else {
					c.getBlockSequence().add( blockNumber, 1 );
					blackPixelPositions.add(blockNumber);
				}
				
				// update vertical pointers
				blockStartY = blockStartY + vBlockLength;
				
				vBlockLength = blockLength;
				if( vBlockRemainder > 0 ) { vBlockLength++; vBlockRemainder--; }
				
				blockEndY = blockEndY + vBlockLength;
				if( blockEndY > c.getY() + c.getH() ) {
					blockEndY = c.getY() + c.getH();
				}
				
				blockNumber++;
			}
			
			vBlockLength = blockLength;
			vBlockRemainder = c.getH() % VBLOCKS_PER_CHAR;
			if( vBlockRemainder > 0 ) { vBlockLength++; vBlockRemainder--; }
			
			// reset vertical pointers
			blockStartY = c.getY();
			blockEndY = c.getY() + vBlockLength;
			
			// update horizontal pointers
			blockStartX = blockStartX + hBlockLength;
			
			hBlockLength = blockLength;
			if( hBlockRemainder > 0 ) { hBlockLength++; hBlockRemainder--; }
			
			blockEndX = blockEndX + hBlockLength;
			if( blockEndX > c.getX() + c.getW() ) {
				blockEndX = c.getX() + c.getW();
			}
			
		}
		
		// lookup up the charcode in the saved in file for any matches
		//String charCode = lookupChar( blackPixelPositions, whitePixelPositions );
		Mapping m = lookupChar( blackPixelPositions, whitePixelPositions );
		
		//if( charCode!=null && charCode.length() > 0 ) {
		if( m != null ) {
			
			//String charValue = mappingsFile.lookupCharCode( charCode );
			//c.setCharCode(charCode);
			//c.setCharValue(charValue);
			c.setCharCode( m.getCharCode() );
			c.setCharValue( m.getCharValue() );
			c.setBlackKeyPoints( m.getBlackKeyPoints() );
			c.setWhiteKeyPoints( m.getWhiteKeyPoints() );
			
		} else { // set temp charCode
			c.setCharCode( intListToString( blackPixelPositions ) );
		}
	}
	
	
	
	
	
	/**
	 * 
	 * 
	 * @param 
	 * @return
	 */
	private String intListToString( ArrayList<Integer> intList ) {
		String s = "";
		for( int i: intList ) {
			s += i + SEP;
		}
		return s;
	}
	
	
	
	
	private Mapping lookupChar( ArrayList<Integer> blackPixelPositions, ArrayList<Integer> whitePixelPositions ) {
		
		//Integer [] blackPixelPositionsArr = new Integer [blackPixelPositions.size()]; 
		//blackPixelPositions.toArray(blackPixelPositionsArr);
		
		//Integer [] whitePixelPositionsArr = new Integer [whitePixelPositions.size()]; 
		//whitePixelPositions.toArray(whitePixelPositionsArr);
		
		//for( Object keyObject: blackKeyPointsMap.keySet() ) {
		for( Mapping m: mappingsFile.getCharMap().values() ) {
			
			//String charCode = (String) keyObject;
			//Integer [] keyPointsArr = ( Integer [] ) blackKeyPointsMap.get(charCode);
			//boolean matchFound = Arrays.asList(blackPixelPositionsArr).containsAll( Arrays.asList(keyPointsArr) );
			//if(matchFound) {
			//	return charCode;
			//}
			boolean containsAllBlackKPs = blackPixelPositions.containsAll( m.getBlackKeyPoints() );
			
			if(containsAllBlackKPs) {
				
				if( m.getWhiteKeyPoints().size() > 0 ) { // has white KPs specified, so check if they match as well
					
					boolean containsAllWhiteKPs = whitePixelPositions.containsAll( m.getWhiteKeyPoints() );
					
					if( containsAllWhiteKPs ) {
						return m;
					}
					
				} else { // no white KPs specified, so matching black KPs is good enough
					return m;
				}
			}
			// TODO: What if there is more than one mapping? Perhaps return a list of possible mappings?
		}
		
		return null;
	}
	
	
	/*
	private void initializeMappings() {
		
		if( blackKeyPointsMap == null ) {
			
			blackKeyPointsMap = new HashMap<String, ArrayList<Integer>>();
			
			for( String charCode: mappingsFile.getCharMap().keySet() ) {
				
				String charCode = (String) charCodeObject;
				String [] strKeyPoints = charCode.split(SEP);
				Integer [] keyPoints = new Integer [ strKeyPoints.length ];
				
				for( int i = 0; i<strKeyPoints.length; i++ ) {
					keyPoints[i] = new Integer( strKeyPoints[i] );
				}
				
				blackKeyPointsMap.put( charCode, keyPoints );
			}
			
		}
		
	}
	*/

	@Override
	public int saveMappings(ArrayList<Char> mappings) {
		
		int statusCode = 1;
		boolean validationOK = true;
		//HashMap<String,String> newMappings = new HashMap<String,String>();
		ArrayList<Mapping> newMappings = new ArrayList<Mapping>();
		
		if( mappings != null ) {
			
			for( Char c: mappings ) {
				
				String newCharValue = c.getCharValue();
				ArrayList<Integer> blackKeyPoints = c.getBlackKeyPoints();
				ArrayList<Integer> whiteKeyPoints = c.getWhiteKeyPoints();
				
				if( newCharValue != null && newCharValue.length() > 0 &&
					blackKeyPoints != null && blackKeyPoints.size() > 0 ) {
					
					/*
					String newCharCode = "";
					
					if( blackKeyPoints != null && blackKeyPoints.size() > 0 ) { // save mapping based on key points (v2.0)
						newCharCode = intListToString(blackKeyPoints);
						
					} else { // save mapping based on charCode (v1.0)
						newCharCode = c.getCharCode();
					}
					
					String existingCharValue = mappingsFile.lookupCharCode( newCharCode ); // check if char is already mapped
					
					if ( existingCharValue == null || existingCharValue.isEmpty() ) { // new mapping
						newMappings.put(newCharCode, newCharValue);
						
					} else if ( !newCharValue.equals(existingCharValue) ) { // updated mapping
						mappingsFile.deleteMapping(newCharCode); // delete old value
						newMappings.put(newCharCode, newCharValue);
					}
					*/
					
					Mapping m = new Mapping();
					m.setCharCode( intListToString(blackKeyPoints) );
					m.setCharValue(newCharValue);
					m.setBlackKeyPoints(blackKeyPoints);
					m.setWhiteKeyPoints(whiteKeyPoints);
					newMappings.add(m);
					
				} else {
					validationOK = false; // mapping info not specified
				}
				
			}
			
			boolean savedSuccessfully = mappingsFile.setMappings(newMappings);
			mappingsFile.relaodCharMap(); // reload char mappings to include new mappings
			
			if( validationOK && savedSuccessfully ) {
				statusCode = 0;
			}
		
		}
		
		return statusCode;
	}
	

	
	
	

	

	
	
	public int getVerticalBlocksPerChar() {
		return VBLOCKS_PER_CHAR;
	}




}
