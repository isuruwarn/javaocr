package org.warn.ocr.engine.impl;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.warn.ocr.api.Scanner;
import org.warn.ocr.core.Char;
import org.warn.ocr.core.Line;
import org.warn.ocr.core.OCREngineRequest;
import org.warn.ocr.core.OCREngineResult;
import org.warn.ocr.engine.AbstractOCREngine;
import org.warn.ocr.mappings.AbstractMappingsFile;
import org.warn.ocr.mappings.Mapping;
import org.warn.ocr.mappings.XMLMappingsFile;
import org.warn.ocr.text.Symbol;
import org.warn.ocr.util.ScanUtils;





public class OCREngineImplv3 extends AbstractOCREngine {
	

	private static final String NAME = "v3";
	private static final String SEP = "-";
	private static final int VBLOCKS_PER_CHAR = 15;
	private AbstractMappingsFile mappingsFile;	
	private BufferedImage inputImage;
	
	
	
	public OCREngineResult processLines( OCREngineRequest req ) {
		
		StringBuilder sb = new StringBuilder();
		ArrayList<String> recognizedCharCodes = new ArrayList<String>();
		ArrayList<String> unrecognizedCharCodes = new ArrayList<String>();
		ArrayList<Char> recognizedChars = new ArrayList<Char>();
		ArrayList<Char> unrecognizedChars = new ArrayList<Char>();
		mappingsFile = new XMLMappingsFile( req.getDialect(), VBLOCKS_PER_CHAR, NAME );
		inputImage = req.getInputImage();
		
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
				
				processChar(c); // map each character into a grid (block representation) of 1s and 0s, 
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
	 * Contains the main character recognition algorithm.
	 * @param c
	 */
	private void processChar( Char c ) {
		
		int w = -1;
		int h = VBLOCKS_PER_CHAR;
		BufferedImage charImg = inputImage.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
		Image resizedCharImg = charImg.getScaledInstance( w, h, Image.SCALE_DEFAULT );
		BufferedImage bufferedCharImg = new BufferedImage( resizedCharImg.getWidth(null), resizedCharImg.getHeight(null), BufferedImage.TYPE_INT_ARGB );
		bufferedCharImg.getGraphics().drawImage( resizedCharImg, 0, 0 , null );
		w = bufferedCharImg.getWidth();
		c.setNoOfHBlocks(w);
		
		int blockNumber = 0;
		ArrayList<Integer> blackPixelPositions = new ArrayList<Integer>();
		ArrayList<Integer> whitePixelPositions = new ArrayList<Integer>();
		
		for( int x=0; x<w; x++ ) {
			
			for( int y=0; y<h; y++ ) {
				
				try {
					byte b = ScanUtils.getBinaryPixelValue( Scanner.BW_THRESHOLD, bufferedCharImg.getRGB( x, y ) );
					//int b = bufferedCharImg.getRGB( x, y );
			        
					if( b == 0 ) {
						c.getBlockSequence().add( blockNumber, 0 );
						whitePixelPositions.add(blockNumber);
					} else {
						c.getBlockSequence().add( blockNumber, 1 );
						blackPixelPositions.add(blockNumber);
					}
					
				} catch(ArrayIndexOutOfBoundsException e ) {
					e.printStackTrace();
				}
		        
		        blockNumber++;
				
			}
			
		}
		
		// lookup up the charcode in the saved in file for any matches
		Mapping m = lookupChar( blackPixelPositions, whitePixelPositions );
		
		if( m != null ) {
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
		
		for( Mapping m: mappingsFile.getCharMap().values() ) {
			
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
	
	

	@Override
	public int saveMappings(ArrayList<Char> mappings) {
		
		int statusCode = 1;
		boolean validationOK = true;
		ArrayList<Mapping> newMappings = new ArrayList<Mapping>();
		
		if( mappings != null ) {
			
			for( Char c: mappings ) {
				
				String newCharValue = c.getCharValue();
				ArrayList<Integer> blackKeyPoints = c.getBlackKeyPoints();
				ArrayList<Integer> whiteKeyPoints = c.getWhiteKeyPoints();
				
				if( newCharValue != null && newCharValue.length() > 0 &&
					blackKeyPoints != null && blackKeyPoints.size() > 0 ) {
					
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
