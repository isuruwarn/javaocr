package com.ocr.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.ocr.core.Char;




/**
 * 
 * 
 * @author isuru
 *
 */
public class ImageUtils {
	
	
	
	

	/**
	 * 
	 * @param imgFile
	 * @return
	 * @throws IOException 
	 */
	public static BufferedImage loadImage( String imgFile ) throws IOException {
		File input = new File( imgFile );
		BufferedImage image = ImageIO.read(input);
		return image;
	}
	
	
	

	/**
	 * Takes in verticalBlocksPerChar used by current Scanner instance, and constructs
	 * an BufferedImage of the block representation. Each block will be blown up based
	 * in equal width and height based the pixelsPerBlock variable. Also, to demarcate 
	 * each block, a marker is added at the top right corner of each block.
	 * 
	 * @param verticalBlocksPerChar
	 * @return
	 */
	public static BufferedImage getBlockImage( Char c, int verticalBlocksPerChar, ArrayList<Integer> keyPoints ) {
		
		int verticalBlocks = verticalBlocksPerChar;
		int horizontalBlocks = c.getBlockSequence().size()/verticalBlocksPerChar;
		BufferedImage blockImg = new BufferedImage( horizontalBlocks, verticalBlocks, BufferedImage.TYPE_INT_RGB );
		
		try {
			int blockIndex = 0;
			for( int x=0; x<horizontalBlocks; x++ ) {
				
				for( int y=0; y<verticalBlocks; y++ ) {
					
					//int rgb = 0xFFFFFF; // white
					//int rgb = 0xDCDCDC; // gainsboro
					int rgb = 0xE0E0E0; // light gray
					if( c.getBlockSequence().get(blockIndex) == 1 ) {
					//if( c.getBlockSequence().contains(blockIndex) ) {
						rgb = 0; // black
					}
					
					if( keyPoints != null && keyPoints.contains( blockIndex ) ) {
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
