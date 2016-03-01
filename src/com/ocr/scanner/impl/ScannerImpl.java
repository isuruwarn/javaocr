package com.ocr.scanner.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.ocr.core.Char;
import com.ocr.core.Line;
import com.ocr.core.ScanRequest;
import com.ocr.core.ScanResult;
import com.ocr.scanner.AbstractScanner;
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
	
	private int minBlanklineHeight = 30; // default value. this can be changed by user
	private int minWhitespaceWidth = 8; // default value. this can be changed by user
	private int height = 0;
	private int width = 0;
	private int linesRead = 0;
	private int charsRead = 0;
	private byte [][] bitmap; // holds black and white (1s and 0s) representation of entire image
	
	private ArrayList<Line> lines;
	private BufferedImage image;
	
	
	
	
	/**
	 * Pre-processing is carried out in this method. Image is scanned to identify
	 * lines and characters, which are returned in as a List of <Line> objects. Each Line object will
	 * contain it's own list of <Char> objects. 
	 * 
	 * @param image
	 * @return
	 */
	public ScanResult scanImage( ScanRequest req ) {
		
		linesRead = 0;
		charsRead = 0;
		minBlanklineHeight = req.getMinBlanklineHeight();
		minWhitespaceWidth = req.getMinWhitespaceWidth();
		lines  = new ArrayList<Line>();
		
		try {
			image = req.getImage();
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
					
					ScanUtils.convertPixelToBW( image, BW_THRESHOLD, i, j ); // convert image to black and white
					
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
		
		ScanResult res = new ScanResult();
		res.setBitmap(bitmap);
		res.setWidth(width);
		res.setHeight(height);
		res.setLines(lines);
		res.setCharsRead(charsRead);
		res.setLinesRead(linesRead);
		return res;
		
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
	
	
	
	public int getMinBlanklineHeight() {
		return minBlanklineHeight;
	}
	
	
	public int getMinWhitespaceWidth() {
		return this.minWhitespaceWidth;
	}
	
	
		
}
