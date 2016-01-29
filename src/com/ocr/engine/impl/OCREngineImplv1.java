package com.ocr.engine.impl;

import com.ocr.core.Char;
import com.ocr.engine.AbstractOCREngine;





public class OCREngineImplv1 extends AbstractOCREngine {
	
	
	private static final String NAME = "v1";
	private static final int VBLOCKS_PER_CHAR = 12;
	
	
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
	public String processChar( Char c, byte [][] bitmap ) {
		
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
					c.getBlockSequence().add( blockNumber, (byte) 0 );
				} else {
					c.getBlockSequence().add( blockNumber, (byte) 1 );
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
		
		return getCharCode(c);
	}
	
	
	
	
	
	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and calculates
	 * charCode
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 */
	public  String getCharCode( Char c ) {
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
	public String getName() {
		return NAME;
	}
	
	

	public int getVerticalBlocksPerChar() {
		return VBLOCKS_PER_CHAR;
	}
	
	

}