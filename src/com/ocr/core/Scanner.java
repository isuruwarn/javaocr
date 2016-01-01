package com.ocr.core;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;



/**
 * Quick overview:
 * 1. Create bitmap of image, where black parts are represented using 1 and white parts using 0
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
 * 3. Unusually tall letters might mislead recognition algorithm
 * 4. Minimum character height is 15 pixels
 * 5. Letters have to be in dark colors
 * 
 * 
 * 
 * @author isuru
 *
 */
public class Scanner {
	
	
	public static final int BW_THREASHOLD = -11200000;
	public static final int MIN_BLANKLINE_HEIGHT = 30;
	public static final int MIN_WHITESPACE_WIDTH = 10;
	public static final int BLOCKS_PER_CHAR = 15;
	
	private int [][] bitmap;
	private Properties charMap;
	
	
	
	public static void main(String[] args) {
		
		String imgFile = "files/img9.png";
		String mapFile = "files/eng15.map";
		
		Scanner scn = new Scanner();
		BufferedImage image = scn.loadImage( imgFile );
		ArrayList<Line> lines = scn.init( image );
		StringBuilder sb = scn.readCharacters( lines, mapFile ); 
		System.out.println(sb);
		
	}
	
	
	
	
	/**
	 * Steps 1, 2 and 3
	 * 
	 * @param image
	 * @return
	 */
	public ArrayList<Line> init( BufferedImage image ) {
		
		ArrayList<Line> lines  = new ArrayList<Line>();
		
		try {
			int height = image.getHeight();
			int width = image.getWidth();
			bitmap = new int [height] [width];
			
			System.out.println("Width: " + width + ", Height: " + height );
			
			int lineHeight = 0;
			int blankLineHeight = 0;
			int lineNumber = 0;
			boolean insideLine = false;
			
			//start processing image horizontally, line by line
			for( int i=0; i<height; i++ ) {
				
				insideLine = false;
				
				for(int j=0; j<width; j++ ) {
					
					// convert image to black and white - for debugging mainly
			        
					int avgRGB = getAvgRGBValue( image.getRGB( j, i ) );
			        
			        if( avgRGB < BW_THREASHOLD ) { // colored pixel
			        	avgRGB = -99999999; // set any color (other than white) as black
			        } else { // white pixel
			        	avgRGB = -000001;
			        }
			        image.setRGB( j, i, avgRGB ); // reset pixel color
			        
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
					if( blankLineHeight > MIN_BLANKLINE_HEIGHT ) { // keep track of blank lines in image
						Line l = new Line();
						l.setLineNumber(lineNumber);
						l.setX(0);
		        		l.setY( i - blankLineHeight );
		        		l.setW(width);
		        		l.setH(blankLineHeight);
		        		l.setBlankLine(true);
		        		lines.add( lineNumber, l );
		        		
		        		//writeCharImages( lines, l, "test/" ); // for debugging only
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
		        		
		        		//writeCharImages( lines, l, "test/" ); // for debugging
		        		
		        		lineNumber++;
					}
					lineHeight = 0;
				}
			}
			
			//writeLineImages( image, lines, "test/" ); // for debugging
			
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
		
		int charWidth = 0;
		int whitespaceWidth = 0;
		int charNumber = 0;
		boolean insideChar = false;
		
		// scan vertically (downwards) 
		for( int x = l.getX(); x < (l.getX()+l.getW()); x++ ) {
			
			insideChar = false;
			
			for( int y = l.getY(); y < (l.getY()+l.getH()); y++ ) {
				
				int b = bitmap[y][x];
				
				// if we find a black pixel, we have come across a character
				if( b != 0 ) {
					insideChar = true;
					//break;
				}
    		}
			
    		if(insideChar) {
    			
    			charWidth++;
    			// keep track of whitespace
    			if( whitespaceWidth > MIN_WHITESPACE_WIDTH ) {
    				Char c = new Char("char" + l.getLineNumber() + "-" + charNumber);
    				c.setCharNumber(charNumber);
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
    			// capture character
    			if( charWidth > 0 ) {
    				Char c = new Char("char" + l.getLineNumber() + "-" + charNumber);
    				c.setCharNumber(charNumber);
    				c.setX( x - charWidth  );
    				c.setY( l.getY() );
    				c.setW(charWidth);
    				c.setH( l.getH() );
    				l.getChars().add( charNumber, c );
    				charNumber++;
    			}
    			charWidth = 0;
    		}
    		
		}
		
	}
	
	
	
	/**
	 * 
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
			
			for( Char c: l.getChars() ) {
				
				if( c.isWhiteSpace() ) {
					sb.append(" ");
					continue;
				}
				
				// a character needs to be a minimum of 15 pixels, otherwise we cannot identify it
				if( c.getH() < BLOCKS_PER_CHAR ) {
					sb.append("?");
					continue;
				}
				
				// Step 4: map each character into a grid of 1s and 0s
				mapToGrid(c);
				String s = getChar( mapFile, c.getCharCode() );
				if( s == null ) { // unrecognized char
					sb.append("?");
				} else {
					sb.append(s);
				}
				
			}
			
			sb.append("\n");
			//writeCharImages( l, "test/" ); // for debugging
		}
		
		//writeCharCodesToFile( lines, mapFile );
		
		// for debugging
		/*
		Char c = lines.get(1).getChars().get(48);
		//mapToGrid(c);
		//printBitmap( c.getY(), c.getY()+c.getH(), c.getX(), c.getX()+c.getW() ); // for debugging
		//c.printSequence();
		//String charCode = c.getCharCode();
		//System.out.println(charCode);
		*/
		
		return sb;
	}
	
	
	
	
	
	/**
	 * Map each character into a grid, which is a fixed number of blocks vertically (BLOCKS_PER_CHAR),
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
		int blockLength = (int) Math.round( (double) c.getH() / BLOCKS_PER_CHAR );
		int noOfHBlocks = c.getW() / blockLength;
		if( noOfHBlocks == 0 ) {
			noOfHBlocks = 1;
		}
		int noOfVBlocks = BLOCKS_PER_CHAR;
		
		// for debugging only
		//String [] str = new String [BLOCKS_PER_CHAR];
		//System.out.format("charX=%d, charY=%d, charWidth=%d, charHeight=%d, blockLength=%d, noOfHBlocks=%d \n", c.getX(), c.getY(), c.getW(), c.getH(), blockLength, noOfHBlocks );
		
		int blockStartX = c.getX();
		int blockStartY = c.getY();
		int blockEndX = c.getX() + blockLength;
		if( blockEndX > c.getX() + c.getW() ) {
			blockEndX = c.getX() + c.getW();
		}
		int blockEndY = c.getY() + blockLength;
		
		// move through grid vertically
		for( int h=0; h<noOfHBlocks; h++ ) {
			
			for( int v=0; v<noOfVBlocks; v++ ) {
				
				// for debugging only
				//if( str[v] == null ) str[v] = "";
				//str[v] += String.format("%d (%d,%d),(%d,%d) ", blockNumber, blockStartY, blockStartX, blockEndY, blockEndX );
				
				boolean whiteSpace = true;
				for( int x=blockStartX; x<blockEndX; x++ ) {
					for( int y=blockStartY; y<blockEndY; y++ ) {
						int b = bitmap[y][x];
						if( b != 0 ) {
							whiteSpace = false;
							break;
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
				blockStartY = blockStartY + blockLength;
				if( ( blockEndY + blockLength ) <= ( c.getY() + c.getH() ) ) {
					blockEndY = blockEndY + blockLength;
				} else {
					blockEndY = c.getY() + c.getH();
				}
				blockNumber++;
			}
			
			// reset vertical pointers
			blockStartY = c.getY();
			blockEndY = c.getY() + blockLength;
			
			// update horizontal pointers
			blockStartX = blockStartX + blockLength;
			if( ( blockEndX + blockLength ) <= ( c.getX() + c.getW() ) ) {
				blockEndX = blockEndX + blockLength;
			} else {
				blockEndX = c.getX() + c.getW();
			}
			
		}
		
		// for debugging
		/*
		for(String s : str) {
			System.out.println(s);
		}
		*/
		
	}
	
	
	
	
	/**
	 * 
	 * @param imgFile
	 * @return
	 */
	public BufferedImage loadImage( String imgFile ) {
		BufferedImage image = null;
		try {
			File input = new File( imgFile );
			image = ImageIO.read(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	
	
	/**
	 * Takes RGB value of pixel and returns 0 for whitespace, 1 for any other color
	 * 
	 * @param pixelValue
	 * @return 0 or 1
	 */
	public int getBinaryPixelValue( int pixelValue ) {
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
	public int getAvgRGBValue( int pixelValue ) {
		int a = (pixelValue>>24)&0xff;
		int r = (pixelValue>>16)&0xff;
		int g = (pixelValue>>8)&0xff;
		int b = pixelValue&0xff;
        int avg = (r+g+b)/3; //calculate average
        int avgPixelValue = (a<<24) | (avg<<16) | (avg<<8) | avg; //replace RGB value with avg
        return avgPixelValue;
	}
	
	
	
	
	/**
	 * Prints a subset of the main bitmap[][]
	 * 
	 * @param startY
	 * @param endY
	 * @param startX
	 * @param endX
	 */
	public void printBitmap( int startY, int endY, int startX, int endX ) {
		for( int i=startY; i<endY; i++ ) {
			for(int j=startX; j<endX; j++ ) {
				System.out.format( "%d  ", bitmap[i][j] );
			}
			System.out.println("");
		}
	}
	
	
	
	
	/**
	 * Outputs all <Line> objects held lines arrayList as image files. 
	 * Useful for debugging.
	 * 
	 * @param lines
	 * @param imgFilePath
	 */
	public void writeLineImages( BufferedImage image, ArrayList<Line> lines, String imgFilePath ) {
		try {
			for(Line l: lines) {			
				//System.out.println( "charLine" + l.getLineNumber() + " - (" + l.getX() + "," + l.getY() + "), w=" + l.getW() + ", h=" + l.getH() );
				BufferedImage lineImg = image.getSubimage( l.getX(), l.getY(), l.getW(), l.getH() );
				File output = new File( imgFilePath + "charLine" + l.getLineNumber() + ".png" );
				ImageIO.write(lineImg, "png", output );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * Outputs all <Char> objects held in the given <Line> object as image files.
	 * Useful for debugging.
	 * 
	 * @param line
	 * @param imgFilePath
	 */
	public void writeCharImages( BufferedImage image, Line line, String imgFilePath ) {
		try {
			for( Char c: line.getChars() ) {
				String name = c.getName();
				if( c.getCharCode()!=null && c.getCharCode().length()>0 ) {
					name += "-" + c.getCharCode();
					//name = c.getCharCode();
				}
				//System.out.println( name + " - (" + c.getX() + "," + c.getY() + "), w=" + c.getW() + ", h=" + c.getH() );
				BufferedImage charImg = image.getSubimage( c.getX(), c.getY(), c.getW(), c.getH() );
				File output = new File( imgFilePath + name + ".png" );
				ImageIO.write(charImg, "png", output );
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 
	 * 
	 * @param lines
	 * @param mapFile
	 */
	public void writeCharCodesToFile( ArrayList<Line> lines, String mapFile ) {
		StringBuilder s = new StringBuilder();
		for(Line l: lines) {
			for( Char c: l.getChars() ) {
				if( c.getCharCode() != null && 
					c.getCharCode().length() > 0 && 
					s.indexOf( c.getCharCode() + "=" ) == -1 ) {
					s.append( c.getCharCode() + "=\n" );
				}
			}
		}
		byte data[] = s.toString().getBytes();
	    Path p = Paths.get(mapFile);
	    try ( OutputStream out = new BufferedOutputStream(
	    		Files.newOutputStream( p, CREATE, APPEND ) 
	    		)
	    ) {
	    	out.write( data, 0, data.length );
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }
	}
	
	
	
	/**
	 * 
	 * 
	 * @param mapFile
	 * @param charCode
	 * @return
	 */
	public String getChar( String mapFile, String charCode ) {
		String c = null;
		try {
			if( charMap == null ) {
				charMap = new Properties();
				FileInputStream in = new FileInputStream(mapFile);
				charMap.load(in);
			}
			c = charMap.getProperty(charCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
	
	
	
	/*
	public void writeCharCodesToPropFile( String mapFile ) {
		Properties charMap = null;
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			charMap = new Properties();
			in = new FileInputStream(mapFile);
			charMap.load(in);
			for(Line l: lines) {
				for( Char c: l.getChars() ) {
					if( c.getCharCode()!=null && c.getCharCode().length()>0 ) {
						String code = c.getName() + "-" + c.getCharCode();
						charMap.setProperty(code, "");
					}
				}
			}
			out = new FileOutputStream(mapFile);
			charMap.store(out, "--- CharCode Mappings ---");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
}
