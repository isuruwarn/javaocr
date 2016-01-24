package com.ocr.core;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Is the rectangular bounds of a character. Also holds the bitmap representation of the character.
 * 
 * @author isuru
 *
 */
public class Char extends ScannedItem {
	
	
	private int charNumber;
	private int lineNumber;
	private int blockLength;
	private int noOfHBlocks;
	private boolean whiteSpace;
	private String name;
	private ArrayList<Byte> sequence = new ArrayList<Byte>();
	
	
	
	public Char() {
		
	}
	
	public Char(String name) {
		this.name = name;
	}
	
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCharNumber() {
		return charNumber;
	}

	public void setCharNumber(int charNumber) {
		this.charNumber = charNumber;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
	public boolean isWhiteSpace() {
		return whiteSpace;
	}

	public void setWhiteSpace(boolean whiteSpace) {
		this.whiteSpace = whiteSpace;
	}

	public ArrayList<Byte> getSequence() {
		return sequence;
	}

	public void setSequence(ArrayList<Byte> sequence) {
		this.sequence = sequence;
	}
	
	
	public int getBlockLength() {
		return blockLength;
	}

	public void setBlockLength(int blockLength) {
		this.blockLength = blockLength;
	}
	
	public int getNoOfHBlocks() {
		return noOfHBlocks;
	}

	public void setNoOfHBlocks(int noOfHBlocks) {
		this.noOfHBlocks = noOfHBlocks;
	}
	
	
	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and prints
	 * sequence.
	 * 
	 * @param verticalBlocksPerChar
	 */
	public void printSequence( int verticalBlocksPerChar ) {
		String [] sb = new String [verticalBlocksPerChar];
		for( int i=0; i<sequence.size(); i++ ) {
			int j = i % verticalBlocksPerChar;
			if( sb[j] == null ) sb[j] = "";
			sb[j] += sequence.get(i) + " ";
		}
		for(String s : sb) {
			System.out.println(s);
		}
	}
	
	
	
	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and calculates
	 * charCode
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 */
	public String getCharCode( int verticalBlocksPerChar ) {
		String s = "";
		String charCode = "";
		for( int i=0; i<sequence.size(); i++ ) {
			s += sequence.get(i);
			if( (i+1) % verticalBlocksPerChar == 0 ) {
				int n = Integer.parseInt( s, 2 );
				charCode += n;
				s = "";
			}
		}
		return charCode;
	}
	
	
	
	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and constructs
	 * an BufferedImage of the block representation. Each block will be blown up based
	 * in equal width and height based the pixelsPerBlock variable. Also, to demarcate 
	 * each block, a marker is added at the top right corner of each block.
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 *
	public BufferedImage getBlockImage( int verticalBlocksPerChar ) {
		
		int pixelsPerBlock = 20;
		int blockStartX = 0;
		int blockStartY = 0;
		int verticalBlocks = verticalBlocksPerChar;
		int horizontalBlocks = sequence.size()/verticalBlocksPerChar;
		BufferedImage blockImg = new BufferedImage( horizontalBlocks*pixelsPerBlock, verticalBlocks*pixelsPerBlock, BufferedImage.TYPE_INT_RGB );
		
		for( int i=0; i<sequence.size(); i++ ) {
			
			// -5000000;
			int rgb = 0xFFFFFF; // white 
			if( sequence.get(i) == 1 ) {
				rgb = 0; // black
			}
			
			try {
				int blockEndX = blockStartX + pixelsPerBlock;
				int blockEndY = blockStartY + pixelsPerBlock;
				for( int y=blockStartY; y<blockEndY; y++ ) {
					for( int x=blockStartX; x<blockEndX; x++ ) {
						int rgb2 = rgb;
						if( x == blockStartX && y == blockStartY ) { rgb2 = 0xC0C0C0; }
						blockImg.setRGB( x, y, rgb2 );
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			blockStartY += pixelsPerBlock;
			if( (i+1) % verticalBlocksPerChar == 0 ) {
				blockStartX += pixelsPerBlock;
				blockStartY = 0;
			}
			
		}
		
		return blockImg;
	}
	*/
	
	
	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and constructs
	 * an BufferedImage of the block representation. Each block will be blown up based
	 * in equal width and height based the pixelsPerBlock variable. Also, to demarcate 
	 * each block, a marker is added at the top right corner of each block.
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 */
	public BufferedImage getBlockImage( int verticalBlocksPerChar, ArrayList<Integer> keyPoints ) {
		
		int verticalBlocks = verticalBlocksPerChar;
		int horizontalBlocks = sequence.size()/verticalBlocksPerChar;
		BufferedImage blockImg = new BufferedImage( horizontalBlocks, verticalBlocks, BufferedImage.TYPE_INT_RGB );
		
		try {
			int blockIndex = 0;
			for( int x=0; x<horizontalBlocks; x++ ) {
				
				for( int y=0; y<verticalBlocks; y++ ) {
					
					//int rgb = 0xFFFFFF; // white
					int rgb = 0xDCDCDC; // gainsboro
					if( sequence.get(blockIndex) == 1 ) {
						rgb = 0; // black
					}
					
					if( keyPoints!=null && keyPoints.contains( blockIndex ) ) {
						rgb = 0xFF0000;
					}
					
					blockImg.setRGB( x, y, rgb );
					blockIndex++;
				}
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return blockImg;
	}
	
}
