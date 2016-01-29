package com.ocr.scanner.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ocr.api.OCREngine;
import com.ocr.core.Char;
import com.ocr.core.Line;
import com.ocr.core.ScanRequest;
import com.ocr.core.ScanResult;
import com.ocr.mappings.MappingsFile;
import com.ocr.scanner.AbstractScanner;
import com.ocr.text.Symbol;
import com.ocr.util.ScanUtils;



/**
 *
 * 
 * 
 * 
 * @author isuru
 *
 */
public class ScannerImpl extends AbstractScanner {
	
	

	public static final int BW_THRESHOLD = -10800000; // average RGB values less than this will be saved as black pixels. 
														// average RGB values greater than this will be saved as white pixels
	
	private int minBlanklineHeight = 30;
	private int minWhitespaceWidth = 8;
	
	private int height = 0;
	private int width = 0;
	private int linesRead = 0;
	private int charsRead = 0;
	private byte [][] bitmap; // holds black and white (1s and 0s) representation of entire image
	
	private ArrayList<Line> lines;
	private ArrayList<String> unrecognizedCharCodes;
	private ArrayList<Char> unrecognizedChars;
	
	private OCREngine ocrEngine;
	private MappingsFile mappingsFile;
	


	public ScannerImpl( OCREngine ocrEngine ) {
		this.ocrEngine = ocrEngine;
	}

	
	

	/**
	 * 
	 * 
	 * @param inputImage
	 * @param mapFile
	 * @return
	 */
	public ScanResult scan( ScanRequest req ) {
		BufferedImage inputImage = req.getImage();
		String dialect = req.getDialect();
		mappingsFile = new MappingsFile( dialect, ocrEngine.getVerticalBlocksPerChar(), ocrEngine.getName() );
		ArrayList<Line> lines = this.init( inputImage ); //reads image and prepares Line and Char objects
		StringBuilder document = this.readCharacters( lines );
		return prepareResult( document );
	}
	
	
	
	
	/**
	 * Steps 1, 2 and 3. Pre-processing is carried out in this method. Image is scanned to identify
	 * lines and characters, which are returned in as a List of <Line> objects. Each Line object will
	 * contain it's own list of <Char> objects. 
	 * 
	 * @param image
	 * @return
	 */
	private ArrayList<Line> init( BufferedImage image ) {
		
		linesRead = 0;
		charsRead = 0;
		unrecognizedCharCodes = new ArrayList<String>();
		unrecognizedChars = new ArrayList<Char>();
		lines  = new ArrayList<Line>();
		
		try {
			height = image.getHeight();
			width = image.getWidth();
			bitmap = new byte [height] [width];
			//System.out.format( "Image W: %d    H: %d \n", width, height );
			
			int lineHeight = 0;
			int blankLineHeight = 0;
			int lineNumber = 0;
			boolean insideLine = false;
			
			//start processing image horizontally, line by line
			for( int i=0; i<height; i++ ) {
				
				insideLine = false;
				
				for(int j=0; j<width; j++ ) {
					
					//convertPixelToBW( image, i, j ); // convert image to black and white
					
			        // Step 1: represent each pixel as 0 or 1, where 0 is white space and 1 is black (or part of character)
					byte b = ScanUtils.getBinaryPixelValue( BW_THRESHOLD, image.getRGB( j, i ) );
			        bitmap[i][j] = b;
			        
			        // if we find a black pixel, we have come across a line
			        if( b != 0 ) {
			        	insideLine = true;
			        }
				}
				
				// Step 2: Scan image horizontally to determine the lines in image
				if(insideLine) {
					
					lineHeight++;
					if( blankLineHeight > minBlanklineHeight ) { // keep track of blank lines in image
						Line l = new Line();
						l.setLineNumber(lineNumber);
						l.setX(0);
		        		l.setY( i - blankLineHeight );
		        		l.setW(width);
		        		l.setH(blankLineHeight);
		        		l.setBlankLine(true);
		        		lines.add( lineNumber, l );
		        		lineNumber++;
					}
					blankLineHeight = 0;
					
				} else {
					
					blankLineHeight++;
					if( lineHeight > 1 ) { // capture line which contains characters
						
						Line l = new Line();
						l.setLineNumber(lineNumber);
		        		l.setX(0);
		        		l.setY( i - lineHeight );
		        		l.setW(width);
		        		l.setH(lineHeight);
		        		lines.add( lineNumber, l );
		        		
		        		// Step 3: Scan each line vertically to determine the characters
		        		processLine(l);
		        		
		        		lineNumber++;
					}
					lineHeight = 0;
				}
			}
			
			linesRead += lineNumber;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return lines;
		
	}
	
	
	
	
	/**
	 * Capture the characters found on a single line, including spaces, and add to Char array of line
	 * 
	 * @param l
	 */
	private void processLine( Line l ) {
		
		int charNumber = 0;
		int charWidth = 0;
		int whitespaceWidth = 0;
		
		// to figure out exact height of char
		int y1 = l.getH(); // will record the highest vertical point (max y value). we start with the lowest point.
		int y2 = 0; // will record the lowest vertical point (min y value). we start with the highest point.
		
		boolean insideChar = false;
		
		// scan vertically (downwards) 
		for( int x = l.getX(); x < (l.getX()+l.getW()); x++ ) {
			
			insideChar = false;
			
			for( int y = l.getY(); y < (l.getY()+l.getH()); y++ ) {
				
				int b = bitmap[y][x];
				
				if( b != 0 ) { // if we find a black pixel, we have come across a character
					
					insideChar = true;
					//break;
					
					int relativeYPosition = y - l.getY();  // y value relative to char height, starting from top
					if( relativeYPosition < y1 ) { // record the highest vertical point (max y value) so far
						y1 = relativeYPosition;
					}
					if( relativeYPosition > y2 ) { // record the lowest vertical point (min y value) so far
						y2 = relativeYPosition;
					}
				}
    		}
			
    		if(insideChar) {
    			
    			charWidth++;
    			
    			if( whitespaceWidth > this.minWhitespaceWidth ) { // keep track of whitespace
    				Char c = new Char("char" + l.getLineNumber() + "-" + charNumber);
    				c.setCharNumber(charNumber);
    				c.setLineNumber( l.getLineNumber() );
    				c.setX( x - whitespaceWidth  );
    				c.setY( l.getY() );
    				c.setW(whitespaceWidth);
    				c.setH( l.getH() );
    				c.setWhiteSpace(true);
    				l.getChars().add( charNumber, c );
    				charNumber++;
    			}
    			whitespaceWidth = 0;
    			
    		} else {
    			
    			whitespaceWidth++;
    			
    			if( charWidth > 0 ) { // capture character
    				
    				Char c = new Char("char" + l.getLineNumber() + "-" + charNumber);
    				c.setCharNumber(charNumber);
    				c.setLineNumber( l.getLineNumber() );
    				c.setX( x - charWidth  );
    				c.setY( l.getY() + y1 );
    				c.setW(charWidth);
    				c.setH( y2-y1 + 1 );
    				l.getChars().add( charNumber, c );
    				charNumber++;
    				
    				// reset vertical pointers
    				y1 = l.getH();
    				y2 = 0;
    			}
    			charWidth = 0;
    		}
    		
		}
		
		charsRead += charNumber;
		
	}
	
	
	
	
	/**
	 * Steps 4 and 5. Should only be called after init() method is run, which means image has been processed 
	 * and we have already captured the lines and characters in image.
	 * 
	 * @param lines
	 * @return
	 */
	private StringBuilder readCharacters( ArrayList<Line> lines ) {
		
		StringBuilder sb = new StringBuilder();
		
		for( Line l: lines ) {
			
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
				if( c.getH() < ocrEngine.getVerticalBlocksPerChar() ) {
					sb.append("?");
					continue;
				}
				
				// Step 4: map each character into a grid (block representation) of 1s and 0s
				//mapToGrid(c);
				String charCode = ocrEngine.processChar( c, bitmap );
				
				// Step 5: lookup up the charcode in the saved in file for any matches
				String s = mappingsFile.lookupCharCode( charCode );
				
				if( s == null ) { // unrecognized char
					
					sb.append("?");
					
					if( unrecognizedCharCodes.indexOf( charCode ) == -1 ) { // eliminate duplicates
						unrecognizedCharCodes.add(charCode);
						unrecognizedChars.add(c);
					}
					
				} else {
					
					if( kombuwa.length() > 0 ) {
						
						if( s.equals( Symbol.KOMBUVA.getUnicodeValue() ) ) {
							kombuwa += s;
						} else {
							sb.append( s + kombuwa );
							kombuwa = "";
						}
						
					} else if( s.equals( Symbol.KOMBUVA.getUnicodeValue() ) ) {
						kombuwa = s;
						
					} else {
						sb.append(s);
					}
				}
				
			}
			
			sb.append("\n");
		}
		
		return sb;
	}
	
	
	
	
	protected ScanResult prepareResult( StringBuilder document ) {
		ScanResult res = new ScanResult();
		res.setDocument(document);
		res.setLines(lines);
		res.setCharsRead(charsRead);
		res.setLinesRead(linesRead);
		//res.setRecognizedCharCodes(recognizedCharCodes);
		//res.setRecognizedChars(recognizedChars);
		res.setUnrecognizedCharCodes(unrecognizedCharCodes);
		res.setUnrecognizedChars(unrecognizedChars);
		res.setWidth(width);
		res.setHeight(height);
		return res;
	}
	
	
	
	public int getMinBlanklineHeight() {
		return minBlanklineHeight;
	}
	
	
	public int getMinWhitespaceWidth() {
		return this.minWhitespaceWidth;
	}
	

	public MappingsFile getMappingsFile() {
		return mappingsFile;
	}
	
	
		
}
