package com.ocr.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Properties;

import com.ocr.util.ScanUtils;
import com.ocr.util.Symbol;



/**
 * 
 * Quick overview:
 * 
 * 1. Create bitmap of image, where dark pixels are saved as 1 and white pixels as 0
 * 2. Scan image horizontally to determine the lines in image
 * 3. Scan each line vertically to determine the characters contained in it
 * 4. Break down each character into a grid (block representation) of 1s and 0s
 * 5. Identify character by looking up the block representations saved in file for any matches
 * 
 * 
 * 
 * Steps:
 * 
 * 1. Create bitmap of image
 * 	1a. Read image using javax.imageio.ImageIO.read().
 * 	1b. Find height and width and use 2 nested for() loops to read RGB value of each pixel.
 * 	1c. Calculate average RGB of each pixel.
 * 	1d. Each pixels will be represented in a binary format using a 2D int array. Dark colored 
 * 		pixels (avgRGB < BW_THREASHOLD) will be saved as 1, and white pixels will be saved as 0.
 * 		
 * 2. While carrying out step 1, we will try to separate out the lines in the image
 * 	2a. Since we are scanning the image horizontally, if the value representing a pixel 
 * 		is 1, we have come across a line. If so, we add it to an ArrayList.
 * 	2b. When number of lines exceed MIN_BLANKLINE_HEIGHT, we mark it as a blank line.
 * 	
 * 3. Scan each line vertically to determine the characters contained in it.
 * 
 * 4. Break down each character into a grid (block representation) of 1s and 0s, which contains a fixed 
 * 	number of blocks vertically (BLOCKS_PER_CHAR), and a variable number of blocks horizontally.
 * 
 * 5. Identify character by looking up the block representations saved in file for any matches
 * 
 * 6. Update StringBuilder object accordingly
 * 
 * 7. Display result
 * 
 * 
 * 
 * Constraints:
 * 1. Letters need to be oriented perfectly horizontally
 * 2. Recognition algorithm is dependent on font type. New font types will have to be mapped.
 * 3. Unusually tall letters in the same line may mislead the recognition algorithm
 * 4. Any blemishes or spots in the image mislead the recognition algorithm
 * 5. Minimum character height is 15 pixels
 * 6. Letters have to be in dark colors, in a light background
 * 
 * 
 * 
 * @author isuru
 *
 */
public class Scanner {
	
	
	public static final int BW_THREASHOLD = -10800000; // average RGB values less than this will be saved as black pixels. 
														// average RGB values greater than this will be saved as white pixels
	
	// TODO Handle thread safety. perhaps use as instance variables?
	public static int minBlanklineHeight = 30;
	public static int minWhitespaceWidth = 8;
	public static int verticalBlocksPerChar = 10;
	
	private int height = 0;
	private int width = 0;
	private int linesRead = 0;
	private int charsRead = 0;
	private int [][] bitmap; // holds black and white (1s and 0s) representation of entire image
	private Properties charMap; // holds the character mappings read from file
	private ArrayList<Line> lines;
	private ArrayList<String> unrecognizedCharCodes;
	private ArrayList<Char> unrecognizedChars;
	
	
	

	/**
	 * Steps 1, 2 and 3
	 * 
	 * @param image
	 * @return
	 */
	public ArrayList<Line> init( BufferedImage image ) {
		
		linesRead = 0;
		charsRead = 0;
		unrecognizedCharCodes = new ArrayList<String>();
		unrecognizedChars = new ArrayList<Char>();
		lines  = new ArrayList<Line>();
		
		try {
			height = image.getHeight();
			width = image.getWidth();
			bitmap = new int [height] [width];
			//System.out.format( "Image W: %d    H: %d \n", width, height );
			
			int lineHeight = 0;
			int blankLineHeight = 0;
			int lineNumber = 0;
			boolean insideLine = false;
			
			//start processing image horizontally, line by line
			for( int i=0; i<height; i++ ) {
				
				insideLine = false;
				
				for(int j=0; j<width; j++ ) {
					
					convertPixelToBW( image, i, j ); // convert image to black and white
					
			        // Step 1: represent each pixel as 0 or 1, where 0 is white space and 1 is black (or part of character)
					int b = getBinaryPixelValue( image.getRGB( j, i ) );
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
		        		
		        		//ScanUtils.writeCharImages( lines, l, "test/" ); // for debugging
		        		
		        		lineNumber++;
					}
					lineHeight = 0;
				}
			}
			
			linesRead += lineNumber;
			//ScanUtils.writeLineImages( image, lines, "test/" ); // for debugging
			
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
		int y1 = l.getH(); // will record the highest vertical point (max y value). we start with the lowest and work our way up.
		int y2 = 0; // will record the lowest vertical point (min y value). we start with the highest and work our way down.
		
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
					if( relativeYPosition < y1 ) {
						y1 = relativeYPosition;
					}
					if( relativeYPosition > y2 ) {
						y2 = relativeYPosition;
					}
				}
    		}
			
    		if(insideChar) {
    			
    			charWidth++;
    			
    			if( whitespaceWidth > minWhitespaceWidth ) { // keep track of whitespace
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
    				//c.setY( l.getY() );
    				c.setY( l.getY() + y1 );
    				c.setW(charWidth);
    				//c.setH( l.getH() );
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
	 * Steps 4 and 5
	 * 
	 * @param lines
	 * @param mapFile
	 * @return
	 */
	public StringBuilder readCharacters( ArrayList<Line> lines, String mapFile ) {
		
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
				
				// a character needs to be a minimum of 15 pixels, otherwise we cannot identify it
				if( c.getH() < verticalBlocksPerChar ) {
					sb.append("?");
					continue;
				}
				
				// Step 4: map each character into a grid (block representation) of 1s and 0s
				mapToGrid(c);
				
				// Step 5: try and identify the character by looking up the saved in file for any matches
				String s = getCharValueFromMap( mapFile, c.getCharCode() );
				
				if( s == null ) { // unrecognized char
					
					sb.append("?");
					
					if( unrecognizedCharCodes.indexOf( c.getCharCode() ) == -1 ) { // eliminate duplicates
						unrecognizedCharCodes.add( c.getCharCode() );
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
			//writeCharImages( l, "test/" ); // for debugging
		}
		
		//writeCharCodesToFile( lines, mapFile ); // for debugging
		
		return sb;
	}
	
	
	
	
	
	/**
	 * Contains the main character recognition algorithm.
	 * Maps each character into a grid, which is a fixed number of blocks vertically (BLOCKS_PER_CHAR),
	 * and a variable number of blocks horizontally. For example, A might be represented as:
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
	private void mapToGrid( Char c ) {
		
		int blockNumber = 0;
		int blockLength = c.getH() / verticalBlocksPerChar;
		int noOfVBlocks = verticalBlocksPerChar;
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
		int vBlockRemainder = c.getH() % verticalBlocksPerChar;
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
					c.getSequence().add( blockNumber, (byte) 0 );
				} else {
					c.getSequence().add( blockNumber, (byte) 1 );
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
			vBlockRemainder = c.getH() % verticalBlocksPerChar;
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
		
	}
	
	
	
	
	
	/**
	 * Takes RGB value of pixel and returns 0 for whitespace, 1 for any other color
	 * 
	 * @param pixelValue
	 * @return 0 or 1
	 */
	private int getBinaryPixelValue( int pixelValue ) {
		pixelValue = getAvgRGBValue(pixelValue);
        if( pixelValue < BW_THREASHOLD ) {
        	pixelValue = 1;
        } else {
        	pixelValue = 0;
        }
        return pixelValue;
	}
	
	
	
	
	
	/**
	 * Takes RGB value of a pixel and returns an average value
	 * 
	 * Thanks to - http://www.dyclassroom.com/image-processing-project/how-to-convert-a-color-image-into-grayscale-image-in-java
	 * 
	 * @param pixelValue
	 * @return int representing average RGB value
	 */
	private int getAvgRGBValue( int pixelValue ) {
		int a = (pixelValue>>24)&0xff;
		int r = (pixelValue>>16)&0xff;
		int g = (pixelValue>>8)&0xff;
		int b = pixelValue&0xff;
        int avg = (r+g+b)/3; //calculate average
        int avgPixelValue = (a<<24) | (avg<<16) | (avg<<8) | avg; //replace RGB value with avg
        return avgPixelValue;
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param mapFile
	 * @param charCode
	 * @return
	 */
	public String getCharValueFromMap( String mapFile, String charCode ) {
		String c = null;
		if( charMap == null ) {
			charMap = ScanUtils.loadPropertiesFile(mapFile);
		}
		c = charMap.getProperty(charCode);
		return c;
	}
	
	
	
	
	public void relaodCharMap( String mapFile ) {
		charMap = ScanUtils.loadPropertiesFile(mapFile);
	}
	
	
	
	/**
	 * 
	 * 
	 * @param image
	 * @param i
	 * @param j
	 */
	private void convertPixelToBW( BufferedImage image, int i, int j ) {
		
		int avgRGB = getAvgRGBValue( image.getRGB( j, i ) );
        
        if( avgRGB < BW_THREASHOLD ) { // colored pixel
        	avgRGB = -99999999; // set any color (other than white) as black
        	//avgRGB = 0;
        	
        } else {
        	//avgRGB = -000001; // set white-ish pixels to the same shade of white
        	avgRGB = -5000000; // for debugging
        }
        
        image.setRGB( j, i, avgRGB ); // reset pixel color
	}
	
	
	
	
	public StringBuilder readCharacters( BufferedImage inputImage, String mapFile ) {
		ArrayList<Line> lines = this.init( inputImage );
		StringBuilder sb = this.readCharacters( lines, mapFile );
		return sb;
	}
	
	
	/**
	 * 
	 * @return unrecognized characters
	 */
	public ArrayList<Char> getUnrecognizedChars() {
		return unrecognizedChars;
	}
	
	
	public int getLinesRead() {
		return linesRead;
	}
	
	
	public int getCharsRead() {
		return charsRead;
	}
	
	public int getHeight() {
		return height;
	}
	
	
	public int getWidth() {
		return width;
	}
	
	/*
	public static int getMinBlanklineHeight() {
		return minBlanklineHeight;
	}
	
	public static void setMinBlanklineHeight(int minBlanklineHeight) {
		Scanner.minBlanklineHeight = minBlanklineHeight;
	}
	
	public static int getMinWhitespaceWidth() {
		return minWhitespaceWidth;
	}

	public static void setMinWhitespaceWidth(int minWhitespaceWidth) {
		Scanner.minWhitespaceWidth = minWhitespaceWidth;
	}
	
	public static int getVerticalBlocksPerChar() {
		return verticalBlocksPerChar;
	}

	public static void setVerticalBlocksPerChar(int verticalBlocksPerChar) {
		Scanner.verticalBlocksPerChar = verticalBlocksPerChar;
	}
	*/
	
}
