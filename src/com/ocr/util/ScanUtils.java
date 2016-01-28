package com.ocr.util;

import java.awt.image.BufferedImage;




/**
 * 
 * 
 * 
 * @author isuru
 *
 */
public class ScanUtils {
	
	
	
	/**
	 * Takes RGB value of a pixel and returns an average value
	 * 
	 * Thanks to - http://www.dyclassroom.com/image-processing-project/how-to-convert-a-color-image-into-grayscale-image-in-java
	 * 
	 * @param pixelValue
	 * @return int representing average RGB value
	 */
	public static int getAvgRGBValue( int pixelValue ) {
		int a = (pixelValue>>24)&0xff;
		int r = (pixelValue>>16)&0xff;
		int g = (pixelValue>>8)&0xff;
		int b = pixelValue&0xff;
        int avg = (r+g+b)/3; //calculate average
        int avgPixelValue = (a<<24) | (avg<<16) | (avg<<8) | avg; //replace RGB value with avg
        return avgPixelValue;
	}
	
	
	

	
	/**
	 * Takes RGB value of pixel and returns 0 for whitespace, 1 for any other color
	 * 
	 * @param pixelValue
	 * @return 0 or 1
	 */
	public static byte getBinaryPixelValue( int bwThreshold, int pixelValue ) {
		byte binaryVal = 0;
		pixelValue = getAvgRGBValue(pixelValue);
        if( pixelValue < bwThreshold ) {
        	binaryVal = 1;
        }
        return binaryVal;
	}
	

	
	
	
	public static void convertPixelToBW( BufferedImage image, int bwThreshold, int i, int j ) {
		
		int avgRGB = getAvgRGBValue( image.getRGB( j, i ) );
        
        if( avgRGB < bwThreshold ) { // colored pixel
        	avgRGB = -99999999; // set any color (other than white) as black
        	//avgRGB = 0;
        	
        } else {
        	avgRGB = -000001; // set white-ish pixels to the same shade of white
        	//avgRGB = -5000000; // for debugging
        }
        
        image.setRGB( j, i, avgRGB ); // reset pixel color
	}
	
	

}
