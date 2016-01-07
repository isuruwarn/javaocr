package com.ocr.util;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.ocr.core.Char;
import com.ocr.core.Line;




/**
 * 
 * 
 * 
 * @author isuru
 *
 */
public class ScanUtils {
	
	
	
	/**
	 * 
	 * @param imgFile
	 * @return
	 */
	public static BufferedImage loadImage( String imgFile ) {
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
	 * Prints a subset of the main bitmap[][]
	 * 
	 * @param startY
	 * @param endY
	 * @param startX
	 * @param endX
	 */
	public static void printBitmap( int [][] bitmap, int startY, int endY, int startX, int endX ) {
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
	public static void writeLineImages( BufferedImage image, ArrayList<Line> lines, String imgFilePath ) {
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
	public static void writeCharImages( BufferedImage image, Line line, String imgFilePath ) {
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
	public static void writeCharCodesToFile( ArrayList<Line> lines, String mapFile ) {
		StringBuilder sb = new StringBuilder();
		for(Line l: lines) {
			for( Char c: l.getChars() ) {
				if( c.getCharCode() != null && 
					c.getCharCode().length() > 0 && 
					sb.indexOf( c.getCharCode() + "=" ) == -1 ) { // check for duplicates
					sb.append( c.getCharCode() + "=\n" );
				}
			}
		}
		appendToFile( sb, mapFile );
	}
	
	
	
	/**
	 * 
	 * @param sb
	 * @param outputFile
	 * @return
	 */
	public static boolean appendToFile( StringBuilder sb, String outputFile ) {
		byte data[] = sb.toString().getBytes();
	    Path p = Paths.get(outputFile);
	    try ( OutputStream out = new BufferedOutputStream(
	    		Files.newOutputStream( p, CREATE, APPEND ) 
	    		)
	    ) {
	    	out.write( data, 0, data.length );
	    	return true;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	
	
	/**
	 * 
	 * @param s
	 * @param outputFile
	 * @return
	 */
	public static boolean writeToFile( StringBuilder s, String outputFile ) {
		byte data[] = s.toString().getBytes();
	    Path p = Paths.get(outputFile);
	    try ( OutputStream out = new BufferedOutputStream(
	    		Files.newOutputStream( p, CREATE, TRUNCATE_EXISTING ) 
	    		)
	    ) {
	    	out.write( data, 0, data.length );
	    	return true;
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	return false;
	    }
	}
	
	
	
	/**
	 * 
	 * 
	 * @param propFilePath
	 * @return
	 */
	public static Properties loadPropertiesFile( String propFilePath ) {
		Properties props = null;
		try {
			props = new Properties();
			FileInputStream in = new FileInputStream(propFilePath);
			props.load(in);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return props;
	}
	
	
	
	
	/**
	 * Creates or modifies a single property of the given properties file
	 * 
	 * @param propFilePath
	 * @param key
	 * @param value
	 * @return
	 */
	public static boolean createOrModifyProperty( String propFilePath, String key, String value ) {
		Properties props = loadPropertiesFile(propFilePath);
		try {
			FileOutputStream out = new FileOutputStream(propFilePath);
			props.setProperty( key, value );
			props.store(out, null);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	
	/**
	 * 
	 * @param propFilePath
	 * @param key
	 * @return 
	 */
	public static boolean deleteProperty( String propFilePath, String key ) {
		Properties props = loadPropertiesFile(propFilePath);
		try {
			FileOutputStream out = new FileOutputStream(propFilePath);
			props.remove(key);
			props.store(out, null);
			out.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
