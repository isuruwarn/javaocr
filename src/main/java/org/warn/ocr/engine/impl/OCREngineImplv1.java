package org.warn.ocr.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;

import org.warn.ocr.core.Char;
import org.warn.ocr.core.Line;
import org.warn.ocr.core.OCREngineRequest;
import org.warn.ocr.core.OCREngineResult;
import org.warn.ocr.engine.AbstractOCREngine;
import org.warn.ocr.mappings.MappingsFile;
import org.warn.ocr.text.Symbol;





public class OCREngineImplv1 extends AbstractOCREngine {
	
	
	private static final String NAME = "v1";
	private static final int VBLOCKS_PER_CHAR = 12;
	private MappingsFile mappingsFile;

	
	
	
	public OCREngineResult processLines( OCREngineRequest req ) {
		
		byte [][] bitmap = req.getBitmap();
		ArrayList<String> recognizedCharCodes = new ArrayList<String>();
		ArrayList<String> unrecognizedCharCodes = new ArrayList<String>();
		ArrayList<Char> recognizedChars = new ArrayList<Char>();
		ArrayList<Char> unrecognizedChars = new ArrayList<Char>();
		mappingsFile = new MappingsFile( req.getDialect(), VBLOCKS_PER_CHAR, NAME );
		StringBuilder sb = new StringBuilder();
		
		
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
				
				if( charValue == null ) { // unrecognized char
					
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
				} else {
					c.getBlockSequence().add( blockNumber, 1 );
					//c.getBlockSequence().add(blockNumber);
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
		
		String charCode = getCharCode(c);
		c.setCharCode(charCode);
		
		// lookup up the charcode in the saved in file for any matches
		String charValue = mappingsFile.lookupCharCode( charCode );
		c.setCharValue(charValue);
	}
	
	
	
	
	
	/**
	 * Returns char code for given char based on OCR algorithm
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 */
	private String getCharCode( Char c ) {
		String s = "";
		String charCode = "";
		for( int i=0; i<c.getBlockSequence().size(); i++ ) {
			s += c.getBlockSequence().get(i);
			if( (i+1) % VBLOCKS_PER_CHAR == 0 ) {
				int n = Integer.parseInt( s, 2 );
				charCode += n;
				s = "";
			}
		}
		return charCode;
	}
	
	
	
	



	@Override
	public int saveMappings(ArrayList<Char> mappings) {
		
		int statusCode = 1;
		boolean validationOK = true;
		HashMap<String,String> newMappings = new HashMap<String,String>();
		
		if( mappings != null ) {
			
			for( Char c: mappings ) {
				
				String newCharValue = c.getCharValue();
				
				if( newCharValue != null && newCharValue.length() > 0 ) {
					
					String newCharCode = c.getCharCode();
					String existingCharValue = mappingsFile.lookupCharCode( newCharCode ); // check if char is already mapped
					
					if ( existingCharValue == null || existingCharValue.isEmpty() ) { // new mapping
						newMappings.put(newCharCode, newCharValue);
						
					} else if ( !newCharValue.equals(existingCharValue) ) { // updated mapping
						mappingsFile.deleteMapping(newCharCode); // delete old value
						newMappings.put(newCharCode, newCharValue);
					}
					
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
