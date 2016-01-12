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
	
	
	public void printSequence() {
		String [] sb = new String [Scanner.verticalBlocksPerChar];
		for( int i=0; i<sequence.size(); i++ ) {
			int j = i % Scanner.verticalBlocksPerChar;
			if( sb[j] == null ) sb[j] = "";
			sb[j] += sequence.get(i) + " ";
		}
		for(String s : sb) {
			System.out.println(s);
		}
	}
	
	
	
	public String getCharCode() {
		String s = "";
		String charCode = "";
		for( int i=0; i<sequence.size(); i++ ) {
			s += sequence.get(i);
			if( (i+1) % Scanner.verticalBlocksPerChar == 0 ) {
				int n = Integer.parseInt( s, 2 );
				charCode += n;
				s = "";
			}
		}
		return charCode;
	}
	
	
	
	public BufferedImage getBlockImage() {
		
		//int x = 0;
		//int y = 0;
		int pixelsPerBlock = 3;
		int blockStartX = 0;
		int blockStartY = 0;
		int verticalBlocks = Scanner.verticalBlocksPerChar;
		int horizontalBlocks = sequence.size()/Scanner.verticalBlocksPerChar;
		BufferedImage blockImg = new BufferedImage( horizontalBlocks*pixelsPerBlock, verticalBlocks*pixelsPerBlock, BufferedImage.TYPE_INT_RGB );
		
		for( int i=0; i<sequence.size(); i++ ) {
			
			//int rgb = 0xFFFFFF; // white
			int rgb = 0xEEEEEE;
			if( sequence.get(i) == 1 ) {
				rgb = 0; // black
			}
			
			try {
				int blockEndX = blockStartX + pixelsPerBlock;
				int blockEndY = blockStartY + pixelsPerBlock;
				for( int y=blockStartY; y<blockEndY; y++ ) {
					for( int x=blockStartX; x<blockEndX; x++ ) {
						blockImg.setRGB( x, y, rgb );
					}
				}
				
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			blockStartY += pixelsPerBlock;
			if( (i+1) % Scanner.verticalBlocksPerChar == 0 ) {
				blockStartX += pixelsPerBlock;
				blockStartY = 0;
			}
			
		}
		
		return blockImg;
	}
	
	
}
