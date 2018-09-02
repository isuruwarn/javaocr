package org.warn.ocr.engine.impl;

import java.util.ArrayList;
import java.util.LinkedList;

import org.warn.ocr.core.Char;
import org.warn.ocr.core.Line;
import org.warn.ocr.core.OCREngineRequest;
import org.warn.ocr.core.OCREngineResult;
import org.warn.ocr.engine.AbstractOCREngine;
import org.warn.ocr.mappings.JSONMappingsFile;
import org.warn.ocr.mappings.Mapping;
import org.warn.ocr.text.Symbol;





public class OCREngineImplv5 extends AbstractOCREngine {
	
	
	private static final String NAME = "v5";
	private static final int VBLOCKS_PER_CHAR = 12;
	private static final double BLK_PKL_MIN_MATCH_PERCENT = 0.8; // percentage of black pixels that should match for char to be considered a possible match
	private static final double WHT_PKL_MIN_MATCH_PERCENT = 0.8; // percentage of white pixels that should match for char to be considered a possible match
	
	private JSONMappingsFile mappingsFile;

	
	
	
	public OCREngineResult processLines( OCREngineRequest req ) {
		
		byte [][] bitmap = req.getBitmap();
		ArrayList<String> recognizedCharCodes = new ArrayList<String>();
		ArrayList<String> unrecognizedCharCodes = new ArrayList<String>();
		ArrayList<Char> recognizedChars = new ArrayList<Char>();
		ArrayList<Char> unrecognizedChars = new ArrayList<Char>();
		mappingsFile = new JSONMappingsFile( req.getDialect(), VBLOCKS_PER_CHAR, NAME );
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
					c.getWhitePixels().add(blockNumber);
				} else {
					c.getBlockSequence().add( blockNumber, 1 );
					c.getBlackPixels().add(blockNumber);
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
		Mapping m = lookupChar(c);
		
		if( m != null ) {
			c.setCharCode( m.getCharCode() );
			c.setCharValue( m.getCharValue() );
		
		} else { // set temp charCode
			c.setCharCode( getCharCode(c) );
		}
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
	
	
	
	private Mapping lookupChar( Char c ) {
		
		Mapping mappedChar = null;
		double maxCombinedMatchPercent = 0;
		//int noOfHBlocks = c.getNoOfHBlocks();
		int charBlkPxlCount = c.getBlackPixels().size();
		int charWhtPxlCount = c.getWhitePixels().size();
		
		for( Mapping m: mappingsFile.getCharMap().values() ) {
			
			//int mappingHBlocks = m.gethBlocks();
			int mappingBlkPxlCount = m.getBlackPixels().size();
			int mappingWhtPxlCount = m.getWhitePixels().size();
			
			boolean isPossibleMatch = false;
			//boolean widthWithinRange = ( noOfHBlocks >= mappingHBlocks - 1 ) && ( noOfHBlocks <= mappingHBlocks + 1 );
			
			int matchingBlkPxlCount = 0;
			int mismatchBlkPxlCount = 0;
			int maximumBlkPxlError = (int) ( mappingBlkPxlCount * (1-BLK_PKL_MIN_MATCH_PERCENT) );
			
			try {
				for( int i=0; i<mappingBlkPxlCount; i++ ) {
					
					int mappingBlkPxlPos = m.getBlackPixels().get(i); 
					int charBlkPxlPos = c.getBlackPixels().get(i);
					
					if( mappingBlkPxlPos == charBlkPxlPos ) {
						matchingBlkPxlCount++;
					} else {
						mismatchBlkPxlCount++;
					}
					
					if( mismatchBlkPxlCount >= maximumBlkPxlError ) {
						break;
					}
					
				}
			} catch( IndexOutOfBoundsException e ) {
				//System.err.println(e.getMessage());
			}
			
			double blkPxlResult = ( matchingBlkPxlCount / mappingBlkPxlCount ) * 100;
			if( blkPxlResult >= BLK_PKL_MIN_MATCH_PERCENT ) {
				isPossibleMatch = true;
			}
			
			if(isPossibleMatch) {
				
				int matchingWhtPxlCount = 0;
				int mismatchWhtPxlCount = 0;
				int maximumWhtPxlError = (int) ( mappingWhtPxlCount * (1-WHT_PKL_MIN_MATCH_PERCENT) );
				
				try {
					for( int i=0; i<mappingWhtPxlCount; i++ ) {
						
						int mappingWhtPxlPos = m.getWhitePixels().get(i);
						int charWhtPxlPos = c.getWhitePixels().get(i);
						
						if( mappingWhtPxlPos == charWhtPxlPos ) {
							matchingWhtPxlCount++;
						} else {
							mismatchWhtPxlCount++;
						}
						
						if( mismatchWhtPxlCount >= maximumWhtPxlError ) {
							break;
						}
						
					}
				} catch( IndexOutOfBoundsException e ) {
					//System.err.println(e.getMessage());
				}
				
				double whtPxlResult = ( matchingWhtPxlCount / mappingWhtPxlCount ) * 100;
				if( whtPxlResult >= WHT_PKL_MIN_MATCH_PERCENT ) {
					isPossibleMatch = true;
				}
				
				if(isPossibleMatch) {
					
					if( mappedChar == null ) {
						mappedChar = m;
						maxCombinedMatchPercent = ( blkPxlResult + whtPxlResult ) / 2;
						
					} else {
						double currCombinedMatchPercent = ( blkPxlResult + whtPxlResult ) / 2;
						if( currCombinedMatchPercent > maxCombinedMatchPercent ) {
							mappedChar = m;
							maxCombinedMatchPercent = currCombinedMatchPercent;
						}
					}
				}
				
			}
			
		}
		
		return mappedChar;
	}
	
	


	@Override
	public int saveMappings(ArrayList<Char> mappings) {
		
		int statusCode = 1;
		boolean validationOK = true;
		ArrayList<Mapping> newMappings = new ArrayList<Mapping>();
		
		if( mappings != null ) {
			
			for( Char c: mappings ) {
				
				String newCharValue = c.getCharValue();
				LinkedList<Integer> blackPixels = c.getBlackPixels();
				LinkedList<Integer> whitePixels = c.getWhitePixels();
				
				if( newCharValue != null && newCharValue.length() > 0 &&
					blackPixels != null && blackPixels.size() > 0 ) {
					
					Mapping m = new Mapping();
					m.setCharCode( getCharCode(c) );
					m.setCharValue(newCharValue);
					m.setBlackPixels(blackPixels);
					m.setWhitePixels(whitePixels);
					m.sethBlocks( c.getNoOfHBlocks() );
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
